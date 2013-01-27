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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * postgres login composite
 * 
 * @author hangum
 *
 */
public class PostgresLoginComposite extends MySQLLoginComposite {

	private static final Logger logger = Logger.getLogger(PostgresLoginComposite.class);
	private static final DBDefine DB_DEFINE = DBDefine.POSTGRE_DEFAULT;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PostgresLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super(DB_DEFINE, parent, style, listGroupName, selGroupName, userDB);
		setText(DB_DEFINE.getDBToString());
	}
	
	@Override
	public void init() {
		
		if(oldUserDB != null) {
			selGroupName = oldUserDB.getGroup_name();
			textDisplayName.setText(oldUserDB.getDisplay_name());
			
			textHost.setText(oldUserDB.getHost());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			textDatabase.setText(oldUserDB.getDb());
			textPort.setText(oldUserDB.getPort());
		} else if(ApplicationArgumentUtils.isTestMode()) {

			textDisplayName.setText("Sample PostgreSQL 9.1");
			
			textHost.setText("127.0.0.1");
			textUser.setText("tadpole");
			textPassword.setText("tadpole");
			textDatabase.setText("test");
			textPort.setText("5432");			
		}

		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) {
				comboGroup.setText(strOtherGroupName);
			} else {
				// 콤보 선택 
				for(int i=0; i<comboGroup.getItemCount(); i++) {
					if(selGroupName.equals(comboGroup.getItem(i))) comboGroup.select(i);
				}
			}
		}
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DB_DEFINE.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setTypes(DB_DEFINE.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setGroup_name(comboGroup.getText().trim());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setLocale(comboLocale.getText().trim());
		userDB.setUsers(textUser.getText());
	
		// 기존 데이터 업데이트
		if(oldUserDB != null) {
			if(!MessageDialog.openConfirm(null, "Confirm", Messages.SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			if(!checkDatabase(userDB)) return false;
			
			try {
				TadpoleSystem_UserDBQuery.updateUserDB(userDB, oldUserDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error(Messages.SQLiteLoginComposite_8, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		// 신규 데이터 저장.
		} else {
	
			// db가 정상적인지 채크해본다 
			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				List showTables = sqlClient.queryForList("tableList", textDatabase.getText());
				
			} catch (Exception e) {
				logger.error("PostgreSQL DB Connection", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", "PostgreSQL Connection Exception", errStatus);
				
				return false;
			}
			
			// preference에 save합니다.
			if(btnSavePreference.getSelection()) {
				try {
					TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
				} catch (Exception e) {
					logger.error("PostgreSQL db preference save", e);
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getShell(), "Error", "PostgreSQL Connection Exception", errStatus); //$NON-NLS-1$
				}
			}
			
		}
		
		return true;
	}

}
