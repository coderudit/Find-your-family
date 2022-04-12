package com.findyourfamily.app.business.reporting;

import com.findyourfamily.app.database.familytree.FamilyTreeGateway;
import com.findyourfamily.app.database.familytree.IFamilyTreeGateway;
import com.findyourfamily.app.database.media.IMediaArchiveGateway;
import com.findyourfamily.app.database.media.MediaArchiveGateway;
import com.findyourfamily.app.database.reporting.*;
import com.findyourfamily.app.models.domain.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Interface type for reporting features of a person, media and person relations.
 */
public class Report implements IReport {

    //Date format to be used.
    private final String DATEFORMAT = "dd/MM/yyyy";

    //Gateway's for getting person, media and person relations from the external source.
    private IReportPersonInfoGateway reportPersonInfoDAO;
    private IReportMediaInfoGateway reportMediaInfoDAO;
    private IReportRelationsInfoGateway reportRelationsInfoDAO;
    private IFamilyTreeGateway familyTreeGateway;

    public Report() {
        reportPersonInfoDAO = new ReportPersonInfoGateway();
        reportMediaInfoDAO = new ReportMediaInfoGateway();
        reportRelationsInfoDAO = new ReportRelationsInfoGateway();
        familyTreeGateway = new FamilyTreeGateway();
    }

    /**
     * Finds a person from the external source with name. If there are multiple entries for the same name,
     * one returned by the external source is used.
     *
     * @param name
     * @return internal type of person
     * @throws IOException
     */
    @Override
    public PersonIdentityInternal findPerson(String name) throws IOException {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        PersonIdentityInternal person;

        try {
            person = reportPersonInfoDAO.findPerson(name);
            if (person == null)
                return null;
            reportPersonInfoDAO.fillAttributes(person, true);
            var personRelations = findPersonRelations(person.getPersonId());

            if (personRelations.getPartner() != null)
                person.setPartner(findName(personRelations.getPartner().getPersonId()));

            for (var parent : personRelations.getParents()) {
                person.addParent(findName(parent.getPersonId()));
            }

            for (var child : personRelations.getChildren()) {
                person.setChildren(findName(child.getPersonId()));
            }

            for (var previousPartner : personRelations.getPreviousPartners()) {
                person.setPreviousPartners(findName(previousPartner.getPersonId()));
            }

        } catch (SQLException sqlException) {
            throw new IOException("Unable to connect to database.");
        }

        return person;
    }

    /**
     * Finds the name corresponding to the person's id.
     *
     * @param id
     * @return name of the person for a given id.
     * @throws IOException
     */
    @Override
    public String findName(int id) throws IOException {
        String name;
        try {
            name = reportPersonInfoDAO.findName(id);
        } catch (SQLException sqlException) {
            throw new IOException("Unable to connect to database.");
        }
        return name;
    }

    /**
     * Returns list of notes and references for a given person with all attributes set as false.
     *
     * @param person
     * @return list of notes and references for a given person.
     * @throws IOException
     */
    @Override
    public List<String> notesAndReferences(PersonIdentityInternal person) throws IOException {
        if (!familyTreeGateway.isPersonExists(person.getPersonId()))
            throw new IllegalArgumentException("Person does not exist in the system.");
        try {
            reportPersonInfoDAO.fillAttributes(person, false);
        } catch (SQLException sqlException) {
            throw new IOException("Unable to connect to database.");
        }
        return person.getNotesAndReferences();
    }

