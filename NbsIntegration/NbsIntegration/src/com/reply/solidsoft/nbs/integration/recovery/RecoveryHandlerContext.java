/**
 * -----------------------------------------------------------------------------
 * File=RecoveryHandlerContext.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The context for recovery using store &amp; forward.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery;

import com.reply.solidsoft.nbs.integration.UpdateDeferredRequestStatisticsListener;
import com.reply.solidsoft.nbs.integration.model.requests.RecoveryRequest;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestAck;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestResults;
import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import com.reply.solidsoft.nbs.integration.extensions.functional.Logger;
import com.reply.solidsoft.nbs.integration.extensions.functional.Func0;
import com.reply.solidsoft.nbs.integration.extensions.functional.Func2;
import com.reply.solidsoft.nbs.integration.extensions.events.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URI;
import com.reply.solidsoft.nbs.integration.data.model.TypedDataTable;

/**
 * The context for recovery using store &amp; forward.
 */
public class RecoveryHandlerContext {

    /**
     * Lock object for accepting responsibility for polling for recovery.
     */
    public static final Object POLLING_FOR_RECOVERY_LOCK = new Object();

    /**
     * A dictionary used to determine which running instance of the API Client
     * is polling for recovery.
     */
    public static final Map<String, Boolean> CLIENT_POLLING_STATUS = new HashMap<String, Boolean>();

    /**
     * The deferred requests.
     */
    private final Func0<List<DeferredRequest>> deferredRequests;

    /**
     * Initializes a new instance of the RecoveryHandlerContext class.
     *
     * @param storeAndForwardService The store and forward service.
     * @param recoveryPollInterval The recovery poll interval.
     * @param deferredRequests The deferred requests.
     * @param submitRecoveryRequest The submit recovery request.
     * @param getRecoveryResult The get recovery result.
     * @param logOnError The log on error.
     * @param updateDeferredRequestStatistics The update deferred request
     * statistics.
     * @param maxBulkPackCount The max bulk pack count.
     * @param language The language.
     */
    public RecoveryHandlerContext(
            StoreAndForwardService storeAndForwardService,
            int recoveryPollInterval,
            Func0<List<DeferredRequest>> deferredRequests,
            Func2<RecoveryRequest, String, RecoveryRequestAck> submitRecoveryRequest,
            Func2<URI, String, RecoveryRequestResults> getRecoveryResult,
            Logger<String, DeferredRequest> logOnError,
            Event<UpdateDeferredRequestStatisticsListener> updateDeferredRequestStatistics,
            int maxBulkPackCount,
            String language) {
        this.setStoreAndForwardService(storeAndForwardService);
        this.recoveryPollInterval = recoveryPollInterval;
        this.requestTable = (TypedDataTable<DeferredRequest>) (this.getStoreAndForwardService() == null ? null : this.getStoreAndForwardService().getRequests());
        this.responseTable = (TypedDataTable<RecoverySinglePackResponse>) (this.getStoreAndForwardService() == null ? null : this.getStoreAndForwardService().getResponses());
        this.deferredRequests = deferredRequests;
        this.updateDeferredRequestStatistics = updateDeferredRequestStatistics;
        this.maxBulkPackCount = maxBulkPackCount;
        this.submitRecoveryRequest = (RecoveryRequest arg1, String arg2) -> submitRecoveryRequest.invoke(arg1, arg2);
        this.getRecoveryResult = (URI arg1, String arg2) -> getRecoveryResult.invoke(arg1, arg2);
        this.logOnError = (String arg1, DeferredRequest arg2) -> logOnError.invoke(arg1, arg2);
        this.language = language;
    }

    /**
     * A value indicating whether cancellation of the thread has been requested.
     */
    private volatile boolean isCancellationRequested = false;

    /**
     * Gets a value indicating whether cancellation of the thread has been
     * requested.
     *
     * @return A value indicating whether cancellation of the thread has been
     * requested.
     */
    public boolean getIsCancellationRequested() {
        return this.isCancellationRequested;
    }

    /**
     * Sets a value indicating whether cancellation of the thread has been
     * requested.
     *
     * @param value A value indicating whether cancellation of the thread has
     * been requested.
     */
    public void setIsCancellationRequested(boolean value) {
        this.isCancellationRequested = value;
    }

    /**
     * The recovery poll interval in milliseconds.
     */
    private final int recoveryPollInterval;

    /**
     * Gets the recovery poll interval in milliseconds.
     *
     * @return The recovery poll interval in milliseconds.
     */
    public final int getRecoveryPollInterval() {
        return recoveryPollInterval;
    }

