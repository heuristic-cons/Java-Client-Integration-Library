/**
 * -----------------------------------------------------------------------------
 * File=BulkRequestVerification.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Extension methods for bulk requests.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.requests.BulkRequest;
import com.reply.solidsoft.nbs.integration.model.responses.LocalValidationResponse;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extension methods for bulk requests.
 */
public final class BulkRequestVerification {

    /**
     * Performs the validations of a bulk request.
     *
     * @param bulkRequest The extended bulk request.
     * @param maxBulkPackCount The maximum number of packs allowed in a bulk
     * request.
     * @return A bulk request validation response record.
     */
    public static LocalValidationResponse validate(BulkRequest bulkRequest, int maxBulkPackCount) {
        LocalValidationResponse okayResponse = new LocalValidationResponse();

        // Rule 1: No duplicate serial numbers allowed for any one product.
        Map<List<String>, List<PackIdentifier>> map
                = Arrays.asList(bulkRequest.getPacks())
                        .stream()
                        .collect(Collectors.groupingBy(
                                p -> Arrays.asList(
                                        p.getProductCodeScheme(),
                                        p.getProductCode(),
                                        p.getSerialNumber()),
                                Collectors.toList()));
        if (map.entrySet().stream().anyMatch(e -> e.getValue().size() > 1)) {
            // Invalid
            LocalValidationResponse tempVar = new LocalValidationResponse();
            tempVar.setOperationCode(62120000);
            tempVar.setWarning(Resources.getAPI_Validation_62120000());
            return tempVar;
        }

        // Rule 2: The stated number of packs must equal the actual number of packs.
        if (bulkRequest.getNumberOfPacks() != bulkRequest.getPacks().length) {
            // Invalid
            LocalValidationResponse tempVar2 = new LocalValidationResponse();
            tempVar2.setOperationCode(62120001);
            tempVar2.setWarning(Resources.getAPI_Validation_62120001());
            return tempVar2;
        }

        // Rule 3: There must be at least one pack in the request.
        if (bulkRequest.getPacks().length <= 0) {
            // Invalid
            LocalValidationResponse tempVar3 = new LocalValidationResponse();
            tempVar3.setOperationCode(62120002);
            tempVar3.setWarning(Resources.getAPI_Validation_62120002());
            return tempVar3;
        }

        // Rule 4: The number of packs in the collection is greater than the maximum allowed.
        if (bulkRequest.getPacks().length > maxBulkPackCount) {
            // Invalid
            LocalValidationResponse tempVar4 = new LocalValidationResponse();
            tempVar4.setOperationCode(62120003);
            tempVar4.setWarning(String.format(Resources.getAPI_Validation_62120003(), maxBulkPackCount));
            return tempVar4;
        }

        return okayResponse;
    }
}
