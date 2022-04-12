package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.database.DatabaseUtility;
import com.findyourfamily.app.models.domain.DateRecord;
import com.findyourfamily.app.models.domain.FileIdentifierInternal;
import com.findyourfamily.app.models.domain.Location;
import com.findyourfamily.app.common.enums.MediaAttributesEnum;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Type for having reporting functionality related to a media.
 */
public class ReportMediaInfoGateway implements IReportMediaInfoGateway {

    //Constants for storing stored procedures call.
    private final String FINDMEDIAQUERY = "{CALL find_mediaFile(?)}";
    private final String FINDMEDIA_FILENAMEQUERY = "{CALL find_mediaFileName(?)}";
    private final String FINDMEDIA_BYTAGQUERY = "CALL find_mediaByTag(?,?,?)";
    private final String FINDMEDIA_BYLOCATIONQUERY = "CALL find_mediaByLocation(?,?,?)";
    private final String FINDMEDIA_BYPERSONQUERY = "CALL find_mediaByPerson(?,?,?)";
    private String GETMEDIAATTRIBUTESQUERY = "{CALL get_mediaAttributes(?)}";

    //Stores database connection.
    private Connection _connection;

    public ReportMediaInfoGateway() {
        _connection = DatabaseUtility.getConnection();
    }

    /**
     * Finds the media file information for a given file name.
     *
     * @param name
     * @return file identifier for a name.
     * @throws SQLException
     */
    @Override
    public FileIdentifierInternal findMediaFile(String name) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDMEDIAQUERY);
            cs.setString(1, name);
            var result = cs.executeQuery();

            if (result == null)
                return null;

            FileIdentifierInternal fileIdentifier;
            while (result.next()) {
                fileIdentifier = new FileIdentifierInternal(result.getInt(1),
                        result.getString(2),
                        result.getString(3));

                var pictureDate = result.getDate(4);
                if (pictureDate != null)
                    fileIdentifier.setDateOfPicture(pictureDate);

                var locationOfPicture = new Location();
                locationOfPicture.setLocationName(result.getString(5));
                locationOfPicture.setCity(result.getString(6));
                locationOfPicture.setProvince(result.getString(7));
                locationOfPicture.setCountry(result.getString(8));

                cs.close();

                return fileIdentifier;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
            //throw new IOException("Unable to make a connection to the database.");
        }
        return null;
    }

    /**
     * Fills all the attributes types for media.
     *
     * @param fileIdentifier
     * @throws SQLException
     */
    @Override
    public void fillAttributes(FileIdentifierInternal fileIdentifier) throws SQLException {
        try {
            var cs = _connection.prepareCall(GETMEDIAATTRIBUTESQUERY);
            cs.setInt(1, fileIdentifier.getMediaId());
            var result = cs.executeQuery();
            Map<String, String> attributes = new HashMap<>();
            List<String> tags = new ArrayList<>();

            if (result == null)
                return;

            while (result.next()) {
                if (result.getString(1) != null &&
                        result.getString(2) != null) {
                    if (result.getString(1).equals(MediaAttributesEnum.TAG.name().toLowerCase(Locale.ROOT))) {
                        tags.add(result.getString(2));
                    } else {
                        attributes.put(result.getString(1),
                                result.getString(2));
                    }
                }
            }

            cs.close();
            fileIdentifier.setTags(tags);
            fileIdentifier.setAttributes(attributes);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Finds the media name for a given id.
     *
     * @param fileId
     * @return
     * @throws SQLException
     */
    @Override
    public String findMediaFile(int fileId) {
        try {
            var cs = _connection.prepareCall(FINDMEDIA_FILENAMEQUERY);
            cs.setInt(1, fileId);
            var result = cs.executeQuery();

            if (result == null)
                return null;

            String name = "";
            while (result.next()) {
                name = result.getString(MediaAttributesEnum.MEDIANAME.name());

                cs.close();

                return name;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            //throw new IOException("Unable to make a connection to the database.");
        }
        return null;
    }

    /**
     * Find all the media files by tag according to the given range.
     *
     * @param tag
     * @param startDate
     * @param endDate
     * @return set of files for a given date range and according to tag.
     */
    @Override
    public Set<FileIdentifierInternal> findMediaByTag(String tag, Date startDate, Date endDate) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDMEDIA_BYTAGQUERY);
            cs.setString(1, tag);
            if (startDate == null)
                cs.setDate(2, null);
            else
                cs.setDate(2, new java.sql.Date(startDate.getTime()));
            if (endDate == null)
                cs.setDate(3, null);
            else
                cs.setDate(3, new java.sql.Date(endDate.getTime()));

            return findMediaByParam(cs).stream().collect(Collectors.toSet());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Find all the media files by location according to the given range.
     *
     * @param location
     * @param startDate
     * @param endDate
     * @return set of files for a given date range and according to location.
     * @throws SQLException
     */
    @Override
    public Set<FileIdentifierInternal> findMediaByLocation(String location, Date startDate, Date endDate) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDMEDIA_BYLOCATIONQUERY);
            cs.setString(1, location);
            if (startDate == null)
                cs.setDate(2, null);
            else
                cs.setDate(2, new java.sql.Date(startDate.getTime()));
            if (endDate == null)
                cs.setDate(3, null);
            else
                cs.setDate(3, new java.sql.Date(endDate.getTime()));

            return findMediaByParam(cs).stream().collect(Collectors.toSet());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Find distinct media for all the individuals within a given time frame. If no start or end date
     * is passed find all the files without restricting to time frame.
     *
     * @param personId
     * @param startDate
     * @param endDate
     * @return the list of files for the individual.
     */
    @Override
    public List<FileIdentifierInternal> findIndividualsMedia(int personId, Date startDate, Date endDate) throws SQLException {
        try {
            var cs = _connection.prepareCall(FINDMEDIA_BYPERSONQUERY);
            cs.setInt(1, personId);
            if (startDate == null)
                cs.setDate(2, null);
            else
                cs.setDate(2, new java.sql.Date(startDate.getTime()));
            if (endDate == null)
                cs.setDate(3, null);
            else
                cs.setDate(3, new java.sql.Date(endDate.getTime()));

            return findMediaByParam(cs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Find media according to the given callable statement created.
     * Currently, it has person id, location or tag.
     *
     * @param cs
     * @return the list of files for the individual.
     * @throws SQLException
     */
    private List<FileIdentifierInternal> findMediaByParam(CallableStatement cs) throws SQLException {
        var result = cs.executeQuery();

        if (result == null)
            return null;

        var files = new ArrayList<FileIdentifierInternal>();
        FileIdentifierInternal fileIdentifier;

        //Loops through all the files and creates a list for i.
        while (result.next()) {
            fileIdentifier = new FileIdentifierInternal(result.getInt(1),
                    result.getString(2),
                    result.getString(3));

            var pictureDate = result.getDate(4);
            if (pictureDate != null)
                fileIdentifier.setDateOfPicture(pictureDate);

            var locationOfPicture = new Location();
            locationOfPicture.setLocationName(result.getString(5));
            locationOfPicture.setCity(result.getString(6));
            locationOfPicture.setProvince(result.getString(7));
            locationOfPicture.setCountry(result.getString(8));
            files.add(fileIdentifier);
        }
        cs.close();
        return files;
    }

}
