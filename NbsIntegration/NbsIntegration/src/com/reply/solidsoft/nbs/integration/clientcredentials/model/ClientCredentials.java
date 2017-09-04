/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentials.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a set of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.clientcredentials.model;

import com.google.gson.annotations.Expose;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Represents a set of client credentials.
 */
public class ClientCredentials implements DataRecord {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    private static final Object TIMESTAMP_LOCK = new Object();

    /**
     * Timestamp counter;
     */
    @Expose(serialize = false, deserialize = false)
    private static long latestTimeStamp;

    /**
     * Timestamp value;
     */
    @Expose(serialize = false, deserialize = false)
    private long timeStamp;

    /**
     * Initializes a new instance of the ClientCredentials class.
     */
    public ClientCredentials() {
    }

    /**
     * Initializes a new instance of the ClientCredentials class.
     *
     * @param organisation The organisation.
     * @param location The location.
     * @param equipment The equipment.
     * @param clientId The client id.
     * @param clientSecret The client secret.
     */
    public ClientCredentials(String organisation, String location, String equipment, String clientId, String clientSecret) {
        this.setOrganisation(organisation);
        this.setLocation(location);
        this.setEquipment(equipment);
        this.setClientId(clientId);
        this.setClientSecret(clientSecret);
    }

    /**
     * Gets the timestamp value for this response record.
     *
     * @return The timestamp value for this response record.
     */
    @Override
    public final long getTimeStamp() {
        synchronized (TIMESTAMP_LOCK) {
            return this.timeStamp;
        }
    }

    /**
     * Sets the timestamp value for this response record.
     *
     * @param value The timestamp value for this response record.
     */
    public final void setTimeStamp(long value) {
        synchronized (TIMESTAMP_LOCK) {
            this.timeStamp = value;
        }
    }

    /**
     * The client identifier.
     */
    @Expose
    private String clientId;

    /**
     * Gets the client identifier.
     *
     * @return The client identifier.
     */
    public final String getClientId() {
        return clientId;
    }

    /**
     * Sets the client identifier.
     *
     * @param value The client identifier.
     */
    public final void setClientId(String value) {
        clientId = value;
    }

    /**
     * The client secret.
     */
    @Expose
    private String clientSecret;

    /**
     * Gets the client secret.
     *
     * @return The client secret.
     */
    public final String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the client secret.
     *
     * @param value The client secret.
     */
    public final void setClientSecret(String value) {
        clientSecret = value;
    }

    /**
     * The equipment that will connect to the NBS to present these credentials.
     */
    @Expose
    private String equipment;

    /**
     * Gets the equipment that will connect to the NBS to present these
     * credentials.
     *
     * @return The equipment that will connect to the NBS to present these
     * credentials.
     */
    public final String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment that will connect to the NBS to present these
     * credentials.
     *
     * @param value The equipment that will connect to the NBS to present these
     * credentials.
     */
    public final void setEquipment(String value) {
        equipment = value;
    }

    /**
     * The location from which the credentials will be used.
     */
    @Expose
    private String location;

    /**
     * Gets the location from which the credentials will be used.
     *
     * @return The location from which the credentials will be used.
     */
    public final String getLocation() {
        return location;
    }

    /**
     * Sets the location from which the credentials will be used.
     *
     * @param value The location from which the credentials will be used.
     */
    public final void setLocation(String value) {
        location = value;
    }

    /**
     * The organisation that requested creation of the credentials.
     */
    @Expose
    private String organisation;

    /**
     * Gets the organisation that requested creation of the credentials.
     *
     * @return The organisation that requested creation of the credentials.
     */
    public final String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the organisation that requested creation of the credentials.
     *
     * @param value The organisation that requested creation of the credentials.
     */
    public final void setOrganisation(String value) {
        organisation = value;
    }

    /**
     * Initializes a new instance of the ClientCredentials class.
     */
    public final void initialize() {
        initialize(0);
    }

    /**
     * Initializes a new instance of the ClientCredentials class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public final void initialize(long currentTimeStamp) {
        synchronized (TIMESTAMP_LOCK) {
            if (latestTimeStamp == Long.MAX_VALUE) {
                latestTimeStamp = 0;
            }

            if (currentTimeStamp > latestTimeStamp) {
                latestTimeStamp = currentTimeStamp;
            }

            latestTimeStamp++;
            this.timeStamp = latestTimeStamp;
        }
    }
}
