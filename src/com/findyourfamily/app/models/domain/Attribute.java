package com.findyourfamily.app.models.domain;

/**
 * Type for storing attribute type id in the external source and its value.
 */
public class Attribute {
    private final int typeId;
    private final String typeValue;

    public Attribute(int typeId, String typeValue) {
        this.typeId = typeId;
        this.typeValue = typeValue;
    }

    /**
     * Gets the type id of attribute.
     *
     * @return type id
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Gets the type value of the attribute.
     *
     * @return type value
     */
    public String getTypeValue() {
        return typeValue;
    }
}
