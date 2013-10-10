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
package com.hangum.tadpole.rdb.core.actions.connections;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * procedure 생성 action
 * 
 * @author hangum
 *
 */
public class CompilePackageAction extends AbstractObjectSelectAction {

	private static final Logger logger = Logger.getLogger(CompilePackageAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.compile";

	public CompilePackageAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, IStructuredSelection sel, UserDBDAO userDB) {
		super(window, actionType);
		setId(ID + actionType.toString());
		window.getSelectionService().addSelectionListener(this);
		this.sel = sel;
		this.userDB = userDB;
	}

	@Override
	public void run() {
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
	}// end method
}
