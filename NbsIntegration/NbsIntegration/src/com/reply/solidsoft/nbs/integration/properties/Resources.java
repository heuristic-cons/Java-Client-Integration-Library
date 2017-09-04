/**
 * -----------------------------------------------------------------------------
 * File=Resources.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Base class for store and forward databases.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.properties;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * A strongly-typed resource class, for looking up localized strings, etc.
 */
public class Resources {

    /**
     * The locale for the resource bundle. Defaults to the current culture.
     */
    private static Locale LOCALE = Locale.getDefault();

    /**
     * The resource bundle which manages access to resources.
     */
    private static ResourceBundle STRING_RESOURCES;

    public Resources() {
    }

    /**
     * Returns the cached ResourceManager instance used by this class.
     *
     * @return The cached ResourceManager instance used by this class.
     */
    public static ResourceBundle getResourceManager() {
        if (STRING_RESOURCES == null) {
            ResourceBundle temp = ResourceBundle.getBundle("com.reply.solidsoft.nbs.integration.properties.IntegrationResources", LOCALE);
            STRING_RESOURCES = temp;
        }

        return STRING_RESOURCES;
    }

    /**
     * Overrides the current thread CurrentUICulture property for all resource
     * lookups using this strongly typed resource class.
     *
     * @return The culture being used by the resource bundle.
     */
    public static Locale getCulture() {
        return LOCALE;
    }

    public static void setCulture(Locale value) {
        LOCALE = value;
    }

    /**
     * Looks up a localized string similar to A batch identifier is required.
     *
     * @return A localized string similar to A batch identifier is required.
     */
    public static String getAPI_Validation_61020000() {
        STRING_RESOURCES = null;
        return getResourceManager().getString("API_Validation_61020000");
    }

    /**
     * Looks up a localized string similar to A product code is required.
     *
     * @return A localized string similar to A product code is required.
     */
    public static String getAPI_Validation_61020001() {
        return getResourceManager().getString("API_Validation_61020001");
    }

    /**
     * Looks up a localized string similar to A product code scheme is required.
     *
     * @return A localized string similar to A product code scheme is required.
     */
    public static String getAPI_Validation_61020002() {
        return getResourceManager().getString("API_Validation_61020002");
    }

    /**
     * Looks up a localized string similar to A serial number is required.
     *
     * @return A localized string similar to A serial number is required.
     */
    public static String getAPI_Validation_61020003() {
        return getResourceManager().getString("API_Validation_61020003");
    }

    /**
     * Looks up a localized string similar to An expiry date is required.
     *
     * @return A localized string similar to An expiry date is required.
     */
    public static String getAPI_Validation_61020004() {
        return getResourceManager().getString("API_Validation_61020004");
    }

    /**
     * Looks up a localized string similar to The batch identifier is invalid.
     *
     * @return A localized string similar to The batch identifier is invalid.
     */
    public static String getAPI_Validation_61020006() {
        return getResourceManager().getString("API_Validation_61020006");
    }

    /**
     * Looks up a localized string similar to The expiry date is invalid.
     *
     * @return A localized string similar to The expiry date is invalid.
     */
    public static String getAPI_Validation_61020007() {
        return getResourceManager().getString("API_Validation_61020007");
    }

    /**
     * Looks up a localized string similar to The product code is invalid.
     *
     * @return A localized string similar to The product code is invalid.
     */
    public static String getAPI_Validation_61020008() {
        return getResourceManager().getString("API_Validation_61020008");
    }

    /**
     * Looks up a localized string similar to The serial number is invalid.
     *
     * @return A localized string similar to The serial number is invalid.
     */
    public static String getAPI_Validation_61020011() {
        return getResourceManager().getString("API_Validation_61020011");
    }

    /**
     * Looks up a localized string similar to Duplicate serial numbers provided.
     * An alert has been raised.
     *
     * @return A localized string similar to Duplicate serial numbers provided.
     * An alert has been raised.
     */
    public static String getAPI_Validation_62120000() {
        return getResourceManager().getString("API_Validation_62120000");
    }

    /**
     * Looks up a localized string similar to The actual number of packs does
     * not match the stated number.
     *
     * @return A localized string similar to The actual number of packs does not
     * match the stated number.
     */
    public static String getAPI_Validation_62120001() {
        return getResourceManager().getString("API_Validation_62120001");
    }

