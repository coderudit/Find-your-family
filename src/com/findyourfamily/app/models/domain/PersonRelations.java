package com.findyourfamily.app.models.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Type for storing the information related to a person.
 */
public class PersonRelations {

    //Stores the current person id.
    private int personId;

    //Stores the parents of current person.
    private Set<PersonIdentityLite> parents;

    //Stores the parents of current person.
    private Set<PersonIdentityLite> children;

    //Stores the parents of current person.
    private PersonIdentityLite partner;

    //Stores the parents of current person.
    private Set<PersonIdentityLite> previousPartners;

    public PersonRelations(int personId) {
        this.personId = personId;
        this.parents = new HashSet<>();
        this.children = new HashSet<>();
        this.previousPartners = new HashSet<>();
    }

    /**
     * Gets the person id
     * @return person id
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Adds a parent to the current person.
     *
     * @param parent
     * @return true if parent was recorded.
     */
    public boolean addParent(PersonIdentityLite parent) {
        if (this.parents == null)
            this.parents = new HashSet<>();
        return this.parents.add(parent);
    }

    /**
     * Adds a child to the current person.
     * @param child
     * @return rue if child was recorded.
     */
    public boolean addChild(PersonIdentityLite child) {
        if (this.children == null)
            this.children = new HashSet<>();
        return this.children.add(child);
    }

    /**
     * Adds a partner to the current person.
     * @param partner
     * @return true if partner was added.
     */
    public boolean addPartner(PersonIdentityLite partner) {
        if (this.partner != null)
            return false;
        this.partner = partner;
        return true;
    }

    /**
     * Removes a partner to the current person.
     * @param partner
     * @return true if partner was removed.
     */
    public boolean removePartner(PersonIdentityLite partner) {
        if (this.partner == null || this.partner.getPersonId() != partner.getPersonId())
            return false;
        this.partner = null;
        return true;
    }

    /**
     * Add previous partners with this person
     * @param previousPartner
     * @return true if previous partners were added.
     */
    public boolean addPreviousPartner(PersonIdentityLite previousPartner){
        if(this.previousPartners == null)
            this.previousPartners = new HashSet<>();
        this.previousPartners.add(previousPartner);
        return true;
    }

    /**
     * Gets the current partner of the person.
     * @return current partner
     */
    public PersonIdentityLite getPartner() {
        return partner;
    }

    /**
     * Gets the children of the current person.
     * @return current children
     */
    public Set<PersonIdentityLite> getChildren() {
        return children;
    }

    /**
     * Gets the parents of the current person.
     * @return current parents
     */
    public Set<PersonIdentityLite> getParents() {
        return parents;
    }

    /**
     * Gets the previous partners of the current person.
     * @return current partners
     */
    public Set<PersonIdentityLite> getPreviousPartners() {
        return previousPartners;
    }
}
