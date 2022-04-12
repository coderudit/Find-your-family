package com.findyourfamily.app.database.familytree;

import com.findyourfamily.app.database.DatabaseUtility;
import com.findyourfamily.app.models.domain.Attribute;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.models.shared.AttributeType;
import com.findyourfamily.app.models.shared.PersonRelationshipLite;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation type for recording information related to a person, and it's relations.
 */
public class FamilyTreeGateway implements IFamilyTreeGateway {

    //Field for storing the database connection.
    private Connection _connection;

    //Constants for storing database stored procedures calls.
    private final String ADDPERSONQUERY = "{CALL add_person(?, ?)}";
    private final String UPDATEPERSONQUERY = "{CALL update_person(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private final String ADDPERSONATTRIBUTETYPEQUERY = "{CALL add_personAttributeType(?, ?)}";
    private final String GETPERSONATTRIBUTETYPESQUERY = "{CALL get_personAttributeTypes()}";
    private final String ADDPERSONATTRIBUTEQUERY = "{CALL add_personAttribute(?, ?, ?)}";
    private final String ISPERSONEXISTSQUERY = "{CALL is_personExists(?, ?)}";
    private final String ADDRELATIONSQUERY = "{CALL add_relations(?, ?, ?, ?)}";

    public FamilyTreeGateway() {
        _connection = DatabaseUtility.getConnection();
    }

