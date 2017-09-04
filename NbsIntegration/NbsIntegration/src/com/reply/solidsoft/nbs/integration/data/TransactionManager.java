/**
 * -----------------------------------------------------------------------------
 * File=TransactionManager.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Manages transactions for a single resource manager.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data;

import com.reply.solidsoft.nbs.integration.data.model.TransactionState;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntry;
import com.reply.solidsoft.nbs.integration.logging.model.LogEntryType;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.time.Instant;
import java.util.UUID;
import com.reply.solidsoft.nbs.integration.logging.LoggingService;

/**
 * Manages transactions for a single resource manager.
 */
public class TransactionManager implements java.io.Closeable {

    /**
     * The resource manager.
     */
    private ResourceManager resourceManager;

    /**
     * The logging service.
     */
    private LoggingService loggingService;

    /**
     * Initializes a new instance of the TransactionManager class.
     *
     * @param manager The resource manager.
     */
    private TransactionManager(ResourceManager manager) {
        this(manager, null);
    }

    /**
     * Initializes a new instance of the TransactionManager class.
     *
     * @param manager The resource manager.
     * @param loggingService The logging service.
     */
    private TransactionManager(ResourceManager manager, LoggingService loggingService) {
        if (manager == null) {
            this.setState(TransactionState.TERMINATED);
            throw new IllegalArgumentException(Resources.getTransactionManager_TContextInitializationError());
        }

        this.resourceManager = manager;
        this.loggingService = loggingService;
        this.setState(TransactionState.ACTIVE);
    }

    /**
     * The current exception.
     */
    private Throwable exception;

    /**
     * Gets the current exception.
     *
     * @return The current exception.
     */
    public final Throwable getException() {
        return exception;
    }

    /**
     * Sets the current exception.
     *
     * @param The current exception.
     */
    private void setException(Throwable value) {
        exception = value;
    }

    /**
     * The current state of the transaction.
     */
    private TransactionState state = TransactionState.values()[0];

    /**
     * Gets the current state of the transaction.
     *
     * @return The current state of the transaction.
     */
    public final TransactionState getState() {
        return state;
    }

    /**
     * Sets the current state of the transaction.
     *
     * @param value The current state of the transaction.
     */
    private void setState(TransactionState value) {
        state = value;
    }

    /**
     * Creates a new instance of the Transaction manager
     *
     * @param manager The resource manager
     * @return An instance of the manager for a new transaction.
     */
    public static TransactionManager newTransaction(ResourceManager manager) {
        return newTransaction(manager, null);
    }

    /**
     * Creates a new instance of the Transaction manager
     *
     * @param manager The resource manager
     * @param loggingService The logging service.
     * @return An instance of the manager for a new transaction.
     */
    public static TransactionManager newTransaction(ResourceManager manager, LoggingService loggingService) {
        return new TransactionManager(manager, loggingService);
    }

    /**
     * Aborts the current transaction.
     */
    public final void abort() {
        switch (this.getState()) {
            case TERMINATED:
                return;
            case ABORTED:
                this.setState(TransactionState.TERMINATED);
                return;
        }

        this.setState(TransactionState.FAILED);
        this.rollback();
    }

