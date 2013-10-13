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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.SecurityHint;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.session.manager.SessionManagerListener;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;

/**
 * 사용자 수정 다이얼로그
 * 
 * @author hangum
 *
 */
public class ModifyUserDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ModifyUserDialog.class);
	
	private UserGroupAUserDAO userDAO;
	
	private Text textGroupName;
	private Text textGroupType;
	private Text textEmail;
	private Text textName;
	private Text textCreateDate;
	
	private Combo comboApproval;
	private Combo comboDel;

	private Text textQuestion;
	private Text textAnswer;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ModifyUserDialog(Shell parentShell, UserGroupAUserDAO userDAO) {
		super(parentShell);
		
		this.userDAO = userDAO;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Modify User"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroupName.setText("Group Name"); //$NON-NLS-1$
		
		textGroupName = new Text(container, SWT.BORDER);
		textGroupName.setEnabled(false);
		textGroupName.setEditable(false);
		textGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(container, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Role"); //$NON-NLS-1$
		
		textGroupType = new Text(container, SWT.BORDER);
		textGroupType.setEnabled(false);
		textGroupType.setEditable(false);
		textGroupType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("email"); //$NON-NLS-1$
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setEnabled(false);
		textEmail.setEditable(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name"); //$NON-NLS-1$
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApproval = new Label(container, SWT.NONE);
		lblApproval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApproval.setText("Approval"); //$NON-NLS-1$
		
		comboApproval = new Combo(container, SWT.READ_ONLY);
		comboApproval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboApproval.add("YES"); //$NON-NLS-1$
		comboApproval.add("NO"); //$NON-NLS-1$
		
		Label lblDelete = new Label(container, SWT.NONE);
		lblDelete.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDelete.setText("Delete"); //$NON-NLS-1$
		
		comboDel = new Combo(container, SWT.READ_ONLY);
		comboDel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboDel.add(Messages.ModifyUserDialog_9);
		comboDel.add("NO"); //$NON-NLS-1$
		
		Label lblCreateDate = new Label(container, SWT.NONE);
		lblCreateDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCreateDate.setText("Create Date"); //$NON-NLS-1$
		
		textCreateDate = new Text(container, SWT.BORDER);
		textCreateDate.setEnabled(false);
		textCreateDate.setEditable(false);
		textCreateDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswordDescription = new Label(container, SWT.NONE);
		lblPasswordDescription.setText("* The information below is used to find the password.");
		lblPasswordDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblQuestion = new Label(container, SWT.NONE);
		lblQuestion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQuestion.setText("Question");

		textQuestion = new Text(container, SWT.BORDER);
		textQuestion.setEnabled(false);
		textQuestion.setEditable(false);
		textQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAnswer = new Label(container, SWT.NONE);
		lblAnswer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnswer.setText("Answer");

		textAnswer = new Text(container, SWT.BORDER);
		textAnswer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textAnswer.setEnabled(false);
		textAnswer.setEditable(false);
		
		initData();
		
//		ManagerSession.sessionManager();

		return container;
	}
	
	/**
	 * 초기 데이터를 설정 합니다.
	 */
	private void initData() {
		textGroupName.setText(userDAO.getUser_group_name());
		textGroupType.setText(userDAO.getRole_type());
		textEmail.setText(userDAO.getEmail());
		textName.setText(userDAO.getName());
		textCreateDate.setText(userDAO.getCreate_time());
		
		comboApproval.setText(userDAO.getApproval_yn());
		comboDel.setText(userDAO.getDelYn());
		String question = userDAO.getSecurity_question();
		if (null!= question && !"".equals(question.trim())) {
			try {
				SecurityHint questionKey = PublicTadpoleDefine.SecurityHint.valueOf(question);
				textQuestion.setText(questionKey.toString());
			} catch (IllegalStateException e) {
				// skip
			}
		}
		textAnswer.setText(StringUtils.trimToEmpty(userDAO.getSecurity_answer()));
	}
	
	@Override
	protected void okPressed() {
		SessionManager.invalidate(11);
		
		if(MessageDialog.openConfirm(getShell(), Messages.ModifyUserDialog_12, Messages.ModifyUserDialog_13)) {
			UserDAO user = new UserDAO();
			user.setApproval_yn(comboApproval.getText());
			user.setDelYn(comboDel.getText());
			user.setSeq(userDAO.getSeq());
			
			// 사용자의 권한을 no로 만들면 session에서 삭제 하도록 합니다.
			if("YES".equals(user.getDelYn()) || "YES".equals(user.getApproval_yn())) {
				String sessionId = SessionManagerListener.getSessionIds(user.getEmail());
				logger.debug("[session id]" + sessionId);
			}
			
			try {
				TadpoleSystem_UserQuery.updateUserData(user);
			} catch (Exception e) {
				logger.error("data update", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", "User Info update", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				
				return;
			}
			
			super.okPressed();	
		} else {
			return;
		}		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(438, 360);
	}

}
