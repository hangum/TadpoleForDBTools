/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;

import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserInfoData;

/**
 * SQL formatter preference page
 * 
 * @author hangum
 *
 */
public class SQLFormatterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Combo comboTabsize;
	private Button btnNoInsertNewDecode;
	private Button btnNoInsertNewIn;

	/**
	 * Create the preference page.
	 */
	public SQLFormatterPreferencePage() {
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblTabSize = new Label(container, SWT.NONE);
		lblTabSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTabSize.setText(Messages.SQLFormatterPreferencePage_0);
		
		comboTabsize = new Combo(container, SWT.READ_ONLY);
		comboTabsize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboTabsize.add("2"); //$NON-NLS-1$
		comboTabsize.add("4"); //$NON-NLS-1$
		comboTabsize.select(0);
		
		btnNoInsertNewDecode = new Button(container, SWT.CHECK);
		btnNoInsertNewDecode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnNoInsertNewDecode.setText(Messages.SQLFormatterPreferencePage_3);
		
		btnNoInsertNewIn = new Button(container, SWT.CHECK);
		btnNoInsertNewIn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnNoInsertNewIn.setText(Messages.SQLFormatterPreferencePage_4);
		
		initDefaultValue();

		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtTabSize = comboTabsize.getText();
		String txtNoInsertDecode = ""+btnNoInsertNewDecode.getSelection(); //$NON-NLS-1$
		String txtNoInsertIn = ""+btnNoInsertNewIn.getSelection(); //$NON-NLS-1$
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateSQLFormatterInfoData(txtTabSize, txtNoInsertDecode, txtNoInsertIn);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE, txtTabSize);
			SessionManager.setUserInfo(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE, txtNoInsertDecode);
			SessionManager.setUserInfo(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE, txtNoInsertIn);			
		} catch(Exception e) {
			e.printStackTrace();
			
			MessageDialog.openError(getShell(), "Confirm", Messages.RDBPreferencePage_5 + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return super.performOk();
	}

	/**
	 * initialize default value
	 */
	private void initDefaultValue() {
		comboTabsize.setText(GetPreferenceGeneral.getDefaultTabSize());
		btnNoInsertNewDecode.setSelection(Boolean.getBoolean(GetPreferenceGeneral.getSQLFormatDecode()));
		btnNoInsertNewIn.setSelection(Boolean.getBoolean(GetPreferenceGeneral.getSQLFormatIn()));
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
