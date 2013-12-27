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
package com.hangum.tadpole.preference.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.SecurityHint;
import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;

/**
 * 사용자 정보 수정
 * 
 * @author hangum
 *
 */
public class UserInfoPerference extends PreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(UserInfoPerference.class);
	
//	private Text textGroupName;
	private Text textEmail;
	private Text textPassword;
	private Text textRePassword;
	private Text textName;
	
	private Combo comboQuestion;
	private Text textAnswer;

	/**
	 * Create the preference page.
	 */
	public UserInfoPerference() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
//		Label lblGroupName = new Label(container, SWT.NONE);
//		lblGroupName.setText(Messages.UserInfoPerference_1);
//		
//		textGroupName = new Text(container, SWT.BORDER);
//		textGroupName.setEnabled(false);
//		textGroupName.setEditable(false);
//		textGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		textGroupName.setText(SessionManager.getGroupName());
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setText(Messages.UserInfoPerference_2);
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setEnabled(false);
		textEmail.setEditable(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.setText(SessionManager.getEMAIL());
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(Messages.UserInfoPerference_5);
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.setText(SessionManager.getName());
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.UserInfoPerference_3);
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setText(SessionManager.getPassword());
		// Because existed password do not decode, to save new password must clear text.
		textPassword.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {
			}
			
			@Override
			public void focusGained(FocusEvent event) {
				textPassword.setText(""); //$NON-NLS-1$
				textRePassword.setText(""); //$NON-NLS-1$
			}
		});
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.UserInfoPerference_4);
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textRePassword.setText(SessionManager.getPassword());
		// Because existed password do not decode, to save new password must clear text.
		textRePassword.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {
			}
			
			@Override
			public void focusGained(FocusEvent event) {
				textRePassword.setText(""); //$NON-NLS-1$
			}
		});
		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblPasswordDescription = new Label(container, SWT.NONE);
		lblPasswordDescription.setText(Messages.UserInfoPerference_11);
		lblPasswordDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblQuestion = new Label(container, SWT.NONE);
		lblQuestion.setText(Messages.UserInfoPerference_12);

		comboQuestion = new Combo(container, SWT.READ_ONLY);
		comboQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (SecurityHint q : PublicTadpoleDefine.SecurityHint.values()) {
			comboQuestion.add(q.toString(), q.getOrderIndex());
			comboQuestion.setData(q.getOrderIndex()+q.toString(), q.getKey());
		}
		
		Label lblAnswer = new Label(container, SWT.NONE);
		lblAnswer.setText(Messages.UserInfoPerference_13);

		textAnswer = new Text(container, SWT.BORDER);
		textAnswer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textAnswer.setText(CipherManager.getInstance().decryption(SessionManager.getSecurityAnswer()));

		// because of reference of textAnswer
		comboQuestion.select(0);
		
		String questionKey = CipherManager.getInstance().decryption(SessionManager.getSecurityQuestion());
		if (null!= questionKey && !"".equals(questionKey.trim())) { //$NON-NLS-1$
			try {
				SecurityHint question = PublicTadpoleDefine.SecurityHint.valueOf(questionKey);
				comboQuestion.select(question.getOrderIndex());
			} catch (IllegalStateException e) {
				// skip
			}
		}

		return container;
	}

	
	@Override
	public boolean performOk() {
		String pass = textPassword.getText().trim();
		String rePass = textRePassword.getText().trim();
		String questionKey = StringUtils.trimToEmpty((String)comboQuestion.getData(comboQuestion.getSelectionIndex() + comboQuestion.getText()));
		String answer = StringUtils.trimToEmpty(textAnswer.getText());
		
		if(!pass.equals(rePass)) {
			MessageDialog.openError(getShell(), Messages.UserInfoPerference_0, Messages.UserInfoPerference_6);
			return false;
		}
		// Password double check
		boolean isPasswordUpdated = !pass.equals(SessionManager.getPassword());
		
		UserDAO user = new UserDAO();
		user.setSeq(SessionManager.getSeq());
		user.setPasswd(pass);
		user.setSecurity_question(questionKey);
		user.setSecurity_answer(answer);
		
		try {
			if (isPasswordUpdated) {
				TadpoleSystem_UserQuery.updateUserPassword(user);
				SessionManager.updateSessionAttribute(SessionManager.SESSEION_NAME.LOGIN_PASSWORD.toString(), user.getPasswd());			
			}
			
			TadpoleSystem_UserQuery.updateUserSecurityHint(user);
			SessionManager.updateSessionAttribute(SessionManager.SESSEION_NAME.SECURITY_QUESTION.toString(), questionKey);			
			SessionManager.updateSessionAttribute(SessionManager.SESSEION_NAME.SECURITY_ANSWER.toString(), answer);
			
			//fix https://github.com/hangum/TadpoleForDBTools/issues/243
			SessionManager.setPassword(user.getPasswd());
		} catch (Exception e) {
			logger.error("password change", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", e.getMessage());			 //$NON-NLS-1$
			
			return false;
		}
		
		return super.performOk();
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
