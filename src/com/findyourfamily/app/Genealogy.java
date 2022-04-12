package com.findyourfamily.app;

import com.findyourfamily.app.common.filehandling.FileUtility;
import com.findyourfamily.app.common.mappers.FileIdentityMapper;
import com.findyourfamily.app.common.mappers.PersonIdentityMapper;
import com.findyourfamily.app.models.domain.*;
import com.findyourfamily.app.business.familytree.FamilyTree;
import com.findyourfamily.app.business.familytree.IFamilyTree;
import com.findyourfamily.app.business.mediaarchive.IMediaArchive;
import com.findyourfamily.app.business.mediaarchive.MediaArchive;
import com.findyourfamily.app.business.reporting.IReport;
import com.findyourfamily.app.business.reporting.Report;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Wrapper type for managing find your family app.
 * 1. It can be used to add persons, their personal attributes like gender, date of birth,
 * location of birth, information related to their relations like parents, children, partner.
 * 2. It can be used to add media files, their media attributes like location of media, media
 * date, persons in the media etc.
 * 3. It can be used to fetch the information related to persons, media and relations between
 * persons.
 */
public class Genealogy {

    //Type for recording information related to persons.
    private IFamilyTree familyTree;

    //Type for recording information related to media.
    private IMediaArchive mediaArchive;

    //Type for reporting information related to persons and media.
    private IReport report;

    private Set<Integer> roots;

    private Set<Integer> nodesCovered;

    public Genealogy() {
        familyTree = new FamilyTree();
        mediaArchive = new MediaArchive();
        report = new Report();
    }

    /**
     * Adds a person to the external source with a given name.
     * Duplicate name can be recorded with new entry created
     * for each record.
     *
     * @param name
     * @return Person Identity type
     * @throws IOException
     */
    public PersonIdentity addPerson(String name) throws IOException {
        //Throw exception for illegal argument when name is null or blank.
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        //Gets an object with internal representation.
        var piInternal = familyTree.addPerson(name);

        //Maps the internal representation object to be returned externally.
        var piExternal = PersonIdentityMapper.mapInternalPIToExternal(piInternal);
        return piExternal;
    }

