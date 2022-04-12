package com.findyourfamily.app.business.mediaarchive;

import com.findyourfamily.app.database.media.IMediaArchiveGateway;
import com.findyourfamily.app.database.media.MediaArchiveGateway;
import com.findyourfamily.app.models.domain.*;
import com.findyourfamily.app.common.enums.MediaAttributesEnum;
import com.findyourfamily.app.models.shared.AttributeType;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * Type for storing media and media attributes, updating people with their media and tagging media files.
 */
public class MediaArchive implements IMediaArchive {

    //Constant for storing file separators.
    private final String FILESEPARATOR1 = "/";
    private final String FILESEPARATOR2 = "\\";

    //Stores family tree database object.
    private IMediaArchiveGateway mediaArchiveDAO;

    //Stores all the attributes present inside the external source.
    private List<AttributeType> attributeTypes;

    public MediaArchive() {
        mediaArchiveDAO = new MediaArchiveGateway();
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
    @Override
    public FileIdentifierInternal addMediaFile(String fileLocation) throws IOException {

        //Separates the file location into folder and file name with extension.
        int index = fileLocation.lastIndexOf(FILESEPARATOR1);
        if (index == -1)
            index = fileLocation.lastIndexOf(FILESEPARATOR2);

        //File name with extension.
        String fileName = fileLocation.substring(index + 1);

        int id;
        try {
            id = mediaArchiveDAO.addMedia(fileLocation, fileName);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }

        return new FileIdentifierInternal(id, fileName, fileLocation);
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
    @Override
    public boolean recordMediaAttributes(FileIdentifierInternal fileIdentifier, Map<String, String> attributes) throws IOException {
        if (!mediaArchiveDAO.isMediaExists(fileIdentifier.getMediaId()))
            throw new IllegalArgumentException("Media does not exist in the system.");
        boolean result;
        boolean isUpdateFileIdentityRequired = false;
        var newAttributes = new ArrayList<Attribute>();
        for (var attribute : attributes.entrySet()) {

            //If the attribute is predefined in the database as media attribute, fill the media, to save
            //later in the database.
            result = fillPredefinedAttributes(fileIdentifier, attribute.getKey().toUpperCase(), attribute.getValue());

            //Mark update file check as true if any of its value changes. If not its saves us from an external
            //call to save it.
            if (result && !isUpdateFileIdentityRequired) {
                isUpdateFileIdentityRequired = true;
            }

            //If the attribute was not predefined, add it as a separate attribute.
            if (!result) {
                var updatedAttribute = recordAttribute(attribute.getKey(), attribute.getValue());
                newAttributes.add(updatedAttribute);
            }
        }

        //Updates the media ,and it's attributes to the database.
        try {
            return mediaArchiveDAO.saveMediaAndMediaAttributes(fileIdentifier, newAttributes, isUpdateFileIdentityRequired);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Saves all the persons corresponding to a given file identifier.
     * If any of the person is not saved, all the changes are roll-backed.
     *
     * @param mediaId
     * @param personIds
     * @return true if all the persons were saved for a file.
     */
    @Override
    public boolean peopleInMedia(int mediaId, List<Integer> personIds) throws IOException {
        if (!mediaArchiveDAO.isMediaExists(mediaId))
            throw new IllegalArgumentException("Media does not exist in the system.");
        return mediaArchiveDAO.addPersonMediaMapping(personIds, mediaId);
    }

    /**
     * Adds tag to the current media file.
     *
     * @param mediaId
     * @param tag
     * @return
     */
    @Override
    public boolean tagMedia(int mediaId, String tag) throws IOException {
        if (!mediaArchiveDAO.isMediaExists(mediaId))
            throw new IllegalArgumentException("Media does not exist in the system.");
        try {
            //Create a new record for the note.
            var newAttribute = recordAttribute(MediaAttributesEnum.TAG.name().toLowerCase(Locale.ROOT), tag);

            //Update in the external source.
            return mediaArchiveDAO.saveMediaAttribute(mediaId, newAttribute.getTypeId(), tag);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Creates a record for attribute type ,and it's value. If the attribute is encountered for the first time,
     * creates an entry for this attribute inside external source to get its type id.
     *
     * @param attribute
     * @param attributeValue
     * @return record of an attribute with its type id and value.
     * @throws IOException
     */
    private Attribute recordAttribute(String attribute, String attributeValue) throws IOException {

        //Do not add the current attribute if attribute type or value is either null or blank.
        if (attributeValue == null || attributeValue.isBlank() || attributeValue == null || attributeValue.isBlank())
            throw new IllegalArgumentException("Invalid attribute values");

        //If there is no record for the attributes get it from the database.
        if (attributeTypes == null || attributeTypes.size() == 0) {
            try {
                attributeTypes = mediaArchiveDAO.getAttributeTypes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Find if current type of attribute is added before.
        var attributeTypeResult = attributeTypes.stream().
                filter(x -> x.getDescription().toLowerCase(Locale.ROOT).equals(attribute.toLowerCase(Locale.ROOT))).findFirst().orElse(null);

        //If this type of attribute is not added before, add it to the database and local cache
        if (attributeTypeResult == null) {
            try {
                int typeId = mediaArchiveDAO.addAttribute(attribute);
                attributeTypeResult = new AttributeType(typeId, attribute);
                attributeTypes.add(attributeTypeResult);
            } catch (SQLException ex) {
                throw new IOException("Unable to add new attribute in the database.");
            }
        }
        return new Attribute(attributeTypeResult.getTypeId(), attributeValue);
    }

    /**
     * Fill pre-defined attributes for a media, if enum does not match, exception is thrown. Then, return false to
     * add it as a new attribute.
     *
     * @param fileIdentifier
     * @param attribute
     * @param attributeValue
     * @return true if a pre-defined attribute is added, return false if a new attribute needs to be added.
     * @throws ParseException
     */
    private boolean fillPredefinedAttributes(FileIdentifierInternal fileIdentifier, String attribute, String attributeValue) {
        try {
            switch (MediaAttributesEnum.valueOf(attribute)) {
                //Takes the date of picture and updates day to it.
                case DAYOFPICTURE:
                    var pictureDate1 = fileIdentifier.getDateOfPicture();
                    if (pictureDate1 == null)
                        pictureDate1 = new Date();
                    pictureDate1.setDate(Integer.parseInt(attributeValue));
                    fileIdentifier.setDateOfPicture(pictureDate1);
                    return true;

                //Takes the date of picture and updates month to it.
                case MONTHOFPICTURE:
                    var pictureDate2 = fileIdentifier.getDateOfPicture();
                    if (pictureDate2 == null)
                        pictureDate2 = new Date();
                    pictureDate2.setMonth(Integer.parseInt(attributeValue) - 1);
                    fileIdentifier.setDateOfPicture(pictureDate2);
                    return true;

                //Takes the date of picture and updates year to it.
                case YEAROFPICTURE:
                    var pictureDate3 = fileIdentifier.getDateOfPicture();
                    if (pictureDate3 == null)
                        pictureDate3 = new Date();
                    pictureDate3.setYear(Integer.parseInt(attributeValue) - 1900);
                    fileIdentifier.setDateOfPicture(pictureDate3);
                    return true;

                case MEDIANAME:
                    fileIdentifier.setMediaName(attributeValue);
                    return true;

                case MEDIALOCATION:
                    fileIdentifier.setMediaLocation(attributeValue);
                    return true;

                //Takes the location of picture and updates location name to it.
                case LOCATIONOFPICTURE:
                    var location1 = fileIdentifier.getLocationOfPicture();
                    if (location1 == null)
                        location1 = new Location();
                    location1.setLocationName(attributeValue);
                    fileIdentifier.setLocationOfPicture(location1);
                    return true;

                //Takes the location of picture and updates city to it.
                case CITYOFPICTURE:
                    var location2 = fileIdentifier.getLocationOfPicture();
                    if (location2 == null)
                        location2 = new Location();
                    location2.setCity(attributeValue);
                    fileIdentifier.setLocationOfPicture(location2);
                    return true;

                //Takes the location of picture and updates province to it.
                case PROVINCEOFPICTURE:
                    var location3 = fileIdentifier.getLocationOfPicture();
                    if (location3 == null)
                        location3 = new Location();
                    location3.setProvince(attributeValue);
                    fileIdentifier.setLocationOfPicture(location3);
                    return true;

                //Takes the location of picture and updates country to it.
                case COUNTRYOFPICTURE:
                    var location4 = fileIdentifier.getLocationOfPicture();
                    if (location4 == null)
                        location4 = new Location();
                    location4.setCountry(attributeValue);
                    fileIdentifier.setLocationOfPicture(location4);
                    return true;

                default:
                    return false;
            }
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
