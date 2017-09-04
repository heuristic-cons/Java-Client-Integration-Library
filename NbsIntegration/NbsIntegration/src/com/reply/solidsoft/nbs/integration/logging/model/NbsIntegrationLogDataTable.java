/**
 * -----------------------------------------------------------------------------
 * File=ILogDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a table of logging data.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;

/**
 * Represents a table of logging data.
 */
public interface NbsIntegrationLogDataTable extends TypedDataTable<LogEntry> {

    /**
     * Returns a log entry that matches the given log entry identifier.
     *
     * @param id The log entry identifier.
     * @return A log entry that matches the given log entry identifier.
     */
    public NbsIntegrationLogEntry getLogEntry(String id);
}