    /**
     * Adds a person to the external source with a given name.
     * Duplicate name can be recorded with new entry created
     * for each record.
     *
     * @param name
     * @return integer id of the person added.
     * @throws SQLException
     */
    @Override
    public int addPerson(String name) throws SQLException {
        try {
            var cs = _connection.prepareCall(ADDPERSONQUERY);
            cs.setString(1, name);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.executeQuery();

            //Gets the person id from the out parameter.
            var id = cs.getInt(2);
            cs.close();

            return id;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * Adds a new attribute type inside the external source.
     *
     * @param attributeType
     * @return integer id of the attribute added, to be used to record attribute value.
     * @throws SQLException
     */
    @Override
    public int addAttribute(String attributeType) throws SQLException {
        try {
            var cs = _connection.prepareCall(ADDPERSONATTRIBUTETYPEQUERY);
            cs.setString(1, attributeType);
            cs.executeQuery();
            var id = cs.getInt(2);

            cs.close();

            return id;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * Get all the attribute types for a person inside the external source.
     *
     * @return list of attribute types
     * @throws SQLException
     */
    @Override
    public List<AttributeType> getAttributeTypes() {
        try {
            var cs = _connection.prepareCall(GETPERSONATTRIBUTETYPESQUERY);
            var result = cs.executeQuery();

            if (result == null)
                return null;

            var attributeTypes = new ArrayList<AttributeType>();

            while (result.next()) {
                var attributeType = new AttributeType(result.getInt(1),
                        result.getString(2));
                attributeTypes.add(attributeType);
            }

            cs.close();

            return attributeTypes;
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Save or update person's attribute inside the external source.
     *
     * @param id
     * @param attributeId
     * @param attributeValue
     * @return true if the information is saved.
     * @throws SQLException
     */
    public boolean savePersonAttribute(int id, int attributeId, String attributeValue) throws SQLException {
        try {
            var cs = _connection.prepareCall(ADDPERSONATTRIBUTEQUERY);
            cs.setInt(1, id);
            cs.setInt(2, attributeId);
            cs.setString(3, attributeValue);

            cs.executeQuery();
            cs.close();

            return true;

        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * Saver person's predefined attributes and new attributes inside a transaction and fails when either of the
     * update fails.
     *
     * @param personIdentity
     * @param attributes
     * @return true if both saves of a person and its attributes are a success.
     * @throws SQLException
     */
    @Override
    public boolean savePersonAndPersonAttributes(PersonIdentityInternal personIdentity, List<Attribute> attributes,
                                                 boolean isPersonIdentityUpdateRequired) throws SQLException {
        try {
            //Starts a transaction
            _connection.setAutoCommit(false);

            if (isPersonIdentityUpdateRequired)
                //Updates pre-defined attributes of a person.
                updatePerson(personIdentity);

            //Updates extra attributes of a person.
            for (var attribute : attributes) {
                savePersonAttribute(personIdentity.getPersonId(), attribute.getTypeId(), attribute.getTypeValue());
            }
            //Commit the transaction, if everything was committed.
            _connection.commit();
        } catch (SQLException exception) {
            _connection.rollback();
            throw exception;
        }
        return true;
    }

    /**
     * Update the person in the external source with the updated pre-defined attributes.
     *
     * @param personIdentity
     * @return true if the person was successfully updated.
     * @throws SQLException
     */
    private boolean updatePerson(PersonIdentityInternal personIdentity) throws SQLException {
        try {
            var cs = _connection.prepareCall(UPDATEPERSONQUERY);
            cs.setInt(1, personIdentity.getPersonId());
            cs.setString(2, personIdentity.getName());
            cs.setString(3, personIdentity.getGender());
            cs.setDate(4, personIdentity.getDateOfBirth() == null ? null : new Date(personIdentity.getDateOfBirth().getYear(),
                    personIdentity.getDateOfBirth().getMonth(), personIdentity.getDateOfBirth().getDate()));
            cs.setString(5, personIdentity.getLocationOfBirth().getLocationName());
            cs.setDate(6, personIdentity.getDateOfDeath() == null ? null : new Date(personIdentity.getDateOfBirth().getYear(),
                    personIdentity.getDateOfBirth().getMonth(), personIdentity.getDateOfBirth().getDate()));
            cs.setString(7, personIdentity.getLocationOfDeath().getLocationName());
            cs.setString(8, personIdentity.getOccupation());
            cs.setString(9, personIdentity.getLocationOfBirth().getCity());
            cs.setString(10, personIdentity.getLocationOfBirth().getProvince());
            cs.setString(11, personIdentity.getLocationOfBirth().getCountry());
            cs.setString(12, personIdentity.getLocationOfDeath().getCity());
            cs.setString(13, personIdentity.getLocationOfDeath().getProvince());
            cs.setString(14, personIdentity.getLocationOfDeath().getCountry());

            cs.executeQuery();
            cs.close();

            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * Checks if the person exists in the external source.
     *
     * @param id
     * @return true if the person exists in the external source otherwise false.
     * @throws SQLException
     */
    @Override
    public boolean isPersonExists(int id) throws IOException {
        try {
            var cs = _connection.prepareCall(ISPERSONEXISTSQUERY);
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER);

            var result = cs.getInt(2);
            cs.close();

            return result == 1 ? true : false;

        } catch (SQLException ex) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Generic method for recording list of relationships that should exist between 2 persons. If any of them fails,
     * nothing gets saved.
     *
     * @param personRelationshipLiteList
     * @return true if the relationship was recorded.
     * @throws SQLException
     */
    @Override
    public boolean recordRelationship(List<PersonRelationshipLite> personRelationshipLiteList) throws SQLException {
        try {
            //Starts a transaction for recording relationship.
            _connection.setAutoCommit(false);

            for (var personRelationshipLite : personRelationshipLiteList) {

                var cs = _connection.prepareCall(ADDRELATIONSQUERY);
                cs.setInt(1, personRelationshipLite.getPerson1Id());
                cs.setInt(2, personRelationshipLite.getPerson2Id());
                cs.setInt(3, personRelationshipLite.getRelationshipType());
                cs.registerOutParameter(4, Types.INTEGER);
                cs.executeQuery();

                //Gets the person id from the out parameter.
                var id = cs.getInt(4);
                if (id == -1) {
                    _connection.rollback();
                    return false;
                }
                cs.close();
            }

            //Commit if all the recording went through.
            _connection.commit();

        } catch (SQLException ex) {
            _connection.rollback();
            throw ex;
        }
        return true;
    }
}
