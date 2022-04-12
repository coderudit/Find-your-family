package com.findyourfamily.app.models.domain;

/**
 * Type for storing only person's id.
 */
public class PersonIdentityLite {

    //Stores person id
    private int personId;

    public PersonIdentityLite(int personId) {
        this.personId = personId;
    }

    /**
     * Gets the person id.
     *
     * @return gets the person id.
     */
    public int getPersonId() {
        return personId;
    }

}
