/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentialsDataTable.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.credentialsservice;

import iBoxDB.LocalServer.DB;
import iBoxDB.LocalServer.Box;
import iBoxDB.LocalServer.CommitResult;
import iBoxDB.LocalServer.DatabaseConfig.Config;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.data.TransactionException;
import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.data.model.TypedRecords;
import com.reply.solidsoft.nbs.integration.extensions.StringExtensions;
import com.reply.solidsoft.nbs.integration.sample.credentialsservice.properties.Resources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Iterator;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;

/**
 * Implements a transactional file record service.
 */
public class ClientCredentialsDataTable implements TypedDataTable<ClientCredentials>, TypedRecords<ClientCredentials> {

    /**
     * The table name.
     */
    private String tableName;

    /**
     * Initializes a new instance of the ClientCredentialsDataTable class.
     *
     * @param service The client credentials service.
     * @param tableName The name of the service table.
     */
    public ClientCredentialsDataTable(SampleClientCredentialsService service, String tableName) {
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
        DB database = new DB(300);
        Config config = database.getConfig();

        config.ensureTable(ClientCredentials.class, tableName, "TimeStamp");
        config.ensureIndex(ClientCredentials.class, tableName, false, "Organisation(50)");
        config.ensureIndex(ClientCredentials.class, tableName, false, "Organisation", "Location(50)");
        config.ensureIndex(ClientCredentials.class, tableName, true, "Organisation", "Location", "Equipment(50)");

        if (SampleClientCredentialsService.getIboxDb() == null) {
            SampleClientCredentialsService.setIboxDb(database.open());
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
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            List<ClientCredentials> clientCredentialsCollection = new ArrayList<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s", this.tableName)).forEach(clientCredentialsCollection::add);

            int count = clientCredentialsCollection.size();
            return count == 0 ? 0L : Collections.max(clientCredentialsCollection, (ClientCredentials cc1, ClientCredentials cc2) -> {
                if (cc1.getTimeStamp() == cc2.getTimeStamp()) {
                    return 0;
                } else if (cc1.getTimeStamp() < cc2.getTimeStamp()) {
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
    private String Name;

    /**
     * Gets the name of the table.
     *
     * @return The name of the table.
     */
    @Override
    public final String getName() {
        return Name;
    }

    /**
     * Sets the name of the table.
     *
     * @param value The name of the table.
     */
    @Override
    public final void setName(String value) {
        Name = value;
    }

    /**
     * Gets a count of the number of records in the table. return A count of the
     * number of records in the table.
     */
    @Override
    public final int getCount() {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials count.
            List<ClientCredentials> clientCredentialsCollection = new ArrayList<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s", this.tableName)).forEach(clientCredentialsCollection::add);
            return clientCredentialsCollection.size();
        }
    }

    /**
     * Gets the client credentials.
     *
     * @return The client credentials.
     */
    @Override
    public List<ClientCredentials> getRecords() {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials entries.
            // Get the log entries
            List<ClientCredentials> clientCredentialsCollection = new ArrayList<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s", this.tableName)).forEach(clientCredentialsCollection::add);
            return clientCredentialsCollection;
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
     * Adds a record to the service.
     *
     * @param record The client credentials to be stored.
     */
    @Override
    public final void add(ClientCredentials record) {
        this.insert(record);
    }

    /**
     * Clears all requests from the service.
     */
    @Override
    public final void clear() {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            try (Box box = SampleClientCredentialsService.getIboxDb().cube()) {
                // Get the client credentials.
                for (ClientCredentials item : box.select(ClientCredentials.class, String.format("from %1$s where TimeStamp>?", this.tableName), 0L)) {
                    // Delete all client credentials from the service
                    box.bind(this.tableName, item.getTimeStamp()).delete();
                }

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to clear client credentials from the table. %1$s", commit));
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
     * @param record The client credentials to be removed from the service.
     */
    @Override
    public final void remove(ClientCredentials record) {
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
     * Returns a set of client credentials that matches the given client
     * credentials identifier.
     *
     * @param connectionIdentifier The connection identifier.
     * @return A set of client credentials that matches the given client
     * credentials identifier.
     */
    public final ClientCredentials getClientCredentials(ConnectionIdentifier connectionIdentifier) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials
            Optional<ClientCredentials> item = getFirstOrDefault(
                    SampleClientCredentialsService
                            .getIboxDb()
                            .select(
                                    ClientCredentials.class,
                                    String.format("from %1$s where Organisation==? & Location==? & Equipment==?", this.tableName),
                                    connectionIdentifier.getOrganisation(),
                                    connectionIdentifier.getLocation(),
                                    connectionIdentifier.getEquipment()));

            if (!item.isPresent()) {
                return null;
            }

            ClientCredentials clientCredentials = item.get();

            // Decrypt the client secret here.
            try {
                clientCredentials.setClientSecret(Encryptor.decrypt(clientCredentials.getClientSecret()));
            } catch (Exception ex) {
                // Log or report the error here
            }

            return clientCredentials;
        }
    }

    /**
     * Returns a set of client credentials that matches the parameters.
     *
     * @param organisation The organsiation.
     * @param location The location.
     * @param equipment The equipment.
     * @return A set of client credentials.
     */
    public final ClientCredentials getClientCredentials(String organisation, String location, String equipment) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials
            return this.getFirstOrDefault(
                    SampleClientCredentialsService
                            .getIboxDb()
                            .select(
                                    ClientCredentials.class,
                                    String.format("from %1$s where Organisation ==? &Location ==? &Equipment ==? ", this.tableName),
                                    organisation,
                                    location,
                                    equipment))
                    .orElse(null);
        }
    }

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    public final Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            Map<String, ClientCredentials> results = new HashMap<>();
            // Get the client credentials
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s where Organisation ==?", this.tableName), organisation).forEach((cc) -> results.put(cc.getEquipment(), cc));
            return results;
        }
    }

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @param location The location of the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    public final Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation, String location) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials
            Map<String, ClientCredentials> results = new HashMap<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s where Organisation ==? & Location ==?", this.tableName), organisation, location).forEach((cc) -> results.put(cc.getEquipment(), cc));
            return results;
        }
    }

    /**
     * Returns a dictionary of client credentials keyed by location.
     *
     * @param organisation The organisation that owns the locations.
     * @return A dictionary of client credentials keyed by location.
     */
    public final Map<String, ClientCredentials> getClientCredentialsByLocation(String organisation) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials
            Map<String, ClientCredentials> results = new HashMap<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s where Organisation ==?", this.tableName), organisation).forEach((cc) -> results.put(cc.getLocation(), cc));
            return results;
        }
    }

    /**
     * Returns a dictionary of client credentials keyed by organisation.
     *
     * @return A dictionary of client credentials keyed by organisation.
     */
    public final Map<String, ClientCredentials> getClientCredentialsByOrganisation() {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            // Get the client credentials
            Map<String, ClientCredentials> results = new HashMap<>();
            SampleClientCredentialsService.getIboxDb().select(ClientCredentials.class, String.format("from %1$s", this.tableName)).forEach((cc) -> results.put(cc.getOrganisation(), cc));
            return results;
        }
    }

    /**
     * Adds a record to the service.
     *
     * @param records The client credentials to be stored.
     */
    @Override
    public final void add(Object records) {
        if (!(records instanceof ClientCredentials)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), ClientCredentials.class.getName()));
        }

        this.add((ClientCredentials) records);
    }

    /**
     * Removes a record from the service.
     *
     * @param record The client credentials to be removed from the service.
     */
    @Override
    public final void remove(Object record) {
        if (!(record instanceof ClientCredentials)) {
            throw new IllegalArgumentException(String.format(Resources.getDataTable_RequestTypeException(), ClientCredentials.class.getName()));
        }

        this.remove((ClientCredentials) record);
    }

    /**
     * Delete a record.
     *
     * @param request The record to be deleted.
     */
    private void delete(ClientCredentials request) {
        // Delete the record by timestamp
        this.doDelete(request.getTimeStamp());
    }

    /**
     * Perform deletion of a record.
     *
     * @param requestTimeStamp The timestamp of the record to be deleted.
     */
    private void doDelete(long requestTimeStamp) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            try (Box box = SampleClientCredentialsService.getIboxDb().cube()) {
                // Get the log entry
                Optional<ClientCredentials> item = getFirstOrDefault(
                        box.select(
                                ClientCredentials.class,
                                String.format("from %1$s where TimeStamp==?", this.tableName),
                                requestTimeStamp));

                if (!item.isPresent()) {
                    return;
                }

                // Delete all log entries from the service
                box.bind(this.tableName, item.get().getTimeStamp()).delete();

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to delete client credentials %1$s from the database. %2$s", requestTimeStamp, commit));
                }
            }
        }
    }

    /**
     * Insert a record.
     *
     * @param request The record to be inserted.
     */
    private void insert(ClientCredentials request) {
        synchronized (SampleClientCredentialsService.LOCK_SERVIVE) {
            try (Box box = SampleClientCredentialsService.getIboxDb().cube()) {
                // Check to see if the log entry has been inserted already
                ClientCredentials item = 
                        this.getClientCredentials(
                                request.getOrganisation(), 
                                request.getLocation(), 
                                request.getEquipment());

                if (null != item) {
                    return;
                }

                // Encrypt the client secret
                try {
                    request.setClientSecret(Encryptor.encrypt(request.getClientSecret()));
                } catch (Exception ex) {
                    // Log or report the error here
                }

                box.bind(this.tableName).insert(request);

                CommitResult commit = box.commit();

                if (commit != CommitResult.OK) {
                    throw new TransactionException(String.format("Failed to insert client credentials %1$s ino the database. %2$s", request.getTimeStamp(), commit));
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
    private Optional<ClientCredentials> getFirstOrDefault(Iterable<ClientCredentials> items) {
        Iterator<ClientCredentials> iterator = items.iterator();
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
