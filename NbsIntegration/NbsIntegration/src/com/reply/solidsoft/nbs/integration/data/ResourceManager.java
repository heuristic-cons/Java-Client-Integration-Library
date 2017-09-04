/**
 * -----------------------------------------------------------------------------
 * File=IResourceManager.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a transaction-supporting resource manager (e.g., a data store).
 * This interface forces concrete implementations to be disposable.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data;

/**
 * Represents a transaction-supporting resource manager (e.g., a data store).
 * This interface forces concrete implementations to be disposable.
 */
public interface ResourceManager extends java.io.Closeable {

    /**
     * Commits changes to the store.
     */
    public void commit();

    /**
     * Commits changes to the store.
     *
     * @param context The optional transaction context for persisting the
     * request.
     */
    public void commit(TransactionManager context);

    /**
     * Rolls back changes to the store.
     */
    public void rollback();

    /**
     * Rolls back changes to the store.
     *
     * @param context The optional transaction context for persisting the
     * request.
     */
    public void rollback(TransactionManager context);
}
