package com.findyourfamily.app.models.domain;

import java.util.*;

public class FileIdentifierInternal implements Comparable<FileIdentifierInternal> {
    private int mediaId;
    private String mediaName;
    private String mediaLocation;
    private Location locationOfPicture;
    private Date dateOfPicture;
    private List<String> tags;
    private Map<String, String> attributes;
    private List<String> personsInMedia;

    public FileIdentifierInternal(int mediaId, String mediaName, String mediaLocation) {
        this.mediaId = mediaId;
        this.mediaName = mediaName;
        this.mediaLocation = mediaLocation;
    }

    /**
     * Gets the media id
     *
     * @return media id
     */
    public int getMediaId() {
        return mediaId;
    }

    /**
     * Gets the media name
     *
     * @return media name
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Sets the mediaName.
     *
     * @param mediaName for the media
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * Gets the location as a String for the media.
     *
     * @return media location
     */
    public String getMediaLocation() {
        return mediaLocation;
    }

    /**
     * Sets the mediaLocation.
     *
     * @param mediaLocation for the media
     */
    public void setMediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    /**
     * Gets the location of the picture for the media.
     *
     * @return media location of picture
     */
    public Location getLocationOfPicture() {
        return locationOfPicture;
    }

    /**
     * Sets the locationOfPicture.
     *
     * @param locationOfPicture for the media
     */
    public void setLocationOfPicture(Location locationOfPicture) {
        this.locationOfPicture = locationOfPicture;
    }

    /**
     * Sets the dateOfPicture.
     *
     * @param date for the media
     */
    public void setDateOfPicture(Date date) {
        this.dateOfPicture = date;
    }

    /**
     * Gets the date of the picture taken for the media.
     *
     * @return media date
     */
    public Date getDateOfPicture() {
        return this.dateOfPicture;
    }

    /**
     * Gets the attributes for the media.
     *
     * @return map of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Gets the tags for the media.
     *
     * @return media tags
     */
    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        if (this.tags == null)
            this.tags = new ArrayList<>();
        this.tags.add(tag);
    }

    /**
     * Sets the tags.
     *
     * @param tags for the media
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes for the media
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Get all the persons in a media.
     *
     * @return person id's
     */
    public List<String> getPersonsInMedia() {
        return personsInMedia;
    }

    /**
     * Add person to list of persons in the media.
     *
     * @param person
     */
    public void addPersonsInMedia(String person) {
        if (this.personsInMedia == null)
            this.personsInMedia = new ArrayList<>();
        this.personsInMedia.add(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileIdentifierInternal)) return false;
        FileIdentifierInternal that = (FileIdentifierInternal) o;
        return getMediaId() == that.getMediaId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMediaId());
    }

    @Override
    public int compareTo(FileIdentifierInternal o) {
        if (this.getDateOfPicture() == null || o.getDateOfPicture() == null)
            return -2;
        return this.getDateOfPicture().compareTo(o.getDateOfPicture());
    }
}
