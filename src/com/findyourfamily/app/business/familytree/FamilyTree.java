package com.findyourfamily.app.business.familytree;

import com.findyourfamily.app.business.reporting.IReport;
import com.findyourfamily.app.business.reporting.Report;
import com.findyourfamily.app.database.familytree.FamilyTreeGateway;
import com.findyourfamily.app.database.familytree.IFamilyTreeGateway;

import com.findyourfamily.app.models.domain.*;
import com.findyourfamily.app.common.enums.PersonAttributesEnum;
import com.findyourfamily.app.common.enums.RelationshipTypeEnum;
import com.findyourfamily.app.models.shared.AttributeType;
import com.findyourfamily.app.models.shared.PersonRelationshipLite;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * Implementation type for recording information related to a person, and it's relations.
 */
public class FamilyTree implements IFamilyTree {

    //Stores family tree database object.
    private IFamilyTreeGateway familyTreeDAO;

    //Stores the object for reporting.
    private IReport report;

    //Stores all the attributes present inside the external source.
    private List<AttributeType> attributeTypes;

    //Map for storing person id ,and it's corresponding relations.
    private Map<Integer, PersonRelations> personsMap;

    public FamilyTree() {
        familyTreeDAO = new FamilyTreeGateway();
        report = new Report();
        personsMap = new HashMap<>();
    }

    /**
     * Gets the persons map, storing person id ,and it's corresponding relations.
     *
     * @return the persons map to be used in other files.
     */
    public Map<Integer, PersonRelations> getPersonsMap() {
        return personsMap;
    }

    /**
     * Adds a new entry to the persons map, storing person id ,and it's corresponding relations.
     *
     * @param personId
     * @param personRelations
     */
    public void addToPersonsMap(Integer personId, PersonRelations personRelations) {
        this.personsMap.put(personId, personRelations);
    }

