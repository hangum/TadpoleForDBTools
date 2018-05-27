/*******************************************************************************
 * Copyright (c) 2018 hangum.
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionElasticsearchGroup;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * elasticsearch login composite
 * 
 * @author hangum
 *
 */
public class ElasticSEarchLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(ElasticSEarchLoginComposite.class);
	
	protected Group grpConnectionType;
//	protected Text textHost;
//	protected Text textPort;
	protected Text textURL;
	
	protected Text textUser;
	protected Text textPassword;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ElasticSEarchLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB, boolean isReadOnly) {
		super(CommonMessages.get().ElasticSearch, DBDefine.ELASTICSEARCH_DEFAULT, parent, style, listGroupName, selGroupName, userDB, isReadOnly);
	}
	
	@Override
	protected void crateComposite() {
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
		compositeBody.setEnabled(isReadOnly);

		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.get().MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(4, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().DatabaseInformation);
		
//		Label lblHost = new Label(grpConnectionType, SWT.NONE);
//		lblHost.setText(Messages.get().Host);
//		
//		textHost = new Text(grpConnectionType, SWT.BORDER);
//		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
//		lblNewLabelPort.setText(Messages.get().Port);
//		
//		textPort = new Text(grpConnectionType, SWT.BORDER);
//		GridData gd_textPort = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
//		gd_textPort.widthHint = 50;
//		textPort.setLayoutData(gd_textPort);
//		
//		Button btnPing = new Button(grpConnectionType, SWT.NONE);
//		btnPing.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				pingTest(textHost.getText(), textPort.getText());
//			}
//		});
//		btnPing.setText(Messages.get().PingTest);
		
		Label lblURL = new Label(grpConnectionType, SWT.NONE);
		lblURL.setText(Messages.get().URL);
		
		textURL = new Text(grpConnectionType, SWT.BORDER);
		textURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.get().User);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.get().Password);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		othersConnectionInfo = new OthersConnectionElasticsearchGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		othersConnectionInfo.setEnabled(isReadOnly);
		
		init();
	}
	
	@Override
	protected void init() {
		// change group title
		grpConnectionType.setText(
				String.format("%s %s", selectDB.getDBToString() , Messages.get().DatabaseInformation)
		);
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( PublicTadpoleDefine.DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
//			textHost.setText(oldUserDB.getHost());
//			textPort.setText(oldUserDB.getPort());
			textURL.setText(oldUserDB.getUrl());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
			othersConnectionInfo.setUserData(oldUserDB);
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {
			preDBInfo.setTextDisplayName(getDisplayName());
			
			textURL.setText("http://192.168.99.100:32769");
//			textHost.setText("127.0.0.1");
//			textPort.setText("9200");
//		} else {
//			textPort.setText("9200"); //$NON-NLS-1$
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
	@Override
	public boolean isValidateInput(boolean isTest) {
		if(!ValidChecker.checkTextCtl(preDBInfo.getComboGroup(), Messages.get().GroupName)) return false;
		if(!ValidChecker.checkTextCtl(preDBInfo.getTextDisplayName(), Messages.get().DisplayName)) return false;
		
		if(!ValidChecker.checkTextCtl(textURL, Messages.get().URL)) return false;
//		if(!ValidChecker.checkNumberCtl(textPort, Messages.get().Port)) return false;
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
//		String dbUrl = String.format("%s:%s", StringUtils.trimToEmpty(textHost.getText()), StringUtils.trimToEmpty(textPort.getText()));
		
		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(textURL.getText());
		userDB.setDb("");

		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));

		String dbOpType = PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).name();
		userDB.setOperation_type(dbOpType);

//		userDB.setHost(StringUtils.trimToEmpty(textHost.getText()));
//		userDB.setPort(StringUtils.trimToEmpty(textPort.getText()));
		userDB.setUsers(StringUtils.trimToEmpty(textUser.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textPassword.getText()));
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString());
		userDB.setIs_resource_download(GetAdminPreference.getIsDefaultDonwload());
		
		// set ext value
		setExtValue();
		
		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();
		
		return true;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

}
