package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.models.domain.FileIdentifierInternal;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Type for having reporting functionality related to a media.
 */
public interface IReportMediaInfoGateway {

    /**
     * Finds the media file information for a given file name.
     *
     * @param name
     * @return file identifier for a name.
     * @throws SQLException
     */
    FileIdentifierInternal findMediaFile(String name) throws SQLException;

    /**
     * Fills all the attributes types for media.
     *
     * @param fileIdentifier
     * @throws SQLException
     */
    void fillAttributes(FileIdentifierInternal fileIdentifier) throws SQLException;

    /**
     * Finds the media name for a given id.
     *
     * @param fileId
     * @return
     * @throws SQLException
     */
    String findMediaFile(int fileId) throws SQLException;

    /**
     * Finds the media by the given tag and range
     *
     * @param tag
     * @param startDate
     * @param endDate
     * @return set of files
     * @throws SQLException
     */
    Set<FileIdentifierInternal> findMediaByTag(String tag, Date startDate, Date endDate) throws SQLException;

    /**
     * Finds the media by the given location and range
     *
     * @param location
     * @param startDate
     * @param endDate
     * @return set of files
     * @throws SQLException
     */
    Set<FileIdentifierInternal> findMediaByLocation(String location, Date startDate, Date endDate) throws SQLException;

    /**
     * Finds the individual media by the given tag and range
     *
     * @param personId
     * @param startDate
     * @param endDate
     * @return list of files
     * @throws SQLException
     */
    List<FileIdentifierInternal> findIndividualsMedia(int personId, Date startDate, Date endDate) throws SQLException;

}
