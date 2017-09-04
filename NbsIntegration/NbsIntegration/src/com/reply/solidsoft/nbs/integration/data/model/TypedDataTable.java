/**
 * -----------------------------------------------------------------------------
 * File=TypedDataTable.java
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
 * @param <T> The data management service record type.
 */
public interface TypedDataTable<T extends DataRecord> extends DataTable {

    /**
     * Gets the list of records.
     *
     * @return The list of records.
     */
    @Override
    public List<T> getRecords();

    /**
     * Adds a record to the table.
     *
     * @param record The record to be stored.
     */
    public void add(T record);

    /**
     * Removes a record from the table.
     *
     * @param record The record to be removed from the store.
     */
    public void remove(T record);
}
