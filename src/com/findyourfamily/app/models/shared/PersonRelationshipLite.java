package com.findyourfamily.app.models.shared;

/**
 * Stores information related to a person.
 */
public class PersonRelationshipLite {
    //Person 1 id.
    private int person1Id;

    //Person 2 id.
    private int person2Id;

    // Type of relation.
    private int relationshipType;

    //If the status is active between person 1 and person 2.
    private int isActive;

    public PersonRelationshipLite(int person1Id, int person2Id, int relationshipType) {
        this.person1Id = person1Id;
        this.person2Id = person2Id;
        this.relationshipType = relationshipType;
    }

    public PersonRelationshipLite(int person1Id, int person2Id, int relationshipType, int isActive) {
        this(person1Id, person2Id, relationshipType);
        this.isActive = isActive;
    }

    /**
     * Gets the person 1 id.
     * @return
     */
    public int getPerson1Id() {
        return person1Id;
    }

    /**
     * Gets the person 2 id.
     * @return
     */
    public int getPerson2Id() {
        return person2Id;
    }

    /**
     * Gets the type of person relation.
     * @return
     */
    public int getRelationshipType() {
        return relationshipType;
    }

    /**
     * Gets if the status is active.
     * @return
     */
    public int getIsActive() {
        return isActive;
    }
}
