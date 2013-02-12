/*******************************************************************************
 * Copyright (c) 2012 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb.mongodb;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.importdb.Activator;
import com.hangum.tadpole.importdb.core.dialog.importdb.QueryUtil;
import com.hangum.tadpole.importdb.core.dialog.importdb.dao.ModTableDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * RDB Table data to Mongodb 
 * 
 * @author hangum
 *
 */
public class RDBTableToMongoDBImport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBTableToMongoDBImport.class);
	
	private UserDBDAO importUserDB;
	private List<ModTableDAO> listModeTable; 
	private UserDBDAO exportUserDB;
	
	public RDBTableToMongoDBImport(UserDBDAO importUserDB, List<ModTableDAO> listModeTable, UserDBDAO exportUserDB) {
		this.importUserDB = importUserDB;
		this.listModeTable = listModeTable;
		this.exportUserDB = exportUserDB;
	}

	/**
	 * table import
	 */
	public Job workTableImport() {
		if(0 == listModeTable.size()) {
			MessageDialog.openInformation(null, "Confirm", "Please select table");
			return null;
		}
		
		// job
		Job job = new Job("Execute data Import.") {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Start import....", IProgressMonitor.UNKNOWN);
				
				try {
					for (ModTableDAO modTableDAO : listModeTable) {
						monitor.subTask(modTableDAO.getName() + " importing...");
						
						insertMongoDB(modTableDAO, exportUserDB);
					}			

				} catch(Exception e) {
					logger.error("press ok button", e);						
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		return job;
	}
	
	/**
	 * 데이터를 입력합니다.
	 * 
	 * @param modTableDAO
	 * @param userDBDAO
	 * @throws Exception
	 */
	private void insertMongoDB(ModTableDAO modTableDAO, UserDBDAO userDBDAO) throws Exception {
		String workTable = modTableDAO.getName();		
		if(logger.isDebugEnabled()) logger.debug("[work table]" + workTable);			
		
		QueryUtil qu = new QueryUtil(userDBDAO, "SELECT * FROM " + workTable);
		while(qu.hasNext()) {
			qu.nextQuery();
			HashMap<Integer, String> mapCNameToIndex = qu.getMapColumns();
			List<HashMap<Integer, Object>> listTableData = qu.getTableDataList();
			
			// row 단위
			DBObject[] arrayDBObject = new DBObject[listTableData.size()];
			for (int i=0; i<listTableData.size(); i++) {
				HashMap<Integer, Object> resultMap = listTableData.get(i);					
				
				DBObject insertObject = new BasicDBObject();
				// column 단위
				for(int j=0; j<mapCNameToIndex.size(); j++) {
					// BigDecimal is not support mongodb 
					if(resultMap.get(j) instanceof java.math.BigDecimal) {
						BigDecimal db = (BigDecimal)resultMap.get(j);
						insertObject.put(mapCNameToIndex.get(j), db.longValue());	
					} else {
						insertObject.put(mapCNameToIndex.get(j), resultMap.get(j));
					}						
				}
				
				arrayDBObject[i] = insertObject;
			}	// end for
			logger.debug("[work table]" + workTable + " size is " + arrayDBObject.length);
		
			MongoDBQuery.insertDocument(importUserDB, workTable, arrayDBObject);
		}
	}
}
