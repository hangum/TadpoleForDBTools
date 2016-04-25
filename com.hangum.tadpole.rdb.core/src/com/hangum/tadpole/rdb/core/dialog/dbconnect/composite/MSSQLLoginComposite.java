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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.DBInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;
import com.hangum.tadpole.session.manager.SessionManager;
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
	
	protected Group grpConnectionType;
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	
	protected Text textJDBCOptions;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MSSQLLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample MSSQL", DBDefine.MSSQL_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
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
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.get().MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(5, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().DatabaseInformation);
		
		Label lblHost = new Label(grpConnectionType, SWT.NONE);
		lblHost.setText(Messages.get().Host);
		
		textHost = new Text(grpConnectionType, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelPort.setText(Messages.get().Port);
		
		textPort = new Text(grpConnectionType, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnPing = new Button(grpConnectionType, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pingTest(textHost.getText(), textPort.getText());
			}
		});
		btnPing.setText(Messages.get().PingTest);
		
		Label lblNewLabelDatabase = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelDatabase.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabelDatabase.setText(Messages.get().Database);
		
		textDatabase = new Text(grpConnectionType, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.get().User);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.get().Password);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(grpConnectionType, SWT.NONE);
		
		Label label = new Label(grpConnectionType, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblJdbcOptions = new Label(grpConnectionType, SWT.NONE);
		lblJdbcOptions.setText(Messages.get().JDBCOptions);
		
		textJDBCOptions = new Text(grpConnectionType, SWT.BORDER);
		textJDBCOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		othersConnectionInfo = new OthersConnectionRDBGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		init();
	}
	
	@Override
	public void init() {
		// change group title
		grpConnectionType.setText(
				String.format("%s %s", selectDB.getDBToString() , Messages.get().DatabaseInformation)
		);
		
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText(PublicTadpoleDefine.DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName());
			
			textHost.setText(oldUserDB.getHost());
			textPort.setText(oldUserDB.getPort());
			textDatabase.setText(oldUserDB.getDb());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
			
			othersConnectionInfo.setUserData(oldUserDB);
			
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName()); //$NON-NLS-1$
			
			textHost.setText("192.168.29.128"); //$NON-NLS-1$
			textPort.setText("1433"); //$NON-NLS-1$
			textDatabase.setText("northwind"); //$NON-NLS-1$
			textUser.setText("sa"); //$NON-NLS-1$
			textPassword.setText("tadpole"); //$NON-NLS-1$
			
