package com.findyourfamily.app.models.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonIdentity {

    //Stores the person id of the current person
    private int personId;

    //Stores the name of the current person
    private String name;

    //Stores the gender of the person
    private String gender;

    //Stores the location of birth of the person
    private Location locationOfBirth;

    //Stores the date of birth
    private Date dateOfBirth;

    //Stores the location of death of the person
    private Location locationOfDeath;

    //Stores the date of death of the person
    private Date dateOfDeath;

    //Stores the occupation of the person
    private String occupation;

    //Stores the notes of the person
    private List<String> notes;

    //Stores the references of the person
    private List<String> references;

    //Stores the additional attributes of the person
    private Map<String, String> additionalAttributes;

    //Stores the parent of the person
    private List<String> parents;

    //Stores the previous partners of the person
    private List<String> previousPartners;

    //Stores the partner of the person
    private String partner;

    //Stores the children of the person
    private List<String> children;

    //Stores the files of the person
    private Set<FileIdentifier> files;

    /**
     * Gets the id of the person
     *
     * @return
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Sets the id of the person
     *
     * @param personId
     */
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    /**
     * Gets the name of the person
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the gender of the person
     *
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the person
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the location of the birth of the person
     *
     * @return
     */
    public Location getLocationOfBirth() {
        return locationOfBirth;
    }

    /**
     * Sets the location of the birth of the person
     * @param locationOfBirth
     */
    public void setLocationOfBirth(Location locationOfBirth) {
        this.locationOfBirth = locationOfBirth;
    }

    /**
     * Gets the date of the birth of the person
     *
     * @return
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of the birth of the person
     * @param dateOfBirth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the location of the death of the person
     *
     * @return
     */
    public Location getLocationOfDeath() {
        return locationOfDeath;
    }

    /**
     * Sets the location of the birth of the person
     * @param locationOfDeath
     */
    public void setLocationOfDeath(Location locationOfDeath) {
        this.locationOfDeath = locationOfDeath;
    }

    /**
     * Gets the date of the death of the person
     *
     * @return
     */
    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    /**
     * Sets the date of the death of the person
     * @param dateOfDeath
     */
    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    /**
     * Gets the occupation of the person
     *
     * @return
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the occupation of the person
     * @param occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Gets the notes of the person
     *
     * @return
     */
    public List<String> getNotes() {
        return notes;
    }

    /**
     * Sets the notes of the person
     * @param notes
     */
    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    /**
     * Gets the references of the person
     *
     * @return
     */
    public List<String> getReferences() {
        return references;
    }

    /**
     * Sets the references of the person
     * @param references
     */
    public void setReferences(List<String> references) {
        this.references = references;
    }

    /**
     * Gets the additional attributes of the person
     *
     * @return
     */
    public Map<String, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    /**
     * Sets the additional attributes of the person
     * @param additionalAttributes
     */
    public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    /**
     * Gets the parent of the person
     *
     * @return list of string of parents name
     */
    public List<String> getParents() {
        return parents;
    }

    /**
     * Sets the parent of the person
     * @param parents
     */
    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    /**
     * Gets the previous partners of the person
     *
     * @return list of string of previous partners name
     */
    public List<String> getPreviousPartners() {
        return previousPartners;
    }

    /**
     * Sets the previous partners of the person
     * @param previousPartners
     */
    public void setPreviousPartners(List<String> previousPartners) {
        this.previousPartners = previousPartners;
    }

    /**
     * Gets the partner of the person
     *
     * @return partner name
     */
    public String getPartner() {
        return partner;
    }

    /**
     * Sets the partner of the person
     * @param partner
     */
    public void setPartner(String partner) {
        this.partner = partner;
    }

    /**
     * Gets the children of the person
     *
     * @return list of string of children
     */
    public List<String> getChildren() {
        return children;
    }

    /**
     * Sets the children of the person
     *
     * @param children
     */
    public void setChildren(List<String> children) {
        this.children = children;
    }

    /**
     * Gets the files of the person
     *
     * @return set of files
     */
    public Set<FileIdentifier> getFiles() {
        return files;
    }

    /**
     * Sets the files of the person
     * @param files
     */
    public void setFiles(Set<FileIdentifier> files) {
        this.files = files;
    }
}
