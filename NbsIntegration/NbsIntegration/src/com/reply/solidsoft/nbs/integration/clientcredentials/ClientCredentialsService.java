/**
 * -----------------------------------------------------------------------------
 * File=IClientCredentialsService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a repository of sets of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.clientcredentials;

import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentialsCollection;
import java.util.Map;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;

/**
 * Represents a repository of sets of client credentials.
 */
public interface ClientCredentialsService extends DataManagementService {

    /**
     * Gets the collection of all client credentials in the repository.
     *
     * @return The collection of all client credentials in the repository.
     */
    public ClientCredentialsCollection getClientCredentialsCollection();

    /**
     * Adds a set of client credentials to the repository.
     *
     * @param clientCredentials A set of client credentials.
     */
    public void add(ClientCredentials clientCredentials);

    /**
     * Clears all sets of client credentials from the repository.
     */
    public void clear();

    /**
     * Returns a set of client credentials that matches the given connection
     * identifier.
     *
     * @param connectionIdentifier The connection identifier.
     * @return A set of client credentials that matches the given client
     * credentials identifier.
     */
    public ClientCredentials getClientCredentials(ConnectionIdentifier connectionIdentifier);

    /**
     * Returns a set of client credentials that matches the parameters.
     *
     * @param organisation The organisation.
     * @param location The location.
     * @param equipment The equipment.
     * @return A set of client credentials.
     */
    public ClientCredentials getClientCredentials(String organisation, String location, String equipment);

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    public Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation);

    /**
     * Returns a dictionary of client credentials keyed by equipment.
     *
     * @param organisation The organisation that owns the equipment.
     * @param location The location of the equipment.
     * @return A dictionary of client credentials keyed by equipment.
     */
    public Map<String, ClientCredentials> getClientCredentialsByEquipment(String organisation, String location);

    /**
     * Returns a dictionary of client credentials keyed by location.
     *
     * @param organisation The organisation that owns the locations.
     * @return A dictionary of client credentials keyed by location.
     *
     */
    public Map<String, ClientCredentials> getClientCredentialsByLocation(String organisation);

    /**
     * Returns a dictionary of client credentials keyed by organisation.
     *
     * @return A dictionary of client credentials keyed by organisation.
     *
     */
    public Map<String, ClientCredentials> getClientCredentialsByOrganisation();

    /**
     * Removes a set of client credentials from the repository.
     *
     * @param clientCredentials The set of client credentials.
     * @return True, if the client credentials were found and removed
     * successfully; otherwise false.
     */
    public boolean remove(ClientCredentials clientCredentials);
}
