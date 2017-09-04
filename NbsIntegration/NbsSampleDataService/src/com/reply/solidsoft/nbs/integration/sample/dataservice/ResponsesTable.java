/**
 * -----------------------------------------------------------------------------
 * File=ResponsesTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record store for responses.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice;

import iBoxDB.LocalServer.Box;
import iBoxDB.LocalServer.CommitResult;
import com.reply.solidsoft.nbs.integration.data.TransactionException;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;
import com.reply.solidsoft.nbs.integration.model.responses.SinglePackResponse;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.PackIdentifierDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RecoverySinglePackResponseDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RequestedPackStateDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.SinglePackResponseDb;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements a transactional file record store for responses.
 */
public class ResponsesTable extends BaseDataTable<RecoverySinglePackResponse> {

    /**
     * Initializes a new instance of the ResponsesTable class.
     */
    public ResponsesTable() {
        super("responses", RecoverySinglePackResponse.class);
    }

    /**
     * Initializes a new instance of the ResponsesTable class.
     *
     * @param service The store & forward data management service.
     */
    public ResponsesTable(SampleStoreAndForwardService service) {
        super(service, "responses", RecoverySinglePackResponse.class);
    }

    /**
     * Gets the deferred request responses.
     *
     * @return The deferred requests.
     */
    @Override
    public List<RecoverySinglePackResponse> getRecords() {
        List<RecoverySinglePackResponse> responses = new ArrayList<>();
        SampleStoreAndForwardService.getIboxDb().select(RecoverySinglePackResponse.class, String.format("from %1$s", this.getName())).forEach(responses::add);

        responses.stream().sorted((RecoverySinglePackResponse r1, RecoverySinglePackResponse r2) -> {
                if (r1.getTimeStamp() == r2.getTimeStamp()) {
                    return 0;
                } else if (r1.getTimeStamp() < r2.getTimeStamp()) {
                    return -1;
                } else {
                    return 1;
                }
            }).map((response) -> {
            Optional<PackIdentifier> packIdentifier = getFirstOrDefault(
                    SampleStoreAndForwardService
                            .getIboxDb()
                            .select(
                                    PackIdentifier.class,
                                    String.format("from %1$s%2$s where TimeStamp==?", this.getName(), "PackIdentifier"),
                                    response.getTimeStamp()));
            if (packIdentifier.isPresent()) {
                response.setPack(packIdentifier.get());
            }
            return response;
        }).map((RecoverySinglePackResponse response) -> {
            Optional<RequestedPackStateDb> requestedPackState = getFirstOrDefault(
                    SampleStoreAndForwardService
                            .getIboxDb()
                            .select(
                                    RequestedPackStateDb.class,
                                    String.format("from %1$s%2$s where TimeStamp==?", this.getName(), "RequestedPackState"),
                                    response.getTimeStamp()));
            if (requestedPackState.isPresent()) {
                response.setRequiredState(RequestedPackState.get(requestedPackState.get().getValue()));
            }
            return response;
        }).forEachOrdered((response) -> {
            Optional<SinglePackResponse> singlePackResponse = getFirstOrDefault(
                    SampleStoreAndForwardService
                            .getIboxDb()
                            .select(
                                    SinglePackResponse.class,
                                    String.format("from %1$s%2$s where TimeStamp==?", this.getName(), "SinglePackResponse"),
                                    response.getTimeStamp()));
            if (singlePackResponse.isPresent()) {
                response.setResult(singlePackResponse.get());
            }
        });

        return responses;
    }

