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

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBWithoutTunnelingGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.template.DBOperationType;

/**
 * sqlite login composite
 * 
 * @author hangum
 *
 */
public class SQLiteLoginComposite extends AbstractLoginComposite {
	
	protected OthersConnectionRDBWithoutTunnelingGroup othersConnectionInfo;
	protected Text textFile;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SQLiteLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample SQLite", DBDefine.SQLite_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	@Override
	protected void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Group grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(2, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.MSSQLLoginComposite_grpConnectionType_text);
		
		Label lblDbFile = new Label(grpConnectionType, SWT.NONE);
		lblDbFile.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblDbFile.setText(Messages.SQLiteLoginComposite_1);
		
		textFile = new Text(grpConnectionType, SWT.BORDER);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		othersConnectionInfo = new OthersConnectionRDBWithoutTunnelingGroup(this, SWT.NONE);
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		init();
	}

	@Override
	protected void init() {
		
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			textFile.setText(oldUserDB.getDb());
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
		} else if(ApplicationArgumentUtils.isTestMode()) {
			
			preDBInfo.setTextDisplayName(getDisplayName());
			
			// os타입별 기본 디렉토리를서정합니다.
			String defaultDir = System.getProperty("user.home") + File.separator + "Chinook_Sqlite.sqlite";
			textFile.setText(defaultDir);
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
		
		textFile.setFocus();
	}
	
	/**
	 * 화면에 값이 올바른지 검사합니다.
	 * 
	 * @return
	 */
	@Override
	public boolean isValidateInput() {
		String strFile = StringUtils.trimToEmpty(textFile.getText());
		
		if("".equals(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()))) {
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, "Group" + Messages.MySQLLoginComposite_10);
			return false;
		} else if("".equals(strFile) ) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_7);
			return false;
		} else if("".equals(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_12 );
			return false;
		}
		
		return true;
	}

	@Override
	public boolean makeUserDBDao() {
		if(!isValidateInput()) return false;
		
		userDB = new UserDBDAO();
		userDB.setDbms_types(getSelectDB().getDBToString());
		userDB.setUrl(String.format(getSelectDB().getDB_URL_INFO(), StringUtils.trimToEmpty(textFile.getText())));
		userDB.setDb(StringUtils.trimToEmpty(textFile.getText()));
		userDB.setGroup_seq(SessionManager.getGroupSeq());
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));
		userDB.setOperation_type(DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).toString());
		userDB.setUsers(""); //$NON-NLS-1$
		userDB.setPasswd(""); //$NON-NLS-1$
		
		// others connection 정보를 입력합니다.
		OthersConnectionInfoDAO otherConnectionDAO = othersConnectionInfo.getOthersConnectionInfo();
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
