package com.findyourfamily.app.database.media;

import com.findyourfamily.app.database.DatabaseUtility;
import com.findyourfamily.app.models.domain.Attribute;
import com.findyourfamily.app.models.domain.FileIdentifierInternal;
import com.findyourfamily.app.models.shared.AttributeType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MediaArchiveGateway implements IMediaArchiveGateway {

    //Field for storing the database connection.
    private Connection _connection;

    //Constants for storing database stored procedures calls.
    private final String ADDMEDIAQUERY = "{CALL add_media(?, ?, ?)}";
    private final String UPDATEMEDIAQUERY = "{CALL update_media(?, ?, ?, ?, ?, ?, ?, ?)}";
    private String GETMEDIAATTRIBUTETYPESQUERY = "{CALL get_mediaAttributeTypes()}";
    private String ADDMEDIAATTRIBUTEQUERY = "{CALL add_mediaAttribute(?, ?, ?)}";
    private String ADDMEDIAATTRIBUTETYPEQUERY = "CALL add_mediaAttributeType(?,?)";
    private String ADDPERSONMEDIAQUERY = "CALL add_personmedia(?,?)";
    private final String ISMEDIAEXISTSQUERY = "{CALL is_mediaExists(?, ?)}";

    public MediaArchiveGateway() {
        _connection = DatabaseUtility.getConnection();
    }

    /**
     * Adds media to the external source with given file location and file name.
     * If same file location is tried for adding, it returns the existing
     * file identifier.
     *
     * @param fileLocation
     * @param fileName
     * @return id of the added file.
     * @throws SQLException
     */
    @Override
    public int addMedia(String fileLocation, String fileName) throws SQLException {
        try {
            var cs = _connection.prepareCall(ADDMEDIAQUERY);
            cs.setString(1, fileLocation);
            cs.setString(2, fileName);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.executeQuery();

            var id = cs.getInt(3);
            cs.close();

            return id;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Updates media values inside the external source.
     *
     * @param fileIdentifier
     * @return true if all the attributes were updated.
     * @throws SQLException
     */
    private boolean updateMedia(FileIdentifierInternal fileIdentifier) throws SQLException {
        try {
            var cs = _connection.prepareCall(UPDATEMEDIAQUERY);
            cs.setInt(1, fileIdentifier.getMediaId());
            cs.setString(2, fileIdentifier.getMediaName());
            cs.setString(3, fileIdentifier.getMediaLocation());
            cs.setDate(4, fileIdentifier.getDateOfPicture() == null ? null : new Date(fileIdentifier.getDateOfPicture().getYear(),
                    fileIdentifier.getDateOfPicture().getMonth(), fileIdentifier.getDateOfPicture().getDate()));
            cs.setString(5, fileIdentifier.getLocationOfPicture().getLocationName());
            cs.setString(6, fileIdentifier.getLocationOfPicture().getCity());
            cs.setString(7, fileIdentifier.getLocationOfPicture().getProvince());
            cs.setString(8, fileIdentifier.getLocationOfPicture().getCountry());

            cs.executeQuery();
            cs.close();

            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
            var cs = _connection.prepareCall(ADDMEDIAATTRIBUTETYPEQUERY);
            cs.setString(1, attributeType);
            cs.executeQuery();
            var id = cs.getInt(2);

            cs.close();

            return id;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Get all the attribute types for a media inside the external source.
     *
     * @return list of attribute types
     * @throws SQLException
     */
    @Override
    public List<AttributeType> getAttributeTypes() {
        try {
            var cs = _connection.prepareCall(GETMEDIAATTRIBUTETYPESQUERY);
            var result = cs.executeQuery();
            List<AttributeType> attributeTypes = new ArrayList<>();

            if (result == null)
                return null;

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
     * Save or update media attribute inside the external source.
     *
     * @param id
     * @param attributeId
     * @param attributeValue
     * @return true if the information is saved.
     * @throws SQLException
     */
    @Override
    public boolean saveMediaAttribute(int id, int attributeId, String attributeValue) throws SQLException {
        try {
            var cs = _connection.prepareCall(ADDMEDIAATTRIBUTEQUERY);
            cs.setInt(1, id);
            cs.setInt(2, attributeId);
            cs.setString(3, attributeValue);

            cs.executeQuery();
            cs.close();

            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Adds a mapping between person ,and it's media file.
     *
     * @param personIds
     * @param mediaId
     * @return true if mapping were added.
     */
    @Override
    public boolean addPersonMediaMapping(List<Integer> personIds, int mediaId) throws IOException {
        try {
            for (var personId : personIds) {
                _connection.setAutoCommit(false);
                var cs = _connection.prepareCall(ADDPERSONMEDIAQUERY);
                cs.setInt(1, personId);
                cs.setInt(2, mediaId);
                cs.executeQuery();

                cs.close();

            }
            _connection.commit();
            return true;
        } catch (SQLException ex) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Checks if the media exists in the external source.
     *
     * @param id
     * @return true if the media exists in the external source otherwise false.
     * @throws SQLException
     */
    @Override
    public boolean isMediaExists(int id) {
        try {
            var cs = _connection.prepareCall(ISMEDIAEXISTSQUERY);
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER);

            var result = cs.getInt(2);
            cs.close();

            return result == 1 ? true : false;

        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Saver media predefined attributes and new attributes inside a transaction and fails when either of the
     * update fails.
     *
     * @param fileIdentity
     * @param attributes
     * @return true if both saves of a person and its attributes are a success.
     * @throws SQLException
     */
    @Override
    public boolean saveMediaAndMediaAttributes(FileIdentifierInternal fileIdentity, List<Attribute> attributes, boolean isFileIdentityUpdateRequired) throws SQLException {
        try {
            _connection.setAutoCommit(false);

            if (isFileIdentityUpdateRequired)
                //Updates pre-defined attributes of a media.
                updateMedia(fileIdentity);

            //Updates extra attributes of a media.
            for (var attribute : attributes) {
                saveMediaAttribute(fileIdentity.getMediaId(), attribute.getTypeId(), attribute.getTypeValue());
            }
            _connection.commit();
        } catch (SQLException exception) {
            _connection.rollback();
            throw exception;
        }
        return true;
    }
}
