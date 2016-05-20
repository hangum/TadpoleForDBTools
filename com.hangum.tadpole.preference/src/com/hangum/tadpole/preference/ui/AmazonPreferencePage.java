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
package com.hangum.tadpole.preference.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetAmazonPreference;

/**
 * Amazon account setting
 * 
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 17.
 *
 */
public class AmazonPreferencePage extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(AmazonPreferencePage.class);
	private Text textAccessKey;
	private Text textSecretKey;

	public AmazonPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite containerMain = new Composite(parent, SWT.NONE);
		containerMain.setLayout(new GridLayout(2, false));
		
		Label lblAccessKey = new Label(containerMain, SWT.NONE);
		lblAccessKey.setText(Messages.get().AmazonPreferencePage_0);
		
		textAccessKey = new Text(containerMain, SWT.BORDER);
		textAccessKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSecretKey = new Label(containerMain, SWT.NONE);
		lblSecretKey.setText(Messages.get().AmazonPreferencePage_1);
		
		textSecretKey = new Text(containerMain, SWT.BORDER | SWT.PASSWORD);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return containerMain;
	}
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		textAccessKey.setText(GetAmazonPreference.getAccessValue());
		textSecretKey.setText(GetAmazonPreference.getSecretValue());
	}
	
	@Override
	public boolean performOk() {
		String txtAccessKey	= textAccessKey.getText();
		String txtSecretKey = textSecretKey.getText();
		
		if(StringUtils.length(txtAccessKey) > 10 && StringUtils.length(txtSecretKey) > 10) {
			try {			
				updateEncriptInfo(PreferenceDefine.AMAZON_ACCESS_NAME, txtAccessKey);
				updateEncriptInfo(PreferenceDefine.AMAZON_SECRET_NAME, txtSecretKey);
				
			} catch(Exception e) {
				logger.error("GeneralPreference saveing", e); //$NON-NLS-1$
				
				MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
				return false;
			}
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
	
}
