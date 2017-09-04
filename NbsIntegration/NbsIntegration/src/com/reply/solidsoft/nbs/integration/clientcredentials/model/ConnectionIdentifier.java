/**
 * -----------------------------------------------------------------------------
 * File=IClientCredentialsIdentifier.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an identifier for a set of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.clientcredentials.model;

/**
 * Represents an identifier for a set of client credentials.
 */
public interface ConnectionIdentifier {

    /**
     * Gets the organization identifier.
     *
     * @return The organization identifier.
     */
    public String getOrganisation();

    /**
     * Sets the organization identifier.
     *
     * @param value The organization identifier.
     */
    public void setOrganisation(String value);

    /**
     * Gets the location identifier.
     *
     * @return The location identifier.
     */
    public String getLocation();

    /**
     * Sets the location identifier.
     *
     * @param value The location identifier.
     */
    public void setLocation(String value);

    /**
     * Gets the equipment identifier.
     *
     * @return The equipment identifier.
     */
    public String getEquipment();

    /**
     * Sets the equipment identifier.
     *
     * @param value The equipment identifier.
     */
    public void setEquipment(String value);
}
