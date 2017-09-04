/**
 * -----------------------------------------------------------------------------
 * File=RecoveryRequest.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A bulk-of-pack request for recovery.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.requests;

/**
 * A bulk-of-pack request for recovery.
 */
public class RecoveryRequest {

    /**
     * The claim of the number of packs contained in the request.
     *
     * The EMVO requires this count in order to provide an additional check.
     */
    private int numberOfPacks;

    /**
     * Gets the claim of the number of packs contained in the request.
     *
     * @return The claim of the number of packs contained in the request.
     */
    public final int getNumberOfPacks() {
        return numberOfPacks;
    }

    /**
     * Sets the claim of the number of packs contained in the request.
     *
     * @param value The claim of the number of packs contained in the request.
     */
    public final void setNumberOfPacks(int value) {
        numberOfPacks = value;
    }

    /**
     * The list of packs contained in the request.
     *
     * Recovery is not a mandated EMVO requirement for blueprint systems. The
     * EMVO has indicated that it has no objection to this addition recovery
     * feature as long as it is clearly marked as being for recovery only. We
     * therefore use a different route to bulk-of-pack requests to distinguish
     * between normal bulk-of-pack and recovery.
     */
    private PackCommand[] packs;

    /**
     * Gets the list of packs contained in the request.
     *
     * @return The list of packs contained in the request.
     */
    public final PackCommand[] getPacks() {
        return packs;
    }

    /**
     * Sets the list of packs contained in the request.
     *
     * @param value The list of packs contained in the request.
     */
    public final void setPacks(PackCommand[] value) {
        packs = value;
    }
}
