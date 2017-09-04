/**
 * -----------------------------------------------------------------------------
 * File=DataResources.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Generic resources (British English).
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.properties;

import java.util.ListResourceBundle;

/**
 * Generic resources.
 */
public class DataResources extends ListResourceBundle {

    /**
     * Gets the CONTENTS of the generic resources.
     *
     * @return The CONTENTS of the generic resources.
     */
    @Override
    public Object[][] getContents() {
        return CONTENTS;
    }
    static final Object[][] CONTENTS = {
        // LOCALIZE THIS
        {"DataTable_DataManagementServiceCannotBeNull", "The data management service cannot be null."},
        {"DataTable_DataManagementServiceNameInvalid", "The data management service name is invalid."},
        {"DataTable_DataTableNameInvalid", "The table name is invalid."},
        {"DataTable_RequestTypeException", "BulkRequest type must be {0}."},
        {"TransactionManager_TContextInitializationError", "The transaction conclearText must be initialized over an instance of a resource manager."}
    // END OF MATERIAL TO LOCALIZE
    };
}
