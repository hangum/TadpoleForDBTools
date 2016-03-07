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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;

/**
 * oracle login composite
 * 
 * SID   jdbc:oracle:thin:@//hostname:port:sid
 * Service Name  jdbc:oracle:thin:@//hostname:port/serviceName
 * 
 * @author hangum
 *
 */
public class TiberoLoginComposite extends AbstractLoginComposite {
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(TiberoLoginComposite.class);
	
	/** sid, service name */
	protected Combo comboConnType;

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
	public TiberoLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample Tibero", DBDefine.TIBERO_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	@Override
	public void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.get().MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Group grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(5, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().MSSQLLoginComposite_grpConnectionType_text);
		
		Label lblHost = new Label(grpConnectionType, SWT.NONE);
		lblHost.setText(Messages.get().DBLoginDialog_1);
		
		textHost = new Text(grpConnectionType, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelPort.setText(Messages.get().DBLoginDialog_5);
		
		textPort = new Text(grpConnectionType, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnPing = new Button(grpConnectionType, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host 	= StringUtils.trimToEmpty(textHost.getText());
				String port 	= StringUtils.trimToEmpty(textPort.getText());
				
				if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
					MessageDialog.openError(null, Messages.get().DBLoginDialog_10, Messages.get().DBLoginDialog_11);
					return;
				}
				
				try {
					if(isPing(host, port)) {
						MessageDialog.openInformation(null, Messages.get().DBLoginDialog_12, Messages.get().DBLoginDialog_13);
					} else {
						MessageDialog.openError(null, Messages.get().DBLoginDialog_14, Messages.get().DBLoginDialog_15);
					}
				} catch(NumberFormatException nfe) {
					MessageDialog.openError(null, Messages.get().MySQLLoginComposite_3, Messages.get().MySQLLoginComposite_4);
				}
			}
		});
		btnPing.setText(Messages.get().DBLoginDialog_btnPing_text);
		
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
		
		Label lblJdbcOptions = new Label(grpConnectionType, SWT.NONE);
		lblJdbcOptions.setText(Messages.get().MySQLLoginComposite_lblJdbcOptions_text);
		
		textJDBCOptions = new Text(grpConnectionType, SWT.BORDER);
		textJDBCOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.get().DBLoginDialog_2);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.get().DBLoginDialog_3);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		othersConnectionInfo = new OthersConnectionRDBGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		init();
	}
	
	@Override
	public void init() {
		
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

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("192.168.29.141");
			textPort.setText("8629");
			textDatabase.setText("tibero");
			textUser.setText("sys");
			textPassword.setText("tibero");
			
		} else {
			textPort.setText(Messages.get().OracleLoginComposite_4);
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
		if(!checkTextCtl(preDBInfo.getComboGroup(), "Group")) return false;
		if(!checkTextCtl(preDBInfo.getTextDisplayName(), "Display Name")) return false; //$NON-NLS-1$
		
		if(!checkTextCtl(textHost, "Host")) return false; //$NON-NLS-1$
		if(!checkTextCtl(textPort, "Port")) return false; //$NON-NLS-1$
		if(!checkTextCtl(textDatabase, "Database")) return false; //$NON-NLS-1$
		if(!checkTextCtl(textUser, "User")) return false; //$NON-NLS-1$
//		if(!checkTextCtl(textPassword, "Password")) return false; //$NON-NLS-1$
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String dbUrl = "";
		if(comboConnType.getText().equals("SID")) {
			dbUrl = String.format(
						getSelectDB().getDB_URL_INFO(), 
						StringUtils.trimToEmpty(textHost.getText()), 
						StringUtils.trimToEmpty(textPort.getText()), 
						StringUtils.trimToEmpty(textDatabase.getText())
					);
		} else if(comboConnType.getText().equals("Service Name")) {
			dbUrl = String.format(
						"jdbc:oracle:thin:@%s:%s/%s", 
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
		
//		userDB.setLocale(comboLocale.getText().trim());

		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();

		return true;
	}
	
}
