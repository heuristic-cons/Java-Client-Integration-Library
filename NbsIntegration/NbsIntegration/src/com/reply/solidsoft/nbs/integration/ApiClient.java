/**
 * -----------------------------------------------------------------------------
 * File=ApiClient.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A client for the NBS API.  The client uses a specific set of client
 * credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration;

import com.google.gson.Gson;
import com.reply.solidsoft.nbs.integration.connection.ApiConnection;
import com.reply.solidsoft.nbs.integration.connection.NbsHttpClient;
import com.reply.solidsoft.nbs.integration.connection.TokenExpiredEventArgs;
import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.extensions.MemoryCache;
import com.reply.solidsoft.nbs.integration.extensions.RequestContext;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryCategory;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryPackIdentifier;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryRequest;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryResponse;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryType;
import com.reply.solidsoft.nbs.integration.logging.InMemoryLoggingService;
import com.reply.solidsoft.nbs.integration.model.DataEntryMode;
import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.model.HttpVerb;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.PackStateTransitionCommand;
import com.reply.solidsoft.nbs.integration.model.RecentRequestKey;
import com.reply.solidsoft.nbs.integration.model.ReportedPackState;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import com.reply.solidsoft.nbs.integration.model.extensions.BulkRequestVerification;
import com.reply.solidsoft.nbs.integration.model.extensions.PackIdentifierVerification;
import com.reply.solidsoft.nbs.integration.model.extensions.RecoveryRequestVerification;
import com.reply.solidsoft.nbs.integration.model.requests.BulkRequest;
import com.reply.solidsoft.nbs.integration.model.requests.RecoveryRequest;
import com.reply.solidsoft.nbs.integration.model.requests.RequestType;
import com.reply.solidsoft.nbs.integration.model.responses.ApiResult;
import com.reply.solidsoft.nbs.integration.model.responses.BulkRequestAck;
import com.reply.solidsoft.nbs.integration.model.responses.BulkRequestResults;
import com.reply.solidsoft.nbs.integration.model.responses.LocalValidationResponse;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestAck;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestResults;
import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;
import com.reply.solidsoft.nbs.integration.model.responses.SinglePackResult;
import com.reply.solidsoft.nbs.integration.offline.OffLineEventArgs;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import com.reply.solidsoft.nbs.integration.recovery.inmemory.InMemoryStoreAndForwardService;
import com.reply.solidsoft.nbs.integration.recovery.RecoveryHandler;
import com.reply.solidsoft.nbs.integration.recovery.RecoveryHandlerContext;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import com.reply.solidsoft.nbs.integration.recovery.model.StoreAndForwardEventArgs;
import com.reply.solidsoft.nbs.integration.recovery.model.StoreAndForwardMode;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import com.reply.solidsoft.nbs.integration.clientcredentials.ClientCredentialsService;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.configuration.NbsIntegrationConfiguration;
import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;
import com.reply.solidsoft.nbs.integration.data.model.TypedRecords;
import com.reply.solidsoft.nbs.integration.logging.LoggingService;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntry;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntryRequest;
import com.reply.solidsoft.nbs.integration.recovery.StoreAndForwardService;

/**
 * A client for the NBS API. The client uses a specific set of client
 * credentials.
 */
public class ApiClient implements java.io.Closeable {

    /**
     * The name of the custom EMVS Data Entry Mode HTTP header.
     */
    private static final String EMVS_DATA_ENTRY_MODE = "emvs-data-entry-mode";

    /**
     * The name of the custom EMVS Data Entry Mode HTTP header.
     */
    private static final String ACCEPT_LANGUAGE = "accept-language";

    /**
     * The context for store &amp; forward-based recovery.
     */
    private RecoveryHandlerContext recoveryHandlerContext;

    /**
     * Lock for NBS HTTP client access.
     */
    private final Object clientLock = new Object();

    /**
     * The logging service.
     */
    private LoggingService loggingService;

    /**
     * An in-memory cache used to detect repeated requests.
     */
    private MemoryCache<RecentRequestKey, SinglePackResult> repeatedRequestCache;

    /**
     * The log table in the logging service.
     */
    private TypedDataTable<LogEntry> logTable;

    /**
     * The deferred request records.
     */
    private TypedRecords<DeferredRequest> deferredRequests;

    /**
     * The recovery single pack responses.
     */
    private TypedRecords<RecoverySinglePackResponse> recoverySinglePackResponse;

    /**
     * The current store and forward mode.
     */
    private StoreAndForwardMode storeAndForwardMode = StoreAndForwardMode.AUTOMATIC;

    /**
     * The NBS HTTP client.
     */
    private NbsHttpClient client;

    /**
     * The request table in the store and forward data management service.
     */
    private TypedDataTable<DeferredRequest> requestTable;

    /**
     * A SAM for HTTP verbs.
     */
    @FunctionalInterface
    public interface Verb {

        /**
         * Perform an HTTP request for the verb.
         *
         * @param uri The URI of the resource.
         * @param content The JSON content of the request body.
         * @param headers A list of HTTP headers.
         * @return An HTTP response.
         * @throws java.io.IOException Signals that an I/O exception of some
         * sort has occurred. This class is the general class of exceptions
         * produced by failed or interrupted I/O operations.
         */
        public HttpResponse doRequest(
                String uri,
                StringEntity content,
                List<Header> headers) throws IOException;
    }

    /**
     * Initializes a new instance of the ApiClient class.
     *
     * @param clientCredentialsService The client credentials service.
     * @param configuration Configuration data for the API client.
     * @param storeAndForwardService The service And forward data management
     * service.
     * @param loggingService A logging service.
     */
    public ApiClient(ClientCredentialsService clientCredentialsService, NbsIntegrationConfiguration configuration, StoreAndForwardService storeAndForwardService, LoggingService loggingService) {
        this(clientCredentialsService, configuration, storeAndForwardService, loggingService, true);
    }

    /**
     * Initializes a new instance of the ApiClient class.
     *
     * @param clientCredentialsService The client credentials service.
     * @param configuration Configuration data for the API client.
     * @param storeAndForwardService The service And forward data management
     * service.
     */
    public ApiClient(ClientCredentialsService clientCredentialsService, NbsIntegrationConfiguration configuration, StoreAndForwardService storeAndForwardService) {
        this(clientCredentialsService, configuration, storeAndForwardService, null, true);
    }

    /**
     * Initializes a new instance of the ApiClient class.
     *
     * @param clientCredentialsService The client credentials service.
     * @param configuration Configuration data for the API client.
     */
    public ApiClient(ClientCredentialsService clientCredentialsService, NbsIntegrationConfiguration configuration) {
        this(clientCredentialsService, configuration, null, null, true);
    }

    /**
     * Initializes a new instance of the ApiClient class.
     *
     * @param clientCredentialsService The client credentials service.
     */
    public ApiClient(ClientCredentialsService clientCredentialsService) {
        this(clientCredentialsService, null, null, null, true);
    }

