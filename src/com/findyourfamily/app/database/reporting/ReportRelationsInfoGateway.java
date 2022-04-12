package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.database.DatabaseUtility;
import com.findyourfamily.app.models.domain.DateRecord;
import com.findyourfamily.app.models.domain.Location;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.models.shared.PersonRelationshipLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Type for having reporting functionality related to a media.
 */
public class ReportRelationsInfoGateway implements IReportRelationsInfoGateway {

    //Constants for storing stored procedures call.
    private String GETPERSONRELATIONSSQUERY = "{CALL get_personRelations(?)}";
    private String FINDPERSONBYIDSQUERY = "{CALL find_personById(?)}";
    private Connection _connection;

    public ReportRelationsInfoGateway() {
        _connection = DatabaseUtility.getConnection();
    }

    /**
     * Get information related to a person, like parents, partner and children.
     *
     * @param personId
     * @return gets the list of person relations information.
     */
    @Override
    public List<PersonRelationshipLite> getPersonRelations(int personId) {
        try {
            var cs = _connection.prepareCall(GETPERSONRELATIONSSQUERY);
            cs.setInt(1, personId);
            var result = cs.executeQuery();

            var personRelations = new ArrayList<PersonRelationshipLite>();

            if (result == null)
                return null;

            while (result.next()) {
                var personRelation = new PersonRelationshipLite(
                        result.getInt(1), result.getInt(2),
                        result.getInt(3), result.getInt(4));
                personRelations.add(personRelation);

            }

            cs.close();

            return personRelations;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Get the person information related to a person id.
     *
     * @param personId
     * @return person identity information.
     */
    @Override
    public PersonIdentityInternal getPersonById(int personId) {
        try {
            var cs = _connection.prepareCall(FINDPERSONBYIDSQUERY);
            cs.setInt(1, personId);
            var result = cs.executeQuery();

            PersonIdentityInternal personIdentity;
            while (result.next()) {
                personIdentity = new PersonIdentityInternal(result.getInt(1),
                        result.getString(2));
                personIdentity.setGender(result.getString(3));
                var birthDate = result.getDate(4);
                personIdentity.setDateOfBirth(birthDate);
                var locationOfBirth = new Location();
                locationOfBirth.setLocationName(result.getString(5));
                locationOfBirth.setCity(result.getString(6));
                locationOfBirth.setProvince(result.getString(7));
                locationOfBirth.setCountry(result.getString(8));
                personIdentity.setLocationOfBirth(locationOfBirth);
                var deathDate = result.getDate(9);
                personIdentity.setDateOfDeath(deathDate);
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
            return null;
        }
        return null;
    }
}
