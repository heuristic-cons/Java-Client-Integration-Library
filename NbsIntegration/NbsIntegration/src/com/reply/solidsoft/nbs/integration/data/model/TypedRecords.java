/**
 * -----------------------------------------------------------------------------
 * File=ITypedRecords.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The record store interface.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data.model;

import java.util.List;

/**
 * The record store interface.
 *
 * @param <TOut> The data management service record type.
 */
public interface TypedRecords<TOut extends DataRecord> {

    /**
     * Gets the list of records.
     *
     * @return The list of records.
     */
    List<TOut> getRecords();
}
