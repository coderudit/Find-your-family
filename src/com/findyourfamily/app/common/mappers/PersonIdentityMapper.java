package com.findyourfamily.app.common.mappers;

import com.findyourfamily.app.models.domain.DateRecord;
import com.findyourfamily.app.models.domain.PersonIdentity;
import com.findyourfamily.app.models.domain.PersonIdentityInternal;

/**
 * Mapper for mapping external type to internal or vice-versa for person identity.
 */
public class PersonIdentityMapper {

    /**
     * Converts person identity internal representation to external.
     *
     * @param piInternal
     * @return external representation of person identity.
     */
    public static PersonIdentity mapInternalPIToExternal(PersonIdentityInternal piInternal) {
        var piExternal = new PersonIdentity();
        piExternal.setPersonId(piInternal.getPersonId());
        piExternal.setName(piInternal.getName());
        piExternal.setGender(piInternal.getGender());
        piExternal.setLocationOfBirth(piInternal.getLocationOfBirth());
        piExternal.setDateOfBirth(piInternal.getDateOfBirth());
        piExternal.setLocationOfDeath(piInternal.getLocationOfDeath());
        piExternal.setDateOfDeath(piInternal.getDateOfDeath());
        piExternal.setOccupation(piInternal.getOccupation());
        piExternal.setNotes(piInternal.getNotes());
        piExternal.setReferences(piInternal.getReferences());
        piExternal.setAdditionalAttributes(piInternal.getAdditionalAttributes());
        piExternal.setParents(piInternal.getParents());
        piExternal.setPartner(piInternal.getPartner());
        piExternal.setPreviousPartners(piInternal.getPreviousPartners());
        piExternal.setChildren(piInternal.getChildren());
        return piExternal;
    }

    /**
     * Converts person identity external representation to internal.
     *
     * @param piExternal
     * @return internal representation of person identity.
     */
    public static PersonIdentityInternal mapExternalPIToInternal(PersonIdentity piExternal) {
        var piInternal = new PersonIdentityInternal(piExternal.getPersonId(), piExternal.getName());
        piInternal.setGender(piExternal.getGender());
        piInternal.setLocationOfBirth(piExternal.getLocationOfBirth());
        if (piExternal.getDateOfBirth() != null)
            piInternal.setDateOfBirth(piExternal.getDateOfBirth());
        piInternal.setLocationOfDeath(piExternal.getLocationOfDeath());
        if (piExternal.getDateOfDeath() != null)
            piInternal.setDateOfDeath(piExternal.getDateOfDeath());
        piInternal.setOccupation(piExternal.getOccupation());
        return piInternal;
    }
}
