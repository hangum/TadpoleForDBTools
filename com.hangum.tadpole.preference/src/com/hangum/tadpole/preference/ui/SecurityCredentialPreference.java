/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetSecurityCredentialPreference;
import org.eclipse.swt.widgets.Group;

/**
 * security credentials
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 23.
 *
 */
public class SecurityCredentialPreference extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(SecurityCredentialPreference.class);
	/** 사용자 억세스 키 시크릿 키를 보여준다 */
	public static  String TEMPLATE_API_KEY = "TDB_ACCESS_KEY: %s\nTDB_SECRET_KEY: %s";
	
	private Combo comboIsUse;
	private Text textAccessKey;
	private Text textSecretKey;
	private Text textHeader;

	/**
	 * Create the preference page.
	 */
	public SecurityCredentialPreference() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));
		
		Label lblUse = new Label(container, SWT.NONE);
		lblUse.setText(Messages.get().SecurityCredentialPreference_0);
		
		comboIsUse = new Combo(container, SWT.READ_ONLY);
		comboIsUse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		for(PublicTadpoleDefine.YES_NO YESNO : PublicTadpoleDefine.YES_NO.values()) {
			comboIsUse.add(YESNO.name());
		}
		comboIsUse.select(0);
		
		Label lblAccesskey = new Label(container, SWT.NONE);
		lblAccesskey.setText(Messages.get().SecurityCredentialPreference_1);
		
		textAccessKey = new Text(container, SWT.BORDER);
		textAccessKey.setEditable(false);
		textAccessKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblSecretKey = new Label(container, SWT.NONE);
		lblSecretKey.setText(Messages.get().SecurityCredentialPreference_2);
		
		textSecretKey = new Text(container, SWT.BORDER);
		textSecretKey.setEditable(false);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnGenerateKey = new Button(container, SWT.NONE);
		btnGenerateKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!MessageDialog.openConfirm(getShell(), Messages.get().SecurityCredentialPreference_3, Messages.get().SecurityCredentialPreference_4)) return;
				
				textSecretKey.setText(Utils.getUniqueID());
				textHeader.setText(String.format(TEMPLATE_API_KEY, textAccessKey.getText(), textSecretKey.getText()));
			}
		});
		btnGenerateKey.setText(Messages.get().SecurityCredentialPreference_5);
		
		Group grpUsage = new Group(container, SWT.NONE);
		grpUsage.setLayout(new GridLayout(1, false));
		grpUsage.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		grpUsage.setText("Usage");
		
		Label lblHttpRequestHeader = new Label(grpUsage, SWT.NONE);
		lblHttpRequestHeader.setText("HTTP Header");
		
		textHeader = new Text(grpUsage, SWT.BORDER | SWT.MULTI);
		textHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			
		Label lblDocUrl = new Label(container, SWT.NONE);
		lblDocUrl.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblDocUrl.setText(Messages.get().RESTAPI_Help);
		new Label(container, SWT.NONE);
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		comboIsUse.setText(GetSecurityCredentialPreference.getSecurityCredentialUse());
		textAccessKey.setText(GetSecurityCredentialPreference.getAccessValue());
		textSecretKey.setText(GetSecurityCredentialPreference.getSecretValue());
		
		// make request header
		textHeader.setText(String.format(TEMPLATE_API_KEY, GetSecurityCredentialPreference.getAccessValue(), GetSecurityCredentialPreference.getSecretValue()));
	}
	
	@Override
	public boolean performOk() {
		String isUse		= comboIsUse.getText();
		String txtAccessKey	= textAccessKey.getText();
		String txtSecretKey = textSecretKey.getText();

		try {
			updateInfo(PreferenceDefine.SECURITY_CREDENTIAL_USE, isUse);
			updateInfo(PreferenceDefine.SECURITY_CREDENTIAL_ACCESS_KEY, txtAccessKey);
			updateInfo(PreferenceDefine.SECURITY_CREDENTIAL_SECRET_KEY, txtSecretKey);
			
		} catch(Exception e) {
			logger.error("api security credential saveing", e); //$NON-NLS-1$
			
			MessageDialog.openError(getShell(), Messages.get().Confirm, Messages.get().GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return super.performOk();
	}
	
	@Override
	public boolean performCancel() {
		initDefaultValue();
		
		return super.performCancel();
	}
	
	@Override
	protected void performApply() {

		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		initDefaultValue();

		super.performDefaults();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}
}
