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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.DBLocaleUtils;

/**
 * cubrid login composite
 * 
 * @author hangum
 *
 */
public class CubridLoginComposite extends MySQLLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4519162649799179626L;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CubridLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample Cubrid", DBDefine.CUBRID_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
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
			comboLocale.setText(oldUserDB.getLocale());
			
			othersConnectionInfo.setUserData(oldUserDB);
			
		} else if(ApplicationArgumentUtils.isTestMode() || ApplicationArgumentUtils.isTestDBMode()) {

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("192.168.29.128");
			textPort.setText("33000");
			textDatabase.setText("demodb");
			textUser.setText("dba");
			textPassword.setText("");
			
		} else {
			textPort.setText("33000");
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
	
	@Override
	public boolean makeUserDBDao(boolean isTest) {
		if(!isValidateInput(isTest)) return false;
		
		String dbUrl = "";
		String selectLocale = StringUtils.trimToEmpty(comboLocale.getText());
		if(selectLocale.equals("") || DBLocaleUtils.NONE_TXT.equals(selectLocale)) {
			dbUrl = String.format(
				getSelectDB().getDB_URL_INFO(), 
				StringUtils.trimToEmpty(textHost.getText()), 
				StringUtils.trimToEmpty(textPort.getText()), 
				StringUtils.trimToEmpty(textDatabase.getText())
			);
			
			if(!"".equals(textJDBCOptions.getText())) {
				dbUrl += "?" + textJDBCOptions.getText();
			}
		} else {
			dbUrl = String.format(
					getSelectDB().getDB_URL_INFO(), 
					StringUtils.trimToEmpty(textHost.getText()), 
					StringUtils.trimToEmpty(textPort.getText()), 
					StringUtils.trimToEmpty(textDatabase.getText())) + "?charset=" + selectLocale;
			
			if(!"".equals(textJDBCOptions.getText())) {
				dbUrl += "&" + textJDBCOptions.getText();
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
		userDB.setLocale(StringUtils.trimToEmpty(comboLocale.getText()));
		
		// 처음 등록자는 권한이 어드민입니다.
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		// others connection 정보를 입력합니다.
		setOtherConnectionInfo();
		
		return true;
	}

}
