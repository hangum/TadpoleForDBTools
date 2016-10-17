/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.dialogs;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;

/**
 * LDAP Configuration dialog
 * @author hangum
 *
 */
public class LDAPConfigurationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(LDAPConfigurationDialog.class);
	
	private Text textLDAPURL;
	private Text textAuthentication;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LDAPConfigurationDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("LDAP Configuration Dialog");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblLdapIp = new Label(composite, SWT.NONE);
		lblLdapIp.setText("LDAP URL");
		
		textLDAPURL = new Text(composite, SWT.BORDER);
		textLDAPURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblExLdapldapexamplecom = new Label(composite, SWT.NONE);
		lblExLdapldapexamplecom.setText("ex) LDAP://ldap.example.com:389");
		
		Label lblAuthentication = new Label(composite, SWT.NONE);
		lblAuthentication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAuthentication.setText("Authentication");
		
		textAuthentication = new Text(composite, SWT.BORDER);
		textAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initUI();

		return container;
	}
	
	private void initUI() {
		textLDAPURL.setText(GetAdminPreference.getLDAPURL());
		textAuthentication.setText(GetAdminPreference.getLDAPAuthentication());
	}
	
	@Override
	protected void okPressed() {
		String strLDAPURL = textLDAPURL.getText();
		String strAuthentication = textAuthentication.getText();
		
		if(StringUtils.isBlank(strLDAPURL)) {
			MessageDialog.openError(getShell(), CommonMessages.get().Warning, String.format(CommonMessages.get().Please_InputText, "LDAP URL"));
			textLDAPURL.setFocus();
			return;
		}
		if(StringUtils.isBlank(strAuthentication)) {
			MessageDialog.openError(getShell(), CommonMessages.get().Warning, String.format(CommonMessages.get().Please_InputText, "Authentication"));
			textAuthentication.setFocus();
			return;
		}
		try {
			UserInfoDataDAO userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_LDAP_URL, strLDAPURL);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_LDAP_URL, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_LDAP_AUTHENTICATION, strAuthentication);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_LDAP_AUTHENTICATION, userInfoDao);
		} catch(Exception e) {
			logger.error("SAVE LDAP Information", e);
		}
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(470, 300);
	}
}