    /**
     * Looks up a localized string similar to The request body is invalid.
     *
     * @return A localized string similar to The request body is invalid.
     */
    public static String getAPI_Validation_62120002() {
        return getResourceManager().getString("API_Validation_62120002");
    }

    /**
     * Looks up a localized string similar to Too many packs in bulk request. A
     * maximum of {0} packs will be accepted.
     *
     * @return A localized string similar to Too many packs in bulk request. A
     * maximum of {0} packs will be accepted.
     */
    public static String getAPI_Validation_62120003() {
        return getResourceManager().getString("API_Validation_62120003");
    }

    /**
     * Looks up a localized string similar to The actual number of packs does
     * not match the stated number.
     *
     * @return A localized string similar to The actual number of packs does not
     * match the stated number.
     */
    public static String getAPI_Validation_63120001() {
        return getResourceManager().getString("API_Validation_63120001");
    }

    /**
     * Looks up a localized string similar to The request body is invalid.
     *
     * @return A localized string similar to The request body is invalid.
     */
    public static String getAPI_Validation_63120002() {
        return getResourceManager().getString("API_Validation_63120002");
    }

    /**
     * Looks up a localized string similar to Too many packs in bulk request. A
     * maximum of {0} packs will be accepted.
     *
     * @return A localized string similar to Too many packs in bulk request. A
     * maximum of {0} packs will be accepted.
     */
    public static String getAPI_Validation_63120003() {
        return getResourceManager().getString("API_Validation_63120003");
    }

    /**
     * Looks up a localized string similar to An intermediate proxy received a
     * bad response from another proxy or the National System.
     *
     * @return A localized string similar to An intermediate proxy received a
     * bad response from another proxy or the National System.
     */
    public static String getApiClient_BadGatewayWarning() {
        return getResourceManager().getString("ApiClient_BadGatewayWarning");
    }

    /**
     * Looks up a localized string similar to Check the request URI.
     *
     * @return A localized string similar to Check the request URI.
     */
    public static String getApiClient_CheckUriMessage() {
        return getResourceManager().getString("ApiClient_CheckUriMessage");
    }

    /**
     * Looks up a localized string similar to The API Client is not started.
     *
     * @return A localized string similar to The API Client is not started.
     */
    public static String getApiClient_ClientNotStarted() {
        return getResourceManager().getString("ApiClient_ClientNotStarted");
    }

    /**
     * Looks up a localized string similar to An intermediate proxy timed out
     * while waiting for a response from another proxy or the National System.
     *
     * @return A localized string similar to An intermediate proxy timed out
     * while waiting for a response from another proxy or the National System.
     */
    public static String getApiClient_GatewayTimeoutWarning() {
        return getResourceManager().getString("ApiClient_GatewayTimeoutWarning");
    }

    /**
     * Looks up a localized string similar to A generic error occurred on the
     * National System.
     *
     * @return A localized string similar to A generic error occurred on the
     * National System.
     */
    public static String getApiClient_InternalServerErrorWarning() {
        return getResourceManager().getString("ApiClient_InternalServerErrorWarning");
    }

    /**
     * Looks up a localized string similar to No content returned from National
     * System.
     *
     * @return A localized string similar to No content returned from National
     * System.
     */
    public static String getApiClient_NoContentWarning() {
        return getResourceManager().getString("ApiClient_NoContentWarning");
    }

    /**
     * Looks up a localized string similar to No response returned from National
     * System.
     *
     * @return A localized string similar to No response returned from National
     * System.
     */
    public static String getApiClient_NoResponseWarning() {
        return getResourceManager().getString("ApiClient_NoResponseWarning");
    }

    /**
     * Looks up a localized string similar to The client is currently offline.
     *
     * @return A localized string similar to The client is currently offline.
     */
    public static String getApiClient_Offline() {
        return getResourceManager().getString("ApiClient_Offline");
    }

    /**
     * Looks up a localized string similar to The API client did not send the
     * record within the time the National System was expecting the record.
     *
     * @return A localized string similar to The API client did not send the
     * record within the time the National System was expecting the record.
     */
    public static String getApiClient_RequestTimeoutWarning() {
        return getResourceManager().getString("ApiClient_RequestTimeoutWarning");
    }

