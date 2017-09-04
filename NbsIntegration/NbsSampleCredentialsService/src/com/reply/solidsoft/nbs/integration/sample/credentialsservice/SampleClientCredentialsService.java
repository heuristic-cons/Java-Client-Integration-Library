/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentialsService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A persisted client credentials service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.credentialsservice;

import iBoxDB.LocalServer.AutoBox;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentialsCollection;
import java.util.HashMap;
import java.util.Map;
import com.reply.solidsoft.nbs.integration.clientcredentials.ClientCredentialsService;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/**
 * A persisted client credentials service.
 */
public class SampleClientCredentialsService implements ClientCredentialsService {

    /**
     * Lock on the service.
     */
    // ReSharper disable once StaticMemberInGenericType
    public static final Object LOCK_SERVIVE = new Object();

    /**
     * Initializes a new instance of the ClientCredentialsService class.
     */
    public SampleClientCredentialsService() {
        // Initialize the data management service for this application
        this.name = "clientCredentials";
        this.tables = new HashMap<>();
        this.getTables().put("clientCredentials", new ClientCredentialsDataTable(this, "clientCredentials"));

        this.transactionLog = null;
    }

    /**
     * The iBoxDB database.
     */
    private static AutoBox iboxDb;

    /**
     * Gets the iBoxDB database.
     *
     * @return The iBoxDB database.
     */
    public static AutoBox getIboxDb() {
        return iboxDb;
    }

    /**
     * Sets the iBoxDB database.
     *
     * @param value The iBoxDB database.
     */
    public static void setIboxDb(AutoBox value) {
        iboxDb = value;
    }

    /**
     * The name of the service.
     */
    private final String name;

    /**
     * Gets the name of the service.
     *
     * @return The name of the service.
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * Gets the collection of client credentials.
     *
     * @return The collection of client credentials.
     */
    @Override
    public final ClientCredentialsCollection getClientCredentialsCollection() {
        return new ClientCredentialsCollection(this.getTables().get("clientCredentials").getRecords());
    }

    /**
     * The collection of data management service tables.
     */
    private final Map<String, DataTable> tables;

    /**
     * Gets the collection of data management service tables.
     *
     * @return The collection of data management service tables.
     */
    @Override
    public final Map<String, DataTable> getTables() {
        return this.tables;
    }

    /**
     * The transaction log table table.
     */
    private final DataTable transactionLog;

    /**
     * Gets the transaction log table table.
     *
     * @return The transaction log table table.
     */
    @Override
    public final DataTable getTransactionLog() {
        return this.transactionLog;
    }

    /**
     * Closes the service.
     */
    @Override
    public void close() {
    }

    /**
     * Adds a set of client credentials to the data management service.
     *
     * @param clientCredentials The client credentials.
     */
    @Override
    public final void add(ClientCredentials clientCredentials) {
        if (clientCredentials == null) {
            return;
        }

        this.getTables().get("clientCredentials").add(clientCredentials);
    }

    /**
     * Clears the data management service of all client credentials.
     */
    @Override
    public final void clear() {
        this.getTables().get("clientCredentials").clear();
    }

    /**
     * Returns a set of client credentials that matches the given client
     * credentials identifier.
     *
     * @param connectionIdentifier The connection identifier.
     * @return A set of client credentials that matches the given client
     * credentials identifier.
     */
    @Override
    public final ClientCredentials getClientCredentials(ConnectionIdentifier connectionIdentifier) {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentials(connectionIdentifier);
    }

    /**
     * Returns a set of client credentials that matches the parameters.
     *
     * @param organisation The organsiation.
     * @param location The location.
     * @param equipment The equipment.
     * @return A set of client credentials.
     */
    @Override
    public final ClientCredentials getClientCredentials(String organisation, String location, String equipment) {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentials(organisation, location, equipment);
    }

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    @Override
    public final Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation) {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentialsByEquipment(organisation);
    }

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @param location The location of the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    @Override
    public final Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation, String location) {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentialsByEquipment(organisation, location);
    }

    /**
     * Returns a dictionary of client credentials keyed by location.
     *
     * @param organisation The organisation that owns the locations.
     * @return A dictionary of client credentials keyed by location.
     */
    @Override
    public final Map<String, ClientCredentials> getClientCredentialsByLocation(String organisation) {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentialsByLocation(organisation);
    }

    /**
     * Returns a dictionary of client credentials keyed by organisation.
     *
     * @return A dictionary of client credentials keyed by organisation.
     */
    @Override
    public final Map<String, ClientCredentials> getClientCredentialsByOrganisation() {
        return ((ClientCredentialsDataTable) this.getTables().get("clientCredentials")).getClientCredentialsByOrganisation();
    }

    /**
     * Removes a set of client credentials from the repository.
     *
     * @param clientCredentials The set of client credentials.
     * @return True, if the client credentials were found and removed
     * successfully; otherwise false.
     */
    @Override
    public final boolean remove(ClientCredentials clientCredentials) {
        if (clientCredentials == null) {
            return false;
        }

        this.getTables().get("clientCredentials").remove(clientCredentials);

        return true;
    }
}
