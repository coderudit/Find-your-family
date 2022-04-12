package com.findyourfamily.app.common.enums;

/**
 * Enum type for storing person relations
 */
public enum RelationshipTypeEnum {

    ParentChild(1),
    Partner(2),
    Dissolution(3);

    private int id;

    //Constructor for passing id's to enum type.
    RelationshipTypeEnum(int id) {
        this.id = id;
    }

    //Gets the id for enum type.
    public int getId() {
        return this.id;
    }
}
