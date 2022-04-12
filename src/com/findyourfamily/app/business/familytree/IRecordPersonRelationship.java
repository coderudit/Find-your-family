package com.findyourfamily.app.business.familytree;

import com.findyourfamily.app.models.domain.PersonIdentityInternal;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Interface type for recording relations of a person.
 */
public interface IRecordPersonRelationship {

    /**
     * Records parent and child relationship.
     * @param parentId
     * @param childId
     * @return true if the parent-child relationship is successfully recorded.
     * @throws IOException
     * @throws SQLException
     */
    boolean recordChild(int parentId, int childId) throws IOException;

    /**
     * Record partnership relationship between partner 1 and partner 2.
     *
     * @param partner1Id
     * @param partner2Id
     * @return true if the partnership relationship is successfully recorded.
     * @throws IOException
     * @throws SQLException
     */
    boolean recordPartnering(int partner1Id, int partner2Id) throws IOException;

    /**
     * Record dissolution relationship between partner 1 and partner 2.
     * @param partner1Id
     * @param partner2Id
     * @return true if the dissolution relationship is successfully recorded.
     * @throws IOException
     * @throws SQLException
     */
    boolean recordDissolution(int partner1Id, int partner2Id) throws IOException;
}
