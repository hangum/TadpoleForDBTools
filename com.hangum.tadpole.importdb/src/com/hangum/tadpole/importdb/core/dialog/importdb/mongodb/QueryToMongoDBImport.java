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
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * query를 몽고디비로 임포트합니다.
 * 
 * @author hangum
 *
 */
public class QueryToMongoDBImport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(QueryToMongoDBImport.class);
	
	private UserDBDAO importUserDB;
	private String colName;
	private String userQuery; 
	private UserDBDAO exportUserDB;
	
	public QueryToMongoDBImport(UserDBDAO importUserDB, String colName,  String userQuery, UserDBDAO exportUserDB) {
		this.importUserDB = importUserDB;
		this.colName = colName;
		this.userQuery = userQuery;
		this.exportUserDB = exportUserDB;
	}
	
	/**
	 * table import
	 */
	public Job workTableImport() {
		if("".equals(userQuery.trim())) {
			MessageDialog.openInformation(null, "Confirm", "Please input the query.");			
			return null;		
		}
		if("".equals(colName.trim())) {
			MessageDialog.openInformation(null, "Confirm", "Please input the collection name.");			
			return null;		
		}
		
		// job
		Job job = new Job("Execute data Import.") {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Start import....", IProgressMonitor.UNKNOWN);
				
				try {
					monitor.subTask(userQuery + " importing...");
					insertMongoDB();
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
	private void insertMongoDB() throws Exception {
		QueryUtil qu = new QueryUtil(exportUserDB, userQuery);
		while(qu.hasNext()) {
			qu.nextQuery();
			HashMap<Integer, String> mapCNameToIndex = qu.getMapColumns();
			List<HashMap<Integer, Object>> listTableData = qu.getTableDataList();
			
			// row 단위
			for (HashMap<Integer, Object> resultMap : listTableData) {
				
				DBObject insertObject = new BasicDBObject();
				// column 단위
				for(int i=0; i<mapCNameToIndex.size(); i++) {
					// BigDecimal is not support mongodb 
					if(resultMap.get(i) instanceof java.math.BigDecimal) {
						BigDecimal db = (BigDecimal)resultMap.get(i);
						insertObject.put(mapCNameToIndex.get(i), db.longValue());	
					} else {
						insertObject.put(mapCNameToIndex.get(i), resultMap.get(i));
					}						
				}
				
				MongoDBQuery.insertDocument(importUserDB, colName, insertObject);
			}
		}
	}
}
