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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.mysql.TriggerDAO;
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
		setText("Compile " + title);
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT){
			if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {			
				
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				ViewCompile();	
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				ProcedureFunctionDAO dao = (ProcedureFunctionDAO)sel.getFirstElement();
				OtherObjectCompile("PROCEDURE", dao.getName().trim().toUpperCase());			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
				PackageCompile();
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				ProcedureFunctionDAO dao = (ProcedureFunctionDAO)sel.getFirstElement();
				OtherObjectCompile("FUNCTION",  dao.getName().trim().toUpperCase());			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				TriggerDAO dao = (TriggerDAO)sel.getFirstElement();
				OtherObjectCompile("TRIGGER",  dao.getName().trim().toUpperCase());			
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			
			}
		}
	}
	
	public void ViewCompile() {
		String viewName = (String)sel.getFirstElement();
		
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
			e.printStackTrace();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
	public void OtherObjectCompile(String objType, String objName) {
		
		
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
			e.printStackTrace();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
	public void PackageCompile() {
		ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)sel.getFirstElement();
		
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
			e.printStackTrace();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

			this.refreshPackage();
		}
	}
	
}