    /**
     * Adds a person to the external source with a given name.
     * Duplicate name can be recorded with new entry created
     * for each record.
     *
     * @param name
     * @return Person Identity type
     * @throws IOException
     */
    @Override
    public PersonIdentityInternal addPerson(String name) throws IOException {
        int id;
        try {
            id = familyTreeDAO.addPerson(name);
        } catch (SQLException exception) { //Catch the SQLException to throw generic IO exception to hide details from the user.
            throw new IOException("Unable to connect to the database.");
        }
        return new PersonIdentityInternal(id, name);
    }

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
    @Override
    public boolean recordAttributes(PersonIdentityInternal person, Map<String, String> attributes) throws IOException {
        if (!familyTreeDAO.isPersonExists(person.getPersonId()))
            throw new IllegalArgumentException("Person does not exist in the system.");

        PersonIdentityInternal personCopy;

        //Creates a copy of the person identity.
        try {
            personCopy = (PersonIdentityInternal) person.clone();
        } catch (CloneNotSupportedException exception) {
            //Only take the person reference when clone exception encountered.
            personCopy = person;
        }

        boolean result;
        boolean isUpdatePersonIdentityRequired = false;
        var newAttributes = new ArrayList<Attribute>();
        for (var attribute : attributes.entrySet()) {

            //If the attribute is predefined in the database as person's attribute, fill the person's copy, to save
            //later in the database.
            result = fillPersonPredefinedAttributes(personCopy, attribute.getKey().toUpperCase(), attribute.getValue());

            //Mark update person check as true if any of its value changes. If not its saves us from an external
            //call to save it.
            if (result && !isUpdatePersonIdentityRequired) {
                isUpdatePersonIdentityRequired = true;
            }

            //If the attribute was not predefined, add it as a separate attribute.
            if (!result) {
                var updatedAttribute = recordAttribute(attribute.getKey(), attribute.getValue());
                newAttributes.add(updatedAttribute);
            }
        }

        //Updates the person ,and it's attributes to the database.
        try {
            return familyTreeDAO.savePersonAndPersonAttributes(personCopy, newAttributes, isUpdatePersonIdentityRequired);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
    }

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
    @Override
    public boolean recordReference(int personId, String reference) throws IOException {
        if (!familyTreeDAO.isPersonExists(personId))
            throw new IllegalArgumentException("Person does not exist in the system.");
        try {
            //Create a new record for the reference.
            var newAttribute = recordAttribute(PersonAttributesEnum.REFERENCES.name().toLowerCase(Locale.ROOT), reference);

            //Update in the external source.
            return familyTreeDAO.savePersonAttribute(personId, newAttribute.getTypeId(), reference);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
    }

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
    @Override
    public boolean recordNote(int personId, String note) throws IOException {
        if (!familyTreeDAO.isPersonExists(personId))
            throw new IllegalArgumentException("Person does not exist in the system.");
        try {
            //Create a new record for the note.
            var newAttribute = recordAttribute(PersonAttributesEnum.NOTES.name().toLowerCase(Locale.ROOT), note);

            //Update in the external source.
            return familyTreeDAO.savePersonAttribute(personId, newAttribute.getTypeId(), note);
        } catch (SQLException exception) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Creates a record for attribute type ,and it's value. If the attribute is encountered for the first time,
     * creates an entry for this attribute inside external source to get its type id.
     *
     * @param attribute
     * @param attributeValue
     * @return record of an attribute with its type id and value.
     * @throws IOException
     */
    private Attribute recordAttribute(String attribute, String attributeValue) throws IOException {

        //Do not add the current attribute if attribute type or value is either null or blank.
        if (attribute == null || attribute.isBlank() || attributeValue == null || attributeValue.isBlank())
            throw new IllegalArgumentException("Invalid attribute values");

        //If there is no record for the attribute types get it from the external source.
        if (attributeTypes == null || attributeTypes.size() == 0)
            attributeTypes = familyTreeDAO.getAttributeTypes();

        //Find if current type of attribute is added before.
        var attributeTypeResult = attributeTypes.stream().
                filter(x -> x.getDescription().toLowerCase(Locale.ROOT).equals(attribute.toLowerCase(Locale.ROOT))).
                findFirst().orElse(null);

        //If this type of attribute is not added before, add it to the external source and local cache
        if (attributeTypeResult == null) {
            try {
                int typeId = familyTreeDAO.addAttribute(attribute);
                attributeTypeResult = new AttributeType(typeId, attribute);
                attributeTypes.add(attributeTypeResult);
            } catch (SQLException ex) {
                throw new IOException("Unable to connect to the database.");
            }
        }
        return new Attribute(attributeTypeResult.getTypeId(), attributeValue);
    }

    /**
     * Fill pre-defined attributes for a person, if enum does not match, exception is thrown. Then, return false to
     * add it as a new attribute.
     *
     * @param person
     * @param attribute
     * @param attributeValue
     * @return true if a pre-defined attribute is added, return false if a new attribute needs to be added.
     * @throws ParseException
     */
    private boolean fillPersonPredefinedAttributes(PersonIdentityInternal person, String attribute,
                                                   String attributeValue) {
        try {
            switch (PersonAttributesEnum.valueOf(attribute)) {
                case PERSONNAME:
                    person.setName(attributeValue);
                    return true;

                case GENDER:
                    person.setGender(attributeValue);
                    return true;

                //Takes the current date of birth and updates date to it.
                case DAYOFBIRTH:
                    var birthDate1 = person.getDateOfBirth();
                    if (birthDate1 == null)
                        birthDate1 = new Date();
                    birthDate1.setDate(Integer.parseInt(attributeValue));
                    person.setDateOfBirth(birthDate1);
                    return true;

                //Takes the current date of birth and updates month to it.
                case MONTHOFBIRTH:
                    var birthDate2 = person.getDateOfBirth();
                    if (birthDate2 == null)
                        birthDate2 = new Date();
                    birthDate2.setMonth(Integer.parseInt(attributeValue) - 1);
                    person.setDateOfBirth(birthDate2);
                    return true;

                //Takes the current date of birth and updates year to it.
                case YEAROFBIRTH:
                    var birthDate3 = person.getDateOfBirth();
                    if (birthDate3 == null)
                        birthDate3 = new Date();
                    birthDate3.setYear(Integer.parseInt(attributeValue) - 1900);
                    person.setDateOfBirth(birthDate3);

                    //Takes the location of birth and updates location name to it.
                case LOCATIONOFBIRTH:
                    var location1 = person.getLocationOfBirth();
                    if (location1 == null)
                        location1 = new Location();
                    location1.setLocationName(attributeValue);
                    person.setLocationOfBirth(location1);
                    return true;

                //Takes the location of birth and updates city to it.
                case CITYOFBIRTH:
                    var location2 = person.getLocationOfBirth();
                    if (location2 == null)
                        location2 = new Location();
                    location2.setCity(attributeValue);
                    person.setLocationOfBirth(location2);
                    return true;

                //Takes the location of birth and updates province to it.
                case PROVINCEOFBIRTH:
                    var location3 = person.getLocationOfBirth();
                    if (location3 == null)
                        location3 = new Location();
                    location3.setProvince(attributeValue);
                    person.setLocationOfBirth(location3);
                    return true;

                //Takes the location of birth and updates country to it.
                case COUNTRYOFBIRTH:
                    var location4 = person.getLocationOfBirth();
                    if (location4 == null)
                        location4 = new Location();
                    location4.setCountry(attributeValue);
                    person.setLocationOfBirth(location4);
                    return true;

                //Takes the current date of death and updates day to it.
                case DAYOFDEATH:
                    var birthDate4 = person.getDateOfDeath();
                    if (birthDate4 == null)
                        birthDate4 = new Date();
                    birthDate4.setDate(Integer.parseInt(attributeValue));
                    person.setDateOfDeath(birthDate4);
                    return true;

                //Takes the current date of death and updates month to it.
                case MONTHOFDEATH:
                    var birthDate5 = person.getDateOfDeath();
                    if (birthDate5 == null)
                        birthDate5 = new Date();
                    birthDate5.setMonth(Integer.parseInt(attributeValue) - 1);
                    person.setDateOfDeath(birthDate5);
                    return true;

                //Takes the current date of death and updates year to it.
                case YEAROFDEATH:
                    var birthDate6 = person.getDateOfDeath();
                    if (birthDate6 == null)
                        birthDate6 = new Date();
                    birthDate6.setYear(Integer.parseInt(attributeValue) - 1900);
                    person.setDateOfDeath(birthDate6);
                    return true;

                //Takes the location of birth and updates location name to it.
                case LOCATIONOFDEATH:
                    var location5 = person.getLocationOfDeath();
                    location5.setLocationName(attributeValue);
                    person.setLocationOfDeath(location5);
                    return true;

                //Takes the location of birth and updates city to it.
                case CITYOFDEATH:
                    var location6 = person.getLocationOfDeath();
                    location6.setCity(attributeValue);
                    person.setLocationOfDeath(location6);
                    return true;

                //Takes the location of birth and updates province to it.
                case PROVINCEOFDEATH:
                    var location7 = person.getLocationOfDeath();
                    location7.setProvince(attributeValue);
                    person.setLocationOfDeath(location7);
                    return true;

                //Takes the location of birth and updates country to it.
                case COUNTRYOFDEATH:
                    var location8 = person.getLocationOfDeath();
                    location8.setCountry(attributeValue);
                    person.setLocationOfDeath(location8);
                    return true;

                case OCCUPATION:
                    person.setOccupation(attributeValue);
                    return true;

                default:
                    return false;

            }
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Records parent and child relationship.
     *
     * @param parentId
     * @param childId
     * @return true if the parent-child relationship is successfully recorded.
     * @throws IOException
     */
    @Override
    public boolean recordChild(int parentId, int childId) throws IOException {

        //Validate if both the persons exist in the system.
        if (!validatePersons(parentId, childId))
            return false;

        //Updates the map with parent ,and it's relations from the external source.
        updateMapWithPerson(parentId);

        //Updates the map with child ,and it's relations from the external source.
        updateMapWithPerson(childId);

        var relationshipRecords = new ArrayList<PersonRelationshipLite>();

        //Record the information between parent and child to save it to the external source.
        relationshipRecords.add(new PersonRelationshipLite(parentId, childId,
                RelationshipTypeEnum.ParentChild.getId()));

        //Checks if the parent has a partner.
        var parentPartner = personsMap.get(parentId).getPartner();

        try {
            //If the parent has a partner, record its entry as well.
            if (parentPartner != null) {

                //Update the persons map with the parent's partner.
                updateMapWithPerson(parentPartner.getPersonId());

                //Record the information between parent's partner and child to save it to the external source.
                relationshipRecords.add(new PersonRelationshipLite(parentPartner.getPersonId(), childId, RelationshipTypeEnum.ParentChild.getId()));
            }

            //Update the parent child relationship to the external source.
            var result = familyTreeDAO.recordRelationship(relationshipRecords);

            //If the records have been updated to the external source, now update the local cache.
            if (result) {

                //Update child entry to parent inside the local cache.
                personsMap.get(parentId).addChild(new PersonIdentityLite(childId));


                //Update parent entry to child inside the local cache.
                personsMap.get(childId).addParent(new PersonIdentityLite(parentId));

                if (parentPartner != null) {
                    //Update child with the partner as parent in the persons map.
                    personsMap.get(childId).addParent(new PersonIdentityLite(parentPartner.getPersonId()));

                    //Update partner with the child in the persons map.
                    personsMap.get(parentPartner.getPersonId()).addChild(new PersonIdentityLite(childId));
                }

            }
            return result;

        } catch (SQLException ex) {
            throw new IOException("Unable to connect to the database.");
        }
    }

    /**
     * Record partnership relationship between partner 1 and partner 2.
     *
     * @param partner1Id
     * @param partner2Id
     * @return true if the partnership relationship is successfully recorded.
     * @throws IOException
     */
    @Override
    public boolean recordPartnering(int partner1Id, int partner2Id) throws IOException {
        //Validate if both the persons exist in the system.
        if (!validatePersons(partner1Id, partner2Id))
            return false;

        //Updates the map with partner 1 ,and it's relations from the external source.
        updateMapWithPerson(partner1Id);

        //Updates the map with partner 2 ,and it's relations from the external source.
        updateMapWithPerson(partner2Id);

        var relationshipRecords = new ArrayList<PersonRelationshipLite>();

        //Store partner relationship to store in the external source.
        relationshipRecords.add(new PersonRelationshipLite(partner1Id, partner2Id, RelationshipTypeEnum.Partner.getId()));

        try {
            //Store the  partner relationship in the external source.
            var result = familyTreeDAO.recordRelationship(relationshipRecords);

            //If the relationship was not stored inside the external source return.
            if (!result)
                return false;

        } catch (SQLException ex) {
            throw new IOException("Unable to connect to the database.");
        }

        //Add person 2 as partner to person 1.
        personsMap.get(partner1Id).addPartner(new PersonIdentityLite(partner2Id));

        //Add person 1 as partner to person 2.
        personsMap.get(partner2Id).addPartner(new PersonIdentityLite(partner1Id));

        return true;
    }

    /**
     * Record dissolution relationship between partner 1 and partner 2.
     * Updates the active status to inactive.
     *
     * @param partner1Id
     * @param partner2Id
     * @return true if the dissolution relationship is successfully recorded.
     * @throws IOException
     */
    @Override
    public boolean recordDissolution(int partner1Id, int partner2Id) throws IOException {
        //Validate if both the persons exist in the system.
        if (!validatePersons(partner1Id, partner2Id))
            return false;

        //Updates the map with partner 1 ,and it's relations from the external source.
        updateMapWithPerson(partner1Id);

        //Updates the map with partner 2 ,and it's relations from the external source.
        updateMapWithPerson(partner2Id);

        var relationshipRecords = new ArrayList<PersonRelationshipLite>();

        //Store partner relationship to store in the external source.
        relationshipRecords.add(new PersonRelationshipLite(partner1Id, partner2Id, RelationshipTypeEnum.Dissolution.getId()));

        try {
            //Store the dissolution relationship in the external source.
            var result = familyTreeDAO.recordRelationship(relationshipRecords);

            //If the relationship was not stored inside the external source return.
            if (!result)
                return false;

        } catch (SQLException ex) {
            throw new IOException("Unable to connect to the database.");
        }

        //Remove person 2 as partner to person 1.
        personsMap.get(partner1Id).removePartner(new PersonIdentityLite(partner2Id));

        //Remove person 1 as partner to person 2.
        personsMap.get(partner2Id).removePartner(new PersonIdentityLite(partner1Id));

        return true;
    }

    /**
     * Validates if person 1 and person 2 exists in the external source.
     *
     * @param person1
     * @param person2
     * @return true if both the persons exist in the external source.
     * @throws SQLException
     */
    private boolean validatePersons(int person1, int person2) throws IOException {
        //Check if the persons exist in the external source.
        if (!(familyTreeDAO.isPersonExists(person1) && familyTreeDAO.isPersonExists(person2)))
            throw new IllegalArgumentException("Person does not exist in the system.");

        //Check if person 1 and person 2 are same.
        if (person1 == person2)
            throw new IllegalArgumentException("Person 1 and Person 2 can't be same.");

        return true;
    }

    /**
     * Updates the map with person and its relations like parents, partner and children.
     *
     * @param personId
     */
    private void updateMapWithPerson(int personId) {
        //Find the person,and it's relations from the external source.
        var person = report.findPersonRelations(personId);

        //If no relations were found for the person inside the external source, make a fresh entry to the persons map.
        if (person == null)
            //If the map already contains a key, update it with the latest data.
            if (personsMap.containsKey(personId))
                personsMap.replace(personId, new PersonRelations(personId));
            else
                //Create a fresh entry
                personsMap.put(personId, new PersonRelations(personId));
            //If relations were found.
        else {
            //If the map already contains a key, update it with the latest data.
            if (personsMap.containsKey(personId))
                personsMap.replace(personId, person);
            else
                //Create a fresh entry
                personsMap.put(personId, person);
        }
    }

}