    /**
     * Record attributes against a given person in the external source.
     * Predefined attributes inside PersonIdentity type will be updated, and
     * new attributes will be created inside the external source. New attributes
     * when recorded multiple times will return the latest value.
     *
     * @param person
     * @param attributes
     * @return true if all the attributes are added, even when single attribute fails
     * it returns falls but still all the other attributes are added.
     * @throws IOException
     */
    public boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) throws IOException {
        //Throws an exception when person passed is null.
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null.");

        //Throws an exception when attributes are null or have no information to record.
        if (attributes == null || attributes.size() == 0)
            throw new IllegalArgumentException("Attributes cannot be null or empty.");

        //Transforms external representation object to internal representation.
        var piInternal = PersonIdentityMapper.mapExternalPIToInternal(person);

        //Returns true if all the attributes were recorded.
        return familyTree.recordAttributes(piInternal, attributes);
    }

    /**
     * Record reference against a person in the external source.
     * Current date and time is added by default to help retrieve
     * them according to it.
     *
     * @param person
     * @param reference
     * @return
     * @throws IOException
     */
    public boolean recordReference(PersonIdentity person, String reference) throws IOException {
        //Throws an exception when person passed is null.
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null.");

        //Throws an exception when reference passed is null or blank.
        if (reference == null || reference.isBlank())
            throw new IllegalArgumentException("Reference cannot be null.");

        return familyTree.recordReference(person.getPersonId(), reference);
    }

    /**
     * Record note against a person in the external source.
     * Current date and time is added by default to help retrieve
     * them according to it.
     *
     * @param person
     * @param note
     * @return
     * @throws IOException
     */
    public boolean recordNote(PersonIdentity person, String note) throws IOException {
        //Throws an exception when person passed is null.
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null.");

        //Throws an exception when note passed is null or blank.
        if (note == null || note.isBlank())
            throw new IllegalArgumentException("Note cannot be null.");

        return familyTree.recordNote(person.getPersonId(), note);
    }

    /**
     * Records parent and child relationship.
     *
     * @param parent
     * @param child
     * @return true if the parent-child relationship is successfully recorded.
     * @throws IOException
     */
    public boolean recordChild(PersonIdentity parent, PersonIdentity child) throws IOException {
        if (parent == null || child == null)
            throw new IllegalArgumentException("Parent or child cannot be null.");
        return familyTree.recordChild(parent.getPersonId(), child.getPersonId());
    }

    /**
     * Record partnership relationship between partner 1 and partner 2.
     *
     * @param partner1
     * @param partner2
     * @return true if the partnership relationship is successfully recorded.
     * @throws IOException
     */
    public boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) throws IOException {
        if (partner1 == null || partner2 == null)
            throw new IllegalArgumentException("Partner 1 or Partner 2 cannot be null.");
        return familyTree.recordPartnering(partner1.getPersonId(), partner2.getPersonId());
    }

    /**
     * Record dissolution relationship between partner 1 and partner 2.
     * Updates the active status to inactive.
     *
     * @param partner1
     * @param partner2
     * @return true if the dissolution relationship is successfully recorded.
     * @throws IOException
     */
    public boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) throws IOException {
        if (partner1 == null || partner2 == null)
            throw new IllegalArgumentException("Partner 1 or Partner 2 cannot be null.");
        return familyTree.recordDissolution(partner1.getPersonId(), partner2.getPersonId());
    }

    /**
     * Adds media to the external source with given file location and file name.
     * If same file location is tried for adding, it returns the existing
     * file identifier.
     *
     * @param fileLocation
     * @return file identifier internal type of the added file.
     * @throws IOException
     */
    public FileIdentifier addMediaFile(String fileLocation) throws IOException {

        //Throw exception if file location is null or blank or location does not exist.
        if (!FileUtility.isFileExists(fileLocation))
            throw new IllegalArgumentException("Invalid file location.");

        //Gets an object with internal representation.
        var fiInternal = mediaArchive.addMediaFile(fileLocation);

        //Maps the internal representation object to be returned externally.
        var fiExternal = FileIdentityMapper.mapInternalFIToExternal(fiInternal);
        return fiExternal;
    }

    /**
     * Record attributes against a given media in the external source.
     * Predefined attributes inside FileIdentifier type will be updated, and
     * new attributes will be created inside the external source. New attributes
     * when recorded multiple times will return the latest value.
     *
     * @param fileIdentifier
     * @param attributes
     * @return true if all the attributes are added, even when single attribute fails
     * it returns falls but still all the other attributes are added.
     * @throws IOException
     */
    public boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) throws IOException {
        if (fileIdentifier == null)
            throw new IllegalArgumentException("File identifier cannot be null.");

        if (attributes == null || attributes.size() == 0)
            throw new IllegalArgumentException("Attributes cannot be null or empty.");


        var fiInternal = FileIdentityMapper.mapExternalFIToInternal(fileIdentifier);
        return mediaArchive.recordMediaAttributes(fiInternal, attributes);
    }

    /**
     * Saves all the persons corresponding to a given file identifier.
     * If any of the person is not saved, all the changes are roll-backed.
     *
     * @param fileIdentifier
     * @param people
     * @return true if all the persons were saved for a file.
     */
    public boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) throws IOException {
        if (fileIdentifier == null)
            throw new IllegalArgumentException("File identifier cannot be null");

        if (people == null || people.size() == 0)
            throw new IllegalArgumentException("People can't be empty.");

        //As only person ids are required, pass only person id to the business layer.
        var personIds = new ArrayList<Integer>();
        for (var person : people) {
            personIds.add(person.getPersonId());
        }

        return mediaArchive.peopleInMedia(fileIdentifier.getMediaId(), personIds);
    }

    /**
     * Adds tag to the current media file.
     *
     * @param fileIdentifier
     * @param tag
     * @return
     */
    public boolean tagMedia(FileIdentifier fileIdentifier, String tag) throws IOException {
        if (fileIdentifier == null)
            throw new IllegalArgumentException("File identifier cannot be null");

        if (tag == null || tag.isBlank())
            throw new IllegalArgumentException("Tag cannot be null or blank.");

        return mediaArchive.tagMedia(fileIdentifier.getMediaId(), tag);
    }

    /**
     * Finds a person from the external source with name. If there are multiple entries for the same name,
     * one returned by the external source is used.
     *
     * @param name
     * @return internal type of person
     * @throws IOException
     */
    public PersonIdentity findPerson(String name) throws IOException, ParseException {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("File location cannot be null or empty.");

        //Find person's attributes
        var piInternal = report.findPerson(name);

        //Map the internal representation to external.
        var piExternal = PersonIdentityMapper.mapInternalPIToExternal(piInternal);

        Set<PersonIdentityInternal> personSet = new HashSet<>();
        personSet.add(piInternal);

        //Get all the files for this person and add it to the person information
        var filesInternal = report.findIndividualsMedia(personSet, null, null);

        Set<FileIdentifier> filesExternal = new HashSet<>();
        for (var file : filesInternal) {
            filesExternal.add(FileIdentityMapper.mapInternalFIToExternal(file));
        }
        piExternal.setFiles(filesExternal);

        return piExternal;
    }

    /**
     * Finds the name corresponding to the person's id.
     *
     * @param id
     * @return
     * @throws IOException
     */
    public String findName(int id) throws IOException {
        if (id <= 0)
            throw new IllegalArgumentException("Id cannot be less than 1.");
        return report.findName(id);
    }

    /**
     * Returns list of notes and references for a given person with all attributes set as false.
     *
     * @param person
     * @return list of notes and references for a given person.
     * @throws IOException
     */
    public List<String> notesAndReferences(PersonIdentity person) throws IOException {
        if (person == null || person.getPersonId() <= 0)
            throw new IllegalArgumentException("Invalid person information passed.");
        var piInternal = PersonIdentityMapper.mapExternalPIToInternal(person);
        return report.notesAndReferences(piInternal);
    }

    /**
     * Finds media file information according to the given name.
     *
     * @param name
     * @return find file information.
     * @throws IOException
     */
    public FileIdentifier findMediaFile(String name) throws IOException {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty.");
        var fiInternal = report.findMediaFile(name);
        var fiExternal = FileIdentityMapper.mapInternalFIToExternal(fiInternal);
        return fiExternal;
    }

    /**
     * Finds the name of the media file for a given id.
     *
     * @param fileId
     * @return name for the given media id.
     * @throws IOException
     */
    public String findMediaFile(int fileId) throws IOException {
        if (fileId <= 0)
            throw new IllegalArgumentException("Id cannot be less than 1.");

        return report.findMediaFile(fileId);
    }

    /**
     * Finds the relation between person 1 and person 2 using recursion
     * backtracking
     * @param person1
     * @param person2
     * @return biological relation containing cousinship and degree of removal.
     */
    public BiologicalRelation findRelation(PersonIdentity person1, PersonIdentity person2) {
        roots = new HashSet<>();
        nodesCovered = new HashSet<>();
        findRoot(familyTree, person1.getPersonId(), false);
        findRoot(familyTree, person2.getPersonId(), true);

        var personRelationsMap = ((FamilyTree) familyTree).getPersonsMap();

        if (roots.size() == 0)
            return new BiologicalRelation(-1, -1);

        int person1Index = -1;
        int person2Index = -1;
        for (var root : roots) {
            person1Index = root == person1.getPersonId() ? 0 : findPositionOfNode(root, personRelationsMap, person1.getPersonId(), 1);
            person2Index = root == person2.getPersonId() ? 0 : findPositionOfNode(root, personRelationsMap, person2.getPersonId(), 1);
            if (person1Index != -1 && person2Index != -1)
                break;
        }

        int counsinship = person1Index == person2Index ? 0 : ((person1Index == -1 || person2Index == -1) ? -1 : Math.min(person1Index, person2Index) - 1);
        int removal = (person1Index == -1 || person2Index == -1) ? -1 : Math.abs(person1Index - person2Index);
        var biologicalRelation = new BiologicalRelation(counsinship, removal);

        return biologicalRelation;
    }

    /**
     * Find descendants of a given person for given generations.
     * @param person
     * @param generations
     * @return
     */
    public Set<PersonIdentity> descendants(PersonIdentity person, Integer generations) {
        int currentGeneration = 0;
        var personsMap = ((FamilyTree) familyTree).getPersonsMap();

        Set<Integer> descendantIds = new HashSet<>();

        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(person.getPersonId());
        while (currentGeneration < generations) {
            var findPerson = personsMap.get(queue.peek());
            if (findPerson == null) {
                findPerson = report.findPersonRelations(queue.peek());
                ((FamilyTree) familyTree).addToPersonsMap(queue.poll(), findPerson);
            }

            for (var child : findPerson.getChildren()) {
                descendantIds.add(child.getPersonId());
                queue.add(child.getPersonId());
            }
            currentGeneration++;
        }

        //Go and fetch data for all the personid's
        var persons = report.findPersons(descendantIds);

        var piExternalPeople = new HashSet<PersonIdentity>();

        for (var personIndex : persons) {
            piExternalPeople.add(PersonIdentityMapper.mapInternalPIToExternal(personIndex));
        }
        return piExternalPeople;

    }

    /**
     * Find ancestors of a given person for given generations.
     *
     * @param person
     * @param generations
     * @return
     */
    public Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
        if (person == null)
            throw new IllegalArgumentException("Invalid person pased.");
        if (generations < 0)
            throw new IllegalArgumentException("Invalid generations passed.");
        int currentGeneration = 0;
        var personsMap = ((FamilyTree) familyTree).getPersonsMap();

        Set<Integer> ascendantIds = new HashSet<>();

        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(person.getPersonId());
        while (currentGeneration < generations) {
            var findPerson = personsMap.get(queue.peek());
            if (findPerson == null) {
                findPerson = report.findPersonRelations(queue.peek());
                ((FamilyTree) familyTree).addToPersonsMap(queue.poll(), findPerson);
            }

            for (var parent : findPerson.getParents()) {
                ascendantIds.add(parent.getPersonId());
                queue.add(parent.getPersonId());
            }
            currentGeneration++;
        }

        //Go and fetch data for all the personid's
        var persons = report.findPersons(ascendantIds);

        var piExternalPeople = new HashSet<PersonIdentity>();

        for (var personIndex : persons) {
            piExternalPeople.add(PersonIdentityMapper.mapInternalPIToExternal(personIndex));
        }
        return piExternalPeople;
    }

    /**
     * Finds all the media taken at the given tag within a given time frame.
     *
     * @param tag
     * @param startDate
     * @param endDate
     * @return set of files.
     * @throws IOException
     * @throws ParseException
     */
    public Set<FileIdentifier> findMediaByTag(String tag, String startDate, String endDate) throws IOException, ParseException {
        //Throws exception when tag are null or is empty.
        if (tag == null || tag.isBlank())
            throw new IllegalArgumentException("Tag cannot be null or empty.");

        var fiInternalSet = report.findMediaByTag(tag, startDate, endDate);
        var fiExternalSet = new HashSet<FileIdentifier>();
        for (var fiInternal : fiInternalSet) {
            fiExternalSet.add(FileIdentityMapper.mapInternalFIToExternal(fiInternal));
        }
        return fiExternalSet;
    }

    /**
     * Finds all the media taken at the given location within a given time frame.
     *
     * @param location
     * @param startDate
     * @param endDate
     * @return set of files.
     * @throws IOException
     * @throws ParseException
     */
    public Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate) throws IOException, ParseException {
        //Throws exception when location are null or is empty.
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be null or empty.");

        var fiInternalSet = report.findMediaByLocation(location, startDate, endDate);
        var fiExternalSet = new HashSet<FileIdentifier>();
        for (var fiInternal : fiInternalSet) {
            fiExternalSet.add(FileIdentityMapper.mapInternalFIToExternal(fiInternal));
        }
        return fiExternalSet;
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
    public Set<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String endDate) throws IOException, ParseException {
        //Throws exception when persons are null or have no persons.
        if (people == null || people.size() == 0)
            throw new IllegalArgumentException("People cannot be null or empty.");

        var piInternalPeople = new HashSet<PersonIdentityInternal>();

        //Maps all the persons to the internal representation.
        for (var person : people) {
            piInternalPeople.add(PersonIdentityMapper.mapExternalPIToInternal(person));
        }

        //Find all the individuals media from th external source.
        var fiInternalSet = report.findIndividualsMedia(piInternalPeople, startDate, endDate);
        var fiExternalSet = new HashSet<FileIdentifier>();
        for (var fiInternal : fiInternalSet) {
            fiExternalSet.add(FileIdentityMapper.mapInternalFIToExternal(fiInternal));
        }
        return fiExternalSet;
    }

    /**
     * Finds the media files for the descendants of a given person.
     * It returns only the distinct files.
     *
     * @param person
     * @return distinct files for a given person descendants.
     * @throws IOException
     */
    public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) throws IOException {

        //Throws exception when person is null.
        if (person == null)
            throw new IllegalArgumentException("Person cannot be null .");

        //Gets the persons map from the family tree.
        var personsMap = ((FamilyTree) familyTree).getPersonsMap();

        var descendantIds = new HashSet<Integer>();

        //Find the given person inside the persons map.
        var findPerson = personsMap.get(person.getPersonId());

        //If person is not present inside the map, fetch it from the database. If it is not present,
        //throw exception
        if (findPerson == null) {
            findPerson = report.findPersonRelations(person.getPersonId());
            if (findPerson == null)
                throw new IllegalArgumentException("Person does not exist in the system.");
            ((FamilyTree) familyTree).addToPersonsMap(person.getPersonId(), findPerson);
        }

        //Find all the descendants of the current person.
        for (var child : findPerson.getChildren()) {
            descendantIds.add(child.getPersonId());
        }

        //If no descendants exists return empty list.
        if (descendantIds.size() == 0)
            return new ArrayList<>();

        //Gets the files in internal format and transform it to external.
        var filesInternal = report.findBiologicalFamilyMedia(descendantIds);
        var fiExternaList = new ArrayList<FileIdentifier>();
        for (var fiInternal : filesInternal) {
            fiExternaList.add(FileIdentityMapper.mapInternalFIToExternal(fiInternal));
        }

        return fiExternaList;
    }

    /**
     * Finds all the ancestors of the current person passed.
     * @param familyTree
     * @param personId
     * @param checkForRoot
     */
    private void findRoot(IFamilyTree familyTree, int personId, boolean checkForRoot) {

        //Gets the persons map from the family tree.
        var personsMap = ((FamilyTree) familyTree).getPersonsMap();

        //Find the given person inside the persons map.
        var findPerson = personsMap.get(personId);

        //If person is not present inside the map, fetch it from the database. If it is not present,
        //throw exception
        if (findPerson == null) {
            findPerson = report.findPersonRelations(personId);
            if (findPerson == null)
                throw new IllegalArgumentException("Person does not exist in the system.");
            ((FamilyTree) familyTree).addToPersonsMap(personId, findPerson);
        }

        //If the end of the root is reached add it to the list.
        if (findPerson.getParents() == null || findPerson.getParents().size() == 0)
            roots.add(findPerson.getPersonId());

        for (var parentId : findPerson.getParents()) {
            if (checkForRoot) {
                if (nodesCovered.contains(parentId.getPersonId())) {
                    roots = new HashSet<>();
                    roots.add(parentId.getPersonId());
                    return;
                }
            } else
                nodesCovered.add(parentId.getPersonId());
            findRoot(familyTree, parentId.getPersonId(), checkForRoot);
        }


        return;
    }

    /**
     * Finds the position of person in the family tree.
     *
     * @param root
     * @param personRelationsMap
     * @param personId
     * @param currentIndex
     * @return
     */
    private int findPositionOfNode(int root, Map<Integer, PersonRelations> personRelationsMap, int personId, int currentIndex) {
        var currentNode = personRelationsMap.get(root);

        //Find the given person inside the persons map.
        currentNode = personRelationsMap.get(root);

        if (currentNode == null)
            return -1;
        if (currentNode.getPersonId() == personId || (currentNode.getChildren() != null && currentNode.getChildren().stream().anyMatch(x -> x.getPersonId() == personId)))
            return currentIndex;

        currentIndex++;
        for (var newRoot : currentNode.getChildren()) {
            int index = findPositionOfNode(newRoot.getPersonId(), personRelationsMap, personId, currentIndex);
            if (index != -1)
                return index;
        }

        return -1;
    }
}
