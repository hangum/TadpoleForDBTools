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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.DBInfoDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DBOperationType;
import com.hangum.tadpole.define.DB_Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * mssql login composite
 * 
 * @author hangum
 *
 */
public class MSSQLLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215286981080340278L;
	private static final Logger logger = Logger.getLogger(MSSQLLoginComposite.class);
	
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MSSQLLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample MSSQL 2000", DBDefine.MSSQL_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	@Override
	public void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Group grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(3, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.MSSQLLoginComposite_grpConnectionType_text);
		
		Label lblHost = new Label(grpConnectionType, SWT.NONE);
		lblHost.setText(Messages.DBLoginDialog_1);
		
		textHost = new Text(grpConnectionType, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelPort.setText(Messages.DBLoginDialog_5);
		
		textPort = new Text(grpConnectionType, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnPing = new Button(grpConnectionType, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host 	= StringUtils.trimToEmpty(textHost.getText());
				String port 	= StringUtils.trimToEmpty(textPort.getText());
				
				if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
					MessageDialog.openError(null, Messages.DBLoginDialog_10, Messages.DBLoginDialog_11);
					return;
				}
				
				try {
					if(isPing(host, port)) {
						MessageDialog.openInformation(null, Messages.DBLoginDialog_12, Messages.DBLoginDialog_13);
					} else {
						MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.DBLoginDialog_15);
					}
				} catch(NumberFormatException nfe) {
					MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
				}
			}
		});
		btnPing.setText(Messages.DBLoginDialog_btnPing_text);
		
		Label lblNewLabelDatabase = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelDatabase.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabelDatabase.setText(Messages.DBLoginDialog_4);
		
		textDatabase = new Text(grpConnectionType, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));		
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.DBLoginDialog_2);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.DBLoginDialog_3);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		othersConnectionInfo = new OthersConnectionRDBGroup(this, SWT.NONE);
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		init();
	}
	
	@Override
	public void init() {
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText(DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName());
			
			textHost.setText(oldUserDB.getHost());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			textDatabase.setText(oldUserDB.getDb());
			textPort.setText(oldUserDB.getPort());
		} else if(ApplicationArgumentUtils.isTestMode()) {

			preDBInfo.setTextDisplayName(getDisplayName()); //$NON-NLS-1$
			
			textHost.setText("192.168.32.128"); //$NON-NLS-1$
			textPort.setText("1433"); //$NON-NLS-1$
			textDatabase.setText("northwind"); //$NON-NLS-1$
			textUser.setText("sa"); //$NON-NLS-1$
			textPassword.setText("tadpole"); //$NON-NLS-1$
		} else {
			textPort.setText("1433"); //$NON-NLS-1$
		}
		
		Combo comboGroup = preDBInfo.getComboGroup();
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
	
	/**
	 * 화면에 값이 올바른지 검사합니다.
	 * 
	 * @return
	 */
	public boolean isValidate() {
		if(!message(preDBInfo.getComboGroup(), "Group")) return false;
		if(!message(preDBInfo.getTextDisplayName(), "Display Name")) return false; //$NON-NLS-1$
		
		if(!message(textHost, "Host")) return false; //$NON-NLS-1$
		if(!message(textPort, "Port")) return false; //$NON-NLS-1$
		if(!message(textDatabase, "Database")) return false; //$NON-NLS-1$
		if(!message(textUser, "User")) return false; //$NON-NLS-1$
		
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
	
	/**
	 * ip정보에 instance 명 검사해서 ping합니다.
	 */
	public boolean isPing(String strHost, String port) {
		if(StringUtils.contains(strHost, "\\")) {
			String strIp 		= StringUtils.substringBefore(strHost, "\\");
			
			return super.isPing(strIp, port);
		} else if(StringUtils.contains(strHost, "/")) {
			String strIp 		= StringUtils.substringBefore(strHost, "/");

			return super.isPing(strIp, port);
		} else {		
			return super.isPing(strHost, port);
		}
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		String dbUrl = ""; 
		String strHost = textHost.getText();
		if(StringUtils.contains(strHost, "\\")) {
			
			String strIp 		= StringUtils.substringBefore(strHost, "\\");
			String strInstance	= StringUtils.substringAfter(strHost, "\\");
			
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					strIp, textPort.getText(), textDatabase.getText()) + ";instance=" + strInstance;			
		} else if(StringUtils.contains(strHost, "/")) {
			
			String strIp 		= StringUtils.substringBefore(strHost, "/");
			String strInstance	= StringUtils.substringAfter(strHost, "/");
			
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					strIp, textPort.getText(), textDatabase.getText()) + ";instance=" + strInstance;
			
		} else {		
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					textHost.getText().trim(), textPort.getText().trim(), textDatabase.getText().trim());
		}
		if(logger.isDebugEnabled()) logger.debug("[db url]" + dbUrl);

		userDB = new UserDBDAO();
		userDB.setTypes(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText().trim());
		userDB.setGroup_name(preDBInfo.getComboGroup().getText().trim());
		userDB.setDisplay_name(preDBInfo.getTextDisplayName().getText().trim());
		userDB.setOperation_type( DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).toString() );
		userDB.setHost(textHost.getText().trim());
		userDB.setPasswd(textPassword.getText().trim());
		userDB.setPort(textPort.getText().trim());
		userDB.setUsers(textUser.getText().trim());
		
		// others connection 정보를 입력합니다.
		OthersConnectionInfoDAO otherConnectionDAO =  othersConnectionInfo.getOthersConnectionInfo();
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?DB_Define.YES_NO.YES.toString():DB_Define.YES_NO.NO.toString());
		userDB.setIs_autocmmit(otherConnectionDAO.isAutoCommit()?DB_Define.YES_NO.YES.toString():DB_Define.YES_NO.NO.toString());
		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?DB_Define.YES_NO.YES.toString():DB_Define.YES_NO.NO.toString());
		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?DB_Define.YES_NO.YES.toString():DB_Define.YES_NO.NO.toString());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?DB_Define.YES_NO.YES.toString():DB_Define.YES_NO.NO.toString());
		
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
			
			int intVersion = 0;
			
			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);				
				// 디비 버전을 찾아옵니다.
				DBInfoDAO dbInfo = (DBInfoDAO)sqlClient.queryForObject("findDBInfo"); //$NON-NLS-1$
				intVersion = Integer.parseInt( StringUtils.substringBefore(dbInfo.getProductversion(), ".") );
				
			} catch (Exception e) {
				logger.error("MSSQL Connection", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MSSQLLoginComposite_8, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
			try {
				if(intVersion <= 8) {
					userDB.setTypes(DBDefine.MSSQL_8_LE_DEFAULT.getDBToString());
				}
				
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error("MSSQL", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MSSQLLoginComposite_10, errStatus); //$NON-NLS-1$
			}
		}
		
		return true;
	}

}
