/**
 * -----------------------------------------------------------------------------
 * File=ILoggingService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a logging service.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging;

import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.extensions.functional.Logger;
import java.util.List;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;

/**
 * Represents a logging service.
 */
public interface LoggingService extends DataManagementService {

    /**
     * Gets the current user.
     * <p>
     * It is the responsibility of any concrete implementation of a logging
     * service to provide the name or identifier of the current user, as
     * represented in the specific environment in which the integration library
     * is being used. The property is immutable.
     *
     * @return the name of the current user.
     */
    public String getCurrentUser();

    /**
     * Gets an action that is used by the API client to perform logging.
     *
     * @return An action that handles logging.
     */
    public Logger<Object, LogEntry> getLog();

    /**
     * Gets the collection of log entries.
     *
     * @return A collection of log entries.
     */
    public List<LogEntry> getLogEntries();
}
