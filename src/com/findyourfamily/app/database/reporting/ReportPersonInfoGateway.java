package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.database.DatabaseUtility;
import com.findyourfamily.app.models.domain.DateRecord;
import com.findyourfamily.app.models.domain.Location;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.common.enums.PersonAttributesEnum;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Type for having reporting functionality related to a person.
 */
public class ReportPersonInfoGateway implements IReportPersonInfoGateway {

    //Stores database connection.
    private Connection _connection;

    //Constants for storing stored procedures call.
    private final String FINDPERSONQUERY = "{CALL find_person(?)}";
    private final String FINDNAMEQUERY = "{CALL find_name(?)}";
    private final String GETPERSONATTRIBUTESQUERY = "{CALL get_personAttributes(?, ?)}";
    private final String FINDPERSONSINMEDIAQUERY = "{CALL find_personInMedia(?)}";

    public ReportPersonInfoGateway() {
        _connection = DatabaseUtility.getConnection();
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
    public PersonIdentityInternal findPerson(String name) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDPERSONQUERY);
            cs.setString(1, name);
            var result = cs.executeQuery();

            if (result == null)
                return null;

            PersonIdentityInternal personIdentity;
            while (result.next()) {
                personIdentity = new PersonIdentityInternal(result.getInt(1),
                        result.getString(2));
                personIdentity.setGender(result.getString(3));

                var birthDate = result.getDate(4);
                if (birthDate != null)
                    personIdentity.setDateOfBirth(birthDate);
                    //personIdentity.setBirthDateRecord(new DateRecord(birthDate.getDay(), birthDate.getMonth(), birthDate.getYear()));

                var locationOfBirth = new Location();
                locationOfBirth.setLocationName(result.getString(5));
                locationOfBirth.setCity(result.getString(6));
                locationOfBirth.setProvince(result.getString(7));
                locationOfBirth.setCountry(result.getString(8));
                personIdentity.setLocationOfBirth(locationOfBirth);

                var deathDate = result.getDate(9);
                if (deathDate != null)
                    personIdentity.setDateOfDeath(deathDate);
                    //personIdentity.setDeathDateRecord(new DateRecord(deathDate.getDay(), deathDate.getMonth(), deathDate.getYear()));

                var locationOfDeath = new Location();
                locationOfDeath.setLocationName(result.getString(10));
                locationOfDeath.setCity(result.getString(11));
                locationOfDeath.setProvince(result.getString(12));
                locationOfDeath.setCountry(result.getString(13));

                personIdentity.setLocationOfDeath(locationOfDeath);
                personIdentity.setOccupation(result.getString(14));

                cs.close();

                return personIdentity;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return null;
    }

    /**
     * Finds the name corresponding to the person's id.
     *
     * @param id
     * @return String name of the person
     * @throws SQLException
     */
    @Override
    public String findName(int id) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDNAMEQUERY);
            cs.setInt(1, id);
            var result = cs.executeQuery();

            if (result == null)
                return null;

            String name;
            while (result.next()) {
                name = result.getString(1);
                cs.close();

                return name;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return null;
    }

    /**
     * Insert all the attributes related to a person. If true is passed get all the
     * attributes otherwise get only references and notes.
     *
     * @param personIdentity
     * @param allAttributesRequired
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, String> fillAttributes(PersonIdentityInternal personIdentity, boolean allAttributesRequired) {
        try {
            var cs = _connection.prepareCall(GETPERSONATTRIBUTESQUERY);
            cs.setInt(1, personIdentity.getPersonId());
            cs.setInt(2, allAttributesRequired ? 1 : 0);
            var result = cs.executeQuery();
            Map<String, String> attributes = new HashMap<>();

            personIdentity.setNotes(new ArrayList<>());
            personIdentity.setReferences(new ArrayList<>());
            personIdentity.setAdditionalAttribute(new HashMap<>());

            if (result == null)
                return null;

            while (result.next()) {
                if (result.getString(1) != null &&
                        result.getString(2) != null) {

                    var attributeType = result.getString(1);

                    //Add to reference as well as to list of notes and references.
                    if (attributeType.toLowerCase(Locale.ROOT).equals(PersonAttributesEnum.REFERENCES.name().toLowerCase(Locale.ROOT))) {
                        personIdentity.addReference(result.getString(2));
                        personIdentity.addNotesAndReferences(result.getString(2));
                    }  //Add to note as well as to list of notes and references.
                    else if (attributeType.toLowerCase(Locale.ROOT).equals(PersonAttributesEnum.NOTES.name().toLowerCase(Locale.ROOT))) {
                        personIdentity.addNote(result.getString(2));
                        personIdentity.addNotesAndReferences(result.getString(2));
                    } else //Otherwise add as an additional attribute.
                    {
                        personIdentity.addAdditionalAttribute(attributeType,
                                result.getString(2));
                    }

                }
            }

            cs.close();

            return attributes;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Integer> findPersonsInMedia(int mediaId) {
        try {
            var cs = _connection.prepareCall(FINDPERSONSINMEDIAQUERY);
            cs.setInt(1, mediaId);

            var result = cs.executeQuery();

            List<Integer> persons = new ArrayList<>();
            while (result.next()) {
                persons.add(result.getInt(1));
            }

            return persons;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


}
