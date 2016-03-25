/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importexport.core.utils.mongodb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.importexport.Activator;
import com.hangum.tadpole.importexport.core.Messages;
import com.hangum.tadpole.importexport.core.editors.mongodb.composite.ModTableDAO;
import com.hangum.tadpole.importexport.core.utils.SQLQueryUtil;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * RDB Table data to Mongodb 
 * 
 * @author hangum
 *
 */
public class RDBTableToMongoDBImport extends DBImport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBTableToMongoDBImport.class);
	
	private List<ModTableDAO> listModeTable; 
	
	public RDBTableToMongoDBImport(UserDBDAO sourceUserDB, UserDBDAO targetUserDB, List<ModTableDAO> listModeTable) {
		super(sourceUserDB, targetUserDB);
		this.listModeTable = listModeTable;
	}

	/**
	 * table import
	 */
	public Job workTableImport() {
		if(0 == listModeTable.size()) {
			MessageDialog.openInformation(null, Messages.get().Confirm, "Please select table");
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
						if(modTableDAO.isExistOnDelete()) MongoDBQuery.existOnDelete(getTargetUserDB(), modTableDAO.getName());
						
						// insert
						insertMongoDB(modTableDAO, strNewColName);
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
	private void insertMongoDB(ModTableDAO modTableDAO, String strNewColName) throws Exception {
		String workTable = modTableDAO.getName();		
		if(logger.isDebugEnabled()) logger.debug("[work table]" + workTable);			
		
		SQLQueryUtil qu = new SQLQueryUtil(getSourceUserDB(), "SELECT * FROM " + workTable);
		while(qu.hasNext()) {
			qu.nextQuery();
			Map<Integer, String> mapCNameToIndex = qu.getMapColumns();
			List<HashMap<Integer, Object>> listTableData = qu.getTableDataList();
			
			// row 단위
			List<DBObject> listDBObject = new ArrayList<DBObject>();
			for (int i=0; i<listTableData.size(); i++) {
				HashMap<Integer, Object> resultMap = listTableData.get(i);					
				
				DBObject insertObject = new BasicDBObject();
				// column 단위
				for(int j=0; j<mapCNameToIndex.size(); j++) {
					// BigDecimal is not support mongodb 
					if(resultMap.get(j) instanceof java.math.BigDecimal) {
						BigDecimal db = (BigDecimal)resultMap.get(j);
						insertObject.put(mapCNameToIndex.get(j), ""+db.longValue());
					} else if(resultMap.get(j) instanceof java.math.BigInteger) {
						BigInteger db = (BigInteger)resultMap.get(j);
						insertObject.put(mapCNameToIndex.get(j), ""+db.longValue());
					} else if(resultMap.get(j) instanceof java.sql.Timestamp) {
						Timestamp ts = (Timestamp)resultMap.get(j);
						
						insertObject.put(mapCNameToIndex.get(j), ts.toString());
					} else {
						insertObject.put(mapCNameToIndex.get(j), resultMap.get(j));
					}						
				}
				
				listDBObject.add(insertObject);
			}	// end for
			logger.debug("[work table]" + strNewColName + " size is " + listDBObject.size());
		
			MongoDBQuery.insertDocument(getTargetUserDB(), strNewColName, listDBObject);
		}
	}
}
