/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentialsUpdate.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents update information for a set of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.clientcredentials.model;

/**
 * Represents update information for a set of client credentials.
 */
public class ClientCredentialsUpdate {

    /**
     * The client credentials with any updates.
     */
    private final ClientCredentials updatedClientCredentials;

    /**
     * Initializes a new instance of the ClientCredentialsUpdate class.
     *
     * @param clientCredentials The client credentials.
     */
    public ClientCredentialsUpdate(ClientCredentials clientCredentials) {
        this.originalClientCredentials = clientCredentials;
        this.updatedClientCredentials = new ClientCredentials(clientCredentials.getOrganisation(), clientCredentials.getLocation(), clientCredentials.getEquipment(), clientCredentials.getClientId(), clientCredentials.getClientSecret());
    }

    /**
     * Gets the client ID.
     *
     * @return The client ID.
     */
    public final String getClientId() {
        return this.updatedClientCredentials.getClientId();
    }

    /**
     * Gets the client secret.
     *
     * @return The client secret.
     */
    public final String getClientSecret() {
        return this.updatedClientCredentials.getClientSecret();
    }

    /**
     * Gets the equipment that will connect to the NBS to present these
     * credentials.
     *
     * @return The equipment that will connect to the NBS to present these
     * credentials.
     */
    public final String getEquipment() {
        return this.updatedClientCredentials.getEquipment();
    }

    /**
     * Sets the equipment that will connect to the NBS to present these
     * credentials.
     *
     * @param value The equipment that will connect to the NBS to present these
     * credentials.
     */
    public final void setEquipment(String value) {
        this.updatedClientCredentials.setEquipment(value);
    }

    /**
     * Gets the location from which the credentials will be used.
     *
     * @return The location from which the credentials will be used.
     */
    public final String getLocation() {
        return this.updatedClientCredentials.getLocation();
    }

    /**
     * Sets the location from which the credentials will be used.
     *
     * @param value The location from which the credentials will be used.
     */
    public final void setLocation(String value) {
        this.updatedClientCredentials.setLocation(value);
    }

    /**
     * Gets the organisation that requested creation of the credentials.
     *
     * @return The organisation that requested creation of the credentials
     */
    public final String getOrganisation() {
        return this.updatedClientCredentials.getOrganisation();
    }

    /**
     * Sets the organisation that requested creation of the credentials.
     *
     * @param value The organisation that requested creation of the credentials
     */
    public final void setOrganisation(String value) {
        this.updatedClientCredentials.setOrganisation(value);
    }

    /**
     * The client credentials.
     */
    private final ClientCredentials originalClientCredentials;

    /**
     * Gets the client credentials.
     *
     * @return The client credentials.
     */
    public final ClientCredentials getOriginalClientCredentials() {
        return originalClientCredentials;
    }
}
