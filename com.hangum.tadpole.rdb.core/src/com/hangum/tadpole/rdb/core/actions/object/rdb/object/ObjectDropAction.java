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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableConstraintsDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectDropAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger .getLogger(ObjectDropAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.drop"; //$NON-NLS-1$

	public ObjectDropAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		
		setId(ID + actionType);
		setText(title);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		try {
			if(!GrantCheckerUtils.ifExecuteQuery(userDB)) return;
		} catch (Exception e) {
			MessageDialog.openError(getWindow().getShell(), Messages.get().Error, e.getMessage());
			return;
		}
		
		if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
			TableDAO dao = (TableDAO)selection.getFirstElement();

			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_3)) {
					for(Object selObjec : selection.toList()) {
						TableDAO selTableDao = (TableDAO)selObjec;
						String strSQL = "drop table " + SQLUtil.getTableName(userDB, selTableDao);// dao.getSysName(); //$NON-NLS-1$
						try {
							if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
								new TajoConnectionManager().executeUpdate(userDB, strSQL);
							} else {
								executeSQL(userDB, strSQL);
							}
						} catch(Exception e) {
							logger.error("drop table", e);
							exeMessage(Messages.get().ObjectDeleteAction_0, e);
						}
					}

					refreshTable();
				}

			} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_3)) {
					try {
						MongoDBQuery.dropCollection(userDB, dao.getName());
						refreshTable();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
			
			TableDAO viewDao = (TableDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_9)) {
				try {
					executeSQL(userDB, "drop view " + viewDao.getSysName()); //$NON-NLS-1$
					
					refreshView();
				} catch(Exception e) {
					logger.error("drop view", e);
					exeMessage(Messages.get().ObjectDeleteAction_1, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.SYNONYM) {
			
			OracleSynonymDAO dao = (OracleSynonymDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_synonym)) {
				try {
					executeSQL(userDB, "drop synonym " + dao.getTable_owner() + "." + dao.getSynonym_name()); //$NON-NLS-1$ //$NON-NLS-2$
					
					refreshSynonym();
				} catch(Exception e) {
					logger.error("drop synoym", e);
					exeMessage(Messages.get().ObjectDeleteAction_1, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
			if(selection.getFirstElement() instanceof InformationSchemaDAO) {			
				InformationSchemaDAO indexDAO = (InformationSchemaDAO)selection.getFirstElement();
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_16)) {
					
					try {
						if(userDB.getDBDefine() != DBDefine.POSTGRE_DEFAULT || userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
							executeSQL(userDB, "drop index " + indexDAO.getINDEX_NAME() + " on " + indexDAO.getTABLE_NAME()); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							executeSQL(userDB, "drop index " + indexDAO.getINDEX_NAME()+ ";"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						
						refreshIndexes();
					} catch(Exception e) {
						logger.error("Delete index", e);
						exeMessage(Messages.get().ObjectDeleteAction_4, e);
					}
				}
			} else {
				MongoDBIndexDAO indexDAO = (MongoDBIndexDAO)selection.getFirstElement();
				if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_16)) { //$NON-NLS-1$ //$NON-NLS-2$
					try {
						MongoDBQuery.dropIndex(userDB, indexDAO.getNs(), indexDAO.getName());
						refreshIndexes();
					} catch(Exception e) {
						logger.error("Collection Delete", e); //$NON-NLS-1$
						exeMessage("Collection", e); //$NON-NLS-1$
					}
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
			TableConstraintsDAO constraintDAO = (TableConstraintsDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, "Do you want to drop Constraints?")) {
				
				try {
					if(userDB.getDBDefine() != DBDefine.POSTGRE_DEFAULT || userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
						executeSQL(userDB, "drop constraints " + constraintDAO.getCONSTRAINT_NAME() + " on " + constraintDAO.getTABLE_NAME()); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						executeSQL(userDB, "drop constraints " + constraintDAO.getCONSTRAINT_NAME()+ ";"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
					this.refreshConstraints();
				} catch(Exception e) {
					logger.error("Delete constraints", e);
					exeMessage("CONSTRAINTS", e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
			ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_24)) {
				
				try {
					if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
						StringBuffer sbQuery = new StringBuffer("drop function " + procedureDAO.getName() + "(");
						
						ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
						pm.isExecuted(procedureDAO, userDB);
						List<InOutParameterDAO> inList = pm.getExecuter().getInParameters();
						InOutParameterDAO inOutParameterDAO = inList.get(0);
						String[] inParams = StringUtils.split(inOutParameterDAO.getRdbType(), ",");
						for(int i=0; i<inParams.length; i++) {
							String name = StringUtils.trimToEmpty(inParams[i]);
							
							if(i == (inParams.length-1)) sbQuery.append(String.format("%s", name));
							else sbQuery.append(String.format("%s, ", name));
						}
						sbQuery.append(")");
						if(logger.isDebugEnabled()) logger.debug("=[PROCEDURES]===> " + sbQuery);
						
						executeSQL(userDB, sbQuery.toString()); //$NON-NLS-1$
					} else {
						executeSQL(userDB, "drop procedure " + procedureDAO.getName()); //$NON-NLS-1$
					}
					
					refreshProcedure();
				} catch(Exception e) {
					logger.error("drop procedure", e);
					exeMessage(Messages.get().ObjectDeleteAction_10, e);
				}
			
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PACKAGES) {
			ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDropAction_1)) {
				try {
					try{
						executeSQL(userDB, "drop package body " + procedureDAO.getName()); //$NON-NLS-1$
					}catch(Exception e){
						// package body는 없을 수도 있음.
					}
					executeSQL(userDB, "drop package " + procedureDAO.getName()); //$NON-NLS-1$
					
					refreshPackage();
				} catch(Exception e) {
					logger.error("drop package", e);
					exeMessage(Messages.get().ObjectDeleteAction_10, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
			ProcedureFunctionDAO functionDAO = (ProcedureFunctionDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_30)) {
				try {
					if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
						executeSQL(userDB, "drop function " + functionDAO.getDefiner() + "." + functionDAO.getName());
					} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
						StringBuffer sbQuery = new StringBuffer("drop function " + functionDAO.getName() + "(");
						
						ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, functionDAO);
						pm.isExecuted(functionDAO, userDB);
						List<InOutParameterDAO> inList = pm.getExecuter().getInParameters();
						InOutParameterDAO inOutParameterDAO = inList.get(0);
						String[] inParams = StringUtils.split(inOutParameterDAO.getRdbType(), ",");
						for(int i=0; i<inParams.length; i++) {
							String name = StringUtils.trimToEmpty(inParams[i]);
							
							if(i == (inParams.length-1)) sbQuery.append(String.format("%s", name));
							else sbQuery.append(String.format("%s, ", name));
						}
						sbQuery.append(")");
						if(logger.isDebugEnabled()) logger.debug("=[FUNCTIONS]===> " + sbQuery);
						
						executeSQL(userDB, sbQuery.toString()); //$NON-NLS-1$
					} else {
						executeSQL(userDB, "drop function " + functionDAO.getName()); //$NON-NLS-1$
					}
					
					refreshFunction();
				} catch(Exception e) {
					logger.error("drop function", e);
					exeMessage(Messages.get().ObjectDeleteAction_17, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
			TriggerDAO triggerDAO = (TriggerDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_36)) {
				try {
					if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
						executeSQL(userDB, "drop trigger " + triggerDAO.getTrigger() + " on " + triggerDAO.getTable_name()); //$NON-NLS-1$
					} else {
						executeSQL(userDB, "drop trigger " + triggerDAO.getTrigger()); //$NON-NLS-1$
					}
					
					refreshTrigger();
				} catch(Exception e) {
					logger.error("drop trigger", e);
					exeMessage(Messages.get().ObjectDeleteAction_18, e);
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT) {
			MongoDBServerSideJavaScriptDAO jsDAO = (MongoDBServerSideJavaScriptDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), Messages.get().Confirm, Messages.get().ObjectDeleteAction_42)) {
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
	
	/**
	 * executeSQL
	 * 
	 * @param userDB
	 * @param cmd
	 * @throws Exception
	 */
	private void executeSQL(UserDBDAO userDB, String cmd) throws Exception {
		RequestResultDAO resultDao = ExecuteDDLCommand.executSQL(userDB, cmd); //$NON-NLS-1$
		if(PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(resultDao.getResult())) {
			exeMessage(Messages.get().ObjectDeleteAction_0, resultDao.getException());		
		}
	}
	
}
