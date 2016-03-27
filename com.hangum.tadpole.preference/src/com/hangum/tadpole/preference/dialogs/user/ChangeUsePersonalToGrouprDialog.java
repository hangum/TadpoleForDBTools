/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.dialogs.user;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystemQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.Activator;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * 개인 사용자에서 그룹 사용자로 수정합니다.
 * 
 * @author hangum
 *
 */
public class ChangeUsePersonalToGrouprDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ChangeUsePersonalToGrouprDialog.class);
	
	private UserDAO userDAO;
	
	private Text textEMail;
	private Text textPasswd;
	private Text textRePasswd;
	private Text textName;
	private Text textAllowIP;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ChangeUsePersonalToGrouprDialog(Shell parentShell) {
		super(parentShell);
		
		UserDAO tmpUserDao = new UserDAO();
		tmpUserDao.setSeq(SessionManager.getUserSeq());
//		tmpUserDao.setAllow_ip(allow_ip);
		this.userDAO = tmpUserDao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ChangeUsePersonalToGrouprDialog_0);
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
		lblEmail.setText(Messages.get().ChangeUsePersonalToGrouprDialog_1);
		
		textEMail = new Text(container, SWT.BORDER);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.get().ChangeUsePersonalToGrouprDialog_2);
		
		textPasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.get().ChangeUsePersonalToGrouprDialog_3);
		
		textRePasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(Messages.get().ChangeUsePersonalToGrouprDialog_4);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAllowIp = new Label(container, SWT.NONE);
		lblAllowIp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAllowIp.setText(Messages.get().ChangeUsePersonalToGrouprDialog_5);
		
		textAllowIP = new Text(container, SWT.BORDER);
		textAllowIP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initData();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
		return container;
	}
	
	/**
	 * 초기 데이터를 설정 합니다.
	 */
	private void initData() {
		textAllowIP.setText(userDAO.getAllow_ip());
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String passwd = StringUtils.trimToEmpty(textPasswd.getText());
		String rePasswd = StringUtils.trimToEmpty(textRePasswd.getText());
		String name = StringUtils.trimToEmpty(textName.getText());
		String strIp = StringUtils.trimToEmpty(textAllowIP.getText());
		
		if(!validation(strEmail, passwd, rePasswd, name, strIp)) return;
		
		if(MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().ChangeUsePersonalToGrouprDialog_7)) {
			UserDAO user = new UserDAO();
			user.setSeq(userDAO.getSeq());
			user.setEmail(strEmail);
			user.setPasswd(passwd);
			user.setName(name);
			user.setAllow_ip(strIp);
			
			try {
				TadpoleSystem_UserQuery.updateUserPersonToGroup(user);
				TadpoleSystemQuery.updateSystemInformation(PublicTadpoleDefine.SYSTEM_USE_GROUP.GROUP.name());
				
				MessageDialog.openInformation(getShell(), Messages.get().Confirm, Messages.get().ChangeUsePersonalToGrouprDialog_9);
				TadpoleApplicationContextManager.initSystem();
				SessionManager.logout();
				
			} catch (Exception e) {
				logger.error("data update", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, "User Info update", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				
				return;
			}
			
			super.okPressed();	
		} else {
			return;
		}		
	}
	
	/**
	 * validation
	 * 
	 * @param strGroupName
	 * @param strEmail
	 * @param strPass
	 * @param rePasswd
	 * @param name
	 */
	private boolean validation(String strEmail, String strPass, String rePasswd, String name, String ip) {

		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_11);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_13);
			textPasswd.setFocus();
			return false;
		} else if("".equals(name)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_15);
			textName.setFocus();
			return false;
		} else if("".equals(ip)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_17);
			textAllowIP.setFocus();
			return false;
		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning,Messages.get().ChangeUsePersonalToGrouprDialog_19);
			textEMail.setFocus();
			return false;
		} else if(!strPass.equals(rePasswd)) {
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_21);
			textPasswd.setFocus();
			return false;
		}
				
		try {
			// 기존 중복 이메일인지 검사합니다.
			if(!TadpoleSystem_UserQuery.isDuplication(strEmail)) {
				MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().ChangeUsePersonalToGrouprDialog_23);
				textEMail.setFocus();
				return false;
			}
		} catch(Exception e) {
			logger.error("check email duplication", e); //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.get().Error, Messages.get().ChangeUsePersonalToGrouprDialog_26 + e.getMessage());
			return false;
		}
		
		return true;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(400, 280);
	}

}
