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

import java.util.List;

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
 * cubrid login composite
 * 
 * @author hangum
 *
 */
public class CubridLoginComposite extends MySQLLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4519162649799179626L;
	private static final Logger logger = Logger.getLogger(CubridLoginComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CubridLoginComposite(Composite parent, int style, List<String> listGroupName) {
		super(DBDefine.CUBRID_DEFAULT, parent, style, listGroupName);
		setText(DBDefine.CUBRID_DEFAULT.getDBToString());
	}
	
	@Override
	public void init() {
		
		if(ApplicationArgumentUtils.isTestMode()) {
			comboGroup.add("Test group");
			comboGroup.select(0);
			
			textHost.setText("127.0.0.1");
			textUser.setText("dba");
			textPassword.setText("");
			textDatabase.setText("demodb");
			textPort.setText("33000");
			
			textDisplayName.setText("Cubrid 8.4.1.2032");
		}
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DBDefine.CUBRID_DEFAULT.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.CUBRID_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setGroup_name(comboGroup.getText().trim());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setLocale(comboLocale.getText().trim());
		userDB.setUsers(textUser.getText());
		
		// 이미 연결한 것인지 검사한다.
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		if( !managerView.isAdd(DBDefine.CUBRID_DEFAULT, userDB) ) {
			MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.DBLoginDialog_24);
			
			return false;
		}

		// db가 정상적인지 채크해본다 
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List showTables = sqlClient.queryForList("tableList", textDatabase.getText());
			
		} catch (Exception e) {
			logger.error("Cubrid DB Connection", e);
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_10, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error("cubrid db preference save", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_11, errStatus); //$NON-NLS-1$
			}
		
		return true;
	}

}
