/**
 * -----------------------------------------------------------------------------
 * File=DataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record store.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice;

import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RequestTransactionLogEntry;
import iBoxDB.LocalServer.Box;
import iBoxDB.LocalServer.CommitResult;
import com.reply.solidsoft.nbs.integration.extensions.StringExtensions;
import com.reply.solidsoft.nbs.integration.data.TransactionException;
import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.sample.dataservice.properties.Resources;
import java.util.*;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;
import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;
import com.reply.solidsoft.nbs.integration.data.model.TypedRecords;

/**
 * Implements a transactional file record store.
 *
 * @param <T> The data management service record type.
 */
public class BaseDataTable<T extends DataRecord> implements TypedDataTable<T>, TypedRecords<T> {

    /**
     * The class to T. The class must be passed through the constructor due to
     * Java's type erasure approach to generics.
     */
    private final Class<T> tClass;

    /**
     * Initializes a new instance of the DataTable{T} class.
     *
     * @param tableName The name of the data management service table.
     * @param tClass The class to T.
     */
    public BaseDataTable(String tableName, Class<T> tClass) {
        this.tClass = tClass;
        if (StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.setName(tableName);
    }

    /**
     * Initializes a new instance of the DataTable{T} class.
     *
     * @param service The data management service.
     * @param tableName The name of the data management service table.
     * @param tClass The class of T.
     */
    public BaseDataTable(SampleStoreAndForwardService service, String tableName, Class<T> tClass) {
        this.tClass = tClass;
        if (service == null) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceCannotBeNull());
        }

        if (StringExtensions.isNullOrWhiteSpace(service.getName())) {
            throw new IllegalArgumentException(Resources.getDataTable_DataManagementServiceNameInvalid());
        }

        if (StringExtensions.isNullOrWhiteSpace(tableName)) {
            throw new IllegalArgumentException(Resources.getDataTable_DataTableNameInvalid());
        }

