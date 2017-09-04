/**
 * -----------------------------------------------------------------------------
 * File=IConfiguration.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents configuration data for the API client.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.configuration;

import com.reply.solidsoft.nbs.integration.recovery.model.StoreAndForwardMode;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;

/**
 * Represents configuration data for the API client.
 */
public interface NbsIntegrationConfiguration {

    /**
     * Gets a connection identifier.
     *
     * @return A connection identifier.
     */
    public ConnectionIdentifier getConnectionIdentifier();

    /**
     * Sets a connection identifier.
     *
     * @param value A connection identifier.
     */
    public void setConnectionIdentifier(ConnectionIdentifier value);

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    public String getBaseUrl();

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    public void setBaseUrl(String value);

    /**
     * Gets the URL for the identity server system.
     *
     * @return The URL for the identity server system.
     */
    public String getIdentityServerUrl();

    /**
     * Sets the URL for the identity server system.
     *
     * @param value The URL for the identity server system.
     */
    public void setIdentityServerUrl(String value);

    /**
     * Gets the current store and forward mode.
     *
     * @return The current store and forward mode.
     */
    public StoreAndForwardMode getStoreAndForwardMode();

    /**
     * Sets the current store and forward mode.
     *
     * @param value The current store and forward mode.
     */
    public void setStoreAndForwardMode(StoreAndForwardMode value);

    /**
     * Gets the recovery poll interval in milliseconds.
     *
     * @return The recovery poll interval in milliseconds.
     */
    public int getRecoveryPollInterval();

    /**
     * Sets the recovery poll interval in milliseconds.
     *
     * @param value The recovery poll interval in milliseconds.
     */
    public void setRecoveryPollInterval(int value);

    /**
     * Gets the number of retries to attempt on failure.
     *
     * @return The number of retries to attempt on failure.
     */
    public int getRetryCount();

    /**
     * Sets the number of retries to attempt on failure.
     *
     * @param value The number of retries to attempt on failure.
     */
    public void setRetryCount(int value);

    /**
     * Gets the retry interval.
     *
     * @return The retry interval.
     */
    public int getRetryInterval();

    /**
     * Sets the retry interval.
     *
     * @param value The retry interval.
     */
    public void setRetryInterval(int value);

    /**
     * Gets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     *
     * @return The maximum number of packs allowed in a bulk request.
     */
    public int getMaxBulkPackCount();

    /**
     * Sets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     *
     * @param value The maximum number of packs allowed in a bulk request.
     */
    public void setMaxBulkPackCount(int value);

    /**
     * Gets a value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
     *
     *
     * By default, bulk requests are not stored when on-line for future
     * forwarding to the the National System. If this value is set to true, when
     * offline, the integration library will convert each pack in the bulk
     * request to single pack request. The results will be contained in the
     * recovery results after forwarding once the library return to on-line
     * mode.
     *
     * @return A value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
     */
    public boolean getStoreAndForwardBulkRequests();

    /**
     * Sets a value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
     *
     *
     * By default, bulk requests are not stored when on-line for future
     * forwarding to the the National System. If this value is set to true, when
     * offline, the integration library will convert each pack in the bulk
     * request to single pack request. The results will be contained in the
     * recovery results after forwarding once the library return to on-line
     * mode.
     *
     * @param value A value indicating whether bulk requests should be stored
     * and forwarded as single pack requests when the client is off-line.
     */
    public void setStoreAndForwardBulkRequests(boolean value);

    /**
     * Gets a value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     *
     * @return a value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     */
    public boolean getDetectRepeatedSinglePackRequests();

    /**
     * Sets a value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     *
     * @param value a value indicating whether the integration library will
     * detect repeated decommission or reactivation requests for single packs
     * over a configured period and prevent passing those requests to the
     * National System.
     */
    public void setDetectRepeatedSinglePackRequests(boolean value);

    /**
     * Gets the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     *
     * @return the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     */
    public int getRepeatedSinglePackRequestsWindowInSeconds();

    /**
     * Sets the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     *
     * @param value the number of seconds that the integration library will
     * retain a knowledge of recent requests when the 'Detect Repeated Single
     * Pack Requests' option is being used.
     */
    public void setRepeatedSinglePackRequestsWindowInSeconds(int value);
}
