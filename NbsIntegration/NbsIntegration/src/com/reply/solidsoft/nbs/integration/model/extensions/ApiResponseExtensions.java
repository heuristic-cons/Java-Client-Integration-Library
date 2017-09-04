/**
 * -----------------------------------------------------------------------------
 * File=ApiResponseExtensions.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Extension methods for API-related types.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

import com.reply.solidsoft.nbs.integration.model.ReportedPackState;

/**
 * Extension methods for API-related types.
 */
public final class ApiResponseExtensions {

    /**
     * Converts a string state to a reported pack state.
     *
     * @param state The string state to be converted.
     * @return A reported pack state.
     */
    public static ReportedPackState convertToPackState(String state) {
        switch (state == null ? "" : state.toLowerCase()) {
            case "active":
                return ReportedPackState.ACTIVE;
            case "supplied":
                return ReportedPackState.SUPPLIED;
            case "stolen":
                return ReportedPackState.STOLEN;
            case "destroyed":
                return ReportedPackState.DESTROYED;
            case "sample":
                return ReportedPackState.SAMPLE;
            case "freesample":
            case "free sample":
                return ReportedPackState.FREE_SAMPLE;
            case "exported":
                return ReportedPackState.EXPORTED;
            case "locked":
                return ReportedPackState.LOCKED;
            case "checkedout":
            case "checked-out":
                return ReportedPackState.CHECKED_OUT;
            case "recalled":
                return ReportedPackState.RECALLED;
            case "withdrawn":
                return ReportedPackState.WITHDRAWN;
            case "expired":
                return ReportedPackState.EXPIRED;
            case "notfound":
            case "not found":
                return ReportedPackState.NOT_FOUND;
            default:
                return ReportedPackState.NONE;
        }
    }
}
