/**
 * -----------------------------------------------------------------------------
 * File=IDataManagementService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a data management service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data;

import java.util.Map;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/**
 * Represents a data management service.
 */
public interface DataManagementService extends java.io.Closeable {

    /**
     * Gets the name of the data management service.
     *
     * @return The name of the data management service.
     */
    public String getName();

    /**
     * Gets a dictionary of data management service tables.
     *
     * @return A dictionary of data management service tables.
     */
    public Map<String, DataTable> getTables();

    /**
     * Gets the transaction log table table.
     *
     * @return The transaction log table.
     */
    public DataTable getTransactionLog();
}
