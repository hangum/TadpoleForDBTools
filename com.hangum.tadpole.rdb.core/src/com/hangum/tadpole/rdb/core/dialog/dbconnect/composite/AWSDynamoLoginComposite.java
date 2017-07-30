/*******************************************************************************
 * Copyright (c) 2017 hangum.
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;

/**
 * Amazon DynamoDB composite.
 * 
 * Amazon DynamoDB Region(http://docs.aws.amazon.com/general/latest/gr/rande.html#rds_region)
 * 
 * @author hangum
 *
 */
public class AWSDynamoLoginComposite extends AbstractLoginComposite {
	private static final Logger logger = Logger.getLogger(AWSDynamoLoginComposite.class);
	
	protected Group grpConnectionType;
	
	private Text textAccesskey;
	private Text textSecretKey;
	private Combo comboRegionName;
	
	protected Text textJDBCOptions;

	public AWSDynamoLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB, boolean isReadOnly) {
		super("Sample DynamoDB", DBDefine.DYNAMODB_DEFAULT, parent, style, listGroupName, selGroupName, userDB, isReadOnly);
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
		compositeBody.setEnabled(isReadOnly);
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.get().MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(2, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.get().DatabaseInformation);

		Label lblAccesskey = new Label(grpConnectionType, SWT.NONE);
		lblAccesskey.setText(Messages.get().AssesKey);
		textAccesskey = new Text(grpConnectionType, SWT.BORDER);
		textAccesskey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSecretKey = new Label(grpConnectionType, SWT.NONE);
		lblSecretKey.setText(Messages.get().SecretKey);
		textSecretKey = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEndpoint = new Label(grpConnectionType, SWT.NONE);
		lblEndpoint.setText(Messages.get().AWSRDSLoginComposite_6);
		
		comboRegionName = new Combo(grpConnectionType, SWT.READ_ONLY);
		comboRegionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRegionName.add("US East (Ohio)");
		comboRegionName.add("US East (N. Virginia)");
		comboRegionName.add("US West (N. California)");
		comboRegionName.add("US West (Oregon)");
		comboRegionName.add("Canada (Central)");
		comboRegionName.add("Asia Pacific (Mumbai)");
		comboRegionName.add("Asia Pacific (Seoul)");
		comboRegionName.add("Asia Pacific (Singapore)");
		comboRegionName.add("Asia Pacific (Sydney)");
		comboRegionName.add("Asia Pacific (Tokyo)");
		comboRegionName.add("EU (Frankfurt)");
		comboRegionName.add("EU (Ireland)");
		comboRegionName.add("EU (London)");
		comboRegionName.add("South America (São Paulo)");
		comboRegionName.setVisibleItemCount(14);
		comboRegionName.select(0);
		
		comboRegionName.setData("US East (Ohio)", 			"us-east-2");
		comboRegionName.setData("US East (N. Virginia)", 	"us-east-1");
		comboRegionName.setData("US West (N. California)", 	"us-west-1");
		comboRegionName.setData("US West (Oregon)",			"us-west-2");
		comboRegionName.setData("Canada (Central)", 		"ca-central-1");
		comboRegionName.setData("Asia Pacific (Mumbai)", 	"ap-south-1");
		comboRegionName.setData("Asia Pacific (Seoul)",		"ap-northeast-2");
		comboRegionName.setData("Asia Pacific (Singapore)", "ap-northeast-1");
		comboRegionName.setData("Asia Pacific (Sydney)",	"ap-southeast-2");
		comboRegionName.setData("Asia Pacific (Tokyo)",		"ap-northeast-1");
		comboRegionName.setData("EU (Frankfurt)",			"eu-central-1");
		comboRegionName.setData("EU (Ireland)",				"eu-west-1");
		comboRegionName.setData("EU (London)",				"eu-west-2");
		comboRegionName.setData("South America (São Paulo)", "sa-east-1");
		
		Label lblJdbcOptions = new Label(grpConnectionType, SWT.NONE);
		lblJdbcOptions.setText(Messages.get().JDBCOptions);
		
		textJDBCOptions = new Text(grpConnectionType, SWT.BORDER);
		textJDBCOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		othersConnectionInfo = new OthersConnectionRDBGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		othersConnectionInfo.setEnabled(isReadOnly);
		
		init();
	}

	@Override
	protected void init() {
		grpConnectionType.setText(
				String.format("%s %s", selectDB.getDBToString() , Messages.get().DatabaseInformation)
		);
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( PublicTadpoleDefine.DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textAccesskey.setText(oldUserDB.getUsers());
			textSecretKey.setText(oldUserDB.getPasswd());
			comboRegionName.setText(oldUserDB.getSchema());
			
			textJDBCOptions.setText(oldUserDB.getUrl_user_parameter());
			othersConnectionInfo.setUserData(oldUserDB);
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {
			preDBInfo.setTextDisplayName(getDisplayName());
			
			textAccesskey.setText("");
			textSecretKey.setText("");
			comboRegionName.setText("");
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
	
	@Override
	public boolean isValidateInput(boolean isTest) {
		String strAccesskey = StringUtils.trimToEmpty(textAccesskey.getText());
		String strSecretkey = StringUtils.trimToEmpty(textSecretKey.getText());
		
		if("".equals(strAccesskey)) { //$NON-NLS-1$
			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().AWSRDSLoginComposite_7);
			textAccesskey.setFocus();
			return false;
		} else if("".equals(strSecretkey)) { //$NON-NLS-1$
			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().AWSRDSLoginComposite_20);
			textSecretKey.setFocus();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
//		return "jdbc:dynamodb:Host=dynamodb.%s.amazonaws.com;Region=%s;AccessKey=%s;SecretKey=%s";
		String strRegion = (String)comboRegionName.getData(comboRegionName.getText());
		String dbUrl = String.format(
				getSelectDB().getDB_URL_INFO(), 
				strRegion, strRegion,
				StringUtils.trimToEmpty(textAccesskey.getText()), 
				StringUtils.trimToEmpty(textSecretKey.getText()) 
			);
	
		if(!"".equals(textJDBCOptions.getText())) {
			dbUrl += ";" + textJDBCOptions.getText();
		}
		
		userDB = new UserDBDAO();
		userDB.setDbms_type(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setUrl_user_parameter(textJDBCOptions.getText());
		userDB.setDb(StringUtils.trimToEmpty(comboRegionName.getText()));
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));
		String dbOpType = PublicTadpoleDefine.DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).name();
		userDB.setOperation_type(dbOpType);
		
		userDB.setUsers(StringUtils.trimToEmpty(textAccesskey.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textSecretKey.getText()));
		
		userDB.setIs_resource_download(GetAdminPreference.getIsDefaultDonwload());
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString());
		
		// set ext value
		setExtValue();
		
		// other connection info
		setOtherConnectionInfo();
		
		return true;
	}

}
