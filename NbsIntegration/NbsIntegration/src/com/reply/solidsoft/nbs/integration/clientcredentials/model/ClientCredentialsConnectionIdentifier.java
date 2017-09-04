/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentialsIdentifier.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A connection identifier for a set of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.clientcredentials.model;

/**
 * A connection identifier for a set of client credentials.
 */
public class ClientCredentialsConnectionIdentifier implements ConnectionIdentifier {

    /**
     * The organization identifier.
     */
    private String organisation;

    /**
     * Gets the organization identifier.
     *
     * @return The organization identifier.
     */
    @Override
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the organization identifier.
     *
     * @param value The organization identifier.
     */
    @Override
    public void setOrganisation(String value) {
        organisation = value;
    }

    /**
     * The location identifier.
     */
    private String location;

    /**
     * Gets the location identifier.
     *
     * @return The location identifier.
     */
    @Override
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location identifier.
     *
     * @param value the location identifier.
     */
    @Override
    public void setLocation(String value) {
        location = value;
    }

    /**
     * The equipment identifier.
     */
    private String equipment;

    /**
     * Gets the equipment identifier.
     *
     * @return The equipment identifier.
     */
    @Override
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment identifier.
     *
     * @param value The equipment identifier.
     */
    @Override
    public void setEquipment(String value) {
        equipment = value;
    }
}
