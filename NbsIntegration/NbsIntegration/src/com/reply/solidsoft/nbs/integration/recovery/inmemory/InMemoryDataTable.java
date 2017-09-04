/**
 * -----------------------------------------------------------------------------
 * File=InMemoryDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record store.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.inmemory;

import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;
import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;

/**
 * Implements a transactional file record store.
 *
 * @param <T> The data management service record type.
 */
public class InMemoryDataTable<T extends DataRecord> implements TypedDataTable<T> {

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The in-memory table.
     */
    private final List<T> inMemoryTable = new ArrayList<>();

    /**
     * Initializes a new instance of the LogDataTable{T} class.
     *
     * @param tableName The name of the data management service table.
     */
    public InMemoryDataTable(String tableName) {
        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.tableName = tableName;
    }

    /**
     * Initializes a new instance of the LogDataTable{T} class.
     *
     * @param dataManagementService The data management service.
     * @param tableName The name of the data management service table.
     */
    public InMemoryDataTable(InMemoryStoreAndForwardService dataManagementService, String tableName) {
        if (dataManagementService == null) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceCannotBeNull());
        }

        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(dataManagementService.getName())) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceNameInvalid());
        }

        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.tableName = tableName;
        this.setDataManagementService(dataManagementService);
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
        return dataManagementService;
    }

    /**
     * Sets the in-memory data management service.
     *
     * @param value The in-memory data management service.
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
            if (0 == inMemoryTable.size()) {
                return 0;
            }

            if (1 == inMemoryTable.size()) {
                return inMemoryTable.get(0).getTimeStamp();
            }

            return Collections.max(inMemoryTable, new InMemoryDataTableComparator()).getTimeStamp();
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
    public List<T> getRecords() {
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
    public final void add(T record) {
        this.doAdd(record);
    }

    /**
     * Clears all requests from the store.
     */
    @Override
    public final void clear() {
        // Delete all deferred requests from the data management service
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
     * Closes the store.
     */
    @Override
    public final void close() {
        // do nothing;
    }

    /**
     * Removes a record from the store.
     *
     * @param record The deferred record to be removed from the store.
     */
    @Override
    public final void remove(T record) {
        this.delete(record);
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
        ((List<InMemoryTransactionLogEntry>) this.getDataManagementService().getTransactionLog().getRecords()).forEach((logEntry) -> {
            this.doDelete(logEntry.getRequestTimeStamp());
        });

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
        // Java's type erasure doesn't allow the following sanity check, 
        // included in the C# version
        //////if (!(records instanceof T)) {
        //////    throw new IllegalArgumentException(
        //////        String.format(
        //////            Resources.getDataTable_RequestTypeException(), 
        //////            T.class.FullName));
        //////}

        this.add((T) records);
    }

    /**
     * Removes a record from the store.
     *
     * @param record The deferred record to be removed from the store.
     */
    @Override
    public final void remove(Object record) {
        // Java's type erasure doesn't allow the following sanity check, 
        // included in the C# version
        //////if (!(record instanceof T)) {
        //////    throw new IllegalArgumentException(
        //////        String.format(
        //////            Resources.DataTable_RequestTypeException, 
        //////            T.class.FullName), 
        //////        "record");
        //////}

        this.remove((T) record);
    }

    /**
     * Clear all entries from the transaction log.
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
    private void delete(T request) {
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
        T recordForDeletion = this.inMemoryTable.stream().filter(r -> r.getTimeStamp() == requestTimeStamp).findFirst().orElse(null);

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
    private void doAdd(T record) {
        // Check to see if the record has been inserted already
        T recordAlreadyInserted = this.inMemoryTable.stream().filter(r -> record.getTimeStamp() == r.getTimeStamp()).findFirst().orElse(null);

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
    private void logTransaction(T request) {
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
    private void removeLogTransaction(T request) {
        // Get the deferred record log entries
        List<InMemoryTransactionLogEntry> deferredRequestLogEntries = (List<InMemoryTransactionLogEntry>) this.getDataManagementService().getTransactionLog().getRecords();

        // remove the log entry
        InMemoryTransactionLogEntry logEntry = deferredRequestLogEntries.stream().filter(entry -> entry.getRequestTimeStamp() == request.getTimeStamp()).findFirst().orElse(null);

        if (logEntry == null) {
            return;
        }

        deferredRequestLogEntries.remove(logEntry);
    }

    /**
     * Comparator for data tables using the time stamp.
     *
     * @param <T> An DataRecord
     */
    public class InMemoryDataTableComparator<T extends DataRecord> implements Comparator<T> {

        /**
         * Compares two records by their timestamps.
         *
         * @param record1 The first record.
         * @param record2 The second record.
         * @return 0, if the two records have identical timestamps; otherwise 1
         * if record 2 has a later timestamp than record 1; otherwise -1..
         */
        @Override
        public int compare(T record1, T record2) {
            if (record1.getTimeStamp() > record2.getTimeStamp()) {
                return -1; // highest value first
            }
            if (record1.getTimeStamp() == record2.getTimeStamp()) {
                return 0;
            }
            return 1;
        }
    }
}
