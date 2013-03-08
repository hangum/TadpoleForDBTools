/*******************************************************************************
 * Copyright (c) 2013 hangum.
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
import com.hangum.tadpole.importdb.core.dialog.importdb.dao.ModTableDAO;
import com.hangum.tadpole.importdb.core.dialog.importdb.utils.MongoDBQueryUtil;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * mongodb collection을 mongodb로 넘깁니다.
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionToMongodBImport extends DBImport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBCollectionToMongodBImport.class);
	
	private List<ModTableDAO> listModeTable; 

	/**
	 * 
	 * @param importUserDB
	 * @param listModeTable
	 * @param exportUserDB
	 */
	public MongoDBCollectionToMongodBImport(UserDBDAO importUserDB, List<ModTableDAO> listModeTable, UserDBDAO exportUserDB) {
		super(importUserDB, exportUserDB);
		
		this.listModeTable = listModeTable;
	}

	@Override
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
						
						// collection is exist on delete.
						String strNewColName = modTableDAO.getReName().trim().equals("")?modTableDAO.getName():modTableDAO.getReName();
						if(modTableDAO.isExistOnDelete()) MongoDBQuery.existOnDelete(importUserDB, modTableDAO.getName());
						
						// insert
						insertMongoDB(modTableDAO, exportUserDB, strNewColName);
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
	private void insertMongoDB(ModTableDAO modTableDAO, UserDBDAO userDBDAO, String strNewColName) throws Exception {
		String workTable = modTableDAO.getName();		
		if(logger.isDebugEnabled()) logger.debug("[work collection]" + workTable);			
		
		MongoDBQueryUtil qu = new MongoDBQueryUtil(userDBDAO, workTable);
		while(qu.hasNext()) {
			qu.nextQuery();
			
			// row 단위
			List<DBObject> listDBObject = qu.getCollectionDataList();
			logger.debug("[work table]" + strNewColName + " size is " + listDBObject.size());
		
			MongoDBQuery.insertDocument(importUserDB, strNewColName, listDBObject);
		}
	}
}
