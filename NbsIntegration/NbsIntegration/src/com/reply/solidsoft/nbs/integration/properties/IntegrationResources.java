/**
 * -----------------------------------------------------------------------------
 * File=IntegrationResources.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Generic resources (British English).
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.properties;

import java.util.ListResourceBundle;

/**
 * Generic resources.
 */
public class IntegrationResources extends ListResourceBundle {

    /**
     * Gets the contents of the generic resources.
     *
     * @return The contents of the generic resources.
     */
    public Object[][] getContents() {
        return contents;
    }
    static final Object[][] contents = {
        // LOCALIZE THIS
        {"API_Validation_61020000", "A batch identifier is required."},
        {"API_Validation_61020001", "A product code is required."},
        {"API_Validation_61020002", "A product code scheme is required."},
        {"API_Validation_61020003", "A serial number is required."},
        {"API_Validation_61020004", "An expiry date is required."},
        {"API_Validation_61020006", "The batch identifier is invalid."},
        {"API_Validation_61020007", "The expiry date is invalid."},
        {"API_Validation_61020008", "The product code is invalid."},
        {"API_Validation_61020011", "The serial number is invalid."},
        {"API_Validation_62120000", "Duplicate serial numbers provided.  An alert has been raised."},
        {"API_Validation_62120001", "The actual number of packs does not match the stated number."},
        {"API_Validation_62120002", "The request body is invalid."},
        {"API_Validation_62120003", "Too many packs in bulk request.  A maximum of {0} packs will be accepted."},
        {"API_Validation_63120001", "The actual number of packs does not match the stated number."},
        {"API_Validation_63120002", "The request body is invalid."},
        {"API_Validation_63120003", "Too many packs in bulk request.  A maximum of {0} packs will be accepted."},
        {"ApiClient_BadGatewayWarning", "An intermediate proxy received a bad response from another proxy or the National System."},
        {"ApiClient_CheckUriMessage", "Check the request URI."},
        {"ApiClient_ClientNotStarted", "The API Client is not started."},
        {"ApiClient_GatewayTimeoutWarning", "An intermediate proxy timed out while waiting for a response from another proxy or the National System."},
        {"ApiClient_InternalServerErrorWarning", "A generic error occurred on the National System."},
        {"ApiClient_NoContentWarning", "No content returned from National System."},
        {"ApiClient_NoResponseWarning", "No response returned from National System."},
        {"ApiClient_Offline", "The client is currently offline."},
        {"ApiClient_RequestTimeoutWarning", "The API client did not send the record within the time the National System was expecting the record."},
        {"ApiClient_ServiceUnavailableWarning", "The National System is temporarily unavailable."},
        {"ApiClient_TooManyRequests", "The API client has sent too many records in a given amount of time."},
        {"ApiClient_UnauthorizedWarning", "The API client is not authorised by the National System to access the required resource."},
        {"ApiClient_UnsupportedHttpVerb", "The requested HTTP verb is not supported."},
        {"DataTable_DataManagementServiceCannotBeNull", "The data management service cannot be null."},
        {"DataTable_DataManagementServiceNameInvalid", "The data management service name is invalid."},
        {"DataTable_DataTableNameInvalid", "The table name is invalid."},
        {"DataTable_RequestTypeException", "BulkRequest type must be {0}."},
        {"Eta", "ETA"},
        {"Expires", "Expires"},
        {"Information", "Information"},
        {"InterchangeException_BaseErrorListMessage", "One or more errors occurred:"},
        {"InterchangeException_BaseMessage", "A technical issue prevented communication with the National System."},
        {"InterchangeException_DeferredRequestNotStored", "Failed to store the deferred record.  The record will not be forwarded to the National System when the current problem is resolved."},
        {"InterchangeException_DeferredRequestNotStoredAndRollbackFailed", "Failed to store the deferred record, and internal rollback failed.  The record will not be forwarded to the National System when the current problem is resolved."},
        {"InterchangeException_NullRequestsTable", "The records data management service table cannot be null."},
        {"InterchangeException_NullResponsesTable", "The responses data management service table cannot be null."},
        {"InvalidBulkRequest_DuplicatesNotAllowed", "Duplicate serial numbers are not allowed in bulk record."},
        {"InvalidBulkRequest_NoPacksProvided", "No packs have been provided in bulk record."},
        {"InvalidBulkRequest_TooManyPacks", "Too many packs in bulk record. Maximum of {0} packs will be accepted."},
        {"Location", "Location"},
        {"Logging_ApiClientStarted", "API client instance started."},
        {"Logging_ApiClientStopped", "API client instance stopped."},
        {"Logging_DeferredRequestNotStored", "Failed to record a deferred request.  The request cannot be stored and forwarded to the National System."},
        {"Logging_RecoveryRequestFailed", "A recovery request failed.  A technical issue prevented communication with the National System. "},
        {"Logging_RecoveryResultsRequestFailed", "A recovery results request failed.  A technical issue prevented communication with the National System."},
        {"Logging_RequestBearerTokenFailed", "The API client failed to obtain a security token from the National System."},
        {"Logging_RequestBearerTokenSucceeded", "The API client obtained a security token from the National System."},
        {"NoOfPacks", "No. of Packs"},
        {"OperationCode", "Operation Code"},
        {"PackState", "Pack State"},
        {"TransactionManager_TContextInitializationError", "The transaction conclearText must be initialized over an instance of a resource manager."},
        {"TransactionManagerError_NotFailedState", "Only failed transactions can be aborted.  Current state is"},
        {"TransactionManagerError_RequestNotStored", "Failed to store the current record.  The record will not be forwarded to the National System when the current problem is resolved."},
        {"TransactionManagerError_RollBackFailed", "The resource manager failed to roll back the transaction."},
        {"TransactionManagerError_UnexpectedStateInRollBack", "Unexpected transaction state reported by resource manager after recording a roll-back"},
        {"TrasactionManagerError_InvalidStateForCommit", "Only active transactions can be committed.  Current state is"},
        {"Uprc", "UPRC"},
        {"VerificationResult_ActiveNotDecommissioned", "A pack state of Active cannot, by definition, be Decommissioned."},
        {"Warning", "Warning"}
    // END OF MATERIAL TO LOCALIZE
    };
}
