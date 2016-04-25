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
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionBigDataGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;

/**
 * hive connect dialog
 * 
 * @author hangum
 *
 */
public class HiveLoginComposite extends AbstractLoginComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HiveLoginComposite.class);
	
	protected Group grpConnectionType;
	protected Combo comboDriverType;
	
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	
	protected Text textJDBCOptions;
	
	protected OthersConnectionBigDataGroup othersConnectionInfo;
	
	/**
	 * @wbp.parser.constructor
	 */
	public HiveLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample Apache Hive", DBDefine.HIVE_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	public HiveLoginComposite(String strDisplayName, DBDefine selectDB,
			Composite parent, int style, List<String> listGroupName,
			String selGroupName, UserDBDAO userDB) {
		super(strDisplayName, selectDB, parent, style, listGroupName, selGroupName, userDB);
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
		
		Label lblDriverType = new Label(grpConnectionType, SWT.NONE);
		lblDriverType.setText(Messages.get().DriverType);
		
		comboDriverType = new Combo(grpConnectionType, SWT.READ_ONLY);
		comboDriverType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
//		comboDriverType.add("Hive Server 1");
//		comboDriverType.setData("Hive Server 1", DBDefine.HIVE_DEFAULT);
		
		comboDriverType.add("Hive Server 2");
		comboDriverType.setData("Hive Server 2", DBDefine.HIVE2_DEFAULT);
		
		comboDriverType.select(0);
//		comboDriverType.setEnabled(false);
		
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
		
		othersConnectionInfo = new OthersConnectionBigDataGroup(this, SWT.NONE, getSelectDB());
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
			
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
			
		 } else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("127.0.0.1"); //$NON-NLS-1$
			textPort.setText("10002"); //$NON-NLS-1$
			textDatabase.setText("default"); //$NON-NLS-1$
			textUser.setText(""); //$NON-NLS-1$
			textPassword.setText(""); //$NON-NLS-1$
			
		} else {
			textPort.setText("10002"); //$NON-NLS-1$
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
		
		// Initialize otherConnectionComposite
		othersConnectionInfo.callBackUIInit(textHost.getText());
		
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
		
		return true;
	}
	
	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		DBDefine selectDB = (DBDefine)comboDriverType.getData(comboDriverType.getText());
		
		String dbUrl = String.format(
				selectDB.getDB_URL_INFO(), 
				StringUtils.trimToEmpty(textHost.getText()), 
				StringUtils.trimToEmpty(textPort.getText()), 
				StringUtils.trimToEmpty(textDatabase.getText())
			);
		
		if(!"".equals(textJDBCOptions.getText())) {
			dbUrl += "?" + textJDBCOptions.getText();
		}

		userDB = new UserDBDAO();
		userDB.setDbms_type(selectDB.getDBToString());
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
		
		// others connection 정보를 입력합니다.
//		setOtherConnectionInfo();
		OthersConnectionInfoDAO otherConnectionDAO =  othersConnectionInfo.getOthersConnectionInfo();
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_autocommit(otherConnectionDAO.isAutoCommit()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_showtables(otherConnectionDAO.isShowTables()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
//		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
//		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
//		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
		userDB.setIs_external_browser(otherConnectionDAO.isExterBrowser()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		return true;
	}
	
	public String getTextHost() {
		return textHost.getText();
	}
}
