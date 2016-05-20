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
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionMongoDBGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.rdb.core.util.DBLocaleUtils;

/**
 * oracle login composite
 * 
 * @author hangum
 *
 */
public class MongoDBLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(MongoDBLoginComposite.class);
	
	protected Group grpConnectionType;
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	protected Combo comboLocale;
	
	protected Text textReplicaSet;
	
	protected Text textJDBCOptions;
	
	protected OthersConnectionMongoDBGroup othersConnectionInfo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MongoDBLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample MongoDB", DBDefine.MONGODB_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
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
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginWidth = 0;
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
		
		Label lblReplicaSet = new Label(grpConnectionType, SWT.NONE);
		lblReplicaSet.setText(Messages.get().MongoDBLoginComposite_lblReplicaSet_text);
		
		textReplicaSet = new Text(grpConnectionType, SWT.BORDER);
		textReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		new Label(grpConnectionType, SWT.NONE);
		
		Label lblExLocalhostlocalhost = new Label(grpConnectionType, SWT.NONE);
		lblExLocalhostlocalhost.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblExLocalhostlocalhost.setText(Messages.get().MongoDBLoginComposite_lblExLocalhostlocalhost_text);
		
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
		
		Label lblLocale = new Label(grpConnectionType, SWT.NONE);
		lblLocale.setText(Messages.get().CharacterSet);
		
		comboLocale = new Combo(grpConnectionType, SWT.NONE);
		comboLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
			
		for(String val : DBLocaleUtils.getMySQLList()) comboLocale.add(val);
		comboLocale.setVisibleItemCount(12);
		comboLocale.select(0);
		
		othersConnectionInfo = new OthersConnectionMongoDBGroup(this, SWT.NONE, getSelectDB());
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
			
			textReplicaSet.setText(oldUserDB.getExt1()==null?"":oldUserDB.getExt1());
			
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName()); //$NON-NLS-1$
			
			textHost.setText("127.0.0.1"); //$NON-NLS-1$
			textPort.setText("27017");			 //$NON-NLS-1$
			textDatabase.setText("test"); //$NON-NLS-1$
			textUser.setText(""); //$NON-NLS-1$
			textPassword.setText(""); //$NON-NLS-1$
			textJDBCOptions.setText("maxPoolSize=20&connectTimeoutMS=3000&socketTimeoutMS=3000");
			
		} else {
			textPort.setText("27017");			 //$NON-NLS-1$
		}
		
		Combo comboGroup = preDBInfo.getComboGroup();
		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) { //$NON-NLS-1$
				comboGroup.setText(strOtherGroupName);
			} else {
				for(int i=0; i<comboGroup.getItemCount(); i++) {
					if(comboGroup.getItem(i).equals(selGroupName)) comboGroup.select(i);
				}
			}
		}
		
		//
		othersConnectionInfo.callBackUIInit(textHost.getText(), textPort.getText());
		
		preDBInfo.getTextDisplayName().setFocus();//textHost.setFocus();
	}
	
	@Override
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
		
		String dbUrl = String.format(
								getSelectDB().getDB_URL_INFO(), 
								StringUtils.trimToEmpty(textHost.getText()), 
								StringUtils.trimToEmpty(textPort.getText()), 
								StringUtils.trimToEmpty(textDatabase.getText())
							);
		if(!"".equals(textJDBCOptions.getText())) {
			dbUrl += "/?" + textJDBCOptions.getText();
		}

		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setUrl_user_parameter(textJDBCOptions.getText());
		userDB.setDb(textDatabase.getText());
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));
		userDB.setOperation_type( PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).toString() );
		userDB.setHost(StringUtils.trimToEmpty(textHost.getText()));
		userDB.setPort(StringUtils.trimToEmpty(textPort.getText()));
		userDB.setUsers(StringUtils.trimToEmpty(textUser.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textPassword.getText()));
		userDB.setLocale(StringUtils.trimToEmpty(comboLocale.getText()));
		userDB.setExt1(StringUtils.trimToEmpty(textReplicaSet.getText()));
		
		// others connection 정보를 입력합니다.
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
		userDB.setListExternalBrowserdao(otherConnectionDAO.getListExterBroswer());
		
//		userDB.setIs_visible(otherConnectionDAO.isVisible()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_summary_report(otherConnectionDAO.isSummaryReport()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		return true;
	}

}
