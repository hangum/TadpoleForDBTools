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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DBOperationType;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	private static final Logger logger = Logger.getLogger(CubridLoginComposite.class);
	
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
		
		 if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textHost.setText(oldUserDB.getHost());
			textPort.setText(oldUserDB.getPort());
			textDatabase.setText(oldUserDB.getDb());			
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
		} else if(ApplicationArgumentUtils.isTestMode()) {

			preDBInfo.setTextDisplayName(getDisplayName());
			
			textHost.setText("192.168.32.128");
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
		
		textHost.setFocus();
	}
	
	@Override
	public boolean connection() {
		if(!testConnection()) return false;
		
		// 기존 데이터 업데이트
		if(getDalog_status() == DATA_STATUS.MODIFY) {
			if(!MessageDialog.openConfirm(null, "Confirm", Messages.SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
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
	
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error("cubrid db preference save", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_11, errStatus); //$NON-NLS-1$
			}
		}
		return true;
	}
	
	@Override
	public boolean makeUserDBDao() {
		if(!isValidateInput()) return false;
		
		final String dbUrl = String.format(
				getSelectDB().getDB_URL_INFO(), 
				textHost.getText().trim(), textPort.getText().trim(), textDatabase.getText()).trim();

		userDB = new UserDBDAO();
		userDB.setDbms_types(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText().trim());
		userDB.setGroup_seq(SessionManager.getGroupSeq());
		userDB.setGroup_name(preDBInfo.getComboGroup().getText().trim());
		userDB.setDisplay_name(preDBInfo.getTextDisplayName().getText().trim());
		userDB.setOperation_type( DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).toString() );
		userDB.setHost(textHost.getText().trim());
		userDB.setPort(textPort.getText().trim());
		userDB.setUsers(textUser.getText().trim());
		userDB.setPasswd(textPassword.getText().trim());
		userDB.setLocale(comboLocale.getText().trim());
		
		// others connection 정보를 입력합니다.
		OthersConnectionInfoDAO otherConnectionDAO =  othersConnectionInfo.getOthersConnectionInfo();
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_autocommit(otherConnectionDAO.isAutoCommit()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_showtables(otherConnectionDAO.isShowTables()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		return true;
	}

}
