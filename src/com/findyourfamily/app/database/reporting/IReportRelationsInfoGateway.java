package com.findyourfamily.app.database.reporting;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.models.shared.PersonRelationshipLite;

import java.util.List;

/**
 * Type for having reporting functionality related to a media.
 */
public interface IReportRelationsInfoGateway {

    /**
     * Get information related to a person, like parents, partner and children.
     *
     * @param personId
     * @return
     */
    List<PersonRelationshipLite> getPersonRelations(int personId);

    /**
     * Get the person information related to a person id.
     *
     * @param personId
     * @return
     */
    PersonIdentityInternal getPersonById(int personId);
}
