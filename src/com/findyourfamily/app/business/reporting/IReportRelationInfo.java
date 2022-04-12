package com.findyourfamily.app.business.reporting;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;
import com.findyourfamily.app.models.domain.PersonRelations;

import java.util.Set;

/**
 * Type for reporting information related to a person.
 */
public interface IReportRelationInfo {

    /**
     * Find all the relations for a given person which includes partner, parents and children.
     *
     * @param personId
     * @return set of person relations for a given person.
     */
    PersonRelations findPersonRelations(int personId);

    /**
     * Find person information for given person id's.
     *
     * @param personIds
     * @return set of person identity information for list of a person id's.
     */
    Set<PersonIdentityInternal> findPersons(Set<Integer> personIds);
}
