/**
 * -----------------------------------------------------------------------------
 * File=LoggingService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file request store.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.loggingservice;

import iBoxDB.LocalServer.*;
import com.reply.solidsoft.nbs.integration.data.model.*;
import com.reply.solidsoft.nbs.integration.logging.*;
import com.reply.solidsoft.nbs.integration.logging.model.*;
import com.reply.solidsoft.nbs.integration.extensions.functional.Logger;
import java.util.*;

/**
 * Implements a transactional file request store.
 */
public class SampleLoggingService implements LoggingService {

    /**
     * Lock on the service.
     */
    public static final Object LOCK_SERVICE = new Object();

    /**
     * Initializes a new instance of the LoggingService class.
     */
    public SampleLoggingService() {
        // Initialize the data management service for this application
        this.setName("apiclientLog");
        this.setTables(new HashMap<>());
        this.getTables().put("log", new LogDataTable(this, "log"));
        this.setTransactionLog(null);
    }

    /**
     * The iBoxDB database.
     */
    private static AutoBox iboxDb;

    /**
     * Gets the iBoxDB database.
     *
     * @return The iBoxDB database.
     */
    public static AutoBox getIboxDb() {
        return iboxDb;
    }

    /**
     * Sets the iBoxDB database.
     *
     * @param value The iBoxDB database.
     */
    public static void setIboxDb(AutoBox value) {
        iboxDb = value;
    }

    /**
     * The name of the service.
     */
    private String name;

    /**
     * Gets the name of the service.
     *
     * @return the name of the service.
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * Sets the name of the service.
     *
     * @param value the name of the service.
     */
    private void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the current user.
     *
     * @return The current user.
     */
    @Override
    public final String getCurrentUser() {
        return System.getProperty("user.name");
    }

    /**
     * Gets an event handler for logging entries.
     *
     * @return An event handler for logging entries.
     */
    @Override
    public final Logger<Object, LogEntry> getLog() {
        return (o, le) -> this.getTables().get("log").add(le);
    }

    /**
     * Gets the current log entries.
     *
     * @return The current log entries.
     */
    @Override
    public final List<LogEntry> getLogEntries() {
        return ((TypedDataTable<LogEntry>) this.getTables().get("log")).getRecords();
    }

    /**
     * The collection of data management service tables.
     */
    private Map<String, DataTable> tables;

    /**
     * Gets the collection of data management service tables.
     *
     * @return the collection of data management service tables.
     */
    @Override
    public final Map<String, DataTable> getTables() {
        return this.tables;
    }

    /**
     * Sets the collection of data management service tables.
     *
     * @param value the collection of data management service tables.
     */
    private void setTables(Map<String, DataTable> value) {
        this.tables = value;
    }

    /**
     * The transaction log table table.
     */
    private DataTable transactionLog;

    /**
     * Gets the transaction log table table.
     *
     * @return The transaction log table table.
     */
    @Override
    public final DataTable getTransactionLog() {
        return transactionLog;
    }

    /**
     * Sets the transaction log table table.
     *
     * @param value The transaction log table table.
     */
    private void setTransactionLog(DataTable value) {
        this.transactionLog = value;
    }

    /**
     * Closes the logging service.
     */
    @Override
    public final void close() {
    }
}
