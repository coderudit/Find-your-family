package com.findyourfamily.app.business.familytree;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;

import java.io.IOException;
import java.util.Map;

/**
 * Interface type for recording information related to a person.
 */
public interface IRecordPersonInfo {

    /**
     * Adds a person to the external source with a given name.
     * Duplicate name can be recorded with new entry created
     * for each record.
     *
     * @param name
     * @return Person Identity type
     * @throws IOException
     */
    PersonIdentityInternal addPerson(String name) throws IOException;

    /**
     * Record attributes against a given person in the external source.
     * Predefined attributes inside PersonIdentity type will be updated, and
     * new attributes will be created inside the external source. New attributes
     * when recorded multiple times will return the latest value.
     *
     * @param person
     * @param attributes
     * @return true if all the attributes are added, even when single attribute fails
     * it returns falls but still all the other attributes are added.
     * @throws IOException
     */
    boolean recordAttributes(PersonIdentityInternal person, Map<String, String> attributes) throws IOException;

    /**
     * Record reference against a person in the external source.
     * Current date and time is added by default to help retrieve
     * them according to it.
     *
     * @param personId
     * @param reference
     * @return
     * @throws IOException
     */
    boolean recordReference(int personId, String reference) throws IOException;

    /**
     * Record note against a person in the external source.
     * Current date and time is added by default to help retrieve
     * them according to it.
     *
     * @param personId
     * @param note
     * @return
     * @throws IOException
     */
    boolean recordNote(int personId, String note) throws IOException;
}
