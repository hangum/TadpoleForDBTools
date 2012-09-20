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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/** 
 * mongodb prefernec page
 * 
 *  @author hangum
 */
public class MongoDBPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Text textLimitCount;
	private Text textMaxCount;
	
	// find page
	private Button btnBasicSearch;
//	private Button btnExtendSearch;
	
	// result page
	private Button btnTreeView;
	private Button btnTableView;
	private Button btnTextView;
	
	public MongoDBPreferencePage() {
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
		lblNewLabel.setText("Limit Count");
		
		textLimitCount = new Text(container, SWT.BORDER);
		textLimitCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Max Count");
		
		textMaxCount = new Text(container, SWT.BORDER);
		textMaxCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Find Page");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnBasicSearch = new Button(composite, SWT.RADIO);
		btnBasicSearch.setText("Basic Search");
		btnBasicSearch.setData(PreferenceDefine.MONGO_DEFAULT_FIND_BASIC);
		
//		btnExtendSearch = new Button(composite, SWT.RADIO);
//		btnExtendSearch.setText("Extend Search");
//		btnExtendSearch.setData(PreferenceDefine.MONGO_DEFAULT_FIND_EXTEND);
		
		Label lblResultPage = new Label(container, SWT.NONE);
		lblResultPage.setText("view result page");
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnTreeView = new Button(composite_1, SWT.RADIO);
		btnTreeView.setText("Tree View");
		btnTreeView.setData(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE);
		
		btnTableView = new Button(composite_1, SWT.RADIO);
		btnTableView.setText("Table View");
		btnTableView.setData(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE);
		
		btnTextView = new Button(composite_1, SWT.RADIO);
		btnTextView.setText("Text View");
		btnTextView.setData(PreferenceDefine.MONGO_DEFAULT_RESULT_TEXT);
		
		initDefaultValue();
		
		return container;
	}
	
	@Override
	public boolean performOk() {

		String txtLimitCount = textLimitCount.getText();
		String txtMacCount = textMaxCount.getText();
		String txtFindPage = "";
		String txtResultPage = "";
		
		try {
			Integer.parseInt(txtLimitCount);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", "Limit Count는 숫자이어야 합니다.");			 //$NON-NLS-1$
			return false;
		}
		try {
			Integer.parseInt(txtMacCount);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", "Max Count는 숫자이어야 합니다.");			 //$NON-NLS-1$
			return false;
		}
		
//		if(btnBasicSearch.getSelection()) {
			txtFindPage = btnBasicSearch.getData().toString();
//		} else {
//			txtFindPage = btnExtendSearch.getData().toString();
//		}
		
		if(btnTreeView.getSelection()) {
			txtResultPage = btnTreeView.getData().toString();
		} else if(btnTableView.getSelection()) {
			txtResultPage = btnTableView.getData().toString();
		} else {
			txtResultPage = btnTextView.getData().toString();
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateMongoDBUserInfoData(txtLimitCount, txtMacCount, txtFindPage, txtResultPage);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.MONGO_DEFAULT_LIMIT, txtLimitCount);
			SessionManager.setUserInfo(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, txtMacCount);
			SessionManager.setUserInfo(PreferenceDefine.MONGO_DEFAULT_FIND, txtFindPage);
			SessionManager.setUserInfo(PreferenceDefine.MONGO_DEFAULT_RESULT, txtResultPage);
		} catch(Exception e) {
			e.printStackTrace();
			
			MessageDialog.openError(getShell(), "Confirm", "데이터를 수정하는 중에 오류가 발생했습니다.\n" + e.getMessage());
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
	 * 초기값을 설정 합니다.
	 */
	private void initDefaultValue() {
		btnBasicSearch.setSelection(false);
//		btnExtendSearch.setSelection(false);
		btnTreeView.setSelection(false);
		btnTableView.setSelection(false);
		btnTextView.setSelection(false);
		
		textLimitCount.setText( "" + GetPreferenceGeneral.getMongoDefaultLimit() );
		textMaxCount.setText( "" + GetPreferenceGeneral.getMongoDefaultMaxCount() );
//		if(PreferenceDefine.MONGO_DEFAULT_FIND_BASIC.equals( GetPreferenceGeneral.getMongoDefaultFindPage() )) {
			btnBasicSearch.setSelection(true);
//		} else {
//			btnExtendSearch.setSelection(true);
//		}
		
		if(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE.equals(GetPreferenceGeneral.getMongoDefaultResultPage() )) {
			btnTreeView.setSelection(true);
		} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals(GetPreferenceGeneral.getMongoDefaultResultPage() )) {
			btnTableView.setSelection(true);
		} else {
			btnTextView.setSelection(true);
		}
		
	}
}
