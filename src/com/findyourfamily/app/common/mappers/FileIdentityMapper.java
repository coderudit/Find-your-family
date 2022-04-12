package com.findyourfamily.app.common.mappers;

import com.findyourfamily.app.models.domain.*;

/**
 * Mapper for mapping external type to internal or vice-versa for file identifier.
 */
public class FileIdentityMapper {

    /**
     * Converts file identifier's internal representation to external.
     *
     * @param fiInternal
     * @return external representation of file identifier.
     */
    public static FileIdentifier mapInternalFIToExternal(FileIdentifierInternal fiInternal) {
        var fiExternal = new FileIdentifier();
        fiExternal.setMediaId(fiInternal.getMediaId());
        fiExternal.setMediaName(fiInternal.getMediaName());
        fiExternal.setMediaLocation(fiInternal.getMediaLocation());
        fiExternal.setLocationOfPicture(fiInternal.getLocationOfPicture());
        fiExternal.setDateOfPicture(fiInternal.getDateOfPicture());
        fiExternal.setAttributes(fiInternal.getAttributes());
        fiExternal.setTags(fiInternal.getTags());
        fiExternal.setPersonsInMedia(fiInternal.getPersonsInMedia());
        return fiExternal;
    }

    /**
     * Converts file identifier's external representation to internal.
     *
     * @param fiExternal
     * @return internal representation of file identifier.
     */
    public static FileIdentifierInternal mapExternalFIToInternal(FileIdentifier fiExternal) {
        var fiInternal = new FileIdentifierInternal(fiExternal.getMediaId(), fiExternal.getMediaName(), fiExternal.getMediaLocation());
        fiInternal.setLocationOfPicture(fiExternal.getLocationOfPicture());
        if (fiExternal.getDateOfPicture() != null)
            fiInternal.setDateOfPicture(fiExternal.getDateOfPicture());
        return fiInternal;
    }
}
