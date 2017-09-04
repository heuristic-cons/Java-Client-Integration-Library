/**
 * -----------------------------------------------------------------------------
 * File=LogDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.loggingservice;

import iBoxDB.LocalServer.Box;
import iBoxDB.LocalServer.CommitResult;
import iBoxDB.LocalServer.DatabaseConfig.Config;
import iBoxDB.LocalServer.DB;
import com.reply.solidsoft.nbs.integration.data.TransactionException;
import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.extensions.StringExtensions;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.sample.loggingservice.model.InstantDb;
import com.reply.solidsoft.nbs.integration.sample.loggingservice.properties.Resources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogDataTable;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntry;

/**
 * Implements a transactional file record service.
 */
public class LogDataTable implements NbsIntegrationLogDataTable {

    /**
     * The table name.
     */
    private String tableName;

    /**
     * Initializes a new instance of the LogDataTable class.
     *
     * @param service The logging service.
     * @param tableName The name of the service table.
     */
    public LogDataTable(SampleLoggingService service, String tableName) {
        if (service == null) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceCannotBeNull());
        }

        if (StringExtensions.isNullOrWhiteSpace(service.getName())) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceNameInvalid());
        }

        if (StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.tableName = tableName;
        String directory = System.getProperty("user.dir");
        DB.root(String.format("%1$s\\", directory));
        DB database = new DB(200);
        Config config = database.getConfig();
        config.ensureTable(LogEntry.class, tableName, "TimeStamp");
        config.ensureTable(InstantDb.class, String.format("%1$s%2$s", tableName, "Time"), "TimeStamp");
        config.ensureIndex(LogEntry.class, tableName, true, "Id(38)");

        if (SampleLoggingService.getIboxDb() == null) {
            SampleLoggingService.setIboxDb(database.open());
        }

        this.setDataManagementService(service);
    }

    /**
     * Gets the current timestamp.
     *
     * @return The timestamp of the last record written to the service, or 0.
     */
    @Override
    public final long getCurrentTimestamp() {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            List<LogEntry> logEntryCollection = new ArrayList<>();
            SampleLoggingService.getIboxDb().select(LogEntry.class, String.format("from %1$s", this.tableName)).forEach(logEntryCollection::add);

            int count = logEntryCollection.size();
            return count == 0 ? 0L : Collections.max(logEntryCollection, (LogEntry le1, LogEntry le2) -> {
                if (le1.getTimeStamp() == le2.getTimeStamp()) {
                    return 0;
                } else if (le1.getTimeStamp() < le2.getTimeStamp()) {
                    return -1;
                } else {
                    return 1;
                }
            }).getTimeStamp();
        }
    }

    /**
     * The name of the table.
     */
    private String name;

    /**
     * Gets the name of the table.
     *
     * @return The name of the table.
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * Sets the name of the table.
     *
     * @param value The name of the table.
     */
    @Override
    public final void setName(String value) {
        this.name = value;
    }

    /**
     * Gets a count of the number of records in the table.
     *
     * @return A count of the number of records in the table.
     */
    @Override
    public final int getCount() {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            // Get the log entries
            List<LogEntry> logEntryCollection = new ArrayList<>();
            SampleLoggingService.getIboxDb().select(LogEntry.class, String.format("from %1$s", this.tableName)).forEach(logEntryCollection::add);
            return logEntryCollection.size();
        }
    }

    /**
     * Gets the log entries.
     *
     * @return The log entries.
     */
    @Override
    public List<LogEntry> getRecords() {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            // Get the log entries
            List<LogEntry> logEntryCollection = new ArrayList<>();
            SampleLoggingService.getIboxDb().select(LogEntry.class, String.format("from %1$s", this.tableName)).forEach(logEntryCollection::add);

            return logEntryCollection.stream().map((logEntry) -> {
                Optional<InstantDb> instantDb = getFirstOrDefault(
                        SampleLoggingService
                                .getIboxDb()
                                .select(
                                        InstantDb.class,
                                        String.format("from %1$s%2$s where TimeStamp==?", this.tableName, "Time"),
                                        logEntry.getTimeStamp()));
                if (instantDb.isPresent()) {
                    logEntry.setTime(instantDb.get().getInstant());
                }
                return logEntry;
            }).sorted((LogEntry le1, LogEntry le2) -> le1.getTime().compareTo(le2.getTime())).collect(Collectors.toList());
        }
    }

    /**
     * The in-memory data management service.
     */
    private DataManagementService dataManagementService;

    /**
     * Gets the in-memory data management service.
     *
     * @return The in-memory data management service.
     */
    @Override
    public final DataManagementService getDataManagementService() {
        return this.dataManagementService;
    }

    /**
     * Sets the in-memory data management service.
     *
     * @param value The in-memory data management service.
     */
    @Override
    public final void setDataManagementService(DataManagementService value) {
        this.dataManagementService = value;
    }

    /**
     * Adds a log entry to the service.
     *
     * @param record The log entry to be stored.
     */
    @Override
    public final void add(LogEntry record) {
        this.insert(record);
    }

    /**
     * Clears all requests from the service.
     */
    @Override
    public final void clear() {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            try (Box box = SampleLoggingService.getIboxDb().cube()) {
                // Get the log entries
                for (LogEntry item : box.select(LogEntry.class, String.format("from %1$s where TimeStamp>?", this.tableName), 0L)) {
                    // Delete all log entries from the service
                    box.bind(this.tableName, item.getTimeStamp()).delete();
                }

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to clear log entries from the log. %1$s", commit));
                }
            }
        }
    }

    /**
     * Commits changes to the service.
     */
    @Override
    public final void commit() {
        commit(null);
    }

    /**
     * Commits changes to the service.
     *
     * @param context The optional transaction context for persisting the
     * record.
     */
    @Override
    public final void commit(TransactionManager context) {
    }

    /**
     * Close the service.
     */
    @Override
    public final void close() {
        // do nothing;
    }

    /**
     * Removes a record from the service.
     *
     * @param record The deferred record to be removed from the service.
     */
    @Override
    public final void remove(LogEntry record) {
        this.delete(record);
    }

    /**
     * Rolls back changes to the service.
     */
    @Override
    public final void rollback() {
        rollback(null);
    }

    /**
     * Rolls back changes to the service.
     *
     * @param context The optional transaction context for persisting the
     * record.
     */
    @Override
    public final void rollback(TransactionManager context) {
    }

    /**
     * log entry that matches the given log entry identifier.
     *
     * Returns a
     *
     * @param id The log entry identifier.
     * @return A log entry that matches the given log entry identifier.
     */
    @Override
    public final NbsIntegrationLogEntry getLogEntry(String id) {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            // Get the log entry
            return getFirstOrDefault(
                    SampleLoggingService.getIboxDb().select(
                            LogEntry.class,
                            String.format("from %1$s where Id==?", this.tableName), id))
                    .orElse(null);
        }
    }

    /**
     * Adds a log entry to the service.
     *
     * @param records The log entry to be stored.
     */
    @Override
    public final void add(Object records) {
        if (!(records instanceof NbsIntegrationLogEntry)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), NbsIntegrationLogEntry.class.getName()));
        }

        this.add((LogEntry) records);
    }

    /**
     * Removes a record from the service.
     *
     * @param record The deferred record to be removed from the service.
     */
    @Override
    public final void remove(Object record) {
        if (!(record instanceof NbsIntegrationLogEntry)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), NbsIntegrationLogEntry.class.getName()));
        }

        this.remove((LogEntry) record);
    }

    /**
     * Delete a log entry.
     *
     * @param logEntry The log entry to be deleted.
     */
    private void delete(NbsIntegrationLogEntry logEntry) {
        // Delete the record by timestamp
        this.doDelete(logEntry.getTimeStamp());
    }

    /**
     * Perform deletion of a log entry.
     *
     * @param requestTimeStamp The timestamp of the log entry to be deleted.
     */
    private void doDelete(long requestTimeStamp) {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            try (Box box = SampleLoggingService.getIboxDb().cube()) {
                // Get the log entry
                Optional<LogEntry> item = getFirstOrDefault(
                        box.select(
                                LogEntry.class,
                                String.format("from %1$s where TimeStamp==?", this.tableName),
                                requestTimeStamp));

                if (!item.isPresent()) {
                    return;
                }

                // Delete all log entries from the service
                box.bind(this.tableName, item.get().getTimeStamp()).delete();

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to delete log entry %1$s from the log. %2$s", requestTimeStamp, commit));
                }
            }
        }
    }

    /**
     * Insert a log entry.
     *
     * @param logEntry The log entry to be inserted.
     */
    private void insert(LogEntry logEntry) {
        synchronized (SampleLoggingService.LOCK_SERVICE) {
            try (Box box = SampleLoggingService.getIboxDb().cube()) {
                // Check to see if the log entry has been inserted already
                Optional<LogEntry> item = getFirstOrDefault(
                        box.select(
                                LogEntry.class,
                                String.format("from %1$s where TimeStamp==?", this.tableName),
                                logEntry.getTimeStamp()));

                if (item.isPresent()) {
                    return;
                }

                box.bind(this.tableName).insert(logEntry);

                if (logEntry.getTime() != null) {
                    InstantDb db2 = new InstantDb(logEntry.getTimeStamp(), logEntry.getTime());
                    box.bind(String.format("%1$s%2$s", this.tableName, "Time")).insert(db2);
                }

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to insert log entry %1$s into the log. %2$s", logEntry.getTimeStamp(), commit));
                }
            }
        }
    }

    /**
     * Gets the first item in a collection or the default (null).
     *
     * @param items The collection.
     * @return An Optional instance containing the first item or null.
     */
    private <T> Optional<T> getFirstOrDefault(Iterable<T> items) {
        Iterator<T> iterator = items.iterator();
        if (iterator.hasNext()) {
            try {
                return Optional.ofNullable(iterator.next());
            } catch (NoSuchElementException emptyEx) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
