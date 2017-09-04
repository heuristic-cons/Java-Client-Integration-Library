/**
 * -----------------------------------------------------------------------------
 * File=ClientCredentialsResources.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Generic resources (British English).
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.credentialsservice.properties;

import java.util.ListResourceBundle;

/**
 * Generic resources.
 */
public class ClientCredentialsResources extends ListResourceBundle {

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
        {"DataTable_DataManagementServiceCannotBeNull", "The data management service cannot be null."},
        {"DataTable_DataManagementServiceNameInvalid", "The data management service name is invalid."},
        {"DataTable_DataTableNameInvalid", "The table name is invalid."},
        {"DataTable_RequestTypeException", "BulkRequest type must be {0}."},
        {"TransactionManager_TContextInitializationError", "The transaction conclearText must be initialized over an instance of a resource manager."}
    // END OF MATERIAL TO LOCALIZE
    };
}
