package com.findyourfamily.app.business.mediaarchive;

import com.findyourfamily.app.models.domain.FileIdentifierInternal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Type for storing media and media attributes, updating people with their media and tagging media files.
 */
public interface IMediaArchive {

    /**
     * Adds media to the external source with given file location and file name.
     * If same file location is tried for adding, it returns the existing
     * file identifier.
     *
     * @param fileLocation
     * @return file identifier internal type of the added file.
     * @throws IOException
     */
    FileIdentifierInternal addMediaFile(String fileLocation) throws IOException;

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
    boolean recordMediaAttributes(FileIdentifierInternal fileIdentifier, Map<String, String> attributes) throws IOException;

    /**
     * Saves all the persons corresponding to a given file identifier.
     * If any of the person is not saved, all the changes are roll-backed.
     *
     * @param mediaId
     * @param personIds
     * @return true if all the persons were saved for a file.
     */
    public boolean peopleInMedia(int mediaId, List<Integer> personIds) throws IOException;

    /**
     * Adds tag to the current media file.
     *
     * @param mediaId
     * @param tag
     * @return
     */
    boolean tagMedia(int mediaId, String tag) throws IOException;
}
