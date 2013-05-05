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
package com.hangum.tadpole.manager.core.dialogs.users;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.UserGroupDAO;
import com.hangum.tadpole.define.DB_Define;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;

/**
 * 신규 유저 등록
 *  
 * @author hangum
 *
 */
public class NewUserDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(NewUserDialog.class);
	
	private Button btnManager;
	private Button btnUser;
	
	private Composite compositeUserGroup;
	/** user group combo로 기 존재하는 그룹 정보  */
	private Combo comboUserGroup;
	
	/** user group text로 신규 그룹명을 입력 받는다 */
	private Text textUserGroup;
	
	private Text textEMail;
	private Text textPasswd;
	private Text textRePasswd;
	private Text textName;
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewUserDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.NewUserDialog_0);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.numColumns = 2;
		
		Label lblUserType = new Label(container, SWT.NONE);
		lblUserType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserType.setText(Messages.NewUserDialog_lblUserType_text);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		btnManager = new Button(composite, SWT.RADIO);
		btnManager.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUserGroup();
			}
		});
		btnManager.setText(Messages.NewUserDialog_btnManager_text_1);
		
		btnUser = new Button(composite, SWT.RADIO);
		btnUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUserGroup();
			}
		});
		btnUser.setText(Messages.NewUserDialog_btnUser_text);
		btnUser.setSelection(true);
		
		Label lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroupName.setText(Messages.NewUserDialog_lblNewLabel_text);
		
		compositeUserGroup = new Composite(container, SWT.NONE);
		compositeUserGroup.setLayout(new GridLayout(1, false));
		compositeUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblIdemail = new Label(container, SWT.NONE);
		lblIdemail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIdemail.setText(Messages.NewUserDialog_1);
		
		textEMail = new Text(container, SWT.BORDER);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText(Messages.NewUserDialog_2);
		
		textPasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePassword.setText(Messages.NewUserDialog_3);
		
		textRePasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(Messages.NewUserDialog_4);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		textEMail.setFocus();
		
		initUserGroup();
		
		return container;
	}
	
	private void initUserGroup() {
		// 유저의 조건에 따른 화면을 초기화 한다.
		if(comboUserGroup != null) comboUserGroup.dispose();
		if(textUserGroup != null) textUserGroup.dispose();
		
		// user를 선택하면 그룹을 입력하도록 합니다.
		if(btnUser.getSelection()) {
			comboUserGroup = new Combo(compositeUserGroup, SWT.READ_ONLY);
			comboUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			try {
				List<UserGroupDAO> listUserGroup = TadpoleSystem_UserGroupQuery.getGroup();
				for (UserGroupDAO userGroupDAO : listUserGroup) {
					
					// admin group에는 등록이 되지 않도록 합니다.
					if(1 != userGroupDAO.getSeq() ) {
						
						// 처음 로그인일 경우에...
						int groupSeq = -1;
						try {
							// 이미 로그인 했을 경우 다이얼로그인창
							groupSeq = SessionManager.getGroupSeq();
						} catch(Exception e) {};
						
						if(userGroupDAO.getSeq() == groupSeq) {
							comboUserGroup.add(userGroupDAO.getName());
							comboUserGroup.setData(userGroupDAO.getName(), userGroupDAO.getSeq());
							
							comboUserGroup.setEnabled(false);
						} else {
							comboUserGroup.add(userGroupDAO.getName());
							comboUserGroup.setData(userGroupDAO.getName(), userGroupDAO.getSeq());
						}
					}
					
				}
				comboUserGroup.select(0);
			} catch (Exception e) {
				logger.error("initUserGroup", e); //$NON-NLS-1$
			}
		// 그룹을 선택하면 신규 그룹 이름을 입력합니다.
		} else {
			textUserGroup = new Text(compositeUserGroup, SWT.BORDER);
			textUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
		compositeUserGroup.layout();
	}
	
	@Override
	protected void okPressed() {
		
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String passwd = StringUtils.trimToEmpty(textPasswd.getText());
		String rePasswd = StringUtils.trimToEmpty(textRePasswd.getText());
		String name = StringUtils.trimToEmpty(textName.getText());
		
		if(!validation(strEmail, passwd, rePasswd, name)) return;
		
		// user 입력시 
		int groupSeq = 0;
		DB_Define.USER_TYPE userType = DB_Define.USER_TYPE.USER;
		if(btnUser.getSelection()) {
			groupSeq = (Integer)comboUserGroup.getData(comboUserGroup.getText());
		} else {
			String strGroupName = StringUtils.trimToEmpty(textUserGroup.getText());
			userType = DB_Define.USER_TYPE.MANAGER;
			// 그룹 등록
			try {
				groupSeq = TadpoleSystem_UserGroupQuery.newUserGroup(strGroupName);
			} catch(Exception e) {
				logger.error(Messages.NewUserDialog_8, e);
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_14, Messages.NewUserDialog_16 + e.getMessage());
				return;
			}
			
		}
		
		try {
			UserDAO loginDAO = TadpoleSystem_UserQuery.newUser(groupSeq, strEmail, passwd, name, userType);
			
			if(!ApplicationArgumentUtils.isTestMode()) {
				MessageDialog.openInformation(getParentShell(), Messages.NewUserDialog_14, Messages.NewUserDialog_21);
			}
			
//			// 정상이면 session에 로그인 정보를 입력하고 
//			SessionManager.newLogin(loginDAO.getSeq(), strEmail, name, loginDAO.getUser_type());
		} catch (Exception e) {
			logger.error(Messages.NewUserDialog_8, e);
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_14, e.getMessage());
			return;
		}
		
		super.okPressed();
	}
	
	/**
	 * validation
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	private boolean validation(String strEmail, String strPass, String rePasswd, String name) {
		// validation
		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_7);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_10);
			textPasswd.setFocus();
			return false;
		} else if("".equals(name)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_13);
			textName.setFocus();
			return false;
		} else if(!isEmail(strEmail)) {
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_15);
			textEMail.setFocus();
			return false;
		}
		
		if(!strPass.equals(rePasswd)) {
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_17);
			textPasswd.setFocus();
			return false;
		}
		
		//  신규 그룹 입력시 오류 검증
		if(!btnUser.getSelection()) {
			String strGroupName = StringUtils.trimToEmpty(textUserGroup.getText());
			if("".equals(strGroupName)) { //$NON-NLS-1$
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_24);
				textUserGroup.setFocus();
				return false;
			}
			
			// 동일한 그룹명이 있는 지 검증한다.
			if(TadpoleSystem_UserGroupQuery.isUserGroup(strGroupName)) {
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_25);
				textUserGroup.setFocus();
				return false;
			}
		}
		
		try {
			// 기존 중복 이메일인지 검사합니다.
			if(!TadpoleSystem_UserQuery.isDuplication(strEmail)) {
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_9);
				textEMail.setFocus();
				return false;
			}
		} catch(Exception e) {
			logger.error(Messages.NewUserDialog_11, e);
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_12 + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * email검사
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern p = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$"); //$NON-NLS-1$
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, Messages.NewUserDialog_19,	true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.NewUserDialog_20, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 265);
	}

}
