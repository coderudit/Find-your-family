package com.findyourfamily.app.business.reporting;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;

import java.io.IOException;
import java.util.List;

/**
 * Type for reporting features related to a person.
 */
public interface IReportPersonInfo {

    /**
     * Finds a person from the external source with name. If there are multiple entries for the same name,
     * one returned by the external source is used.
     *
     * @param name
     * @return internal type of person
     * @throws IOException
     */
    PersonIdentityInternal findPerson(String name) throws IOException;

    /**
     * Finds the name corresponding to the person's id.
     *
     * @param id
     * @return name of the person for a given id.
     * @throws IOException
     */
    String findName(int id) throws IOException;

    /**
     * Returns list of notes and references for a given person.
     *
     * @param person
     * @return list of notes and references for a given person.
     * @throws IOException
     */
    List<String> notesAndReferences(PersonIdentityInternal person) throws IOException;


}
