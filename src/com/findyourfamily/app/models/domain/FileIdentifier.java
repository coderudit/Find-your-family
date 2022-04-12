package com.findyourfamily.app.models.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * External type for storing information related to file.
 */
public class FileIdentifier {

    //Stores the media id.
    private int mediaId;

    //Stores the media id.
    private String mediaName;

    //Stores the media id.
    private String mediaLocation;

    //Stores the media id.
    private Location locationOfPicture;

    //Stores the media id.
    private Date dateOfPicture;

    //Stores the media id.
    private List<String> tags;

    //Stores the media id.
    private Map<String, String> attributes;

    //Store the persons found in media.
    private List<String> personsInMedia;

    /**
     * Gets the media id
     * @return media id
     */
    public int getMediaId() {
        return mediaId;
    }

    /**
     * Sets the media id.
     * @param mediaId for the media
     */
    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * Gets the media name
     * @return media name
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Sets the mediaName.
     * @param mediaName for the media
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * Gets the location as a String for the media.
     * @return media location
     */
    public String getMediaLocation() {
        return mediaLocation;
    }

    /**
     * Sets the mediaLocation.
     * @param mediaLocation for the media
     */
    public void setMediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    /**
     * Gets the location of the picture for the media.
     * @return media location of picture
     */
    public Location getLocationOfPicture() {
        return locationOfPicture;
    }

    /**
     * Sets the locationOfPicture.
     * @param locationOfPicture for the media
     */
    public void setLocationOfPicture(Location locationOfPicture) {
        this.locationOfPicture = locationOfPicture;
    }

    /**
     * Gets the date of the picture taken for the media.
     * @return media date
     */
    public Date getDateOfPicture() {
        return dateOfPicture;
    }

    /**
     * Sets the dateOfPicture.
     * @param dateOfPicture for the media
     */
    public void setDateOfPicture(Date dateOfPicture) {
        this.dateOfPicture = dateOfPicture;
    }

    /**
     * Gets the tags for the media.
     * @return media tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tags.
     * @param tags for the media
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gets the attributes for the media.
     * @return map of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * @param attributes for the media
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Get all the persons in a media
     * @return person id's
     */
    public List<String> getPersonsInMedia() {
        return personsInMedia;
    }

    /**
     * Sets the persons in the media
     * @param personsInMedia
     */
    public void setPersonsInMedia(List<String> personsInMedia) {
        this.personsInMedia = personsInMedia;
    }
}
