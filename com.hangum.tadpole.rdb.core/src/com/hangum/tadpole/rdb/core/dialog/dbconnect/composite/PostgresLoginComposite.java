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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;

/**
 * postgresql login composite
 * 
 * @author hangum
 *
 */
public class PostgresLoginComposite extends MySQLLoginComposite {
	private static final Logger logger = Logger.getLogger(PostgresLoginComposite.class);
	protected Combo comboSSL;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PostgresLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample PostgreSQL", DBDefine.POSTGRE_DEFAULT, parent, style, listGroupName, selGroupName, userDB); //$NON-NLS-1$
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
		
		Label lblSupportSSL = new Label(grpConnectionType, SWT.NONE);
		lblSupportSSL.setText(Messages.get().PostgresLoginComposite_1);
		
		comboSSL = new Combo(grpConnectionType, SWT.READ_ONLY);
		comboSSL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		comboSSL.add(PublicTadpoleDefine.YES_NO.YES.name());
		comboSSL.add(PublicTadpoleDefine.YES_NO.NO.name());
		comboSSL.select(1);
		
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
			preDBInfo.getComboOperationType().setText( PublicTadpoleDefine.DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textHost.setText(oldUserDB.getHost());
			textPort.setText(oldUserDB.getPort());
			textDatabase.setText(oldUserDB.getDb());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
			comboSSL.setText(oldUserDB.getExt1());
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
			
			othersConnectionInfo.setUserData(oldUserDB);
			
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("127.0.0.1"); //$NON-NLS-1$
			textPort.setText("5432"); //$NON-NLS-1$
			textDatabase.setText("tadpole"); //$NON-NLS-1$
			textUser.setText("postgres"); //$NON-NLS-1$
			textPassword.setText("tadpole"); //$NON-NLS-1$
			
			comboSSL.setText("NO"); //$NON-NLS-1$
			textJDBCOptions.setText("&loginTimeout=10"); //$NON-NLS-1$
			
		} else {
			textPort.setText("5432"); //$NON-NLS-1$
			comboSSL.setText("NO"); //$NON-NLS-1$
			textJDBCOptions.setText("&loginTimeout=5&socketTimeout=5"); //$NON-NLS-1$
		}

		Combo comboGroup = preDBInfo.getComboGroup();
		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) { //$NON-NLS-1$
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
	
	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String dbUrl = String.format(
								getSelectDB().getDB_URL_INFO(), 
								StringUtils.trimToEmpty(textHost.getText()), 
								StringUtils.trimToEmpty(textPort.getText()), 
								StringUtils.trimToEmpty(textDatabase.getText())
							);
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(comboSSL.getText())) {
			dbUrl += "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"; //$NON-NLS-1$
			
			if(!"".equals(textJDBCOptions.getText())) { //$NON-NLS-1$
				dbUrl += "&" + textJDBCOptions.getText(); //$NON-NLS-1$
			}
		} else {
			if(!"".equals(textJDBCOptions.getText())) { //$NON-NLS-1$
				dbUrl += "?" + textJDBCOptions.getText(); //$NON-NLS-1$
			}
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
		
		userDB.setExt1(comboSSL.getText());
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();
		
		return true;
	}

}
