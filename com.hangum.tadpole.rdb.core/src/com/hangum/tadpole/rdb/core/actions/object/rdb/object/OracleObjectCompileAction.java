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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.OracleObjectCompileUtils;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBInfoDialog;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class OracleObjectCompileAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OracleObjectCompileAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.compile";
	
	public OracleObjectCompileAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if (DBDefine.getDBDefine(userDB) != DBDefine.ORACLE_DEFAULT) return;
		
		if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {			
			
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
			viewCompile((String)selection.getFirstElement(), userDB);	
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
		
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
			ProcedureFunctionDAO dao = (ProcedureFunctionDAO)selection.getFirstElement();
			otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE.PROCEDURE, "PROCEDURE", dao.getName().trim().toUpperCase(), userDB);			
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PACKAGES) {
			packageCompile((ProcedureFunctionDAO)selection.getFirstElement(), userDB);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
			ProcedureFunctionDAO dao = (ProcedureFunctionDAO)selection.getFirstElement();
			otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE.FUNCTION, "FUNCTION",  dao.getName().trim().toUpperCase(), userDB);			
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
			TriggerDAO dao = (TriggerDAO)selection.getFirstElement();
			otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE.TRIGGER, "TRIGGER",  dao.getName().trim().toUpperCase(), userDB);			
		}
	}
	
	/**
	 * view compile
	 * 
	 * @param selection
	 * @param userDB
	 */
	public void viewCompile(String viewName, UserDBDAO userDB) {
		try {
			String result = OracleObjectCompileUtils.viewCompile(viewName, userDB);
			TDBInfoDialog dialog = new TDBInfoDialog(null, "Compile result", result);
			dialog.open();
			
		} catch (Exception e) {
			logger.error(viewName + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", viewName + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			refreshObject(PublicTadpoleDefine.QUERY_DDL_TYPE.VIEW, viewName.trim(), userDB);
		}
	}
	
	/**
	 * other object compile
	 * 
	 * @param actionType 
	 * @param objType
	 * @param objName
	 * @param userDB
	 */
	public void otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE actionType, String objType, String objName, UserDBDAO userDB) {
		try {
			String result = OracleObjectCompileUtils.otherObjectCompile(actionType, objType, objName, userDB);
			TDBInfoDialog dialog = new TDBInfoDialog(null, "Compile result", result.toString());
			dialog.open();
			
		} catch (Exception e) {
			logger.error(objName + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", objName + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			refreshObject(actionType, objName, userDB);
		}
	}
	
	/**
	 * package compile
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public void packageCompile(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		try {
			String result = OracleObjectCompileUtils.packageCompile(procedureDAO.getName(), userDB);
			TDBInfoDialog dialog = new TDBInfoDialog(null, "Compile result", result.toString());
			dialog.open();
			
		} catch (Exception e) {
			logger.error(procedureDAO.getName() + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", procedureDAO.getName() + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			refreshObject(PublicTadpoleDefine.QUERY_DDL_TYPE.PACKAGE, procedureDAO.getName(), userDB);
		}
	}
	
}
