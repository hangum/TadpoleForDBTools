/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;

/**
 * sqlite login composite
 * 
 * @author hangum
 *
 */
public class SQLiteLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -444340316081961365L;

	private static final Logger logger = Logger.getLogger(SQLiteLoginComposite.class);
	
	protected Combo comboGroup;
	protected Text textFile;
	protected Text textDisplayName;
	
	protected Button btnSavePreference;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SQLiteLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super(DBDefine.SQLite_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
		setText(Messages.SQLiteLoginComposite_0);
	}
	
	@Override
	protected void crateComposite() {
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite container = new Composite(this, SWT.NONE);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 3;
		gl_container.horizontalSpacing = 3;
		gl_container.marginHeight = 3;
		gl_container.marginWidth = 3;
		container.setLayout(gl_container);
		container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblGroup = new Label(compositeBody, SWT.NONE);
		lblGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroup.setText(Messages.SQLiteLoginComposite_lblGroup_text);
		
		comboGroup = new Combo(compositeBody, SWT.NONE);
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (String strGroup : listGroupName) comboGroup.add(strGroup);
		
		Label lblDisplayName = new Label(compositeBody, SWT.NONE);
		lblDisplayName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDisplayName.setText(Messages.SQLiteLoginComposite_2);
		
		textDisplayName = new Text(compositeBody, SWT.BORDER);
		textDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		new Label(compositeBody, SWT.NONE);
		
		Label lblDbFile = new Label(compositeBody, SWT.NONE);
		lblDbFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDbFile.setText(Messages.SQLiteLoginComposite_1);
		
		textFile = new Text(compositeBody, SWT.BORDER);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		btnSavePreference = new Button(compositeBody, SWT.CHECK);
		btnSavePreference.setText(Messages.SQLiteLoginComposite_btnSavePreference_text);
		btnSavePreference.setSelection(true);
		
		init();
	}

	@Override
	protected void init() {
		
		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			textFile.setText(oldUserDB.getDb());
			textDisplayName.setText(oldUserDB.getDisplay_name());
		} else if(ApplicationArgumentUtils.isTestMode()) {
			
			textDisplayName.setText("Sample SQLite 3.7.2");			
			textFile.setText("C:/tadpole-test.db");//Messages.SQLiteLoginComposite_3); //$NON-NLS-1$
		}
		
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
		
	}

	@Override
	protected boolean connection() {
		String strFile = StringUtils.trimToEmpty(textFile.getText());
		
		if("".equals(comboGroup.getText().trim())) {
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, "Group" + Messages.MySQLLoginComposite_10);
			return false;
		} else if("".equals( strFile ) ) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_7);
			return false;
		} else if("".equals(StringUtils.trimToEmpty(textDisplayName.getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_12 );
			return false;
		}
		
		if( !new File(strFile).exists() ) {
			if( !MessageDialog.openConfirm(null, Messages.SQLiteLoginComposite_6, Messages.SQLiteLoginComposite_9) ) return false; 
		}
		
		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.SQLite_DEFAULT.getDBToString());
		userDB.setUrl(String.format(DBDefine.SQLite_DEFAULT.getDB_URL_INFO(), textFile.getText()));
		userDB.setDb(textFile.getText());
		userDB.setGroup_name(comboGroup.getText().trim());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setPasswd(""); //$NON-NLS-1$
		userDB.setUsers(""); //$NON-NLS-1$
		
		// 기존 데이터 업데이트
		if(oldUserDB != null) {
			if(!MessageDialog.openConfirm(null, "Confirm", Messages.SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			if(!checkDatabase(userDB)) return false;
			
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
			// 이미 연결한 것인지 검사한다.
			if(!connectValidate(userDB)) return false;
			
			// preference에 save합니다.
			if(btnSavePreference.getSelection()) {
				try {
					TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
				} catch (Exception e) {
					logger.error(Messages.SQLiteLoginComposite_8, e);
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
					
					return false;
				}
			}
			
		}
		
		return true;		
	}

}