    /**
     * The request table in the store and forward data management service.
     */
    private final TypedDataTable<DeferredRequest> requestTable;

    /**
     * Gets the request table in the store and forward data management service.
     *
     * @return The request table in the store and forward data management
     * service.
     */
    public final TypedDataTable<DeferredRequest> getRequestTable() {
        return requestTable;
    }

    /**
     * The store &amp; forward statistics handler.
     */
    private final Event<UpdateDeferredRequestStatisticsListener> updateDeferredRequestStatistics;

    /**
     * Gets the store &amp; forward statistics handler.
     *
     * @return The store &amp; forward statistics handler.
     */
    public final Event<UpdateDeferredRequestStatisticsListener> getUpdateDeferredRequestStatistics() {
        return updateDeferredRequestStatistics;
    }

    /**
     * Gets the collection of currently deferred requests.
     *
     * @return The collection of currently deferred requests.
     */
    public final List<DeferredRequest> getDeferredRequests() {
        return this.deferredRequests.invoke();
    }

    /**
     * The API function for submitting recovery requests.
     */
    private final Func2<RecoveryRequest, String, RecoveryRequestAck> submitRecoveryRequest;

    /**
     * Gets the API function for submitting recovery requests.
     *
     * @return The API function for submitting recovery requests.
     */
    public final Func2<RecoveryRequest, String, RecoveryRequestAck> getSubmitRecoveryRequest() {
        return submitRecoveryRequest;
    }

    /**
     * The API function for requesting recovery results.
     */
    private final Func2<URI, String, RecoveryRequestResults> getRecoveryResult;

    /**
     * Gets the API function for requesting recovery results.
     *
     * @return The API function for requesting recovery results.
     */
    public final Func2<URI, String, RecoveryRequestResults> getGetRecoveryResult() {
        return getRecoveryResult;
    }

    /**
     * The action for logging when requests are deferred.
     */
    private final Logger<String, DeferredRequest> logOnError;

    /**
     * Gets the action for logging when requests are deferred.
     *
     * @return The action for logging when requests are deferred.
     */
    public final Logger<String, DeferredRequest> getLogOnError() {
        return logOnError;
    }

    /**
     * The maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     *
     * If set the 0, the API will use the default value.
     */
    private final int maxBulkPackCount;

    /**
     * Gets the maximum number of packs allowed in a bulk request. This value is
     * ultimately dictated by the National System.
     *
     * If set the 0, the API will use the default value.
     *
     * @return The maximum number of packs allowed in a bulk request.
     */
    public final int getMaxBulkPackCount() {
        return maxBulkPackCount;
    }

    /**
     * The response table in the store and forward data management service.
     */
    private final TypedDataTable<RecoverySinglePackResponse> responseTable;

    /**
     * Gets the response table in the store and forward data management service.
     *
     * @return The response table in the store and forward data management
     * service.
     */
    public final TypedDataTable<RecoverySinglePackResponse> getResponseTable() {
        return responseTable;
    }

    /**
     * The requested language for recovery results.
     */
    private final String language;

    /**
     * Gets the requested language for recovery results.
     *
     * @return The requested language for recovery results.
     */
    public final String getLanguage() {
        return language;
    }

    /**
     * The store &amp; forward service.
     */
    private StoreAndForwardService storeAndForwardService;

    /**
     * Gets the store &amp; forward service.
     *
     * @return The store &amp; forward service.
     */
    public final StoreAndForwardService getStoreAndForwardService() {
        return storeAndForwardService;
    }

    /**
     * Sets the store &amp; forward service.
     *
     * @param value The store &amp; forward service.
     */
    public final void setStoreAndForwardService(StoreAndForwardService value) {
        storeAndForwardService = value;
    }

    /**
     * A value indicating whether this instance of the API Client is polling for
     * recovery.
     */
    private boolean isResponsibleForRecovery;

    /**
     * Gets a value indicating whether this instance of the API Client is
     * polling for recovery.
     *
     * @return A value indicating whether this instance of the API Client is
     * polling for recovery.
     */
    public final boolean getIsResponsibleForRecovery() {
        return isResponsibleForRecovery;
    }

    /**
     * Sets a value indicating whether this instance of the API Client is
     * polling for recovery.
     *
     * @param value A value indicating whether this instance of the API Client
     * is polling for recovery.
     */
    public final void setIsResponsibleForRecovery(boolean value) {
        isResponsibleForRecovery = value;
    }
}