    /**
     * Commits changes to the resource manager;
     *
     * @return True, if committed; otherwise false.
     */
    public final boolean commit() {
        if (this.getState() != TransactionState.ACTIVE) {
            // The resource manager has not rolled the transaction back, so throw an error.
            this.setException(new TransactionException(String.format("%1$s %2$s.", Resources.getTrasactionManagerError_InvalidStateForCommit(), this.getState())));
            // Log exception
            LogEntry logEntry = new LogEntry();
            logEntry.setEntryType(LogEntryType.ERROR);
            logEntry.setId(UUID.randomUUID().toString());
            logEntry.setMessage(this.getException().getMessage());
            logEntry.setSeverity(3);
            logEntry.setTime(Instant.now());

            if (this.loggingService != null) {
                com.reply.solidsoft.nbs.integration.extensions.functional.Action2 logAction = this.loggingService.getLog();
                logAction.invoke(this, logEntry);
            }

            return false;
        }

        this.setState(TransactionState.PARTIALLY_COMMITTED);

        // BulkRequest that the resource manager commits a transaction.
        try {
            this.resourceManager.commit(this);
        } catch (Throwable ex) {
            this.setState(TransactionState.FAILED);
            this.setException(ex);
            this.rollback();
            return false;
        }

        switch (this.getState()) {
            case FAILED:
                // The transaction has failed, so will be rolled back
                this.rollback();
                return false;
            case ABORTED:
                // The transaction has been aborted
                this.setState(TransactionState.TERMINATED);
                return false;
            case PARTIALLY_COMMITTED:
                this.setState(TransactionState.COMMITTED);
                break;
        }

        if (this.getState() == TransactionState.COMMITTED) {
            this.setState(TransactionState.TERMINATED);
        }

        if (this.getState() == TransactionState.TERMINATED) {
            return true;
        }

        this.setState(TransactionState.TERMINATED);

        // The resource manager has not committed the transaction back and not rolled it back, so throw an error.
        this.setException(new TransactionException(String.format("%1$s: %2$s.", Resources.getTransactionManagerError_UnexpectedStateInRollBack(), this.getState())));
        // Log exception
        LogEntry logEntry = new LogEntry();
        logEntry.setEntryType(LogEntryType.ERROR);
        logEntry.setId(UUID.randomUUID().toString());
        logEntry.setMessage(this.getException().getMessage());
        logEntry.setSeverity(3);
        logEntry.setTime(Instant.now());

        if (this.loggingService != null) {
            com.reply.solidsoft.nbs.integration.extensions.functional.Action2 logAction = this.loggingService.getLog();
            logAction.invoke(this, logEntry);
        }

        return false;
    }

    /**
     * Close the transaction.
     */
    @Override
    public final void close() throws java.io.IOException {
        if (this.getState() == TransactionState.PARTIALLY_COMMITTED) {
            try {
                if (!this.commit()) {
                    this.rollback();
                }
            } catch (Throwable ex) {
                // Log exception
                LogEntry logEntry = new LogEntry();
                logEntry.setEntryType(LogEntryType.ERROR);
                logEntry.setId(UUID.randomUUID().toString());
                logEntry.setMessage(ex.getMessage());
                logEntry.setSeverity(3);
                logEntry.setTime(Instant.now());

                if (this.loggingService != null) {
                    com.reply.solidsoft.nbs.integration.extensions.functional.Action2 logAction = this.loggingService.getLog();
                    logAction.invoke(this, logEntry);
                }
            }
        }

        this.setState(TransactionState.TERMINATED);

        if (this.resourceManager != null) {
            this.resourceManager.close();
        }
    }

    /**
     * Rolls back changes to the resource manager;
     */
    public final void rollback() {
        if (this.getState() != TransactionState.FAILED) {
            // If the transaction has already been aborted, treat this call as idempotent.
            if (this.getState() == TransactionState.ABORTED) {
                return;
            }

            this.setState(TransactionState.ABORTED);
        }

        try {
            this.resourceManager.rollback(this);

            if (this.getState() == TransactionState.FAILED) {
                this.setState(TransactionState.ABORTED);
            }

            if (this.getState() == TransactionState.ABORTED) {
                this.setState(TransactionState.TERMINATED);
            }

            if (this.getState() == TransactionState.TERMINATED) {
                return;
            }

            this.setState(TransactionState.TERMINATED);

            // The resource manager has not rolled the transaction back, so throw an error.
            this.setException(new TransactionException(String.format("%1$s: %2$s.", Resources.getTransactionManagerError_UnexpectedStateInRollBack(), this.getState())));
            throw this.getException();
        } catch (Throwable ex) {
            this.setState(TransactionState.TERMINATED);

            // The resource manager has not rolled the transaction back, so throw an error.
            this.setException(new Exception(String.format("%1$s", Resources.getTransactionManagerError_RollBackFailed()), ex));
            // Log exception
            LogEntry logEntry = new LogEntry();
            logEntry.setEntryType(LogEntryType.ERROR);
            logEntry.setId(UUID.randomUUID().toString());
            logEntry.setMessage(ex.getMessage());
            logEntry.setSeverity(3);
            logEntry.setTime(Instant.now());

            if (this.loggingService != null) {
                com.reply.solidsoft.nbs.integration.extensions.functional.Action2 logAction = this.loggingService.getLog();
                logAction.invoke(this, logEntry);
            }
        }
    }
}