    /**
     * Looks up a localized string similar to The National System is temporarily
     * unavailable.
     *
     * @return A localized string similar to The National System is temporarily
     * unavailable.
     */
    public static String getApiClient_ServiceUnavailableWarning() {
        return getResourceManager().getString("ApiClient_ServiceUnavailableWarning");
    }

    /**
     * Looks up a localized string similar to The API client has sent too many
     * records in a given amount of time.
     *
     * @return A localized string similar to The API client has sent too many
     * records in a given amount of time.
     */
    public static String getApiClient_TooManyRequests() {
        return getResourceManager().getString("ApiClient_TooManyRequests");
    }

    /**
     * Looks up a localized string similar to The API client is not authorised
     * by the National System to access the required resource.
     *
     * @return A localized string similar to The API client is not authorised by
     * the National System to access the required resource.
     */
    public static String getApiClient_UnauthorizedWarning() {
        return getResourceManager().getString("ApiClient_UnauthorizedWarning");
    }

    /**
     * Looks up a localized string similar to The requested HTTP verb is not
     * supported.
     *
     * @return A localized string similar to The requested HTTP verb is not
     * supported.
     */
    public static String getApiClient_UnsupportedHttpVerb() {
        return getResourceManager().getString("ApiClient_UnsupportedHttpVerb");
    }

    /**
     * Looks up a localized string similar to The data management service cannot
     * be null.
     *
     * @return A localized string similar to The data management service cannot
     * be null.
     */
    public static String getDataTable_DataManagementServiceCannotBeNull() {
        return getResourceManager().getString("DataTable_DataManagementServiceCannoBeNull");
    }

    /**
     * Looks up a localized string similar to The data management service name
     * is invalid.
     *
     * @return A localized string similar to The data management service name is
     * invalid.
     */
    public static String getDataTable_DataManagementServiceNameInvalid() {
        return getResourceManager().getString("DataTable_DataManagementServiceNameInvalid");
    }

    /**
     * Looks up a localized string similar to The table name is invalid.
     *
     * @return A localized string similar to The table name is invalid.
     */
    public static String getDataTable_DataTableNameInvalid() {
        return getResourceManager().getString("DataTable_DataTableNameInvalid");
    }

    /**
     * Looks up a localized string similar to BulkRequest type must be {0}.
     *
     * @return A localized string similar to BulkRequest type must be {0}.
     */
    public static String getDataTable_RequestTypeException() {
        return getResourceManager().getString("DataTable_RequestTypeException");
    }

    /**
     * Looks up a localized string similar to ETA.
     *
     * @return A localized string similar to ETA.
     */
    public static String getEta() {
        return getResourceManager().getString("Eta");
    }

    /**
     * Looks up a localized string similar to Expires.
     *
     * @return A localized string similar to Expires.
     */
    public static String getExpires() {
        return getResourceManager().getString("Expires");
    }

    /**
     * Looks up a localized string similar to Information.
     *
     * @return A localized string similar to Information.
     */
    public static String getInformation() {
        return getResourceManager().getString("Information");
    }

    /**
     * Looks up a localized string similar to One or more errors occurred:.
     *
     * @return A localized string similar to One or more errors occurred:.
     */
    public static String getInterchangeException_BaseErrorListMessage() {
        return getResourceManager().getString("InterchangeException_BaseErrorListMessage");
    }

    /**
     * Looks up a localized string similar to A technical issue prevented
     * communication with the National System.
     *
     * @return A localized string similar to A technical issue prevented
     * communication with the National System.
     */
    public static String getInterchangeException_BaseMessage() {
        return getResourceManager().getString("InterchangeException_BaseMessage");
    }

    /**
     * Looks up a localized string similar to Failed to store the deferred
     * record. The record will not be forwarded to the National System when the
     * current problem is resolved.
     *
     * @return A localized string similar to Failed to store the deferred
     * record. The record will not be forwarded to the National System when the
     * current problem is resolved.
     */
    public static String getInterchangeException_DeferredRequestNotStored() {
        return getResourceManager().getString("InterchangeException_DeferredRequestNotStored");
    }

