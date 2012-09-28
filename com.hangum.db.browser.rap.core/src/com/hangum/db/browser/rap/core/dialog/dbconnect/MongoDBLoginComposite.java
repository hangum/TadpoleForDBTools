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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;
import com.hangum.db.util.ApplicationArgumentUtils;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.mongodb.DB;

/**
 * oracle login composite
 * 
 * @author hangum
 *
 */
public class MongoDBLoginComposite extends MySQLLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(MongoDBLoginComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MongoDBLoginComposite(Composite parent, int style, List<String> listGroupName) {
		super(DBDefine.MONGODB_DEFAULT, parent, style, listGroupName);
		setText(DBDefine.MONGODB_DEFAULT.getDBToString());
	}
	
	@Override
	public void init() {
		
		if(ApplicationArgumentUtils.isTestMode()) {
			comboGroup.add("Test group");
			comboGroup.select(0);
			
			textHost.setText("127.0.0.1");
			textUser.setText("");
			textPassword.setText("");
			textDatabase.setText("test");
			textPort.setText("27017");
			
			textDisplayName.setText("MongoDB Default");
		}
	}
	
	public boolean isValidate() {
		
		if(!message(textHost, "Host")) return false; //$NON-NLS-1$
		if(!message(textPort, "Port")) return false; //$NON-NLS-1$
		if(!message(textDatabase, "Database")) return false; //$NON-NLS-1$		
		if(!message(textDisplayName, "Display Name")) return false; //$NON-NLS-1$
		
		String host 	= StringUtils.trimToEmpty(textHost.getText());
		String port 	= StringUtils.trimToEmpty(textPort.getText());

		try {
			if(!isPing(host, port)) {
				MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.MySQLLoginComposite_8);
				return false;
			}
		} catch(NumberFormatException nfe) {
			MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DBDefine.MONGODB_DEFAULT.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.MONGODB_DEFAULT.getDBToString());
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
		if( !managerView.isAdd(DBDefine.MONGODB_DEFAULT, userDB) ) {
			MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.DBLoginDialog_24);
			
			return false;
		}

		// db가 정상적인지 채크해본다 
		try {
			DB mongoDB = MongoConnectionManager.getInstance(userDB);

		} catch (Exception e) {
			logger.error("MongoDB Connection error", e);
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_10, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error("MongoDB info save", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", "MongoDB", errStatus); //$NON-NLS-1$
			}
		
		return true;
	}

}
