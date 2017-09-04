/**
 * -----------------------------------------------------------------------------
 * File=StoreAndForwardService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file request store.
 * -----------------------------------------------------------------------------
 */
 package com.reply.solidsoft.nbs.integration.sample.dataservice;

import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RequestTransactionLogEntry;
import iBoxDB.LocalServer.AutoBox;
import iBoxDB.LocalServer.DB;
import com.reply.solidsoft.nbs.integration.recovery.BaseStoreAndForwardService;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RecoverySinglePackResponseDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.PackIdentifierDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.RequestedPackStateDb;
import com.reply.solidsoft.nbs.integration.sample.dataservice.model.SinglePackResponseDb;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/** 
 *   Implements a transactional file request store.
*/
public class SampleStoreAndForwardService extends BaseStoreAndForwardService
{
	/** 
	 *   Lock on the requests table.
	*/
	public static final Map<String, Object> TABLE_LOCKS = new HashMap<String, Object>();
	static
	{
		TABLE_LOCKS.put("requests", new Object());
		TABLE_LOCKS.put("responses", new Object());
		TABLE_LOCKS.put("transactions", new Object());
	}

	/** 
	 *   Initializes a new instance of the StoreAndForwardService class.
	*/
        @SuppressWarnings("LeakingThisInConstructor")
	public SampleStoreAndForwardService()
	{
		super("storeAndForward", new RequestsTable(), new ResponsesTable());
		// Initialize the data management service tables
		((RequestsTable)this.getRequests()).setDataManagementService(this);
		((ResponsesTable)this.getResponses()).setDataManagementService(this);

		// Create the transaction log
		this.transactionLog = new BaseDataTable<>(this, "transactions", RequestTransactionLogEntry.class);

                String directory = System.getProperty("user.dir");
		DB.root(String.format("%1$s\\", directory));
		DB database = new DB(100);
		String responsesTableName = ((ResponsesTable)this.getResponses()).getName();
		database.getConfig().ensureTable(DeferredRequest.class, ((RequestsTable)this.getRequests()).getName(), "TimeStamp");
		database.getConfig().ensureTable(RecoverySinglePackResponseDb.class, responsesTableName, "TimeStamp");
		database.getConfig().ensureTable(PackIdentifierDb.class, String.format("%1$s%2$s", responsesTableName, "PackIdentifier"), "TimeStamp");
		database.getConfig().ensureTable(RequestedPackStateDb.class, String.format("%1$s%2$s", responsesTableName, "RequestedPackState"), "TimeStamp");
		database.getConfig().ensureTable(SinglePackResponseDb.class, String.format("%1$s%2$s", responsesTableName, "SinglePackResponse"), "TimeStamp");
		database.getConfig().ensureTable(RequestTransactionLogEntry.class, "transactions", "TimeStamp");
		database.getConfig().ensureIndex(RequestTransactionLogEntry.class, "transactions", "RequestTimeStamp");
		setIboxDb(database.open());
	}

	/** 
	 *   The iBoxDB database.
	*/
	private static AutoBox iboxDb;
	
	/** 
	 *   Gets or sets the iBoxDB database.
	 *   @return The iBoxDB database.
	*/
	public static AutoBox getIboxDb()
	{
		return iboxDb;
	}
	
	/** 
	 *   Sets the iBoxDB database.
	 *   @param value The iBoxDB database.
	*/
	public static void setIboxDb(AutoBox value)
	{
		iboxDb = value;
	}

	/** 
	 *   The transaction log table table.
	*/
	private final DataTable transactionLog;
	
	/** 
	 *   Gets the transaction log table table.
	 *   @return The transaction log table table.
	*/
	@Override
	public DataTable getTransactionLog()
	{
		return this.transactionLog;
	}
        
        /**
         * Record the acknowledgement of receipt of a list of deferred requests by the national system.
         *
         * @param requests The list of deferred requests.
         */
        @Override
        public void acknowledgeRequest(List<DeferredRequest> requests)
        {
            ((RequestsTable)this.getRequests()).acknowledgeRequest(requests);
        }
}