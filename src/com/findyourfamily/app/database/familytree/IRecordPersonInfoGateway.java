package com.findyourfamily.app.database.familytree;

import com.findyourfamily.app.models.domain.Attribute;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.models.shared.AttributeType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface type for recording information related to a person.
 */
public interface IRecordPersonInfoGateway {

    /**
     * Adds a person to the external source with a given name.
     * Duplicate name can be recorded with new entry created
     * for each record.
     *
     * @param name
     * @return integer id of the person added.
     * @throws SQLException
     */
    int addPerson(String name) throws SQLException;

    /**
     * Adds a new attribute type inside the external source.
     *
     * @param attributeType
     * @return integer id of the attribute added, to be used to record attribute value.
     * @throws SQLException
     */
    int addAttribute(String attributeType) throws SQLException;

    /**
     * Get all the attribute types for a person inside the external source.
     *
     * @return list of attribute types
     * @throws SQLException
     */
    List<AttributeType> getAttributeTypes();

    /**
     * Save or update person's attribute inside the external source.
     *
     * @param id
     * @param attributeId
     * @param attributeValue
     * @return true if the information is saved.
     * @throws SQLException
     */
    boolean savePersonAttribute(int id, int attributeId, String attributeValue) throws SQLException;

    /**
     * Saver person's predefined attributes and new attributes inside a transaction and fails when either of the
     * update fails.
     *
     * @param personIdentity
     * @param attributes
     * @return true if both saves of a person and its attributes are a success.
     * @throws SQLException
     */
    boolean savePersonAndPersonAttributes(PersonIdentityInternal personIdentity, List<Attribute> attributes, boolean isPersonIdentityUpdateRequired) throws SQLException;

    /**
     * Checks if the person exists in the external source.
     *
     * @param id
     * @return true if the person exists in the external source otherwise false.
     * @throws IOException
     */
    boolean isPersonExists(int id) throws IOException;
}
