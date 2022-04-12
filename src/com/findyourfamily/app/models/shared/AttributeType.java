package com.findyourfamily.app.models.shared;

/**
 * Attribute type for storing its type and description.
 */
public class AttributeType {

    //Stores the type id of the attribute.
    private int typeId;

    //Stores the description of the attribute.
    private String description;

    public AttributeType(int typeId, String description) {
        this.typeId = typeId;
        this.description = description;
    }

    /**
     * Gets the id of the type.
     *
     * @return
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Gets the description of the type.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }
}
