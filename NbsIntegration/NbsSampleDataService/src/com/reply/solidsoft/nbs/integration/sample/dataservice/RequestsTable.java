/**
 * -----------------------------------------------------------------------------
 * File=RequestsTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record store.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice;

import iBoxDB.LocalServer.Box;
import iBoxDB.LocalServer.CommitResult;
import com.reply.solidsoft.nbs.integration.data.TransactionException;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implements a transactional file record store.
 */
public class RequestsTable extends BaseDataTable<DeferredRequest> {

    /**
     * Initializes a new instance of the RequestsTable class.
     */
    public RequestsTable() {
        super("requests", DeferredRequest.class);
    }

    /**
     * Initializes a new instance of the RequestsTable class.
     *
     * @param service The data management service.
     */
    public RequestsTable(SampleStoreAndForwardService service) {
        super(service, "requests", DeferredRequest.class);
    }
    
        /**
         * Record the acknowledgement of receipt of a list of deferred requests by the national system.
         *
         * @param requests The list of deferred requests.
         */
        public void acknowledgeRequest(List<DeferredRequest> requests)
        {
            synchronized (SampleStoreAndForwardService.TABLE_LOCKS.get(this.getName()))
            {
                try (Box box = SampleStoreAndForwardService.getIboxDb().cube())
                {
                    requests.stream().map((request) -> this.getFirstOrDefault(
                            box.select(
                                    DeferredRequest.class,
                                    String.format("from %1$s where TimeStamp==?", this.getName()),
                                    request.getTimeStamp()))).filter((item) -> !(!item.isPresent())).map((item) -> {
                        // Get the deferred request
                        item.get().setAcknowledged(true);
                        return item;
                    }).forEachOrdered((item) -> {
                        // Delete all log entries from the service
                        box.bind(this.getName(), item.get().getTimeStamp()).update(item.get());
                    });

                    CommitResult commit = box.commit();

                    if (commit != CommitResult.OK)
                    {
                        throw new TransactionException(String.format("Failed to commit deletion to data table %1$s. %2$s", this.getName(), commit));
                    }
                }
            }
        }
        
            /**
     * Gets the first item in a collection or the default (null).
     *
     * @param <T> The class of item in the collection.
     * @param items The collection.
     * @return An Optional instance containing the first item or null.
     */
    private Optional<DeferredRequest> getFirstOrDefault(Iterable<DeferredRequest> items) {
        Iterator<DeferredRequest> iterator = items.iterator();
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
