/**
 * -----------------------------------------------------------------------------
 * File=IDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The records store interface.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data.model;

import java.util.List;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.data.ResourceManager;

/**
 * The records store interface.
 */
public interface DataTable extends ResourceManager {

    /**
     * Gets the in-memory data management service.
     *
     * @return The in-memory data management service.
     */
    public DataManagementService getDataManagementService();

    /**
     * Sets the in-memory data management service.
     *
     * @param value The in-memory data management service.
     */
    public void setDataManagementService(DataManagementService value);

    /**
     * Gets the current timestamp for requests.
     *
     * @return The timestamp of the last record written to the table, or 0.
     */
    public long getCurrentTimestamp();

    /**
     * Gets the name of the table.
     *
     * @return The name of the table.
     */
    public String getName();

    /**
     * Sets the name of the table.
     *
     * @param value The name of the table.
     */
    public void setName(String value);

    /**
     * Gets the list of records.
     *
     * @return The list of records
     */
    public List getRecords();

    /**
     * Gets a count of the number of records in the table.
     *
     * @return A count of the number of records in the table.
     */
    public int getCount();

    /**
     * Adds a record to the store.
     *
     * @param record The record to be stored.
     */
    public void add(Object record);

    /**
     * Clears all requests from the table.
     */
    public void clear();

    /**
     * Removes a record from the store.
     *
     * @param record The record to be removed from the store.
     */
    public void remove(Object record);
}