    /**
     * Looks up a localized string similar to Failed to store the deferred
     * record, and internal rollback failed. The record will not be forwarded to
     * the National System when the current problem is resolved.
     *
     * @return A localized string similar to Failed to store the deferred
     * record, and internal rollback failed. The record will not be forwarded to
     * the National System when the current problem is resolved.
     */
    public static String getInterchangeException_DeferredRequestNotStoredAndRollbackFailed() {
        return getResourceManager().getString("InterchangeException_DeferredRequestNotStoredAndRollbackFailed");
    }

    /**
     * Looks up a localized string similar to The records data management
     * service table cannot be null.
     *
     * @return A localized string similar to The records data management service
     * table cannot be null.
     */
    public static String getInterchangeException_NullRequestsTable() {
        return getResourceManager().getString("InterchangeException_NullRequestsTable");
    }

    /**
     * Looks up a localized string similar to The responses data management
     * service table cannot be null.
     *
     * @return A localized string similar to The responses data management
     * service table cannot be null.
     */
    public static String getInterchangeException_NullResponsesTable() {
        return getResourceManager().getString("InterchangeException_NullResponsesTable");
    }

    /**
     * Looks up a localized string similar to Duplicate serial numbers are not
     * allowed in bulk record.
     *
     * @return A localized string similar to Duplicate serial numbers are not
     * allowed in bulk record.
     */
    public static String getInvalidBulkRequest_DuplicatesNotAllowed() {
        return getResourceManager().getString("InvalidBulkRequest_DuplicatesNotAllowed");
    }

    /**
     * Looks up a localized string similar to No packs have been provided in
     * bulk record.
     *
     * @return A localized string similar to No packs have been provided in bulk
     * record.
     */
    public static String getInvalidBulkRequest_NoPacksProvided() {
        return getResourceManager().getString("InvalidBulkRequest_NoPacksProvided");
    }

    /**
     * Looks up a localized string similar to Too many packs in bulk record.
     * Maximum of {0} packs will be accepted.
     *
     * @return A localized string similar to Too many packs in bulk record.
     * Maximum of {0} packs will be accepted.
     */
    public static String getInvalidBulkRequest_TooManyPacks() {
        return getResourceManager().getString("InvalidBulkRequest_TooManyPacks");
    }

    /**
     * Looks up a localized string similar to Location.
     *
     * @return A localized string similar to Location.
     */
    public static String getLocation() {
        return getResourceManager().getString("Location");
    }

    /**
     * Looks up a localized string similar to API client instance started.
     *
     * @return A localized string similar to API client instance started.
     */
    public static String getLogging_ApiClientStarted() {
        return getResourceManager().getString("Logging_ApiClientStarted");
    }

    /**
     * Looks up a localized string similar to API client instance stopped.
     *
     * @return A localized string similar to API client instance stopped.
     */
    public static String getLogging_ApiClientStopped() {
        return getResourceManager().getString("Logging_ApiClientStopped");
    }

    /**
     * Looks up a localized string similar to Failed to record a deferred
     * request. The request cannot be stored and forwarded to the National
     * System.
     *
     * @return A localized string similar to Failed to record a deferred
     * request. The request cannot be stored and forwarded to the National
     * System.
     */
    public static String getLogging_DeferredRequestNotStored() {
        return getResourceManager().getString("Logging_DeferredRequestNotStored");
    }

    /**
     * Looks up a localized string similar to A recovery request failed. A
     * technical issue prevented communication with the National System. .
     *
     * @return A localized string similar to A recovery request failed. A
     * technical issue prevented communication with the National System. .
     */
    public static String getLogging_RecoveryRequestFailed() {
        return getResourceManager().getString("Logging_RecoveryRequestFailed");
    }

    /**
     * Looks up a localized string similar to A recovery results request failed.
     * A technical issue prevented communication with the National System.
     *
     * @return A localized string similar to A recovery results request failed.
     * A technical issue prevented communication with the National System.
     */
    public static String getLogging_RecoveryResultsRequestFailed() {
        return getResourceManager().getString("Logging_RecoveryResultsRequestFailed");
    }

