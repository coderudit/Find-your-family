package com.findyourfamily.app.database.media;

import com.findyourfamily.app.models.domain.Attribute;
import com.findyourfamily.app.models.domain.FileIdentifierInternal;
import com.findyourfamily.app.models.shared.AttributeType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface type for recording information related to a media, and it's mapping
 * to person.
 */
public interface IMediaArchiveGateway {

    /**
     * Adds media to the external source with given file location and file name.
     * If same file location is tried for adding, it returns the existing
     * file identifier.
     *
     * @param fileLocation
     * @param fileName
     * @return
     * @throws SQLException
     */
    int addMedia(String fileLocation, String fileName) throws SQLException;

    /**
     * Adds a new attribute type inside the external source.
     *
     * @param attributeType
     * @return integer id of the attribute added, to be used to record attribute value.
     * @throws SQLException
     */
    int addAttribute(String attributeType) throws SQLException;

    /**
     * Get all the attribute types for a media inside the external source.
     *
     * @return list of attribute types
     * @throws SQLException
     */
    List<AttributeType> getAttributeTypes() throws SQLException;

    /**
     * Save or update media attribute inside the external source.
     *
     * @param id
     * @param attributeId
     * @param attributeValue
     * @return true if the information is saved.
     * @throws SQLException
     */
    boolean saveMediaAttribute(int id, int attributeId, String attributeValue) throws SQLException;

    /**
     * Adds a mapping between person ,and it's media file.
     * @param personIds
     * @param mediaId
     * @return true if mapping were added.
     */
    boolean addPersonMediaMapping(List<Integer> personIds, int mediaId) throws IOException;

    /**
     * Checks if the media exists in the external source.
     *
     * @param id
     * @return true if the media exists in the external source otherwise false.
     * @throws SQLException
     */
    boolean isMediaExists(int id);

    /**
     * Saver media predefined attributes and new attributes inside a transaction and fails when either of the
     * update fails.
     *
     * @param fileIdentity
     * @param attributes
     * @return true if both saves of a person and its attributes are a success.
     * @throws SQLException
     */
    boolean saveMediaAndMediaAttributes(FileIdentifierInternal fileIdentity, List<Attribute> attributes, boolean isFileIdentityUpdateRequired) throws SQLException;
}
