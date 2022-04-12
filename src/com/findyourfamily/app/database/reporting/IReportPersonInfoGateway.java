package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Type for having reporting functionality related to a person.
 */
public interface IReportPersonInfoGateway {

    /**
     * Finds a person from the external source with name. If there are multiple entries for the same name,
     * one returned by the external source is used.
     *
     * @param name
     * @return internal type of person
     * @throws SQLException
     */
    PersonIdentityInternal findPerson(String name) throws SQLException;

    /**
     * Finds the name corresponding to the person's id.
     *
     * @param id
     * @return String name of the person
     * @throws SQLException
     */
    String findName(int id) throws SQLException;

    /**
     * Insert all the attributes related to a person. If true is passed get all the
     * attributes otherwise get only references and notes.
     *
     * @param personIdentity
     * @param allAttributesRequired
     * @return
     * @throws SQLException
     */
    Map<String, String> fillAttributes(PersonIdentityInternal personIdentity,
                                       boolean allAttributesRequired) throws SQLException;

    /**
     * Find all the persons in media
     *
     * @param mediaId
     * @return
     * @throws IOException
     */
    List<Integer> findPersonsInMedia(int mediaId);
}
