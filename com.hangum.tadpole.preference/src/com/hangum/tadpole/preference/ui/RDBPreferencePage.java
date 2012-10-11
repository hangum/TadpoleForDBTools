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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.preference.internal.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserInfoData;

/**
 * rdb preference
 * 
 * @author hangum
 *
 */
public class RDBPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Text textSelectLimit;
	private Text textResultPage;
	private Text textOraclePlan;

	public RDBPreferencePage() {
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
		lblNewLabel.setText(Messages.DefaultPreferencePage_0);
		
		textSelectLimit = new Text(container, SWT.BORDER);
		textSelectLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(Messages.DefaultPreferencePage_other_labelText_1);
		
		textResultPage = new Text(container, SWT.BORDER);		
		textResultPage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		label.setText(""); //$NON-NLS-1$
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText(Messages.DefaultPreferencePage_other_labelText);
		
		textOraclePlan = new Text(container, SWT.BORDER);
		textOraclePlan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Button btnCreatePlanTable = new Button(container, SWT.NONE);
		btnCreatePlanTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSimpleMessageDialog planDialog = new TadpoleSimpleMessageDialog(getShell(), textOraclePlan.getText(), planTable);
				planDialog.open();
			}
		});
		btnCreatePlanTable.setText(Messages.RDBPreferencePage_btnCreatePlanTable_text);
		
		initDefaultValue();

		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtOraclePlan = textOraclePlan.getText();
		
		try {
			Integer.parseInt(txtSelectLimit);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_0 + Messages.RDBPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		try {
			Integer.parseInt(txtResultPage);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_other_labelText_1 + Messages.RDBPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		if("".equals(txtOraclePlan)) { //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", Messages.RDBPreferencePage_3);			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateRDBUserInfoData(txtSelectLimit, txtResultPage, txtOraclePlan);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, txtSelectLimit);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, txtResultPage);
			SessionManager.setUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, txtOraclePlan);			
		} catch(Exception e) {
			e.printStackTrace();
			
			MessageDialog.openError(getShell(), "Confirm", Messages.RDBPreferencePage_5 + e.getMessage()); //$NON-NLS-1$
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
		textSelectLimit.setText( "" + GetPreferenceGeneral.getQueryResultCount() ); //$NON-NLS-1$
		textResultPage.setText( "" + GetPreferenceGeneral.getPageCount() ); //$NON-NLS-1$
		textOraclePlan.setText( GetPreferenceGeneral.getPlanTableName() );
		
	}
	
	public static String planTable = 
			" CREATE TABLE plan_table (							  \r\n" + 
					"         STATEMENT_ID       VARCHAR2(30),    \r\n" + 
					"         plan_id            NUMBER,          \r\n" + 
					"         TIMESTAMP          DATE,            \r\n" + 
					"         remarks            VARCHAR2(4000),  \r\n" + 
					"         operation          VARCHAR2(30),    \r\n" + 
					"         options            VARCHAR2(255),   \r\n" + 
					"         object_node        VARCHAR2(128),   \r\n" + 
					"         object_owner       VARCHAR2(30),    \r\n" + 
					"         object_name        VARCHAR2(30),    \r\n" + 
					"         object_alias       VARCHAR2(65),    \r\n" + 
					"         object_instance    NUMERIC,         \r\n" + 
					"         object_type        VARCHAR2(30),    \r\n" + 
					"         optimizer          VARCHAR2(255),   \r\n" + 
					"         search_columns     NUMBER,          \r\n" + 
					"         ID                 NUMERIC,         \r\n" + 
					"         parent_id          NUMERIC,         \r\n" + 
					"         DEPTH              NUMERIC,         \r\n" + 
					"         POSITION           NUMERIC,         \r\n" + 
					"         COST               NUMERIC,         \r\n" + 
					"         CARDINALITY        NUMERIC,         \r\n" + 
					"         BYTES              NUMERIC,         \r\n" + 
					"         other_tag          VARCHAR2(255),   \r\n" + 
					"         partition_start    VARCHAR2(255),   \r\n" + 
					"         partition_stop     VARCHAR2(255),   \r\n" + 
					"         partition_id       NUMERIC,         \r\n" + 
					"         other              LONG,            \r\n" + 
					"         distribution       VARCHAR2(30),    \r\n" + 
					"         cpu_cost           NUMERIC,         \r\n" + 
					"         io_cost            NUMERIC,         \r\n" + 
					"         temp_space         NUMERIC,         \r\n" + 
					"         access_predicates  VARCHAR2(4000),  \r\n" + 
					"         filter_predicates  VARCHAR2(4000),  \r\n" + 
					"         projection         VARCHAR2(4000),  \r\n" + 
					"         TIME               NUMERIC,         \r\n" + 
					"         qblock_name        VARCHAR2(30)     \r\n" + 
					" ) ";
}

