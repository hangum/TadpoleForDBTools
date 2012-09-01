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
package com.hangum.tadpole.preference.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * general preference
 * 
 * @author hangum
 *
 */
public class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Text textSessionTime;
	private Text textExportDelimit;

	public GeneralPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.DefaultPreferencePage_2);
		
		textSessionTime = new Text(container, SWT.BORDER);
		textSessionTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExportDilimit = new Label(container, SWT.NONE);
		lblExportDilimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExportDilimit.setText(Messages.GeneralPreferencePage_lblExportDilimit_text);
		
		textExportDelimit = new Text(container, SWT.BORDER);
		textExportDelimit.setText(Messages.GeneralPreferencePage_text_text);
		textExportDelimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initDefaultValue();
		
		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtSessionTime = textSessionTime.getText();
		String txtExportDelimit = textExportDelimit.getText();
		
		try {
			Integer.parseInt(txtSessionTime);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_2 + Messages.GeneralPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateGeneralUserInfoData(txtSessionTime);
			TadpoleSystem_UserInfoData.updateGeneralExportDelimitData(txtExportDelimit);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, txtSessionTime);			
			SessionManager.setUserInfo(PreferenceDefine.EXPORT_DILIMITER, txtExportDelimit);
		} catch(Exception e) {
			e.printStackTrace();
			
			MessageDialog.openError(getShell(), "Confirm", Messages.GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
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
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		textSessionTime.setText( "" + GetPreferenceGeneral.getSessionTimeout() ); //$NON-NLS-1$
		textExportDelimit.setText( "" + GetPreferenceGeneral.getExportDelimit() ); //$NON-NLS-1$
	}

}
