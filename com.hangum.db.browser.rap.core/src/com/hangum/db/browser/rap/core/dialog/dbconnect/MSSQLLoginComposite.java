/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.dialog.dbconnect;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;
import com.hangum.db.util.ApplicationArgumentUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * mssql login composite
 * 
 * @author hangum
 *
 */
public class MSSQLLoginComposite extends MySQLLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215286981080340278L;
	private static final Logger logger = Logger.getLogger(MSSQLLoginComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MSSQLLoginComposite(Composite parent, int style) {
		super(DBDefine.MSSQL_DEFAULT, parent, style);
		setText(DBDefine.MSSQL_DEFAULT.getDBToString());
	}
	
	@Override
	public void init() {
		if(ApplicationArgumentUtils.isTestMode()) {
			textHost.setText("192.168.61.130"); //$NON-NLS-1$
			textPort.setText("1433"); //$NON-NLS-1$
			textDatabase.setText("northwind"); //$NON-NLS-1$
			textUser.setText("sa"); //$NON-NLS-1$
			textPassword.setText("tadpole"); //$NON-NLS-1$
			
		}
		
		textDisplayName.setText("MSSQL Server"); //$NON-NLS-1$
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DBDefine.MSSQL_DEFAULT.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.MSSQL_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setUsers(textUser.getText());
		
		// 이미 연결한 것인지 검사한다.
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		if( !managerView.isAdd(DBDefine.MSSQL_DEFAULT, userDB) ) {
			MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.DBLoginDialog_24);
			
			return false;
		}

		// db가 정상적인지 채크해본다 
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			sqlClient.queryForList("tableList", textDatabase.getText()); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("MSSQL Connection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MSSQLLoginComposite_8, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error("MSSQL", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MSSQLLoginComposite_10, errStatus); //$NON-NLS-1$
			}
		
		return true;
	}

}