        this.setName(tableName);
    }

    /**
     * Gets the current timestamp.
     *
     * @return The timestamp of the last record written to the store, or 0.
     */
    @Override
    public final long getCurrentTimestamp() {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get(this.getName())) {
            List<T> collection = new ArrayList<>();
            SampleStoreAndForwardService.getIboxDb().select(tClass, String.format("from %1$s", this.getName())).forEach(collection::add);

            int count = collection.size();
            return count == 0 ? 0L : Collections.max(collection, (T record1, T record2) -> {
                if (record1.getTimeStamp() == record2.getTimeStamp()) {
                    return 0;
                } else if (record1.getTimeStamp() < record2.getTimeStamp()) {
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
        // Get the log entries
        List<T> dataRecordCollection = new ArrayList<>();
        SampleStoreAndForwardService.getIboxDb().select(tClass, String.format("from %1$s", this.getName())).forEach(dataRecordCollection::add);
        return dataRecordCollection.size();
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
     * Gets the deferred requests.
     *
     * @return The deferred requests.
     */
    @Override
    public List<T> getRecords() {
        List<T> records = new ArrayList<>();
        SampleStoreAndForwardService.getIboxDb().select(tClass, String.format("from %1$s", this.getName())).forEach(records::add);
        return records;
    }

    /**
     * Gets the current transaction log timestamp.
     *
     * @return The timestamp of the last record written to the transaction log,
     * or 0.
     */
    private static long getCurrentLogTimestamp() {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get("transactions")) {
            // Get the deferred record log entries
            List<RequestTransactionLogEntry> deferredRequestLogEntries = new ArrayList<>();
            SampleStoreAndForwardService.getIboxDb().select(RequestTransactionLogEntry.class, "from transactions").forEach(deferredRequestLogEntries::add);

            int count = deferredRequestLogEntries.size();
            return count == 0 ? 0L : Collections.max(deferredRequestLogEntries, (RequestTransactionLogEntry le1, RequestTransactionLogEntry le2) -> {
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
     * Adds a record to the store.
     *
     * @param record The deferred record to be stored.
     */
    @Override
    public void add(T record) {
        this.insert(record);
    }

    /**
     * Clears all records from the store.
     */
    @Override
    public void clear() {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get(this.getName())) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                // Get the table entries
                for (T item : box.select(tClass, String.format("from %1$s where TimeStamp>?", this.getName()), 0L)) {
                    // Delete all table entries from the service
                    box.bind(this.getName(), item.getTimeStamp()).delete();
                }

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to clear the data table %1$s. %2$s", this.getName(), commit));
                }
            }
        }
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
        // Clear the transaction log.
        clearLogTransaction();
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
    public void remove(T record) {
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
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get("transactions")) {
            // Get the deferred record log entries
            for (RequestTransactionLogEntry logEntry : SampleStoreAndForwardService.getIboxDb().select(RequestTransactionLogEntry.class, "from transactions")) {
                this.doDelete(logEntry.getRequestTimeStamp());
            }

            // Clear the transaction log.
            clearLogTransaction();
        }
    }

    /**
     * Adds a record to the store.
     *
     * @param record The deferred record to be stored.
     */
    @Override
    public final void add(Object record) {
        if (!(tClass.isInstance(record))) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException()));
        }

        this.add((T) record);
    }

    /**
     * Removes a record from the store.
     *
     * @param record The deferred record to be removed from the store.
     */
    @Override
    public final void remove(Object record) {
        if (!(tClass.isInstance(record))) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException()));
        }

        this.remove((T) record);
    }

    /**
     * Log a transaction.
     *
     * @param request The record whose transaction is logged.
     */
    private static <T> void logTransaction(T request) {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get("transactions")) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                RequestTransactionLogEntry requestTransactionLogEntry = new RequestTransactionLogEntry(getCurrentLogTimestamp());
                requestTransactionLogEntry.setRequestTimeStamp(((DataRecord) request).getTimeStamp());
                box.bind("transactions").insert(requestTransactionLogEntry);

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to commit insert to transaction log. %1$s", commit));
                }
            }
        }
    }

    /**
     * Clear all entries from the transaction log.
     */
    private static void clearLogTransaction() {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get("transactions")) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                List<RequestTransactionLogEntry> transactions = new ArrayList();
                box.select(RequestTransactionLogEntry.class, "from transactions where TimeStamp>?", 0L).forEach(transactions::add);;
                // Get the transaction log entries
                for (RequestTransactionLogEntry item : transactions) {
                    // Delete all transaction log entries from the service
                    box.bind("transactions", item.getTimeStamp()).delete();
                }

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to clear the transaction log. %1$s", commit));
                }
            }
        }
    }

    /**
     * Remove an entry from the transaction log.
     *
     * @param record The entry record.
     */
    private static <T> void removeLogTransaction(T record) {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get("transactions")) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                // Get the log entry
                Optional<RequestTransactionLogEntry> item = getFirstLogEntryOrDefault(box.select(RequestTransactionLogEntry.class,
                                "from transactions where RequestTimeStamp==?",
                                ((DataRecord) record).getTimeStamp()));

                if (!item.isPresent()) {
                    return;
                }

                // Delete all log entries from the service
                box.bind("transactions", item.get().getTimeStamp()).delete();

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to remove record %1$s from the transaction log. %2$s", ((DataRecord) record).getTimeStamp(), commit));
                }
            }
        }
    }

    /**
     * Delete a record.
     *
     * @param request The record to be deleted.
     */
    private void delete(T request) {
        // Delete the record by timestamp
        this.doDelete(request.getTimeStamp());

        // Remove the log entry
        removeLogTransaction(request);
    }

    /**
     * Perform deletion of a record.
     *
     * @param requestTimeStamp The timestamp of the record to be deleted.
     */
    private void doDelete(long requestTimeStamp) {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get(this.getName())) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                // Get the log entry
                Optional<T> item = getFirstOrDefault(
                        box.select(
                                this.tClass,
                                String.format("from %1$s where TimeStamp==?", this.getName()),
                                requestTimeStamp));

                if (!item.isPresent()) {
                    return;
                }

                // Delete all log entries from the service
                box.bind(this.getName(), item.get().getTimeStamp()).delete();

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to commit deletion to data table %1$s. %2$s", this.getName(), commit));
                }
            }
        }
    }

    /**
     * Insert a record.
     *
     * @param request The record to be inserted.
     */
    private void insert(T request) {
        synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get(this.getName())) {
            try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
                // Check to see if record has been inserted already
                Optional<T> item = getFirstOrDefault(
                        box.select(
                                this.tClass,
                                String.format("from %1$s where TimeStamp==?", this.getName()),
                                request.getTimeStamp())
                );

                if (item.isPresent()) {
                    return;
                }

                // Log the transaction
                logTransaction(request);

                box.bind(this.getName()).insert(request);

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to commit insert to data table %1$s. %2$s", this.getName(), commit));
                }
            }
        }
    }

    private static Optional<RequestTransactionLogEntry> getFirstLogEntryOrDefault(Iterable<RequestTransactionLogEntry> items) {
        Iterator<RequestTransactionLogEntry> iterator = items.iterator();
        if (iterator.hasNext()) {
            try {
                return Optional.ofNullable(iterator.next());
            } catch (NoSuchElementException emptyEx) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    /**
     * Gets the first item in a collection or the default (null).
     *
     * @param <T> The class of item in the collection.
     * @param items The collection.
     * @return An Optional instance containing the first item or null.
     */
    private Optional<T> getFirstOrDefault(Iterable<T> items) {
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