    /**
     * Initializes a new instance of the ApiClient class.
     *
     * @param clientCredentialsService The client credentials service.
     * @param configuration Configuration data for the API client.
     * @param storeAndForwardService The service And forward data management
     * service.
     * @param loggingService A logging service.
     * @param start Indicates whether the API client is started automatically.
     */
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public ApiClient(ClientCredentialsService clientCredentialsService, NbsIntegrationConfiguration configuration, StoreAndForwardService storeAndForwardService, LoggingService loggingService, boolean start) {
        if (configuration != null) {
            this.storeAndForwardMode = configuration.getStoreAndForwardMode();
            this.setConnectionIdentifier(configuration.getConnectionIdentifier());
            this.setBaseUrl(configuration.getBaseUrl());
            this.setIdentityServerUrl(configuration.getIdentityServerUrl());
            this.setRetryCount(configuration.getRetryCount());
            this.setRetryInterval(configuration.getRetryInterval());
            this.setMaxBulkPackCount(configuration.getMaxBulkPackCount());
            this.setStoreAndForwardBulkRequests(configuration.getStoreAndForwardBulkRequests());
            this.setDetectRepeatedSinglePackRequests(configuration.getDetectRepeatedSinglePackRequests());
            this.setRepeatedSinglePackRequestsWindowInSeconds(configuration.getRepeatedSinglePackRequestsWindowInSeconds());
        }

        this.clientCredentialsService = clientCredentialsService;

        StoreAndForwardService contextStoreAndForwardService = null;

        if (storeAndForwardService == null) {
            if (this.storeAndForwardMode != StoreAndForwardMode.NONE && this.storeAndForwardMode != null) {
                contextStoreAndForwardService = new InMemoryStoreAndForwardService();
            }
        } else {
            contextStoreAndForwardService = storeAndForwardService;
        }

        this.requestTable = (TypedDataTable<DeferredRequest>) (contextStoreAndForwardService == null
                ? null
                : contextStoreAndForwardService.getRequests());
        this.deferredRequests = (TypedRecords<DeferredRequest>) (contextStoreAndForwardService == null
                ? null
                : contextStoreAndForwardService.getTables().get("requests"));
        this.recoverySinglePackResponse
                = (TypedRecords<RecoverySinglePackResponse>) (contextStoreAndForwardService == null
                        ? null
                        : contextStoreAndForwardService.getTables().get("responses"));

        this.loggingService = loggingService;
        this.setIsLogging(true);

        if (loggingService == null) {
            this.loggingService = new InMemoryLoggingService();
            this.setIsLogging(false);
        }

        this.logTable = (TypedDataTable<LogEntry>) (this.loggingService == null ? null : this.loggingService.getTables().get("log"));

        this.repeatedRequestCache = new MemoryCache(
                this.getRepeatedSinglePackRequestsWindowInSeconds(),
                600,
                100);

        if (start) {
            this.start();
        }

        int recoveryPollInterval = configuration == null || configuration.getRecoveryPollInterval() <= 0
                ? 30000
                : configuration.getRecoveryPollInterval();

        // Set up the recovery thread context
        this.recoveryHandlerContext = new RecoveryHandlerContext(
                contextStoreAndForwardService,
                recoveryPollInterval,
                () -> this.requestTable == null ? null : this.requestTable.getRecords(),
                this::submitRecoveryRequest,
                this::getRecoveryResult,
                this::logOnError,
                this.updateDeferredRequestStatistics,
                this.getMaxBulkPackCount(),
                this.getLanguage());

        // Start the recovery thread
        RecoveryHandler recoveryHandler = new RecoveryHandler(this.recoveryHandlerContext);
        recoveryHandler.start();
    }

    /**
     * The listener for 'Deferring Request' events.
     * <p>
     * This event is raised when the library defers a request to the National
     * System while in off-line mode. If the library is in automatic Store &amp;
     * Forward mode, the deferred requests will be stored using the current
     * Store &amp; Forward service and forwarded to the National System at a
     * later time when the integration library returns to on-line mode. If the
     * library is in manual Store &amp; Forward mode, it is the responsibility
     * of the event handler to manage storage and forwarding of the deferred
     * request.
     */
    public com.reply.solidsoft.nbs.integration.extensions.events.Event<DeferringRequestListener> deferringRequest = new com.reply.solidsoft.nbs.integration.extensions.events.Event<>();

    /**
     * The store &amp; forward statistics listener.
     */
    public com.reply.solidsoft.nbs.integration.extensions.events.Event<UpdateDeferredRequestStatisticsListener> updateDeferredRequestStatistics = new com.reply.solidsoft.nbs.integration.extensions.events.Event<>();

    /**
     * The off-line notification listener.
     */
    public com.reply.solidsoft.nbs.integration.extensions.events.Event<OffLineModeChangedListener> offLineModeChanged = new com.reply.solidsoft.nbs.integration.extensions.events.Event<>();

    /**
     * The client credentials service for this client.
     */
    private ClientCredentialsService clientCredentialsService;

    /**
     * Gets the client credentials service for this client.
     *
     * @return The client credentials service for this client.
     */
    public final ClientCredentialsService getClientCredentialsService() {
        return clientCredentialsService;
    }

    /**
     * A connection identifier.
     */
    private ConnectionIdentifier connectionIdentifier;

    /**
     * Gets a connection identifier.
     *
     * @return A connection identifier.
     */
    public final ConnectionIdentifier getConnectionIdentifier() {
        return connectionIdentifier;
    }

    /**
     * Sets a connection identifier.
     *
     * @param value A connection identifier.
     */
    public final void setConnectionIdentifier(ConnectionIdentifier value) {
        connectionIdentifier = value;
    }

    /**
     * A value indicating whether the integration library is offline.
     */
    private boolean isOffline;

    /**
     * Gets a value indicating whether the integration library is offline.
     *
     * @return A value indicating whether the integration library is offline.
     */
    public final boolean getIsOffline() {
        return isOffline;
    }

    /**
     * Sets a value indicating whether the integration library is offline.
     *
     * @param value A value indicating whether the integration library is
     * offline.
     */
    private void setIsOffline(boolean value) {
        isOffline = value;
    }

