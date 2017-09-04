/**
 * -----------------------------------------------------------------------------
 * File=Configuration.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Configuration data for the API client.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.configuration;

import com.reply.solidsoft.nbs.integration.recovery.model.StoreAndForwardMode;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;

/**
 * Configuration data for the API client.
 */
public class Configuration implements NbsIntegrationConfiguration {

    /**
     * The base URL for the national system.
     */
    private String baseUrl;

    /**
     * A connection identifier.
     */
    private ConnectionIdentifier connectionIdentifier;

    /**
     * Gets a connection identifier.
     *
     * @return A connection identifier.
     */
    @Override
    public ConnectionIdentifier getConnectionIdentifier() {
        return connectionIdentifier;
    }

    /**
     * Sets a connection identifier.
     *
     * @param value A connection identifier.
     */
    @Override
    public void setConnectionIdentifier(ConnectionIdentifier value) {
        connectionIdentifier = value;
    }

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    @Override
    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    @Override
    public void setBaseUrl(String value) {
        this.baseUrl = value.endsWith("/") ? value : value + "/";
    }

    /**
     * The URL for the identity server system.
     */
    private String identityServerUrl;

    /**
     * Gets the URL for the identity server system.
     *
     * @return The URL for the identity server system.
     */
    @Override
    public String getIdentityServerUrl() {
        return identityServerUrl;
    }

    /**
     * Sets the URL for the identity server system.
     *
     * @param value The URL for the identity server system.
     */
    @Override
    public void setIdentityServerUrl(String value) {
        identityServerUrl = value;
    }

    /**
     * The current store and forward mode.
     */
    private StoreAndForwardMode storeAndForwardMode = StoreAndForwardMode.AUTOMATIC;

    /**
     * Gets the current store and forward mode.
     *
     * @return The current store and forward mode.
     */
    @Override
    public StoreAndForwardMode getStoreAndForwardMode() {
        return storeAndForwardMode;
    }

    /**
     * Sets the current store and forward mode.
     *
     * @param value The current store and forward mode.
     */
    @Override
    public void setStoreAndForwardMode(StoreAndForwardMode value) {
        storeAndForwardMode = value;
    }

    /**
     * The recovery poll interval in milliseconds.
     */
    private int recoveryPollInterval = 300000;

    /**
     * Gets the recovery poll interval in milliseconds.
     *
     * @return The recovery poll interval in milliseconds.
     */
    @Override
    public int getRecoveryPollInterval() {
        return recoveryPollInterval;
    }

    /**
     * Sets the recovery poll interval in milliseconds.
     *
     * @param value The recovery poll interval in milliseconds.
     */
    @Override
    public void setRecoveryPollInterval(int value) {
        recoveryPollInterval = value;
    }

    /**
     * The number of retries to attempt on failure.
     */
    private int retryCount = 0;

    /**
     * Gets the number of retries to attempt on failure.
     *
     * @return The number of retries to attempt on failure.
     */
    @Override
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Sets the number of retries to attempt on failure.
     *
     * @param value The number of retries to attempt on failure.
     */
    @Override
    public void setRetryCount(int value) {
        retryCount = value;
    }

    /**
     * The retry interval.
     */
    private int retryInterval = 10000;

    /**
     * Gets the retry interval.
     *
     * @return The retry interval.
     */
    @Override
    public int getRetryInterval() {
        return retryInterval;
    }

    /**
     * Sets the retry interval.
     *
     * @param value The retry interval.
     */
    @Override
    public void setRetryInterval(int value) {
        retryInterval = value;
    }

    /**
     * The maximum number of packs allowed in a bulk request.
     */
    private int maxBulkPackCount = 500000;

    /**
     * Gets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     *
     * If set the 0, the API will use the default value.
     *
     * @return value The maximum number of packs allowed in a bulk request.
     */
    @Override
    public int getMaxBulkPackCount() {
        return maxBulkPackCount;
    }

    /**
     * Sets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     * <p>
     * If set the 0, the API will use the default value.
     *
     * @param value The maximum number of packs allowed in a bulk request.
     */
    @Override
    public void setMaxBulkPackCount(int value) {
        maxBulkPackCount = value;
    }

    /**
     * A value indicating whether bulk requests should be stored and forwarded
     * as single pack requests when the client is off-line.
     */
    private boolean storeAndForwardBulkRequests;

    /**
     * Gets a value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
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
    @Override
    public final boolean getStoreAndForwardBulkRequests() {
        return storeAndForwardBulkRequests;
    }

    /**
     * Sets a value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
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
    @Override
    public final void setStoreAndForwardBulkRequests(boolean value) {
        storeAndForwardBulkRequests = value;
    }

    /**
     * A value indicating whether the integration library will detect repeated
     * decommission or reactivation requests for single packs over a configured
     * period and prevent passing those requests to the National System.
     */
    private boolean detectRepeatedSinglePackRequests;

    /**
     * Gets a value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     *
     * @return A value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     */
    @Override
    public final boolean getDetectRepeatedSinglePackRequests() {
        return detectRepeatedSinglePackRequests;
    }

    /**
     * Sets a value indicating whether the integration library will detect
     * repeated decommission or reactivation requests for single packs over a
     * configured period and prevent passing those requests to the National
     * System.
     *
     * @param value A value indicating whether the integration library will
     * detect repeated decommission or reactivation requests for single packs
     * over a configured period and prevent passing those requests to the
     * National System.
     */
    @Override
    public final void setDetectRepeatedSinglePackRequests(boolean value) {
        detectRepeatedSinglePackRequests = value;
    }

    /**
     * The number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     */
    private int repeatedSinglePackRequestsWindowInSeconds;

    /**
     * Gets the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     *
     * @return The number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     */
    @Override
    public final int getRepeatedSinglePackRequestsWindowInSeconds() {
        return repeatedSinglePackRequestsWindowInSeconds;
    }

    /**
     * Sets the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     *
     * @param value The number of seconds that the integration library will
     * retain a knowledge of recent requests when the 'Detect Repeated Single
     * Pack Requests' option is being used.
     */
    @Override
    public final void setRepeatedSinglePackRequestsWindowInSeconds(int value) {
        repeatedSinglePackRequestsWindowInSeconds = value;
    }
}