    /**
     * Finds media file information according to the given name.
     *
     * @param name
     * @return find file information.
     * @throws IOException
     */
    @Override
    public FileIdentifierInternal findMediaFile(String name) throws IOException {
        FileIdentifierInternal fileIdentifier;
        try {
            fileIdentifier = reportMediaInfoDAO.findMediaFile(name);
            if (fileIdentifier == null)
                return null;
            reportMediaInfoDAO.fillAttributes(fileIdentifier);
            var personsList = reportPersonInfoDAO.findPersonsInMedia(fileIdentifier.getMediaId());

            for (var personId : personsList) {
                fileIdentifier.addPersonsInMedia(findName(personId));
            }
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
        return fileIdentifier;
    }

    /**
     * Finds the name of the media file for a given id.
     *
     * @param fileId
     * @return name for the given media id.
     * @throws IOException
     */
    @Override
    public String findMediaFile(int fileId) throws IOException {
        String name;
        try {
            name = reportMediaInfoDAO.findMediaFile(fileId);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
        return name;
    }

    /**
     * Find all the relations for a given person which includes partner, parents and children.
     *
     * @param personId
     * @return set of person relations for a given person.
     */
    @Override
    public PersonRelations findPersonRelations(int personId) {
        var personRelationsInfo = reportRelationsInfoDAO.getPersonRelations(personId);
        var personRelation = new PersonRelations(personId);
        for (var relation : personRelationsInfo) {
            //If relation is partner and is active, add a partner.
            if (relation.getRelationshipType() == 2 && relation.getIsActive() == 1) {
                personRelation.addPartner(new PersonIdentityLite(relation.getPerson2Id()));
            }
            //If relation is partner and is not active, add as a previous partner.
            else if (relation.getRelationshipType() == 2 && relation.getIsActive() == 0) {
                personRelation.addPreviousPartner(new PersonIdentityLite(relation.getPerson2Id()));
            }
            //If relation is parent-child, add persons 1 as parent.
            else if (relation.getRelationshipType() == 1 && relation.getPerson2Id() == personId) {
                personRelation.addParent(new PersonIdentityLite(relation.getPerson1Id()));
            } else //If relation is parent-child, add person 2 as child.
            {
                personRelation.addChild(new PersonIdentityLite(relation.getPerson2Id()));
            }
        }
        return personRelation;
    }

    /**
     * Find person information for given person id's.
     *
     * @param personIds
     * @return set of person identity information for list of a person id's.
     */
    @Override
    public Set<PersonIdentityInternal> findPersons(Set<Integer> personIds) {
        Set<PersonIdentityInternal> persons = new HashSet<>();
        for (var id : personIds) {
            persons.add(reportRelationsInfoDAO.getPersonById(id));
        }
        return persons;
    }

    /**
     * Finds the media files having a given tag and lying withing the given range.
     *
     * @param tag
     * @param startDate
     * @param endDate
     * @return set of distinct files.
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Set<FileIdentifierInternal> findMediaByTag(String tag, String startDate, String endDate) throws IOException, ParseException {

        Set<FileIdentifierInternal> files;
        try {

            var startD = (startDate == null || startDate.isBlank()) ? null : convertStringDateToDateFormat(startDate);
            var endD = (endDate == null || endDate.isBlank()) ? null : convertStringDateToDateFormat(endDate);

            files = reportMediaInfoDAO.findMediaByTag(tag, startD, endD);

            for (var file : files) {
                reportMediaInfoDAO.fillAttributes(file);
            }
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
        return files;
    }

    /**
     * Finds the media files having a given location and lying withing the given range.
     *
     * @param location
     * @param startDate
     * @param endDate
     * @return set of distinct files.
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Set<FileIdentifierInternal> findMediaByLocation(String location, String startDate, String endDate) throws IOException, ParseException {
        Set<FileIdentifierInternal> files;
        try {

            var startD = (startDate == null || startDate.isBlank()) ? null : convertStringDateToDateFormat(startDate);
            var endD = (endDate == null || endDate.isBlank()) ? null : convertStringDateToDateFormat(endDate);

            files = reportMediaInfoDAO.findMediaByLocation(location, startD, endD);

            for (var file : files) {
                reportMediaInfoDAO.fillAttributes(file);
            }
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to database.");
        }
        return files;
    }

    /**
     * Find distinct media for all the individuals within a given time frame. If no start or end date
     * is passed find all the files without restricting to time frame.
     *
     * @param people
     * @param startDate
     * @param endDate
     * @return set of distinct files for all the individuals.
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Set<FileIdentifierInternal> findIndividualsMedia(Set<PersonIdentityInternal> people,
                                                            String startDate, String endDate) throws IOException, ParseException {
        Set<FileIdentifierInternal> files = new TreeSet<>(
                Comparator.comparing(
                        FileIdentifierInternal::getDateOfPicture, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(
                        FileIdentifierInternal::getMediaName, Comparator.naturalOrder()
                )
        );

        try {
            var startD = (startDate == null || startDate.isBlank()) ? null : convertStringDateToDateFormat(startDate);
            var endD = (endDate == null || endDate.isBlank()) ? null : convertStringDateToDateFormat(endDate);

            try {
                for (var person : people) {
                    files.addAll(reportMediaInfoDAO.findIndividualsMedia(person.getPersonId(), startD, endD));
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            for (var file : files) {
                reportMediaInfoDAO.fillAttributes(file);
            }
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to database.");
        }
        return files;
    }

    /**
     * Finds the media files for the descendants of a given person.
     * It returns only the distinct files.
     *
     * @param persons
     * @return distinct files for a given person descendants.
     * @throws IOException
     */
    @Override
    public List<FileIdentifierInternal> findBiologicalFamilyMedia(Set<Integer> persons) throws IOException {

        Set<FileIdentifierInternal> files = new TreeSet<>(
                Comparator.comparing(
                        FileIdentifierInternal::getDateOfPicture, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(
                        FileIdentifierInternal::getMediaName, Comparator.naturalOrder()
                )
        );

        try {
            for (var personId : persons) {
                files.addAll(reportMediaInfoDAO.findIndividualsMedia(personId, null, null));
            }
            for (var file : files) {
                reportMediaInfoDAO.fillAttributes(file);
            }
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to database.");
        }
        return files.stream().toList();
    }

    /**
     * Converts date in string form to util.date.
     *
     * @param stringDate
     * @return date in util.date format.
     * @throws ParseException
     */
    private Date convertStringDateToDateFormat(String stringDate) throws ParseException {
        Date date;
        var dateFormat = new SimpleDateFormat(DATEFORMAT);
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        return date;
    }


}
