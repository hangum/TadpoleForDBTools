/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.importdb.core.dialog.importdb.utils.MongoDBQueryUtil;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.mongodb.DBObject;

/**
 * Collection analyizer
 * 
 * @author hangum
 *
 */
public class MongodbCollectionAnalyizeAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongodbCollectionAnalyizeAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.collection.analyzer"; //$NON-NLS-1$
	
	public MongodbCollectionAnalyizeAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		String originalName = selection.getFirstElement().toString();
		
		if(selection.getFirstElement() instanceof TableDAO) {
			TableDAO tableDao = (TableDAO)selection.getFirstElement();
			if(logger.isDebugEnabled()) logger.debug("\t collection name is " + tableDao.getName());
			
			Map mapCollectionStructure = new HashMap<String, CollectionFieldDAO>();
			
			try {
				MongoDBQueryUtil qu = new MongoDBQueryUtil(userDB, tableDao.getName());
				while(qu.nextQuery()) {
					
					// row 단위
					List<DBObject> listDBObject = qu.getCollectionDataList();
					for (DBObject dbObject : listDBObject) {
						
						for (String strKey : dbObject.keySet()) {
							
							if(!mapCollectionStructure.containsKey(strKey)) {
								CollectionFieldDAO dao = new CollectionFieldDAO(strKey, "", "");
								
								mapCollectionStructure.put(strKey, dao);
							}
						}
					}
					if(logger.isDebugEnabled()) logger.debug("[work table]" + tableDao.getName() + " size is " + listDBObject.size());
				}
				
				Set<String> keys = mapCollectionStructure.keySet();
				for (String string : keys) {
					logger.debug("unique structure is : \t" + string);
				}
				
			} catch(Exception e) {
				logger.error("next query", e);
			}
			
		}

//		if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Confirm", Messages.ObjectMongodbReIndexAction_2)) { //$NON-NLS-1$
//			try {
//				MongoDBQuery.reIndexCollection(userDB, originalName);
//
//			} catch (Exception e) {
//				logger.error("mongodb rename", e); //$NON-NLS-1$
//
//				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//				ExceptionDetailsErrorDialog.openError(null, "Error", "Rename Collection", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//
//		}
		
	}
	
}
