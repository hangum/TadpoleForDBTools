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
package com.hangum.tadpole.rdb.core.actions.object.rdb;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.mysql.TriggerDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.system.TadpoleSystemCommons;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectDeleteAction extends AbstractObjectAction {
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
	public void run() {
		
		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
			TableDAO dao = (TableDAO)sel.getFirstElement();

			if(DBDefine.getDBDefine(userDB.getDbms_types()) != DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_2, dao.getName() + Messages.ObjectDeleteAction_3)) {
					try {
						TadpoleSystemCommons.executSQL(getUserDB(), "drop table " + dao.getName()); //$NON-NLS-1$
						refreshTable();
					} catch(Exception e) {
						logger.error(Messages.ObjectDeleteAction_5, e);
						exeMessage(Messages.ObjectDeleteAction_0, e);
					}
				}

			} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_2, dao.getName() + Messages.ObjectDeleteAction_3)) {
					try {
						MongoDBQuery.dropCollection(getUserDB(), dao.getName());
						refreshTable();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
			
			String viewName = (String)sel.getFirstElement();
			if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_8, viewName + Messages.ObjectDeleteAction_9)) {
				try {
					TadpoleSystemCommons.executSQL(getUserDB(), "drop view " + viewName); //$NON-NLS-1$
					
					refreshView();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_11, e);
					exeMessage(Messages.ObjectDeleteAction_1, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			if(sel.getFirstElement() instanceof InformationSchemaDAO) {			
				InformationSchemaDAO indexDAO = (InformationSchemaDAO)sel.getFirstElement();
				if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_14, indexDAO.getTABLE_NAME()+ Messages.ObjectDeleteAction_15 + indexDAO.getINDEX_NAME() + Messages.ObjectDeleteAction_16)) {
					
					try {
						if(DBDefine.getDBDefine(userDB.getDbms_types()) != DBDefine.POSTGRE_DEFAULT) {
							TadpoleSystemCommons.executSQL(getUserDB(), "drop index " + indexDAO.getINDEX_NAME() + " on " + indexDAO.getTABLE_NAME()); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							TadpoleSystemCommons.executSQL(getUserDB(), "drop index " + indexDAO.getINDEX_NAME()+ ";"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						
						refreshIndexes();
					} catch(Exception e) {
						logger.error(Messages.ObjectDeleteAction_19, e);
						exeMessage(Messages.ObjectDeleteAction_4, e);
					}
				}
			} else {
				MongoDBIndexDAO indexDAO = (MongoDBIndexDAO)sel.getFirstElement();
				if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_14, indexDAO.getNs() + " [ " + indexDAO.getName() + "] " + Messages.ObjectDeleteAction_16)) { //$NON-NLS-1$ //$NON-NLS-2$
					try {
						MongoDBQuery.dropIndex(getUserDB(), indexDAO.getNs(), indexDAO.getName());
						refreshIndexes();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
			ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)sel.getFirstElement();
			if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_23, procedureDAO.getName() + Messages.ObjectDeleteAction_24)) {
				try {
					TadpoleSystemCommons.executSQL(getUserDB(), "drop procedure " + procedureDAO.getName()); //$NON-NLS-1$
					
					refreshProcedure();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_26, e);
					exeMessage(Messages.ObjectDeleteAction_10, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
			ProcedureFunctionDAO functionDAO = (ProcedureFunctionDAO)sel.getFirstElement();
			if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_29, functionDAO.getName() + Messages.ObjectDeleteAction_30)) {
				try {
					TadpoleSystemCommons.executSQL(getUserDB(), "drop function " + functionDAO.getName()); //$NON-NLS-1$
					
					refreshFunction();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_32, e);
					exeMessage(Messages.ObjectDeleteAction_17, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
			TriggerDAO triggerDAO = (TriggerDAO)sel.getFirstElement();
			if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_35,  triggerDAO.getTrigger() + Messages.ObjectDeleteAction_36)) {
				try {
					TadpoleSystemCommons.executSQL(getUserDB(), "drop trigger " + triggerDAO.getTrigger()); //$NON-NLS-1$
					
					refreshTrigger();
				} catch(Exception e) {
					logger.error(Messages.ObjectDeleteAction_38, e);
					exeMessage(Messages.ObjectDeleteAction_18, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			MongoDBServerSideJavaScriptDAO jsDAO = (MongoDBServerSideJavaScriptDAO)sel.getFirstElement();
			if(MessageDialog.openConfirm(window.getShell(), Messages.ObjectDeleteAction_35,  jsDAO.getName() + Messages.ObjectDeleteAction_42)) {
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
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
			
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
				setEnabled(this.sel.size() > 0);
			} else setEnabled(false);
		}

	}
	
}
