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
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.sql.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.sql.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.mysql.TriggerDAO;
import com.hangum.tadpole.sql.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystemCommons;
import com.hangum.tadpole.sql.query.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectDeleteAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger .getLogger(ObjectDeleteAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.delete"; //$NON-NLS-1$

	public ObjectDeleteAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		
		setId(ID + actionType);
		setText("Drop " + title); //$NON-NLS-1$
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
			TableDAO dao = (TableDAO)selection.getFirstElement();

			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_2, Messages.ObjectDeleteAction_3)) {
					String strSQL = "drop table " + dao.getSysName();
					try {
						if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
							new TajoConnectionManager().executeUpdate(userDB, strSQL);
						} else {
							TadpoleSystemCommons.executSQL(userDB, strSQL); //$NON-NLS-1$
						}
						
						//
						TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), userDB, strSQL);
						
						refreshTable();
					} catch(Exception e) {
						logger.error(Messages.ObjectDeleteAction_5, e);
						exeMessage(Messages.ObjectDeleteAction_0, e);
					}
				}

			} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_2, Messages.ObjectDeleteAction_3)) {
					try {
						MongoDBQuery.dropCollection(userDB, dao.getName());
						refreshTable();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
			
			String viewName = (String)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_8, Messages.ObjectDeleteAction_9)) {
				try {
					TadpoleSystemCommons.executSQL(userDB, "drop view " + viewName); //$NON-NLS-1$
					
					refreshView();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_11, e);
					exeMessage(Messages.ObjectDeleteAction_1, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.SYNONYM) {
			
			OracleSynonymDAO dao = (OracleSynonymDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_8, Messages.ObjectDeleteAction_synonym)) {
				try {
					TadpoleSystemCommons.executSQL(userDB, "drop synonym " + dao.getTable_owner() + "." + dao.getSynonym_name()); //$NON-NLS-1$
					
					refreshSynonym();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_11, e);
					exeMessage(Messages.ObjectDeleteAction_1, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			if(selection.getFirstElement() instanceof InformationSchemaDAO) {			
				InformationSchemaDAO indexDAO = (InformationSchemaDAO)selection.getFirstElement();
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_14, Messages.ObjectDeleteAction_16)) {
					
					try {
						if(userDB.getDBDefine() != DBDefine.POSTGRE_DEFAULT) {
							TadpoleSystemCommons.executSQL(userDB, "drop index " + indexDAO.getINDEX_NAME() + " on " + indexDAO.getTABLE_NAME()); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							TadpoleSystemCommons.executSQL(userDB, "drop index " + indexDAO.getINDEX_NAME()+ ";"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						
						refreshIndexes();
					} catch(Exception e) {
						logger.error(Messages.ObjectDeleteAction_19, e);
						exeMessage(Messages.ObjectDeleteAction_4, e);
					}
				}
			} else {
				MongoDBIndexDAO indexDAO = (MongoDBIndexDAO)selection.getFirstElement();
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_14, Messages.ObjectDeleteAction_16)) { //$NON-NLS-1$ //$NON-NLS-2$
					try {
						MongoDBQuery.dropIndex(userDB, indexDAO.getNs(), indexDAO.getName());
						refreshIndexes();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
			ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_23, Messages.ObjectDeleteAction_24)) {
				try {
					TadpoleSystemCommons.executSQL(userDB, "drop procedure " + procedureDAO.getName()); //$NON-NLS-1$
					
					refreshProcedure();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_26, e);
					exeMessage(Messages.ObjectDeleteAction_10, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
			ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_23, Messages.ObjectDeleteAction_24)) {
				try {
					try{
					TadpoleSystemCommons.executSQL(userDB, "drop package body " + procedureDAO.getName()); //$NON-NLS-1$
					}catch(Exception e){
						// package body는 없을 수도 있음.
					}
					TadpoleSystemCommons.executSQL(userDB, "drop package " + procedureDAO.getName()); //$NON-NLS-1$
					
					refreshPackage();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_26, e);
					exeMessage(Messages.ObjectDeleteAction_10, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
			ProcedureFunctionDAO functionDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_29, Messages.ObjectDeleteAction_30)) {
				try {
					TadpoleSystemCommons.executSQL(userDB, "drop function " + functionDAO.getName()); //$NON-NLS-1$
					
					refreshFunction();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_32, e);
					exeMessage(Messages.ObjectDeleteAction_17, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
			TriggerDAO triggerDAO = (TriggerDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_35, Messages.ObjectDeleteAction_36)) {
				try {
					TadpoleSystemCommons.executSQL(userDB, "drop trigger " + triggerDAO.getTrigger()); //$NON-NLS-1$
					
					refreshTrigger();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_38, e);
					exeMessage(Messages.ObjectDeleteAction_18, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			MongoDBServerSideJavaScriptDAO jsDAO = (MongoDBServerSideJavaScriptDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.ObjectDeleteAction_35, Messages.ObjectDeleteAction_42)) {
				try {
					MongoDBQuery.deleteJavaScirpt(userDB, jsDAO.getName());					
					refreshJS();
				} catch(Exception e) {
					logger.error("MongoDB ServerSide JavaScript delelte", e); //$NON-NLS-1$
					exeMessage("JavaScript", e); //$NON-NLS-1$
				}
			}
		}
		
	}	// end method
	
}
