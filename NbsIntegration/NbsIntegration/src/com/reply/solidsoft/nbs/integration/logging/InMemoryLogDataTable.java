/**
 * -----------------------------------------------------------------------------
 * File=InMemoryLogDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements an in-memory log data table.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging;

import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import com.reply.solidsoft.nbs.integration.recovery.inmemory.InMemoryTransactionLogEntry;
import java.util.ArrayList;
import java.util.List;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogDataTable;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntry;

/**
 * Implements an in-memory log data table.
 */
public class InMemoryLogDataTable implements NbsIntegrationLogDataTable {

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The in-memory table.
     */
    private final List<LogEntry> inMemoryTable = new ArrayList<>();

    /**
     * Initializes a new instance of the InMemoryLogDataTable class.
     *
     * @param tableName The name of the logging service table.
     */
    public InMemoryLogDataTable(String tableName) {
        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.tableName = tableName;
    }

    /**
     * Initializes a new instance of the InMemoryLogDataTable class.
     *
     * @param loggingService The logging service.
     * @param tableName The name of the logging service table.
     */
    public InMemoryLogDataTable(InMemoryLoggingService loggingService, String tableName) {
        if (loggingService == null) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceCannotBeNull());
        }

        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(loggingService.getName())) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceNameInvalid());
        }

        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.tableName = tableName;
        this.setDataManagementService(loggingService);
    }

    /**
     * The in-memory logging service.
     */
    private DataManagementService dataManagementService;

    /**
     * Gets the in-memory logging service.
     *
     * @return The in-memory logging service.
     */
    @Override
    public final DataManagementService getDataManagementService() {
        return dataManagementService;
    }

    /**
     * Sets the in-memory logging service.
     *
     * @param value The in-memory logging service.
     */
    @Override
    public final void setDataManagementService(DataManagementService value) {
        dataManagementService = value;
    }

    /**
     * Gets the current timestamp.
     *
     * @return The timestamp of the last record written to the store, or 0.
     */
    @Override
    public final long getCurrentTimestamp() {
        try {
            LogEntry logEntry = this.inMemoryTable.stream().max((r1, r2) -> Long.compare(r1.getTimeStamp(), r2.getTimeStamp())).orElse(null);
            if (null == logEntry) {
                return 0;
            }

            return logEntry.getTimeStamp();
        } catch (IllegalStateException e) {
            return 0;
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
        return name;
    }

    /**
     * Sets the name of the table.
     *
     * @param value The name of the table.
     */
    @Override
    public final void setName(String value) {
        name = value;
    }

    /**
     * Gets a count of the number of records in the table.
     *
     * @return A count of the number of records in the table.
     */
    @Override
    public final int getCount() {
        return this.inMemoryTable.size();
    }

    /**
     * Gets the collection of records.
     *
     * @return The collection of records.
     */
    @Override
    public List<LogEntry> getRecords() {
        return this.inMemoryTable;
    }

    /**
     * Gets the current transaction log timestamp.
     *
     * @return The timestamp of the last record written to the transaction log,
     * or 0.
     */
    private long getCurrentLogTimestamp() {
        return this.getDataManagementService().getTransactionLog().getCurrentTimestamp();
    }

    /**
     * Adds a record to the store.
     *
     * @param record The deferred record to be stored.
     */
    @Override
    public final void add(LogEntry record) {
        this.doAdd(record);
    }

    /**
     * Clears all requests from the store.
     */
    @Override
    public final void clear() {
        // Delete all deferred requests from the logging service
        this.inMemoryTable.clear();
    }

    /**
     * Commits changes to the store.
     */
    @Override
    public final void commit() {
        commit(null);
    }

    /**
     * Commits changes to the store.
     *
     * @param context The optional transaction context for persisting the
     * record.
     */
    @Override
    public final void commit(TransactionManager context) {
        // clear the transaction log.
        this.clearLogTransaction();
    }

    /**
     * Removes a record from the store.
     *
     * @param record The deferred record to be removed from the store.
     */
    @Override
    public final void remove(LogEntry record) {
        this.delete(record);
    }

    /**
     * Gets a log entry by identifier.
     *
     * @param id The log entry identifier.
     * @return A log entry.
     */
    @Override
    public final NbsIntegrationLogEntry getLogEntry(String id) {
        // Get the log entry
        return this.inMemoryTable.stream().filter(r -> id.equals(r.getId())).findFirst().orElse(null);
    }

    /**
     * Rolls back changes to the store.
     */
    @Override
    public final void rollback() {
        rollback(null);
    }

    /**
     * Rolls back changes to the store.
     *
     * @param context The optional transaction context for persisting the
     * record.
     */
    @Override
    public final void rollback(TransactionManager context) {
        // Delete each of the uncommitted records.
        ((List<InMemoryTransactionLogEntry>) this.getDataManagementService()
                .getTransactionLog()
                .getRecords())
                .forEach((logEntry) -> this.doDelete(logEntry.getRequestTimeStamp()));

        // clear the transaction log.
        this.clearLogTransaction();
    }

    /**
     * Adds a record to the store.
     *
     * @param records The deferred record to be stored.
     */
    @Override
    public final void add(Object records) {
        if (!(records instanceof LogEntry)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), LogEntry.class.getName()));
        }

        this.add((LogEntry) records);
    }

    /**
     * Removes a record from the store.
     *
     * @param record The deferred record to be removed from the store.
     */
    @Override
    public final void remove(Object record) {
        if (!(record instanceof LogEntry)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), LogEntry.class.getName()));
        }

        this.remove((LogEntry) record);
    }

    /**
     * Closes the store.
     */
    @Override
    public void close() {
        // do nothing;
    }

    /**
     * clear all entries from the transaction log.
     */
    private void clearLogTransaction() {
        // Get the deferred record log entries
        this.getDataManagementService().getTransactionLog().clear();
    }

    /**
     * Delete a record.
     *
     * @param request The record to be deleted.
     */
    private void delete(LogEntry request) {
        // Delete the record by timestamp
        this.doDelete(request.getTimeStamp());

        // remove the log entry
        this.removeLogTransaction(request);
    }

    /**
     * Perform deletion of a record.
     *
     * @param requestTimeStamp The timestamp of the record to be deleted.
     */
    private void doDelete(long requestTimeStamp) {
        LogEntry recordForDeletion = this.inMemoryTable.stream().filter(r -> r.getTimeStamp() == requestTimeStamp).findFirst().orElse(null);

        if (recordForDeletion != null) {
            // Delete the record
            this.inMemoryTable.remove(recordForDeletion);
        }
    }

    /**
     * Add a record.
     *
     * @param record The record to be inserted.
     */
    private void doAdd(LogEntry record) {
        // Check to see if the record has been inserted already
        LogEntry recordAlreadyInserted = this.inMemoryTable.stream().filter(r -> record.getTimeStamp() == r.getTimeStamp()).findFirst().orElse(null);

        if (recordAlreadyInserted != null) {
            return;
        }

        // Log the transaction
        this.logTransaction(record);

        // add the record
        this.inMemoryTable.add(record);
    }

    /**
     * Log a transaction.
     *
     * @param request The record whose transaction is logged.
     */
    private void logTransaction(LogEntry request) {
        // Get the deferred record log entries
        List<InMemoryTransactionLogEntry> deferredRequestLogEntries = (List<InMemoryTransactionLogEntry>) this.getDataManagementService().getTransactionLog().getRecords();

        // add the log entry
        InMemoryTransactionLogEntry tempVar = new InMemoryTransactionLogEntry(this.getCurrentLogTimestamp());
        tempVar.setRequestTimeStamp(request.getTimeStamp());
        deferredRequestLogEntries.add(tempVar);
    }

    /**
     * remove an entry from the transaction log.
     *
     * @param request The record type.
     */
    private void removeLogTransaction(LogEntry request) {
        // Get the deferred record log entries
        List<InMemoryTransactionLogEntry> deferredRequestLogEntries = (List<InMemoryTransactionLogEntry>) this.getDataManagementService().getTransactionLog().getRecords();

        // remove the log entry
        InMemoryTransactionLogEntry logEntry = deferredRequestLogEntries.stream().filter(entry -> entry.getRequestTimeStamp() == request.getTimeStamp()).findFirst().orElse(null);

        if (logEntry == null) {
            return;
        }

        deferredRequestLogEntries.remove(logEntry);
    }
}
