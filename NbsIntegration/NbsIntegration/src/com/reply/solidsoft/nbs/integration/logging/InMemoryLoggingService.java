/**
 * -----------------------------------------------------------------------------
 * File=InMemoryLoggingService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The in memory logging service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging;

import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.extensions.functional.Logger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntry;

/**
 * The in memory logging service.
 */
public class InMemoryLoggingService implements LoggingService {

    /**
     * The in-memory store of log entries.
     */
    private final List<LogEntry> logEntries = new ArrayList<>();

    /**
     * An empty string.
     * <p>
     * Obtaining the current user name or identifier is specific to the
     * environment in which the integration library is being used, and hence the
     * built-in default in-memory logging service cannot take responsibility for
     * this. The property is mutable, so that code external to the library can
     * set this if required.
     */
    private String currentUser = "";

    /**
     * Gets an empty string.
     *
     * @return An empty string.
     */
    @Override
    public final String getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets an empty string.
     *
     * @param value A string
     */
    public final void setCurrentUser(String value) {
        currentUser = value;
    }

    /**
     * Adds a log entry to the in-memory store.
     */
    @Override
    public final Logger<Object, LogEntry> getLog() {
        return (o, le) -> this.logEntries.add(le);
    }

    /**
     * Gets the collection of log entries.
     *
     * @return The collection of log entries.
     */
    @Override
    public final List<LogEntry> getLogEntries() {
        return this.logEntries;
    }

    /**
     * The name of the in-memory log service.
     */
    private final String name = "inMemoryLogService";

    /**
     * Gets the name of the in-memory log service.
     *
     * @return The name of the in-memory log service.
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Gets the data tables associated with this service.
     */
    @Override
    public final Map<String, DataTable> getTables() {
        Map<String, DataTable> tables = new HashMap<>();
        DataTable put = tables.put("log", new InMemoryLogDataTable(this, "log"));
        return tables;
    }

    /**
     * Gets the transaction log. This is always null for the in-memory service.
     *
     * @return The transaction log.
     */
    @Override
    public final DataTable getTransactionLog() {
        return null;
    }

    /**
     * Clears the collection of log entries.
     */
    public final void clear() {
        this.logEntries.clear();
    }

    /**
     * archive log data older than the given date.
     *
     * @param olderThan The date and time for which older records will be
     * archived.
     * @return Returns a collection of log entries for archival
     */
    public final List<NbsIntegrationLogEntry> archive(Instant olderThan) {
        List<LogEntry> archiveRecords
                = this.logEntries
                        .stream()
                        .filter(entry -> entry.getTime().isBefore(Instant.now()))
                        .collect(Collectors.toList());

        ArrayList<NbsIntegrationLogEntry> results = new ArrayList<>();

        archiveRecords.stream().map((archiveRecord) -> {
            boolean remove = this.logEntries.remove(archiveRecord);
            return archiveRecord;
        }).forEachOrdered((archiveRecord) -> {
            results.add(archiveRecord);
        });

        return results;
    }

    /**
     * Close the service.
     */
    @Override
    public final void close() {
    }
}
