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

import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;
import com.hangum.tadpole.util.ManagerSession;

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
		lblType.setText("Type"); //$NON-NLS-1$
		
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
		
		initData();
		
		ManagerSession.sessionManager();

		return container;
	}
	
	/**
	 * 초기 데이터를 설정 합니다.
	 */
	private void initData() {
		textGroupName.setText(userDAO.getUser_group_name());
		textGroupType.setText(userDAO.getUser_type());
		textEmail.setText(userDAO.getEmail());
		textName.setText(userDAO.getName());
		textCreateDate.setText(userDAO.getCreate_time());
		
		comboApproval.setText(userDAO.getApproval_yn());
		comboDel.setText(userDAO.getDelYn());		
	}
	
	@Override
	protected void okPressed() {
		if(MessageDialog.openConfirm(getShell(), Messages.ModifyUserDialog_12, Messages.ModifyUserDialog_13)) {
			UserDAO user = new UserDAO();
			user.setApproval_yn(comboApproval.getText());
			user.setDelYn(comboDel.getText());
			user.setSeq(userDAO.getSeq());
			
			// 사용자를
//			if("YES".equals(user.getDelYn())) {
//			}
			
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
		createButton(parent, IDialogConstants.OK_ID, "Ok", true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(438, 291);
	}

}