    /**
     * The base URL for the national system.
     */
    private String baseUrl;

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    public final String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    public final void setBaseUrl(String value) {
        baseUrl = value;
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
    public final String getIdentityServerUrl() {
        return identityServerUrl;
    }

    /**
     * Sets the URL for the identity server system.
     *
     * @param value The URL for the identity server system.
     */
    public final void setIdentityServerUrl(String value) {
        identityServerUrl = value;
    }

    /**
     * The number of retries to attempt on failure.
     */
    private int retryCount;

    /**
     * Gets the number of retries to attempt on failure.
     *
     * @return The number of retries to attempt on failure.
     */
    public final int getRetryCount() {
        return retryCount;
    }

    /**
     * Sets the number of retries to attempt on failure.
     *
     * @param value The number of retries to attempt on failure.
     */
    public final void setRetryCount(int value) {
        retryCount = value;
    }

    /**
     * The retry interval.
     */
    private int retryInterval;

    /**
     * Gets the retry interval.
     *
     * @return The retry interval.
     */
    public final int getRetryInterval() {
        return retryInterval;
    }

    /**
     * Sets the retry interval.
     *
     * @param value The retry interval.
     */
    public final void setRetryInterval(int value) {
        retryInterval = value;
    }

    /**
     * The maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     * <p>
     * If set the 0, the API will use the default value.
     */
    private int maxBulkPackCount = 500000;

    /**
     * Gets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     * <p>
     * If set the 0, the API will use the default value.
     *
     * @return The maximum number of packs allowed in a bulk request.
     */
    public final int getMaxBulkPackCount() {
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
    public final void setMaxBulkPackCount(int value) {
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
     * <p>
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
    public final boolean getStoreAndForwardBulkRequests() {
        return storeAndForwardBulkRequests;
    }

    /**
     * Sets a value indicating whether bulk requests should be stored and
     * forwarded as single pack requests when the client is off-line.
     * <p>
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
    public final void setDetectRepeatedSinglePackRequests(boolean value) {
        detectRepeatedSinglePackRequests = value;
    }

    /**
     * The number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     */
    private int repeatedSinglePackRequestsWindowInSeconds = 60;

    /**
     * Gets the number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     *
     * @return The number of seconds that the integration library will retain a
     * knowledge of recent requests when the 'Detect Repeated Single Pack
     * Requests' option is being used.
     */
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
    public final void setRepeatedSinglePackRequestsWindowInSeconds(int value) {
        repeatedSinglePackRequestsWindowInSeconds = value;
    }

    /**
     * The data entry mode.
     */
    private DataEntryMode dataEntryMode = DataEntryMode.NON_MANUAL;

    /**
     * Gets the data entry mode.
     *
     * @return The data entry mode.
     */
    public final DataEntryMode getDataEntryMode() {
        return dataEntryMode;
    }

    /**
     * Sets the data entry mode.
     *
     * @param value The data entry mode.
     */
    public final void setDataEntryMode(DataEntryMode value) {
        dataEntryMode = value;
    }

    /**
     * A value indicating whether the client is started.
     */
    private boolean isStarted;

    /**
     * Gets a value indicating whether the client is started.
     *
     * @return A value indicating whether the client is started.
     */
    public final boolean getIsStarted() {
        return isStarted;
    }

    /**
     * Sets a value indicating whether the client is started.
     *
     * @param value A value indicating whether the client is started.
     */
    private void setIsStarted(boolean value) {
        isStarted = value;
    }

    /**
     * The language required for the response messages.
     */
    private String language = "";

    /**
     * Gets the language required for the response messages.
     *
     * @return The language required for the response messages.
     */
    public final String getLanguage() {
        return language;
    }

    /**
     * Sets the language required for the response messages.
     *
     * @param value The language required for the response messages.
     */
    public final void setLanguage(String value) {
        language = value;
    }

    /**
     * A value indicating whether the log data is provided to a logging service.
     */
    private boolean isLogging;

    /**
     * Gets a value indicating whether the log data is provided to a logging
     * service.
     *
     * @return A value indicating whether the log data is provided to a logging
     * service.
     */
    public final boolean getIsLogging() {
        return isLogging;
    }

    /**
     * Sets a value indicating whether the log data is provided to a logging
     * service.
     *
     * @param value A value indicating whether the log data is provided to a
     * logging service.
     */
    public final void setIsLogging(boolean value) {
        isLogging = value;
    }

    /**
     * Gets the store and forward data management service.
     *
     * @return The store and forward data management service.
     */
    public final StoreAndForwardService getStoreAndForwardDataManagementService() {
        return this.recoveryHandlerContext.getStoreAndForwardService();
    }

    /**
     * Sets the store and forward data management service.
     *
     * @param value The store and forward data management service.
     */
    public final void setStoreAndForwardDataManagementService(StoreAndForwardService value) {
        this.recoveryHandlerContext.setStoreAndForwardService(value);
        this.requestTable = (TypedDataTable<DeferredRequest>) (this.recoveryHandlerContext.getStoreAndForwardService() == null
                ? null
                : this.recoveryHandlerContext.getStoreAndForwardService().getRequests());
        this.deferredRequests = (TypedRecords<DeferredRequest>) (this.recoveryHandlerContext.getStoreAndForwardService() == null
                ? null
                : this.recoveryHandlerContext.getStoreAndForwardService().getTables().get("requests"));
        this.recoverySinglePackResponse
                = (TypedRecords<RecoverySinglePackResponse>) (this.recoveryHandlerContext.getStoreAndForwardService() == null
                ? null
                : this.recoveryHandlerContext.getStoreAndForwardService().getTables().get("responses"));
    }

    /**
     * Gets the store and forward mode.
     *
     * @return The store and forward mode.
     */
    public final StoreAndForwardMode getStoreAndForwardMode() {
        return this.storeAndForwardMode;
    }

    /**
     * Sets the store and forward mode.
     *
     * @param value The store and forward mode.
     */
    public final void setStoreAndForwardMode(StoreAndForwardMode value) {
        if (this.recoveryHandlerContext.getStoreAndForwardService() == null) {
            if (value != StoreAndForwardMode.NONE && value != null) {
                this.recoveryHandlerContext.setStoreAndForwardService(new InMemoryStoreAndForwardService());
            }
        }

        this.storeAndForwardMode = (value != null) ? value : StoreAndForwardMode.NONE;
    }

    /**
     * Gets the collection of currently deferred requests that have not been
     * acknowledged by the National System.
     *
     * @return The collection of currently deferred requests that have not been
     * acknowledged by the National System.
     */
    public final List<DeferredRequest> getDeferredRequests() {
        return this.requestTable.getRecords()
                .stream()
                .filter(deferredRequest -> !deferredRequest.getAcknowledged())
                .collect(Collectors.toList());
    }

    /**
     * Gets the collection of results for previously deferred requests which
     * were subsequently forwarded to the national System.
     *
     * @return the collection of results for previously deferred requests which
     * were subsequently forwarded to the national System.
     */
    public final List<RecoverySinglePackResponse> getStoredAndForwardResults() {
        return this.recoverySinglePackResponse.getRecords()
                .stream()
                .sorted((RecoverySinglePackResponse r1, RecoverySinglePackResponse r2) -> {
                    if (r1.getTimeStamp() == r2.getTimeStamp()) {
                        return 0;
                    } else if (r1.getTimeStamp() < r2.getTimeStamp()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }).collect(Collectors.toList());
    }

    /**
     * Gets the collection of log entries.
     *
     * @return The collection of log entries.
     */
    public final List<LogEntry> getLogEntries() {
        return this.loggingService == null ? null : this.loggingService.getLogEntries();
    }

    /**
     * Defer the current request using the configured data service.
     *
     * @param deferredRequest The deferred request.
     *
     * This method only has any effect if the Store and Forward mode is manual,
     * a deferred record data management service has been configured and a
     * deferred request is provided.
     */
    public final void deferRequest(DeferredRequest deferredRequest) {
        if (this.getStoreAndForwardMode() != StoreAndForwardMode.MANUAL || this.requestTable == null || deferredRequest == null) {
            return;
        }

        this.doDeferRequest(deferredRequest);
    }

    /**
     * Verify a pack of medicine.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @return A request result from the national system.
     */
    public final SinglePackResult verify(PackIdentifier pack, Boolean isManual) {
        return verify(pack, isManual, null);
    }

    /**
     * Verify a pack of medicine.
     *
     * @param pack The pack.
     * @return A request result from the national system.
     */
    public final SinglePackResult verify(PackIdentifier pack) {
        return verify(pack, null, null);
    }

    /**
     * VERIFY a pack of medicine.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final SinglePackResult verify(PackIdentifier pack, Boolean isManual, String language) {
        LogEntry logEntry = this.getSinglePackRequestLogEntry(pack);

        try {
            return this.doRequest(logEntry, HttpVerb.GET, pack, null, null, isManual, language, RequestType.VERIFY).singlePackResult();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Decommission the unique identifier of a pack of medicine and mark it as
     * supplied.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @return A request result from the national system.
     */
    public final SinglePackResult supply(PackIdentifier pack, Boolean isManual) {
        return supply(pack, isManual, null);
    }

    /**
     * Decommission the unique identifier of a pack of medicine and mark it as
     * supplied.
     *
     * @param pack The pack.
     * @return A request result from the national system.
     */
    public final SinglePackResult supply(PackIdentifier pack) {
        return supply(pack, null, null);
    }

    /**
     * Decommission the unique identifier of a pack of medicine and mark it as
     * supplied.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final SinglePackResult supply(PackIdentifier pack, Boolean isManual, String language) {
        LogEntry logEntry = this.getSinglePackRequestLogEntry(pack);

        try {
            Gson gson = new Gson();
            return this.doRequest(
                    logEntry,
                    HttpVerb.PATCH,
                    pack,
                    gson.toJson(new PackStateTransitionCommand(RequestedPackState.SUPPLIED)),
                    RequestedPackState.SUPPLIED,
                    isManual,
                    language,
                    RequestType.SUPPLY).singlePackResult();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Decommission the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param command The requested pack state.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @return A request result from the national system.
     */
    public final SinglePackResult decommission(PackIdentifier pack, RequestedPackState command, Boolean isManual) {
        return decommission(pack, command, isManual, null);
    }

    /**
     * Decommission the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param command The requested pack state.
     * @return A request result from the national system.
     */
    public final SinglePackResult decommission(PackIdentifier pack, RequestedPackState command) {
        return decommission(pack, command, null, null);
    }

    /**
     * Decommission the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param command The requested pack state.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @return A request result from the national system.
     */
    public final SinglePackResult decommission(PackIdentifier pack, String command, Boolean isManual) {
        for (RequestedPackState value : RequestedPackState.values()) {
            if (command == null ? value.getValue() == null : command.equals(value.getValue())) {
                return decommission(pack, value, isManual, null);
            }
        }

        return decommission(pack, null, isManual, null);
    }

    /**
     * Decommission the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param command The requested pack state.
     * @return A request result from the national system.
     */
    public final SinglePackResult decommission(PackIdentifier pack, String command) {
        for (RequestedPackState value : RequestedPackState.values()) {
            if (command == null ? value.getValue() == null : command.equals(value.getValue())) {
                return decommission(pack, value, null, null);
            }
        }

        for (RequestedPackState value : RequestedPackState.values()) {
            if (command == null ? value.getValue() == null : command.equals(value.getDisplayName())) {
                return decommission(pack, value, null, null);
            }
        }

        RequestedPackState nullCommand = null;
        return decommission(pack, nullCommand, null, null);
    }

    /**
     * Decommission the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param command The requested pack state.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final SinglePackResult decommission(PackIdentifier pack, RequestedPackState command, Boolean isManual, String language) {
        LogEntry logEntry = this.getSinglePackRequestLogEntry(pack);
        RecentRequestKey recentRequestKey = new RecentRequestKey(
                pack,
                command,
                ((isManual != null) ? isManual : false)
                        ? DataEntryMode.MANUAL
                        : DataEntryMode.NON_MANUAL,
                language);

        // Record the request
        if (this.getDetectRepeatedSinglePackRequests()) {
            SinglePackResult result = this.repeatedRequestCache.get(recentRequestKey);

            if (null != result) {
                return result;
            }
        }

        try {
            Gson gson = new Gson();
            SinglePackResult singlePackResult = this.doRequest(
                    logEntry,
                    HttpVerb.PATCH,
                    pack,
                    gson.toJson(new PackStateTransitionCommand(command)),
                    command,
                    isManual,
                    language,
                    RequestType.DECOMMISSION).singlePackResult();

            if (this.getDetectRepeatedSinglePackRequests() && this.getRepeatedSinglePackRequestsWindowInSeconds() > 0) {
                SinglePackResult result = this.repeatedRequestCache.get(recentRequestKey);

                if (null == result) {
                    this.repeatedRequestCache.put(recentRequestKey, singlePackResult);
                }
            }

            return singlePackResult;
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Reactivate the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @return A request result from the national system.
     */
    public final SinglePackResult reactivate(PackIdentifier pack, Boolean isManual) {
        return reactivate(pack, isManual, null);
    }

    /**
     * Reactivate the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @return A request result from the national system.
     */
    public final SinglePackResult reactivate(PackIdentifier pack) {
        return reactivate(pack, null, null);
    }

    /**
     * Reactivate the unique identifier for a pack of medicine.
     *
     * @param pack The pack.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final SinglePackResult reactivate(PackIdentifier pack, Boolean isManual, String language) {
        LogEntry logEntry = this.getSinglePackRequestLogEntry(pack);
        RecentRequestKey recentRequestKey = new RecentRequestKey(
                pack,
                RequestedPackState.ACTIVE,
                ((isManual != null) ? isManual : false)
                        ? DataEntryMode.MANUAL
                        : DataEntryMode.NON_MANUAL,
                language);

        // Record the request
        if (this.getDetectRepeatedSinglePackRequests()) {
            SinglePackResult result = this.repeatedRequestCache.get(recentRequestKey);

            if (null != result) {
                return result;
            }
        }

        try {
            Gson gson = new Gson();
            SinglePackResult singlePackResult = this.doRequest(
                    logEntry,
                    HttpVerb.PATCH,
                    pack,
                    gson.toJson(new PackStateTransitionCommand(RequestedPackState.ACTIVE)),
                    RequestedPackState.ACTIVE,
                    isManual,
                    language,
                    RequestType.REACTIVATE).singlePackResult();

            if (this.getDetectRepeatedSinglePackRequests() && this.getRepeatedSinglePackRequestsWindowInSeconds() > 0) {
                SinglePackResult result = this.repeatedRequestCache.get(recentRequestKey);

                if (null == result) {
                    this.repeatedRequestCache.put(recentRequestKey, singlePackResult);
                }
            }

            return singlePackResult;
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Submit a bulk of pack request to the national system.
     *
     * @param bulkRequest The bulk of pack request.
     * @return A request result from the national system.
     */
    public final BulkRequestAck submitBulkRequest(BulkRequest bulkRequest) {
        return submitBulkRequest(bulkRequest, null);
    }

    /**
     * Submit a bulk of pack request to the national system.
     *
     * @param bulkRequest The bulk of pack request.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final BulkRequestAck submitBulkRequest(BulkRequest bulkRequest, String language) {
        if (this.getMaxBulkPackCount() > 0) {
            bulkRequest.setMaxBulkPackCount(this.getMaxBulkPackCount());
        }

        LogEntry logEntry = this.getBulkPackLogEntry(LogEntryCategory.BULK_PACK_REQUEST);

        // Validate the bulk request
        LocalValidationResponse validationResponse = BulkRequestVerification.validate(bulkRequest, this.getMaxBulkPackCount());

        if (validationResponse.getOperationCode() > 0) {
            // The bulk request has failed validation
            return new BulkRequestAck(validationResponse);
        }

        try {
            Gson gson = new Gson();
            return this.preprocessRequest(
                    logEntry,
                    HttpVerb.POST,
                    "product/packs",
                    gson.toJson(bulkRequest),
                    null,
                    null,
                    language,
                    RequestType.SUBMIT_BULK_REQUEST).bulkRequestAck();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Records the result of a previous bulk of pack request.
     *
     * @param resultsUri The location of the results resource.
     * @return A request result from the national system.
     * @throws java.net.MalformedURLException Thrown to indicate that a
     * malformed URL has occurred. Either no legal protocol could be found in a
     * specification string or the string could not be parsed.
     */
    public final BulkRequestResults getBulkResult(URI resultsUri) throws MalformedURLException {
        return getBulkResult(resultsUri, null);
    }

    /**
     * Records the result of a previous bulk of pack request.
     *
     * @param resultsUri The location of the results resource.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     * @throws java.net.MalformedURLException Thrown to indicate that a
     * malformed URL has occurred. Either no legal protocol could be found in a
     * specification string or the string could not be parsed.
     */
    public final BulkRequestResults getBulkResult(URI resultsUri, String language) throws MalformedURLException {
        LogEntry logEntry = this.getBulkResultsLogEntry(LogEntryCategory.BULK_PACK_RESULTS_REQUEST);

        try {
            return this.preprocessRequest(
                    logEntry,
                    HttpVerb.GET,
                    resultsUri.toURL().toString(),
                    null,
                    null,
                    null,
                    language,
                    RequestType.GET_BULK_RESULT).bulkRequestResults();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Submit a bulk of pack request to the national system.
     *
     * @param recoveryRequest The recovery request.
     * @return A request result from the national system.
     */
    public final RecoveryRequestAck submitRecoveryRequest(RecoveryRequest recoveryRequest) {
        return submitRecoveryRequest(recoveryRequest, null);
    }

    /**
     * Submit a bulk of pack request to the national system.
     *
     * @param recoveryRequest The recovery request.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final RecoveryRequestAck submitRecoveryRequest(RecoveryRequest recoveryRequest, String language) {
        LogEntry logEntry = this.getRecoveryLogEntry(LogEntryCategory.RECOVERY_REQUEST);

        // Validate the bulk request
        LocalValidationResponse validationResponse
                = RecoveryRequestVerification.validate(
                        recoveryRequest,
                        this.getMaxBulkPackCount());

        if (validationResponse.getOperationCode() > 0) {
            // The bulk request has failed validation
            return new RecoveryRequestAck(validationResponse);
        }

        try {
            Gson gson = new Gson();
            return this.preprocessRequest(
                    logEntry,
                    HttpVerb.POST,
                    "recovery/packs",
                    gson.toJson(recoveryRequest),
                    null,
                    null,
                    language,
                    RequestType.SUBMIT_RECOVERY_REQUEST).recoveryRequestAck();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Records the result of a previous recovery request.
     *
     * @param resultsUri The URI of the results resource.
     * @return A request result from the national system.
     */
    public final RecoveryRequestResults getRecoveryResult(URI resultsUri) {
        return getRecoveryResult(resultsUri, null);
    }

    /**
     * Records the result of a previous recovery request.
     *
     * @param resultsUri The URI of the results resource.
     * @param language Selects a specific language for response messages.
     * @return A request result from the national system.
     */
    public final RecoveryRequestResults getRecoveryResult(URI resultsUri, String language) {
        LogEntry logEntry = this.getBulkResultsLogEntry(LogEntryCategory.RECOVERY_RESULTS_REQUEST);
        String url;

        try {
            url = resultsUri.toURL().toString();
        } catch (MalformedURLException ex) {
            url = "";
        }

        try {
            return this.preprocessRequest(
                    logEntry,
                    HttpVerb.GET,
                    url,
                    null,
                    null,
                    null,
                    language,
                    RequestType.GET_RECOVERY_RESULT).recoveryRequestResults();
        } finally {
            this.doLogging(logEntry);
        }
    }

    /**
     * Starts the API client.
     */
    public final void start() {
        if (this.getIsStarted()) {
            return;
        }

        synchronized (this.clientLock) {
            this.client = new NbsHttpClient(
                    new ApiConnection(
                            this.getBaseUrl(),
                            this.getIdentityServerUrl()),
                    this.loggingService,
                    this.getClientCredentialsService(),
                    this.getConnectionIdentifier(),
                    this::tokenExpired,
                    this.getIsLogging());
            this.setIsStarted(true);
        }

        LogEntry logEntry = this.getLogEntry();
        logEntry.setEntryType(LogEntryType.INFORMATION);
        logEntry.setMessage(Resources.getLogging_ApiClientStarted());
        this.doLogging(logEntry);
    }

    /**
     * Stops the API client.
     */
    public final void stop() {
        synchronized (this.clientLock) {
            this.setIsStarted(false);
        }

        LogEntry logEntry = this.getLogEntry();
        logEntry.setEntryType(LogEntryType.INFORMATION);
        logEntry.setMessage(Resources.getLogging_ApiClientStopped());
        this.doLogging(logEntry);
    }

    /**
     * Closes the API client
     */
    @Override
    public void close() throws java.io.IOException {
        synchronized (this.clientLock) {
            this.client.close();
            this.setIsStarted(false);
            this.client = null;
            this.recoveryHandlerContext.setIsCancellationRequested(true);

            synchronized (RecoveryHandlerContext.POLLING_FOR_RECOVERY_LOCK) {
                if (!this.recoveryHandlerContext.getIsResponsibleForRecovery()) {
                    return;
                }

                this.recoveryHandlerContext.setIsResponsibleForRecovery(false);

                String key = this.recoveryHandlerContext.getStoreAndForwardService().getName();
                if (RecoveryHandlerContext.CLIENT_POLLING_STATUS.containsKey(key)) {
                    RecoveryHandlerContext.CLIENT_POLLING_STATUS.put(key, false);
                }
            }
        }
    }

    /**
     * Returns a new single pack request log entry.
     *
     * @param pack The pack identifier.
     * @return The LogEntry.
     */
    private LogEntry getSinglePackRequestLogEntry(PackIdentifier pack) {
        LogEntry logEntry = this.getLogEntry();
        logEntry.setPack(new LogEntryPackIdentifier(pack));
        logEntry.setEntryCategory(LogEntryCategory.SINGLE_PACK_REQUEST);
        return logEntry;
    }

    /**
     * Returns a new bulk pack log entry.
     *
     * @param category the log entry category
     * @return The LogEntry.
     */
    private LogEntry getBulkPackLogEntry(LogEntryCategory category) {
        LogEntry logEntry = this.getLogEntry();
        logEntry.setEntryCategory(category);
        return logEntry;
    }

    /**
     * Returns a new recovery request log entry.
     *
     * @param category The log entry category.
     * @return The LogEntry.
     */
    private LogEntry getRecoveryLogEntry(LogEntryCategory category) {
        LogEntry logEntry = this.getLogEntry();
        logEntry.setEntryCategory(category);
        return logEntry;
    }

    /**
     * Returns a new bulk results request log entry.
     *
     * @param category The log entry category.
     * @return The LogEntry.
     */
    private LogEntry getBulkResultsLogEntry(LogEntryCategory category) {
        LogEntry logEntry = this.getLogEntry();
        logEntry.setEntryCategory(category);
        return logEntry;
    }

    /**
     * Returns a new log entry.
     *
     * @return The LogEntry.
     */
    private LogEntry getLogEntry() {
        LogEntry logEntry = new LogEntry(this.logTable.getCurrentTimestamp());
        logEntry.setUser(this.loggingService.getCurrentUser());
        logEntry.setId(UUID.randomUUID().toString());
        logEntry.setTime(Instant.now());
        return logEntry;
    }

    /**
     * Defer the current request using the configured data service.
     *
     * @param deferredRequest The deferred request.
     */
    private void doDeferRequest(DeferredRequest deferredRequest) {
        try {
            // Persist deferred record.
            try (TransactionManager transaction = TransactionManager.newTransaction(this.requestTable)) {
                try {
                    this.requestTable.add(deferredRequest);
                    transaction.commit();
                } catch (RuntimeException exception) {
                    try {
                        // Attempt the rollback of the transaction.
                        transaction.rollback();
                    } catch (RuntimeException e) {
                        throw new InterchangeException(String.format("%1$s", Resources.getInterchangeException_DeferredRequestNotStoredAndRollbackFailed()), exception);
                    } finally {
                        this.logOnError(Resources.getLogging_DeferredRequestNotStored(), deferredRequest);
                    }

                    throw new InterchangeException(String.format("%1$s", Resources.getInterchangeException_DeferredRequestNotStored()), exception);
                }
            }
        } catch (RuntimeException | IOException exception) {
            this.logOnError(Resources.getLogging_DeferredRequestNotStored(), deferredRequest);
            throw new InterchangeException(String.format("%1$s", Resources.getInterchangeException_DeferredRequestNotStored()), exception);
        }
    }

    /**
     * Processes an API request.
     *
     * @param logEntry The current log entry
     * @param verb The HTTP verb required for this request.
     * @param pack The pack.
     * @param body The content that will be passed in the body of the request.
     * @param requestedState The desired state to which the pack should be
     * transitioned.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @param requestType The API request type.
     * @return A request result from the National System.
     */
    private ApiResult doRequest(
            NbsIntegrationLogEntry logEntry,
            HttpVerb verb,
            PackIdentifier pack,
            String body,
            RequestedPackState requestedState,
            Boolean isManual,
            String language,
            RequestType requestType) {
        // Validate the request
        LocalValidationResponse validationResponse = PackIdentifierVerification
                .validate(
                        pack,
                        (isManual == null ? false : isManual)
                                ? DataEntryMode.MANUAL
                                : DataEntryMode.NON_MANUAL);

        if (validationResponse.getOperationCode() > 0) {
            // The pack has failed validation
            return new ApiResult(validationResponse);
        }

        URIBuilder uriBuilder = new URIBuilder();
        NbsHttpClient.buildOptionalParametersQueryString(
                uriBuilder,
                pack.getBatchId(),
                pack.getExpiryDate());
        String uri = String.format(
                "product/%1$s/%2$s/pack/%3$s%4$s",
                pack.getProductCodeScheme().toLowerCase(),
                pack.getProductCode(),
                pack.getSerialNumber(),
                uriBuilder.toString());
        return this.preprocessRequest(
                logEntry,
                verb,
                uri,
                body,
                requestedState,
                isManual,
                language,
                requestType);
    }

    /**
     * Preprocess and perform an API request.
     *
     * @param logEntry The current log entry.
     * @param verb The HTTP verb required for this request.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @param requestedState The desired state to which the pack should be
     * transitioned.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param requestType The API request type.
     * @return A request result from the National System.
     */
    private ApiResult preprocessRequest(
            NbsIntegrationLogEntry logEntry,
            HttpVerb verb,
            String uri,
            String body,
            RequestedPackState requestedState,
            Boolean isManual,
            RequestType requestType) {
        return preprocessRequest(
                logEntry,
                verb,
                uri,
                body,
                requestedState,
                isManual,
                null,
                requestType);
    }

    /**
     * Preprocess and perform an API request.
     *
     * @param logEntry The current log entry.
     * @param verb The HTTP verb required for this request.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @param requestedState The desired state to which the pack should be
     * transitioned.
     * @param requestType The API request type.
     * @return A request result from the National System.
     */
    private ApiResult preprocessRequest(
            NbsIntegrationLogEntry logEntry,
            HttpVerb verb,
            String uri,
            String body,
            RequestedPackState requestedState,
            RequestType requestType) {
        return preprocessRequest(
                logEntry,
                verb,
                uri,
                body,
                requestedState,
                null,
                null,
                requestType);
    }

    /**
     * Preprocess and perform an API request.
     *
     * @param logEntry The current log entry.
     * @param verb The HTTP verb required for this request.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @param requestType The API request type.
     * @return A request result from the National System.
     */
    private ApiResult preprocessRequest(
            NbsIntegrationLogEntry logEntry,
            HttpVerb verb,
            String uri,
            String body,
            RequestType requestType) {
        return preprocessRequest(
                logEntry,
                verb,
                uri,
                body,
                null,
                null,
                null,
                requestType);
    }

    /**
     * Preprocess and perform an API request.
     *
     * @param logEntry The current log entry.
     * @param verb The HTTP verb required for this request.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @param requestedState The desired state to which the pack should be
     * transitioned.
     * @param isManual Indicates whether the data entry is manual or scanned.
     * @param language Selects a specific language for response messages.
     * @param requestType The API request type.
     * @return A request result from the National System.
     */
    @SuppressWarnings("null")
    private ApiResult preprocessRequest(
            NbsIntegrationLogEntry logEntry,
            HttpVerb verb,
            String uri,
            String body,
            RequestedPackState requestedState,
            Boolean isManual,
            String language,
            RequestType requestType) {
        if (!this.getIsStarted()) {
            ApiResult notStartedResult = new ApiResult(
                    ReportedPackState.NONE,
                    requestType,
                    true,
                    Resources.getApiClient_ClientNotStarted()
            );

            logEntry.setMessage(Resources.getApiClient_ClientNotStarted());
            logEntry.setEntryType(LogEntryType.WARNING);
            logEntry.setResponse(new LogEntryResponse(notStartedResult));

            return notStartedResult;
        }

        // Populate the log entry
        logEntry.setRequest(new LogEntryRequest());
        NbsIntegrationLogEntryRequest request = logEntry.getRequest();
        request.setVerb(verb.getValue());
        request.setUri(uri);
        request.setBody(body);
        request.setRequestedState(requestedState == null ? null : requestedState.getValue());
        request.setIsManual((isManual != null) ? isManual : false);
        request.setLanguage(language);

        Verb httpVerbMethod
                = verb == HttpVerb.GET
                        ? this::doGet
                        : verb == HttpVerb.PATCH
                                ? this::doPatch
                                : verb == HttpVerb.POST
                                        ? this::doPost
                                        : null;

        if (httpVerbMethod == null) {
            ApiResult unsupportedVerbResult = new ApiResult(
                    ReportedPackState.NONE,
                    requestType,
                    true,
                    Resources.getApiClient_UnsupportedHttpVerb()
            );

            logEntry.setMessage(Resources.getApiClient_UnsupportedHttpVerb());
            logEntry.setEntryType(LogEntryType.WARNING);
            logEntry.setResponse(new LogEntryResponse(unsupportedVerbResult));

            return unsupportedVerbResult;
        }

        // Defne predicates for header manipulation.
        Predicate<Header> predicateEmvsDataEntryMode
                = h -> (h.getName() == null ? EMVS_DATA_ENTRY_MODE == null : h.getName().equals(EMVS_DATA_ENTRY_MODE));
        Predicate<Header> predicateAcceptLanguage
                = h -> (h.getName() == null ? ACCEPT_LANGUAGE == null : h.getName().equals(ACCEPT_LANGUAGE));

        if (((this.getDataEntryMode() == DataEntryMode.MANUAL) && ((isManual == null) ? false : !isManual.equals(false)))
                || ((this.getDataEntryMode() == DataEntryMode.NON_MANUAL) && ((isManual == null) ? false : isManual.equals(true)))) {
            this.client.getDefaultRequestHeaders().removeIf(predicateEmvsDataEntryMode);
            this.client.getDefaultRequestHeaders().add(new BasicHeader(EMVS_DATA_ENTRY_MODE, "manual"));
        }

        this.client.getDefaultRequestHeaders().removeIf(predicateAcceptLanguage);

        if (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(language)) {
            if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getLanguage())) {
                this.client.getDefaultRequestHeaders().add(new BasicHeader(ACCEPT_LANGUAGE, this.getLanguage()));
            }
        } else {
            this.client.getDefaultRequestHeaders().add(new BasicHeader(ACCEPT_LANGUAGE, language));
        }

        DeferredRequest deferredRequest = null;

        if (this.getStoreAndForwardMode() != StoreAndForwardMode.NONE && this.requestTable != null) {
            deferredRequest = new DeferredRequest(this.requestTable.getCurrentTimestamp());
            deferredRequest.setVerb(verb.getValue());
            deferredRequest.setUri(uri);
            deferredRequest.setBody(body);
            deferredRequest.setRequestedState(requestedState == null ? null : requestedState.getValue());
            deferredRequest.setIsManual((isManual != null) ? isManual : false);
            deferredRequest.setLanguage(language);
        }

        try {
            return this.performRequest(logEntry, requestType, httpVerbMethod, uri, body, deferredRequest);
        } finally {
            this.client.getDefaultRequestHeaders().removeIf(predicateEmvsDataEntryMode);
            this.client.getDefaultRequestHeaders().add(new BasicHeader(EMVS_DATA_ENTRY_MODE, "non-manual"));
        }
    }

    /**
     * Performs a pre-processed API request.
     *
     * @param logEntry The current log entry.
     * @param requestType The API request type.
     * @param httpVerbMethod The method required for the given HTTP verb.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @return A request result from the National System.
     */
    private ApiResult performRequest(
            NbsIntegrationLogEntry logEntry,
            RequestType requestType,
            Verb httpVerbMethod,
            String uri,
            String body) {
        return performRequest(
                logEntry,
                requestType,
                httpVerbMethod,
                uri,
                body,
                null);
    }

    /**
     * Provides general processing of responses when the National System service
     * is unavailable.
     *
     * @param requestContext Context data for the request.
     */
    private void unavailableServiceResponseProcessing(RequestContext requestContext) {
        if (requestContext.getResponse().getStatusLine().getStatusCode()
                == HttpStatusCode.SC_SERVICE_UNAVAILABLE) {
            requestContext.setMessage(
                    String.format("%1$s", Resources.getApiClient_ServiceUnavailableWarning()));
        }

        if (requestContext.getRemainingAttempts() == 0) {
            this.defaultResponseProcessing(requestContext);
        }

        requestContext.setRemainingAttempts(requestContext.getRemainingAttempts() - 1);
    }

    /**
     * Provides default processing for responses from the National System.
     *
     * @param requestContext Context data for the request.
     * @return A processed result from the API.
     */
    private ApiResult defaultResponseProcessing(RequestContext requestContext) {
        this.deferRequest(
                requestContext.getRequestType(),
                requestContext.getDeferredRequest(),
                requestContext.getResponse().getStatusLine().getReasonPhrase(),
                new ClientProtocolException(com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(requestContext.getMessage())
                        ? String.format(
                                "%1$s: %2$s",
                                (int) requestContext.getResponse().getStatusLine().getStatusCode(),
                                requestContext.getResponse().getStatusLine().getReasonPhrase())
                        : requestContext.getMessage().trim()));

        ApiResult defaultResult = new ApiResult(
                ReportedPackState.NONE,
                requestContext.getRequestType(),
                requestContext.getResponse(),
                true,
                requestContext.getMessage());
        requestContext.getLogEntry().setMessage(requestContext.getMessage());
        requestContext.getLogEntry().setEntryType(LogEntryType.WARNING);
        requestContext.getLogEntry().setResponse(new LogEntryResponse(defaultResult));
        return defaultResult;
    }

    /**
     * Performs a pre-processed API request.
     *
     * @param logEntry The current log entry.
     * @param requestType The API request type.
     * @param httpVerbMethod The method required for the given HTTP verb.
     * @param uri The URI for the requested resource.
     * @param body The content that will be passed in the body of the request.
     * @param deferredRequest The deferred request.
     * @return A request result from the National System.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private ApiResult performRequest(
            NbsIntegrationLogEntry logEntry,
            RequestType requestType,
            Verb httpVerbMethod,
            String uri,
            String body,
            DeferredRequest deferredRequest) {
        HttpResponse response = null;
        int remainingAttempts = this.getRetryCount() < 1 ? 1 : this.getRetryCount() + 1;
        String message = "";
        StringEntity content = new StringEntity((body != null) ? body : "", "UTF-8");
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-type", "application/json"));

        while (remainingAttempts > 0) {
            try {
                if (!this.getIsOffline() || requestType == RequestType.SUBMIT_RECOVERY_REQUEST) {
                    logEntry.setRequest(new LogEntryRequest());
                    logEntry.getRequest().setUri(uri);
                    logEntry.getRequest().setBody(body);
                    response = httpVerbMethod.doRequest(uri, content, headers);

                    // The identity server may still be starting after the National System
                    // has recovered.
                    @SuppressWarnings("LocalVariableHidesMemberVariable")
                    int retryCount = 3;
                    final int RetryIntervalMilliseconds = 30000;

                    while (((response == null ? HttpStatusCode.SC_NOTDEFINED : response.getStatusLine().getStatusCode()) == HttpStatusCode.SC_UNAUTHORIZED) && retryCount > 0) {
                        Thread.sleep(RetryIntervalMilliseconds);

                        // The token may have expired.  Create a new NBS HTTP client and retry.
                        this.tokenExpired(this, this.client.getTokenExpiredEventArgs());
                        response = httpVerbMethod.doRequest(uri, content, headers);
                        retryCount--;
                    }
                } else {
                    message = Resources.getApiClient_Offline();

                    this.deferRequest(requestType, deferredRequest, message, new ClientProtocolException(message));

                    ApiResult offlineResult = new ApiResult(
                            ReportedPackState.NONE,
                            requestType,
                            true,
                            message
                    );

                    logEntry.setMessage(message);
                    logEntry.setEntryType(LogEntryType.WARNING);
                    logEntry.setResponse(new LogEntryResponse(offlineResult));
                    return offlineResult;
                }
            } catch (IOException | InterruptedException ioEx) {
                message = ioEx.getMessage();
                this.deferRequest(requestType, deferredRequest, message, ioEx);
                logEntry.setEntryType(LogEntryType.ERROR);
            }

            if (response == null) {
                // Replace any arbitrary whitespace regions with single space.
                message = message.replaceAll("\\s+", " ");
                ApiResult result = new ApiResult(
                        ReportedPackState.NONE,
                        requestType,
                        true,
                        message
                );

                logEntry.setMessage(message);
                logEntry.setEntryType(
                        logEntry.getEntryType() == LogEntryType.INFORMATION
                        ? LogEntryType.WARNING
                        : logEntry.getEntryType());
                logEntry.setResponse(new LogEntryResponse(result));
                return result;
            }

            int delay = this.getRetryInterval();

            RequestContext requestContext = new RequestContext();
            requestContext.setRequestType(requestType);
            requestContext.setDeferredRequest(deferredRequest);
            requestContext.setResponse(response);
            requestContext.setMessage(message);
            requestContext.setLogEntry(logEntry);

            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatusCode.SC_NOT_FOUND:
                    if (requestType == RequestType.GET_RECOVERY_RESULT) {
                        // The identity server may still be starting after the National System
                        // has recovered.
                        remainingAttempts = 3;
                        delay = 30000;
                        break;
                    }

                    message = Resources.getApiClient_CheckUriMessage();
                case HttpStatusCode.SC_OK:
                case HttpStatusCode.SC_ACCEPTED:
                case HttpStatusCode.SC_UNPROCESSABLE_ENTITY:
                case HttpStatusCode.SC_CONFLICT:
                case HttpStatusCode.SC_FORBIDDEN:
                    Gson gson = new Gson();
                    ApiResult result = null;
                    try (StringWriter writer = new StringWriter()) {
                        IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName("UTF-8"));
                        String jsonContent = writer.toString();
                        result = gson.fromJson(jsonContent, ApiResult.class);
                    } catch (IOException ex) {
                        String e = ex.getMessage();
                        // ignore;
                    }

                    if (result == null) {
                        ApiResult nullResult = new ApiResult(
                                ReportedPackState.NONE,
                                requestType,
                                response,
                                true,
                                String.format("%1$s: %2$s  %3$s", (int) response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), message).trim());
                        logEntry.setMessage(com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(nullResult.getInformation()) ? nullResult.getWarning() : nullResult.getInformation());
                        logEntry.setResponse(new LogEntryResponse(nullResult));
                        return nullResult;
                    }

                    result.setStatusCode(response.getStatusLine().getStatusCode());

                    try {
                        Header locationHeader = response.getFirstHeader("Location");
                        if (null != locationHeader) {
                            result.setLocation(new URI(locationHeader.getValue()));
                        }
                    } catch (URISyntaxException uriEx) {
                        // ignore;
                    }

                    logEntry.setMessage(
                            com.reply.solidsoft.nbs.integration.extensions.StringExtensions
                                    .isNullOrWhiteSpace(result.getInformation())
                            ? result.getWarning()
                            : result.getInformation());
                    logEntry.setUprc(result.getUprc());
                    logEntry.setResponse(new LogEntryResponse(result));

                    // If a response was returned from the National System, switch to on-line mode if required.
                    if (!this.getIsOffline()) {
                        return result;
                    }

                    this.setIsOffline(false);
                    // Invoke each event listener.
                    this.offLineModeChanged.listeners().forEach((listener)
                            -> listener.invoke(this, new OffLineEventArgs(false)));
                    return result;
                case HttpStatusCode.SC_UNAUTHORIZED:
                    Header aggregatedHeader = Arrays.asList(
                            response.getHeaders("WWW-Authenticate")).stream().reduce((current, header) -> {
                        return new BasicHeader(current.getName(), current.getValue() + String.format("%1$s;", header));
                    }).get();
                    message = String.format("%1$s  %2$s", Resources.getApiClient_UnauthorizedWarning(), aggregatedHeader.getValue().trim());
                    requestContext.setMessage(message);
                    this.defaultResponseProcessing(requestContext);
                    break;
                case HttpStatusCode.SC_GATEWAY_TIMEOUT:
                    message = String.format("%1$s", Resources.getApiClient_GatewayTimeoutWarning());
                    requestContext.setMessage(message);
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                case HttpStatusCode.SC_REQUEST_TIMEOUT:
                    message = String.format("%1$s", Resources.getApiClient_RequestTimeoutWarning());
                    requestContext.setMessage(message);
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                case HttpStatusCode.SC_BAD_GATEWAY:
                    message = String.format("%1$s", Resources.getApiClient_BadGatewayWarning());
                    requestContext.setMessage(message);
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                case HttpStatusCode.SC_INTERNAL_SERVER_ERROR:
                    message = String.format("%1$s", Resources.getApiClient_InternalServerErrorWarning());
                    requestContext.setMessage(message);
                    //int[] xxx = new intremainingAttempts
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                case 429:
                    message = String.format("%1$s", Resources.getApiClient_TooManyRequests());
                    requestContext.setMessage(message);
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                case HttpStatusCode.SC_SERVICE_UNAVAILABLE:
                    message = String.format("%1$s", Resources.getApiClient_ServiceUnavailableWarning());
                    requestContext.setMessage(message);
                    requestContext.setRemainingAttempts(remainingAttempts);
                    this.unavailableServiceResponseProcessing(requestContext);
                    remainingAttempts = requestContext.getRemainingAttempts();
                    break;
                default:
                    this.defaultResponseProcessing(requestContext);
                    break;
            }

            if (remainingAttempts > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException exception) {
                    message = exception.getMessage();
                    logEntry.setEntryType(LogEntryType.ERROR);
                }
            }
        }

        ApiResult finalResult = new ApiResult(ReportedPackState.NONE, requestType, response, true);
        logEntry.setMessage(message);
        logEntry.setEntryType(LogEntryType.WARNING);
        logEntry.setResponse(new LogEntryResponse(finalResult));
        return finalResult;
    }

    /**
     * Defers a request, if required.
     *
     * @param requestType The type of the request.
     * @param request The deferred representation of the request
     * @param message The message describing the reason the request is being
     * deferred.
     * @param innerException The inner exception, if it exists.
     */
    private void deferRequest(RequestType requestType, DeferredRequest request, String message, Exception innerException) {
        // This method is only called if all attempts to reach the National System have failed.
        // Hence, off-line mode is entered here.
        if (!this.getIsOffline()) {
            this.setIsOffline(true);

            // Invoke each event listener.
            this.offLineModeChanged.listeners().forEach((listener) -> {
                listener.invoke(this, new OffLineEventArgs(true));
            });
        }

        if (this.getStoreAndForwardMode() == StoreAndForwardMode.NONE
                || requestType == RequestType.SUBMIT_RECOVERY_REQUEST
                || requestType == RequestType.GET_BULK_RESULT
                || requestType == RequestType.GET_RECOVERY_RESULT) {
            return;
        }

        StoreAndForwardEventArgs eventArgs = new StoreAndForwardEventArgs(new InterchangeException(String.format("%1$s  %2$s", Resources.getInterchangeException_BaseMessage(), message).trim(), innerException), message, request, this.storeAndForwardMode);

        // Invoke each event listener.
        this.deferringRequest.listeners().forEach((listener) -> {
            listener.invoke(this, eventArgs);
        });

        if (this.getStoreAndForwardMode() != StoreAndForwardMode.AUTOMATIC || eventArgs.getCancel()) {
            return;
        }

        if (requestType == RequestType.SUBMIT_BULK_REQUEST) {
            // Break the request into separate single pack requests
            Gson gson = new Gson();
            BulkRequest bulkRequest = gson.fromJson(request.getBody(), BulkRequest.class);

            if (!this.getStoreAndForwardBulkRequests() || bulkRequest == null) {
                return;
            }

            for (PackIdentifier pack : bulkRequest.getPacks()) {
                String body = "";
                URIBuilder uriBuilder = new URIBuilder();
                NbsHttpClient.buildOptionalParametersQueryString(
                        uriBuilder,
                        pack.getBatchId(),
                        pack.getExpiryDate());
                String uri = String.format(
                        "product/%1$s/%2$s/pack/%3$s%4$s",
                        pack.getProductCodeScheme().toLowerCase(),
                        pack.getProductCode(),
                        pack.getSerialNumber(),
                        uriBuilder.toString());
                String verb = "Get";

                if (!com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(bulkRequest.getState())) {
                    try {
                        RequestedPackState state = RequestedPackState.valueOf(bulkRequest.getState());
                        verb = "Patch";
                        body = gson.toJson(new PackStateTransitionCommand(state));
                    } catch (Throwable ex) {
                        // ignore;
                    }
                }

                DeferredRequest deferredRequest = new DeferredRequest(this.requestTable.getCurrentTimestamp());
                deferredRequest.setBody(body);
                deferredRequest.setLanguage(request.getLanguage());
                deferredRequest.setIsManual(request.getIsManual());
                deferredRequest.setRequestedState(bulkRequest.getState());
                deferredRequest.setUri(uri);
                deferredRequest.setVerb(verb);
                this.doDeferRequest(deferredRequest);
            }

            return;
        }

        this.doDeferRequest(request);
    }

    /**
     * Logs an entry from within an event handler.
     *
     * @param message The log message.
     */
    private void logOnError(String message) {
        this.logOnError(message, null);
    }

    /**
     * Logs an entry from within an event handler.
     *
     * @param message The log message.
     * @param deferredRequest An optional deferred request.
     */
    private void logOnError(String message, DeferredRequest deferredRequest) {
        try {
            LogEntry logEntry = this.getLogEntry();
            logEntry.setEntryType(LogEntryType.ERROR);
            logEntry.setSeverity(1);
            logEntry.setMessage(message);
            if (deferredRequest != null) {
                logEntry.setRequest(new LogEntryRequest(deferredRequest));
            }

            this.doLogging(logEntry);
        } catch (java.lang.Exception e) {
            // ignored
        }
    }

    /**
     * Performs the HTTP Get.
     *
     * @param uri The URI for the requested resource.
     * @param content The content that will be passed in the body of the
     * request.
     * @param headers The list of HTTP headers.
     * @return The Patch response.
     */
    private HttpResponse doGet(
            String uri,
            StringEntity content,
            List<Header> headers) throws IOException {
        synchronized (this.clientLock) {
            return this.client.get(uri, headers);
        }
    }

    /**
     * Performs the HTTP Patch.
     *
     * @param uri The URI for the requested resource.
     * @param content The content that will be passed in the body of the
     * request.
     * @param headers The list of HTTP headers.
     * @return The Patch response.
     */
    private HttpResponse doPatch(
            String uri,
            StringEntity content,
            List<Header> headers) throws IOException {
        synchronized (this.clientLock) {
            return this.client.patch(uri, content, headers);
        }
    }

    /**
     * Performs the HTTP Post.
     *
     * @param uri The URI for the requested resource.
     * @param content The content that will be passed in the body of the
     * request.
     * @param headers The list of HTTP headers.
     * @return The Patch response.
     */
    private HttpResponse doPost(
            String uri,
            StringEntity content,
            List<Header> headers) throws IOException {
        synchronized (this.clientLock) {
            return this.client.post(uri, content, headers);
        }
    }

    /**
     * The token has expired. Create a new NBS HTTP client.
     *
     * @param sender The sender.
     * @param eventArgs The event arguments.
     */
    private void tokenExpired(Object sender, TokenExpiredEventArgs eventArgs) {
        // Renew the HTTP client.
        synchronized (this.clientLock) {
            this.client = new NbsHttpClient(
                    new ApiConnection(this.getBaseUrl(),
                            this.getIdentityServerUrl()),
                    this.loggingService,
                    this.getClientCredentialsService(),
                    this.getConnectionIdentifier(),
                    this::tokenExpired,
                    this.getIsLogging(),
                    eventArgs == null ? null : eventArgs.getRefreshToken());
        }
    }

    /**
     * Performs logging.
     *
     * @param logEntry The log entry.
     */
    private void doLogging(LogEntry logEntry) {
        if (this.getIsLogging()) {
            this.loggingService.getLog().invoke(this, logEntry);
        }
    }
}
