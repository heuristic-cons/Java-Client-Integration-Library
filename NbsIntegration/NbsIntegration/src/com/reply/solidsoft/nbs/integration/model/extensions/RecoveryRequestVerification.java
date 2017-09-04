/**
 * -----------------------------------------------------------------------------
 * File=RecoveryRequestVerification.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Extension methods for recovery requests.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

import com.reply.solidsoft.nbs.integration.model.responses.LocalValidationResponse;
import com.reply.solidsoft.nbs.integration.model.requests.RecoveryRequest;
import com.reply.solidsoft.nbs.integration.properties.Resources;

/**
 * Extension methods for recovery requests.
 */
public final class RecoveryRequestVerification {

    /**
     * Performs the validations of a bulk request.
     *
     * @param recoveryRequest The extended bulk request.
     * @param maxBulkPackCount The maximum number of packs allowed in a bulk
     * request.
     * @return A bulk request validation response record.
     */
    public static LocalValidationResponse validate(RecoveryRequest recoveryRequest, int maxBulkPackCount) {
        LocalValidationResponse okayResponse = new LocalValidationResponse();

        // Rule 1: The stated number of packs must equal the actual number of packs.
        if (recoveryRequest.getNumberOfPacks() != recoveryRequest.getPacks().length) {
            // Invalid
            LocalValidationResponse tempVar = new LocalValidationResponse();
            tempVar.setOperationCode(62120001);
            tempVar.setWarning(Resources.getAPI_Validation_63120001());
            return tempVar;
        }

        // Rule 2: There must be at least one pack in the request.
        if (recoveryRequest.getPacks().length <= 0) {
            // Invalid
            LocalValidationResponse tempVar2 = new LocalValidationResponse();
            tempVar2.setOperationCode(62120002);
            tempVar2.setWarning(Resources.getAPI_Validation_63120002());
            return tempVar2;
        }

        // Rule 3: The number of packs in the collection is greater than the maximum allowed.
        if (recoveryRequest.getPacks().length > maxBulkPackCount) {
            // Invalid
            LocalValidationResponse tempVar3 = new LocalValidationResponse();
            tempVar3.setOperationCode(62120003);
            tempVar3.setWarning(String.format(Resources.getAPI_Validation_63120003(), maxBulkPackCount));
            return tempVar3;
        }

        return okayResponse;
    }
}
