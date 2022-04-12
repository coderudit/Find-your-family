package com.findyourfamily.app.database.familytree;

import com.findyourfamily.app.models.shared.PersonRelationshipLite;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface type for recording information related to a person relations.
 */
public interface IRecordPersonRelationshipGateway {

    /**
     * Generic method for recording list of relationships that should exist between 2 persons. If any of them fails,
     * nothing gets saved.
     *
     * @param personRelationshipLite
     * @return true if the relationship was recorded.
     * @throws SQLException
     */
    boolean recordRelationship(List<PersonRelationshipLite> personRelationshipLite) throws SQLException;
}
