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

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	
	public OracleObjectCompileAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT){
			if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {			
				
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				ViewCompile(selection, userDB);	
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				ProcedureFunctionDAO dao = (ProcedureFunctionDAO)selection.getFirstElement();
				OtherObjectCompile("PROCEDURE", dao.getName().trim().toUpperCase(), userDB);			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
				packageCompile(selection, userDB);
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				ProcedureFunctionDAO dao = (ProcedureFunctionDAO)selection.getFirstElement();
				OtherObjectCompile("FUNCTION",  dao.getName().trim().toUpperCase(), userDB);			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				TriggerDAO dao = (TriggerDAO)selection.getFirstElement();
				OtherObjectCompile("TRIGGER",  dao.getName().trim().toUpperCase(), userDB);			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			
			}
		}
	}
	
	/**
	 * view compile
	 * 
	 * @param selection
	 * @param userDB
	 */
	public void ViewCompile(IStructuredSelection selection, UserDBDAO userDB) {
		String viewName = (String)selection.getFirstElement();
		
		String sqlQuery = "ALTER VIEW " + userDB.getUsers() + "." + viewName.trim().toUpperCase() + " COMPILE ";

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			
			sqlQuery = "Select * From sys.user_Errors where name='"+ viewName.trim().toUpperCase() +"' and type = 'VIEW' order by type, sequence ";
			
			rs = statement.executeQuery(sqlQuery);
			
			StringBuffer result = new StringBuffer("Complete object compile...\n\n");
			while (rs.next()) {
				result.append("[" + rs.getString("line") + "Line / " + rs.getString("line") + "Column] " + rs.getString("text") + "\n");
			}
			
			MessageDialog.openError(null, "Compile result", result.toString());
			
		} catch (Exception e) {
			logger.error(viewName + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", viewName + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
	/**
	 * other object compile
	 * 
	 * @param objType
	 * @param objName
	 * @param userDB
	 */
	public void OtherObjectCompile(String objType, String objName, UserDBDAO userDB) {
		String sqlQuery = "ALTER "+objType+" " + userDB.getUsers() + "." + objName.trim().toUpperCase() + " COMPILE DEBUG ";

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			
			sqlQuery = "Select * From sys.user_Errors where name='"+ objName +"' and type = '"+objType+"' order by type, sequence ";
			
			rs = statement.executeQuery(sqlQuery);
			
			StringBuffer result = new StringBuffer("Complete object compile...\n\n");
			while (rs.next()) {
				result.append("[" + rs.getString("line") + "Line / " + rs.getString("line") + "Column] " + rs.getString("text") + "\n");
			}
			
			MessageDialog.openError(null, "Compile result", result.toString());
			
		} catch (Exception e) {
			logger.error(objName + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", objName + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
	/**
	 * package compile
	 * 
	 * @param selection
	 * @param userDB
	 */
	public void packageCompile(IStructuredSelection selection, UserDBDAO userDB) {
		
		ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
		
		String sqlQuery = "ALTER PACKAGE " + userDB.getUsers() + "." + procedureDAO.getName().trim().toUpperCase() + " COMPILE DEBUG SPECIFICATION ";
		String sqlBodyQuery = "ALTER PACKAGE " + userDB.getUsers() + "." + procedureDAO.getName().trim().toUpperCase() + " COMPILE DEBUG BODY ";

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			statement.execute(sqlBodyQuery);
			
			
			sqlQuery = "Select * From sys.user_Errors where name='"+ procedureDAO.getName().trim().toUpperCase() +"' and type in ('PACKAGE', 'PACKAGE BODY') order by type, sequence ";
			
			rs = statement.executeQuery(sqlQuery);
			
			StringBuffer result = new StringBuffer("Complete object compile...\n\n");
			while (rs.next()) {
				result.append("[" + rs.getString("line") + "Line / " + rs.getString("line") + "Column] " + rs.getString("text") + "\n");
			}
			
			MessageDialog.openError(null, "Compile result", result.toString());
			
			
		} catch (Exception e) {
			logger.error(procedureDAO.getName() + " compile", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", procedureDAO.getName() + " compile error", errStatus); //$NON-NLS-1$
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
}