    /**
     * Adds a record to the store.
     *
     * @param record The deferred record to be stored.
     */
    @Override
    public void add(RecoverySinglePackResponse record) {
        // Get the transaction log entries
        try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
            super.add(new RecoverySinglePackResponseDb(record));
            if (record.getPack() != null) {
                box.bind(String.format("%1$s%2$s", this.getName(), "PackIdentifier")).insert(new PackIdentifierDb(record.getTimeStamp(), record.getPack()));
            }

            if (record.getRequiredState() != null) {
                box.bind(String.format("%1$s%2$s", this.getName(), "RequestedPackState")).insert(new RequestedPackStateDb(record.getTimeStamp(), record.getRequiredState()));
            }

            if (record.getResult() != null) {
                box.bind(String.format("%1$s%2$s", this.getName(), "SinglePackResponse")).insert(new SinglePackResponseDb(record.getTimeStamp(), record.getResult()));
            }

            CommitResult commit = box.commit();

            if (commit != CommitResult.OK) {
                throw new TransactionException(String.format("Failed to insert a RecoverySinglePackResponse record into the data table %1$s. %2$s", this.getName(), commit));
            }
        }
    }

    /**
     * Removes a record to the store.
     *
     * @param record The deferred record to be removed.
     */
    @Override
    public void remove(RecoverySinglePackResponse record) {
        // Get the transaction log entries
        try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
            box.bind(String.format("%1$s%2$s", this.getName(), "SinglePackResponse"), record.getTimeStamp()).delete();
            box.bind(String.format("%1$s%2$s", this.getName(), "RequestedPackState"), record.getTimeStamp()).delete();
            box.bind(String.format("%1$s%2$s", this.getName(), "PackIdentifier"), record.getTimeStamp()).delete();
            super.remove(record);

            CommitResult commit = box.commit();

            if (commit != CommitResult.OK) {
                throw new TransactionException(String.format("Failed to delete a RecoverySinglePackResponse record from the data table %1$s. %2$s", this.getName(), commit));
            }
        }
    }

    /**
     * Clears all records from the store.
     */
    @Override
    public void clear() {
        // Get the transaction log entries
        try (Box box = SampleStoreAndForwardService.getIboxDb().cube()) {
            // Get the SinglePackResponse entries
            Iterable<SinglePackResponseDb> singlePackResponseEntries = box.select(SinglePackResponseDb.class, String.format("from %1$s%2$s where TimeStamp>?", this.getName(), "SinglePackResponse"), 0L);

            for (SinglePackResponseDb item : singlePackResponseEntries) {
                // Delete all SinglePackResponse entries from the service
                box.bind(String.format("%1$s%2$s", this.getName(), "SinglePackResponse"), item.getTimeStamp()).delete();
            }

            // Get the PackIdentifier entries
            Iterable<PackIdentifierDb> packIdentifierEntries = box.select(PackIdentifierDb.class, String.format("from %1$s%2$s where TimeStamp>?", this.getName(), "PackIdentifier"), 0L);

            for (PackIdentifierDb item : packIdentifierEntries) {
                // Delete all SinglePackResponse entries from the service
                box.bind(String.format("%1$s%2$s", this.getName(), "PackIdentifier"), item.getTimeStamp()).delete();
            }

            // Get the RequestedPackState entries
            Iterable<RequestedPackStateDb> requestedPackStateEntries = box.select(RequestedPackStateDb.class, String.format("from %1$s%2$s where TimeStamp>?", this.getName(), "RequestedPackState"), 0L);

            for (RequestedPackStateDb item : requestedPackStateEntries) {
                // Delete all SinglePackResponse entries from the service
                box.bind(String.format("%1$s%2$s", this.getName(), "RequestedPackState"), item.getTimeStamp()).delete();
            }

            super.clear();

            CommitResult commit = box.commit();

            if (commit != CommitResult.OK) {
                throw new TransactionException(String.format("Failed to clear the responses from the database. %1$s", commit));
            }
        }
    }

    /**
     * Gets the first item in a collection or the default (null).
     *
     * @param <TObj> The class of item in the collection.
     * @param items The collection.
     * @return An Optional instance containing the first item or null.
     */
    private <TObj> Optional<TObj> getFirstOrDefault(Iterable<TObj> items) {
        Iterator<TObj> iterator = items.iterator();
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