//			textJDBCOptions.setText(";loginTimeout=5;socketTimeout=5"); //$NON-NLS-1$
			
		} else {
			textPort.setText("1433"); //$NON-NLS-1$
//			textJDBCOptions.setText(";loginTimeout=5;socketTimeout=5"); //$NON-NLS-1$
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
		
		preDBInfo.getTextDisplayName().setFocus();
	}
	
	/**
	 * 화면에 값이 올바른지 검사합니다.
	 * 
	 * @return
	 */
	public boolean isValidateInput(boolean isTest) {
		if(!ValidChecker.checkTextCtl(preDBInfo.getComboGroup(), Messages.get().GroupName)) return false;
		if(!ValidChecker.checkTextCtl(preDBInfo.getTextDisplayName(), Messages.get().DisplayName)) return false;
		
		if(!ValidChecker.checkTextCtl(textHost, Messages.get().Host)) return false;
		if(!ValidChecker.checkNumberCtl(textPort, Messages.get().Port)) return false;
//		MSSQL은 인스턴스 이름이 없으면 마스터로 접속합니다. 해서 입력하지 않아도 접속하도록 수정합니다.
//		if(!ValidChecker.checkTextCtl(textDatabase, Messages.get().Database)) return false;
		if(!ValidChecker.checkTextCtl(textUser, Messages.get().User)) return false;
		
		return true;
	}
	
	/**
	 * ip정보에 instance 명 검사해서 ping합니다.
	 */
	public boolean isPing(String strHost, String port) {
		if(StringUtils.contains(strHost, "\\")) {
			String strIp = StringUtils.substringBefore(strHost, "\\");
			
			return ValidChecker.isPing(strIp, port);
		} else if(StringUtils.contains(strHost, "/")) {
			String strIp 		= StringUtils.substringBefore(strHost, "/");

			return ValidChecker.isPing(strIp, port);
		} else {		
			return ValidChecker.isPing(strHost, port);
		}
	}
	
	@Override
	public boolean saveDBData() {
		if(!testConnection(false)) return false;
		
		// 기존 데이터 업데이트
		if(getDataActionStatus() == DATA_STATUS.MODIFY) {
			if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			try {
				TadpoleSystem_UserDBQuery.updateUserDB(userDB, oldUserDB, SessionManager.getUserSeq());
			} catch (Exception e) {
				logger.error("MSSQL connection", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, Messages.get().SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		// 신규 데이터 저장.
		} else {
			
			if(userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT) {
				int intVersion = 0;
				
				try {
					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);				
					// 디비 버전을 찾아옵니다.
					DBInfoDAO dbInfo = (DBInfoDAO)sqlClient.queryForObject("findDBInfo"); //$NON-NLS-1$
					intVersion = Integer.parseInt( StringUtils.substringBefore(dbInfo.getProductversion(), ".") );
					
					if(intVersion <= 8) {
						userDB.setDbms_type(DBDefine.MSSQL_8_LE_DEFAULT.getDBToString());
					}
				} catch (Exception e) {
					logger.error("MSSQL Connection", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, Messages.get().MSSQLLoginComposite_8, errStatus); //$NON-NLS-1$
					
					return false;
				}				
			}
			
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getUserSeq());
			} catch (Exception e) {
				logger.error("MSSQL connection save", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, Messages.get().MSSQLLoginComposite_10, errStatus); //$NON-NLS-1$
				
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String dbUrl = ""; 
		String strHost = StringUtils.trimToEmpty(textHost.getText());
		String strPort = StringUtils.trimToEmpty(textPort.getText());
		String strDB = StringUtils.trimToEmpty(textDatabase.getText());
		
		if(StringUtils.contains(strHost, "\\")) {
			
			String strIp 		= StringUtils.substringBefore(strHost, "\\");
			String strInstance	= StringUtils.substringAfter(strHost, "\\");
			
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					strIp, 
					strPort, 
					strDB) + ";instance=" + strInstance;			
		} else if(StringUtils.contains(strHost, "/")) {
			
			String strIp 		= StringUtils.substringBefore(strHost, "/");
			String strInstance	= StringUtils.substringAfter(strHost, "/");
			
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					strIp, 
					strPort, 
					strDB) + ";instance=" + strInstance;
			
		} else {		
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					strHost, 
					strPort, 
					strDB);
		}
		if(!"".equals(textJDBCOptions.getText())) {
			if(StringUtils.endsWith(dbUrl, ";")) {
				dbUrl += textJDBCOptions.getText();
			} else {
				dbUrl += ";" + textJDBCOptions.getText();	
			}
		}

		if(logger.isDebugEnabled()) logger.debug("[db url]" + dbUrl);

		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setUrl_user_parameter(textJDBCOptions.getText());
		userDB.setDb(StringUtils.trimToEmpty(textDatabase.getText()));
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));

		String dbOpType = PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).name();
		userDB.setOperation_type(dbOpType);
		if(dbOpType.equals(PublicTadpoleDefine.DBOperationType.PRODUCTION.name()) || dbOpType.equals(PublicTadpoleDefine.DBOperationType.BACKUP.name()))
		{
			userDB.setIs_lock(PublicTadpoleDefine.YES_NO.YES.name());
		}

		userDB.setHost(StringUtils.trimToEmpty(textHost.getText()));
		userDB.setPort(StringUtils.trimToEmpty(textPort.getText()));
		userDB.setUsers(StringUtils.trimToEmpty(textUser.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textPassword.getText()));
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();
		
		return true;
	}

}