    /**
     * Looks up a localized string similar to The API client failed to obtain a
     * security token from the National System.
     *
     * @return A localized string similar to The API client failed to obtain a
     * security token from the National System.
     */
    public static String getLogging_RequestBearerTokenFailed() {
        return getResourceManager().getString("Logging_RequestBearerTokenFailed");
    }

    /**
     * Looks up a localized string similar to The API client obtained a security
     * token from the National System.
     *
     * @return A localized string similar to The API client obtained a security
     * token from the National System.
     */
    public static String getLogging_RequestBearerTokenSucceeded() {
        return getResourceManager().getString("Logging_RequestBearerTokenSucceeded");
    }

    /**
     * Looks up a localized string similar to No. of Packs.
     *
     * @return A localized string similar to No. of Packs.
     */
    public static String getNoOfPacks() {
        return getResourceManager().getString("NoOfPacks");
    }

    /**
     * Looks up a localized string similar to Operation Code.
     *
     * @return A localized string similar to Operation Code.
     */
    public static String getOperationCode() {
        return getResourceManager().getString("OperationCode");
    }

    /**
     * Looks up a localized string similar to Pack State.
     *
     * @return A localized string similar to Pack State.
     */
    public static String getPackState() {
        return getResourceManager().getString("PackState");
    }

    /**
     * Looks up a localized string similar to The transaction conclearText must
     * be initialized over an instance of a resource manager.
     *
     * @return A localized string similar to The transaction conclearText must
     * be initialized over an instance of a resource manager.
     */
    public static String getTransactionManager_TContextInitializationError() {
        return getResourceManager().getString("TransactionManager_TContextInitializationError");
    }

    /**
     * Looks up a localized string similar to Only failed transactions can be
     * aborted. Current state is.
     *
     * @return A localized string similar to Only failed transactions can be
     * aborted. Current state is.
     */
    public static String getTransactionManagerError_NotFailedState() {
        return getResourceManager().getString("TransactionManagerError_NotFailedState");
    }

    /**
     * Looks up a localized string similar to Failed to store the current
     * record. The record will not be forwarded to the National System when the
     * current problem is resolved.
     *
     * @return A localized string similar to Failed to store the current record.
     * The record will not be forwarded to the National System when the current
     * problem is resolved.
     */
    public static String getTransactionManagerError_RequestNotStored() {
        return getResourceManager().getString("TransactionManagerError_RequestNotStored");
    }

    /**
     * Looks up a localized string similar to The resource manager failed to
     * roll back the transaction.
     *
     * @return A localized string similar to The resource manager failed to roll
     * back the transaction.
     */
    public static String getTransactionManagerError_RollBackFailed() {
        return getResourceManager().getString("TransactionManagerError_RollBackFailed");
    }

    /**
     * Looks up a localized string similar to Unexpected transaction state
     * reported by resource manager after recording a roll-back.
     *
     * @return A localized string similar to Unexpected transaction state
     * reported by resource manager after recording a roll-back.
     */
    public static String getTransactionManagerError_UnexpectedStateInRollBack() {
        return getResourceManager().getString("TransactionManagerError_UnexpectedStateInRollBack");
    }

    /**
     * Looks up a localized string similar to Only active transactions can be
     * committed. Current state is.
     *
     * @return A localized string similar to Only active transactions can be
     * committed. Current state is.
     */
    public static String getTrasactionManagerError_InvalidStateForCommit() {
        return getResourceManager().getString("TrasactionManagerError_InvalidStateForCommit");
    }

    /**
     * Looks up a localized string similar to UPRC.
     *
     * @return A localized string similar to UPRC.
     */
    public static String getUprc() {
        return getResourceManager().getString("Uprc");
    }

    /**
     * Looks up a localized string similar to A pack state of Active cannot, by
     * definition, be Decommissioned.
     *
     * @return A localized string similar to A pack state of Active cannot, by
     * definition, be Decommissioned.
     */
    public static String getVerificationResult_ActiveNotDecommissioned() {
        return getResourceManager().getString("VerificationResult_ActiveNotDecommissioned");
    }

    /**
     * Looks up a localized string similar to Warning.
     *
     * @return A localized string similar to Warning.
     */
    public static String getWarning() {
        return getResourceManager().getString("Warning");
    }
}
