package com.findyourfamily.app.models.domain;

import java.util.*;

public class PersonIdentityInternal implements Cloneable {
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

    //Stores the date of birth
    private DateRecord birthDateRecord;

    //Stores the location of death of the person
    private Location locationOfDeath;

    //Stores the date of death of the person
    private DateRecord deathDateRecord;

    //Stores the date of death
    private Date dateOfDeath;

    //Stores the occupation of the person
    private String occupation;

    //Stores the notes of the person
    private List<String> notes;

    //Stores the references of the person
    private List<String> references;

    //Stores the notes and references of the person
    private List<String> notesAndReferences;

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

    public PersonIdentityInternal(int personId, String name) {
        this.name = name;
        this.personId = personId;
        this.notes = new ArrayList<>();
        this.references = new ArrayList<>();
        this.additionalAttributes = new HashMap<>();
    }

    /**
     * Gets the id of the person
     *
     * @return
     */
    public int getPersonId() {
        return personId;
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
     * Gets the name of the person
     *
     * @return
     */
    public String getName() {
        return name;
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
     * Gets the date of the birth of the person
     *
     * @return
     */
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Gets the date of the birth of the person
     *
     */
    public void setDateOfBirth(Date date) {
        this.dateOfBirth = date;
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
        return this.dateOfDeath;
    }

    /**
     * Sets the date of the death
     * @param date
     */
    public void setDateOfDeath(Date date) {
        this.dateOfDeath = date;
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
     * Add new reference to the list of references
     * @param reference
     */
    public void addReference(String reference) {
        if (this.references == null)
            this.references = new ArrayList<>();
        this.references.add(reference);
    }

    /**
     * Add new note to the list of notes
     * @param note
     */
    public void addNote(String note) {
        if (this.notes == null)
            this.notes = new ArrayList<>();
        this.notes.add(note);
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
     * Set list of notes
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
     * Add both notes and references to a single list
     * @param noteAndReference
     */
    public void addNotesAndReferences(String noteAndReference) {
        if (this.notesAndReferences == null)
            this.notesAndReferences = new ArrayList<>();
        this.notesAndReferences.add(noteAndReference);
    }

    /**
     * Gets both notes and references in a single list
     * @return
     */
    public List<String> getNotesAndReferences() {
        return this.notesAndReferences;
    }

    /**
     * Adds the additional attribute with key and value
     * @param additionalAttributeKey
     * @param additionalAttributeValue
     */
    public void addAdditionalAttribute(String additionalAttributeKey, String additionalAttributeValue) {
        this.additionalAttributes.putIfAbsent(additionalAttributeKey, additionalAttributeValue);
    }

    /**
     * Sets the additional attributes.
     * @param attributes
     */
    public void setAdditionalAttribute(Map<String, String> attributes) {
        this.additionalAttributes = attributes;
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
     * Gets the parent of the person
     *
     * @return list of string of parents name
     */
    public List<String> getParents() {
        return parents;
    }

    /**
     * Add parent to the list of current parents
     * @param parent
     */
    public void addParent(String parent) {
        if (this.parents == null)
            this.parents = new ArrayList<>();
        this.parents.add(parent);
    }

    /**
     * Get the previous partners
     * @return
     */
    public List<String> getPreviousPartners() {
        return previousPartners;
    }

    /**
     * Add previous partner to the list
     * @param previousPartner
     */
    public void setPreviousPartners(String previousPartner) {
        if (this.previousPartners == null)
            this.previousPartners = new ArrayList<>();
        this.previousPartners.add(previousPartner);
    }

    /**
     * Get the partner of the current person
     * @return
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
     * @param child
     */
    public void setChildren(String child) {
        if (this.children == null)
            this.children = new ArrayList<>();
        this.children.add(child);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonIdentityInternal)) return false;
        PersonIdentityInternal that = (PersonIdentityInternal) o;
        return getPersonId() == that.getPersonId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonId());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        PersonIdentityInternal newPerson = (PersonIdentityInternal) super.clone();
        newPerson.setReferences(new ArrayList<>());
        newPerson.setNotes(new ArrayList<>());
        return newPerson;
    }
}
