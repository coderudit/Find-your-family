package com.findyourfamily.app.business.reporting;

import com.findyourfamily.app.models.domain.FileIdentifierInternal;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * Interface type for reporting features related to media.
 */
public interface IReportMediaInfo {

    /**
     * Finds media file information according to the given name.
     *
     * @param name
     * @return find file information.
     * @throws IOException
     */
    FileIdentifierInternal findMediaFile(String name) throws IOException;

    /**
     * Finds the name of the media file for a given id.
     *
     * @param fileId
     * @return name for the given media id.
     * @throws IOException
     */
    String findMediaFile(int fileId) throws IOException;

    /**
     * Finds the media files having a given tag and lying withing the given range.
     *
     * @param tag
     * @param startDate
     * @param endDate
     * @return set of distinct files.
     * @throws IOException
     * @throws ParseException
     */
    Set<FileIdentifierInternal> findMediaByTag(String tag, String startDate, String endDate) throws IOException, ParseException;

    /**
     * Finds the media files having a given location and lying withing the given range.
     *
     * @param location
     * @param startDate
     * @param endDate
     * @return set of distinct files.
     * @throws IOException
     * @throws ParseException
     */
    Set<FileIdentifierInternal> findMediaByLocation(String location, String startDate, String endDate) throws IOException, ParseException;

    /**
     * Finds the media files for given set of people and lying withing the given range.
     *
     * @param people
     * @param startDate
     * @param endDate
     * @return set of distinct files for a given set of people.
     * @throws IOException
     * @throws ParseException
     */
    Set<FileIdentifierInternal> findIndividualsMedia(Set<PersonIdentityInternal> people, String startDate, String
            endDate) throws IOException, ParseException;

    /**
     * Finds the media files for the given set of people.
     * @param people
     * @return
     * @throws IOException
     */
    List<FileIdentifierInternal> findBiologicalFamilyMedia(Set<Integer> people) throws IOException;
}
