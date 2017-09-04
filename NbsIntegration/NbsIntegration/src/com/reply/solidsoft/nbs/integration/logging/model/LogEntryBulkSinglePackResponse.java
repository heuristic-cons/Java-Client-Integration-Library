/**
 * -----------------------------------------------------------------------------
 * File=LogEntryBulkSinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Verification response for a single pack within a bulk of packs.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.model.responses.BulkSinglePackResponse;

/**
 * Verification response for a single pack within a bulk of packs.
 */
public class LogEntryBulkSinglePackResponse implements NbsIntegrationLogEntryBulkSinglePackResponse {

    /**
     * Initializes a new instance of the LogEntryBulkSinglePackResponse class.
     */
    public LogEntryBulkSinglePackResponse() {
    }

    /**
     * Initializes a new instance of the LogEntryBulkSinglePackResponse class.
     *
     * @param response The response.
     */
    public LogEntryBulkSinglePackResponse(BulkSinglePackResponse response) {
        this.setPack(new LogEntryPackIdentifier(response.getPack()));
        this.setResult(new LogEntrySinglePackResponse(response.getResult()));
    }

    /**
     * The pack to be verified.
     */
    private NbsIntegrationLogEntryPackIdentifier pack;

    /**
     * Gets the pack to be verified.
     *
     * @return The pack to be verified.
     */
    @Override
    public final NbsIntegrationLogEntryPackIdentifier getPack() {
        return pack;
    }

    /**
     * Sets the pack to be verified.
     *
     * @param value The pack to be verified.
     */
    @Override
    public final void setPack(NbsIntegrationLogEntryPackIdentifier value) {
        pack = value;
    }

    /**
     * The response for the verification of the single pack.
     */
    private NbsIntegrationLogEntrySinglePackResponse result;

    /**
     * Gets the response for the verification of the single pack.
     *
     * @return The response for the verification of the single pack.
     */
    @Override
    public final NbsIntegrationLogEntrySinglePackResponse getResult() {
        return result;
    }

    /**
     * Sets the response for the verification of the single pack.
     *
     * @param value The response for the verification of the single pack.
     */
    @Override
    public final void setResult(NbsIntegrationLogEntrySinglePackResponse value) {
        result = value;
    }
}
