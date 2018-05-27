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

import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.utils.DBLocaleUtils;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.HistoryHubConnectionGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * oracle login composite
 * 
 * 오라클은 jdbc
 * 
 * SID   jdbc:oracle:thin:@//hostname:port:sid
 * Service Name  jdbc:oracle:thin:@//hostname:port/serviceName
 * 
 * @author hangum
 *
 */
public class OracleLoginComposite extends AbstractLoginComposite {
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(OracleLoginComposite.class);
	
	protected Group grpConnectionType;
	/** sid, service name */
	protected Combo comboConnType;

	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	protected Combo comboLocale;
	
	protected Text textJDBCOptions;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OracleLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB, boolean isUIReadOnly) {
		super("Sample Oracle", DBDefine.ORACLE_DEFAULT, parent, style, listGroupName, selGroupName, userDB, isUIReadOnly);
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
		preDBInfo.setEnabled(isReadOnly);
		
		grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(5, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().DatabaseInformation);
		
		Label lblHost = new Label(grpConnectionType, SWT.NONE);
		lblHost.setText(Messages.get().Host);
		
		textHost = new Text(grpConnectionType, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textHost.setEnabled(isReadOnly);
		
		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelPort.setText(Messages.get().Port);
		
		textPort = new Text(grpConnectionType, SWT.BORDER);
		GridData gd_textPort = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textPort.widthHint = 50;
		textPort.setLayoutData(gd_textPort);
		textPort.setEnabled(isReadOnly);
		
		Button btnPing = new Button(grpConnectionType, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pingTest(textHost.getText(), textPort.getText());
			}
		});
		btnPing.setText(Messages.get().PingTest);
		
		comboConnType = new Combo(grpConnectionType, SWT.READ_ONLY);
		GridData gd_comboConnType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboConnType.minimumWidth = 100;
		gd_comboConnType.widthHint = 100;
		comboConnType.setLayoutData(gd_comboConnType);
		comboConnType.add("SID");
		comboConnType.add("Service Name");
		comboConnType.select(0);
		
		textDatabase = new Text(grpConnectionType, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		textDatabase.setEnabled(isReadOnly);
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.get().User);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUser.setEnabled(isReadOnly);
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.get().Password);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		textPassword.setEnabled(isReadOnly);
		new Label(grpConnectionType, SWT.NONE);
		
		Label label = new Label(grpConnectionType, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		Label lblJdbcOptions = new Label(grpConnectionType, SWT.NONE);
		lblJdbcOptions.setText(Messages.get().JDBCOptions);
		
		textJDBCOptions = new Text(grpConnectionType, SWT.BORDER);
		textJDBCOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		textJDBCOptions.setEnabled(isReadOnly);
		
		Label lblLocale = new Label(grpConnectionType, SWT.NONE);
		lblLocale.setText(Messages.get().CharacterSet);
		
		comboLocale = new Combo(grpConnectionType, SWT.NONE);
		comboLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		comboLocale.setVisibleItemCount(12);
			
		for(String val : DBLocaleUtils.getOracleList()) {
			comboLocale.add(val);
			comboLocale.setData(StringUtils.substringBefore(val, "|").trim(), val);
		}
		comboLocale.select(0);
		
		if(PublicTadpoleDefine.ACTIVE_PRODUCT_TYPE == PublicTadpoleDefine.PRODUCT_TYPE.TadpoleHistoryHub) {
			othersConnectionInfo = new HistoryHubConnectionGroup(this, SWT.NONE, getSelectDB());
		} else {
			othersConnectionInfo = new OthersConnectionRDBGroup(this, SWT.NONE, getSelectDB());
		}
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		othersConnectionInfo.setEnabled(isReadOnly);


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
			if("".equals(oldUserDB.getExt1())) {
				comboConnType.select(0);
			} else {
				comboConnType.setText(oldUserDB.getExt1());
			}
			textDatabase.setText(oldUserDB.getDb());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
			comboLocale.setText(oldUserDB.getLocale()==null?DBLocaleUtils.NONE_TXT:oldUserDB.getLocale());
			
			othersConnectionInfo.setUserData(oldUserDB);
			
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("192.168.29.128"); //$NON-NLS-1$
			textPort.setText("1521"); //$NON-NLS-1$
			textDatabase.setText("XE"); //$NON-NLS-1$
			textUser.setText("HR"); //$NON-NLS-1$
			textPassword.setText("tadpole"); //$NON-NLS-1$
//			
//			오라클은 옵션을 프로퍼티로 설정해야합니다. 
//			
//			textJDBCOptions.setText("oracle.net.CONNECT_TIMEOUT=10000;oracle.jdbc.ReadTimeout=10000"); //$NON-NLS-1$
		} else {
			textPort.setText("1521"); //$NON-NLS-1$
//			textJDBCOptions.setText("oracle.net.CONNECT_TIMEOUT=10000;oracle.jdbc.ReadTimeout=10000"); //$NON-NLS-1$
		}
		
		Combo comboGroup = preDBInfo.getComboGroup();
		if(comboGroup.getItems().length == 0) {
			if("".equals(selGroupName)) comboGroup.add(strOtherGroupName);
			else comboGroup.setText(selGroupName);

			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) comboGroup.setText(strOtherGroupName);
			else comboGroup.setText(selGroupName);
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
		if(!ValidChecker.checkTextCtl(textDatabase, Messages.get().Database)) return false;
		if(!ValidChecker.checkTextCtl(textUser, Messages.get().User)) return false;
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String dbUrl = "";
		String selectLocale = StringUtils.trimToEmpty(comboLocale.getText()) == DBLocaleUtils.NONE_TXT?"":StringUtils.trimToEmpty(comboLocale.getText());
		if(comboConnType.getText().equals("SID")) {
			dbUrl = String.format(
						getSelectDB().getDB_URL_INFO(), 
						StringUtils.trimToEmpty(textHost.getText()), 
						StringUtils.trimToEmpty(textPort.getText()), 
						StringUtils.trimToEmpty(textDatabase.getText())
					);
		} else if(comboConnType.getText().equals("Service Name")) {
			dbUrl = String.format(
						"jdbc:oracle:thin:@//%s:%s/%s", 
						StringUtils.trimToEmpty(textHost.getText()), 
						StringUtils.trimToEmpty(textPort.getText()), 
						StringUtils.trimToEmpty(textDatabase.getText())
					);
		}
		if(!"".equals(textJDBCOptions.getText())) {
			dbUrl += "?" + textJDBCOptions.getText();
		}

		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setUrl_user_parameter(textJDBCOptions.getText());
		userDB.setDb(StringUtils.trimToEmpty(textDatabase.getText()));
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));

		String dbOpType = PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).name();
		userDB.setOperation_type(dbOpType);
//		if(dbOpType.equals(PublicTadpoleDefine.DBOperationType.PRODUCTION.name()) || dbOpType.equals(PublicTadpoleDefine.DBOperationType.BACKUP.name()))
//		{
//			userDB.setIs_lock(PublicTadpoleDefine.YES_NO.YES.name());
//		}

		userDB.setHost(StringUtils.trimToEmpty(textHost.getText()));
		userDB.setPort(StringUtils.trimToEmpty(textPort.getText()));
		userDB.setUsers(StringUtils.trimToEmpty(textUser.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textPassword.getText()));
		
		userDB.setLocale(selectLocale);
		userDB.setIs_resource_download(GetAdminPreference.getIsDefaultDonwload());

		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString());

		// sid or service name
		userDB.setExt1(comboConnType.getText());
		if(oldUserDB != null) oldUserDB.setExt1(comboConnType.getText());
		
		// set ext value
		setExtValue();

		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();

		return true;
	}
	
}
