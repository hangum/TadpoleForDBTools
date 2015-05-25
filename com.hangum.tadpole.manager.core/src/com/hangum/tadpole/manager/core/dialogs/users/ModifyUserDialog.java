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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.session.manager.SessionManagerListener;

/**
 * admin의 사용자 수정 다이얼로그
 * 
 * @author hangum
 *
 */
public class ModifyUserDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ModifyUserDialog.class);
	
	private UserDAO userDAO;
	
	private Text textEmail;
	private Text textName;
	private Text textCreateDate;
	
	private Combo comboApproval;
	private Combo comboUserConfirm;
	private Combo comboDel;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ModifyUserDialog(Shell parentShell, UserDAO userDAO) {
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
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("email"); //$NON-NLS-1$
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setEditable(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name"); //$NON-NLS-1$
		
		textName = new Text(container, SWT.BORDER);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApproval = new Label(container, SWT.NONE);
		lblApproval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApproval.setText("Approval"); //$NON-NLS-1$
		
		comboApproval = new Combo(container, SWT.READ_ONLY);
		comboApproval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboApproval.add("YES"); //$NON-NLS-1$
		comboApproval.add("NO"); //$NON-NLS-1$
		
		Label lblUserConfirm = new Label(container, SWT.NONE);
		lblUserConfirm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserConfirm.setText("User Confirm"); //$NON-NLS-1$
		
		comboUserConfirm = new Combo(container, SWT.READ_ONLY);
		comboUserConfirm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboUserConfirm.add("YES"); //$NON-NLS-1$
		comboUserConfirm.add("NO"); //$NON-NLS-1$
		
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
		textCreateDate.setEditable(false);
		textCreateDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initData();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
		return container;
	}
	
	/**
	 * 초기 데이터를 설정 합니다.
	 */
	private void initData() {

		textEmail.setText(userDAO.getEmail());
		textName.setText(userDAO.getName());
		textCreateDate.setText(userDAO.getCreate_time());
		
		comboApproval.setText(userDAO.getApproval_yn());
		comboUserConfirm.setText(userDAO.getIs_email_certification());
		comboDel.setText(userDAO.getDelYn());
	}
	
	@Override
	protected void okPressed() {
		if(MessageDialog.openConfirm(getShell(), Messages.ModifyUserDialog_12, Messages.ModifyUserDialog_13)) {
			UserDAO user = new UserDAO();
			user.setSeq(userDAO.getSeq());
			user.setApproval_yn(comboApproval.getText());
			user.setIs_email_certification(comboUserConfirm.getText());
			user.setDelYn(comboDel.getText());
			
			
			// 사용자의 권한을 no로 만들면 session에서 삭제 하도록 합니다.
			if("YES".equals(user.getDelYn()) || "YES".equals(user.getApproval_yn())) {
				String sessionId = SessionManagerListener.getSessionIds(user.getEmail());
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
		return new Point(400, 250);
	}

}
