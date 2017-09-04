/**
 * -----------------------------------------------------------------------------
 * File=IDataRecord.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a data management service record.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data.model;

/**
 * Represents a data management service record.
 */
public interface DataRecord {

    /**
     * Gets the timestamp value for this deferred record.
     *
     * @return The timestamp value for this deferred record.
     */
    public long getTimeStamp();
}
