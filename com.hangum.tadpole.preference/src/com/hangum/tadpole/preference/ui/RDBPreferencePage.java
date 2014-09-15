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
package com.hangum.tadpole.preference.ui;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.preference.internal.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserInfoData;

/**
 * rdb preference
 * 
 * @author hangum
 *
 */
public class RDBPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(RDBPreferencePage.class);
	
	private Text textSelectLimit;
	private Text textResultPage;
	private Text textOraclePlan;
	
	private Label lblUserFont;
	
	private Combo comboRDBNumberComma;

	public RDBPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNumberColumnAdd = new Label(container, SWT.NONE);
		lblNumberColumnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNumberColumnAdd.setText(Messages.RDBPreferencePage_lblNumberColumnAdd_text);
		
		comboRDBNumberComma = new Combo(container, SWT.READ_ONLY);
		comboRDBNumberComma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.YES.toString());
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.NO.toString());
		comboRDBNumberComma.select(0);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.DefaultPreferencePage_0);
		
		textSelectLimit = new Text(container, SWT.BORDER);
		textSelectLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText(Messages.DefaultPreferencePage_other_labelText_1);
		
		textResultPage = new Text(container, SWT.BORDER);		
		textResultPage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblResultViewFont = new Label(container, SWT.NONE);
		lblResultViewFont.setText(Messages.RDBPreferencePage_lblResultViewFont_text);
		
		lblUserFont = new Label(container, SWT.NONE);
		lblUserFont.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblUserFont.setText("");
		new Label(container, SWT.NONE);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setFontData();
			}
		});
		btnNewButton.setText(Messages.RDBPreferencePage_btnNewButton_text);
		
		Label lblQueryTimeout = new Label(container, SWT.NONE);
		lblQueryTimeout.setText(Messages.RDBPreferencePage_lblQueryTimeout_text);
		
		textQueryTimeout = new Text(container, SWT.BORDER);
		textQueryTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		label.setText(""); //$NON-NLS-1$
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText(Messages.DefaultPreferencePage_other_labelText);
		
		textOraclePlan = new Text(container, SWT.BORDER);
		textOraclePlan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Button btnCreatePlanTable = new Button(container, SWT.NONE);
		btnCreatePlanTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSimpleMessageDialog planDialog = new TadpoleSimpleMessageDialog(getShell(), textOraclePlan.getText(), ORACLE_PLAN_TABLE);
				planDialog.open();
			}
		});
		btnCreatePlanTable.setText(Messages.RDBPreferencePage_btnCreatePlanTable_text);
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	/**
	 * 사용자 폰트를 설정합니다.
	 */
	private void setFontData() {
		FontDialog fd = new FontDialog(getShell(), SWT.NONE);
		FontData fdFont = fd.open();
		if(fdFont != null) {
			lblUserFont.setText(fdFont.getName()+"|"+fdFont.getHeight()+"|"+fdFont.getStyle());
		}
	}
	
	@Override
	public boolean performOk() {
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtQueryTimtout = textQueryTimeout.getText();
		String txtOraclePlan = textOraclePlan.getText();
		String txtRDBNumberColumnIsComman = comboRDBNumberComma.getText();
		String txtFontInfo = lblUserFont.getText();
		
		if(!NumberUtils.isNumber(txtSelectLimit)) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_0 + Messages.RDBPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		if(!NumberUtils.isNumber(txtResultPage)) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_other_labelText_1 + Messages.RDBPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		if(!NumberUtils.isNumber(txtQueryTimtout)) {
			MessageDialog.openError(getShell(), "Confirm", "Query timeout is " + Messages.RDBPreferencePage_0);
			return false;
		}
		
		if("".equals(txtOraclePlan)) { //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", Messages.RDBPreferencePage_3);			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateRDBUserInfoData(txtSelectLimit, txtResultPage, txtQueryTimtout, txtOraclePlan, txtRDBNumberColumnIsComman, txtFontInfo);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, txtSelectLimit);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, txtResultPage);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_QUERY_TIMEOUT, txtQueryTimtout);
			
			SessionManager.setUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, txtOraclePlan);		
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, txtRDBNumberColumnIsComman);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_FONT, txtFontInfo);
			
			
		} catch(Exception e) {
			logger.error("RDBPreference saveing", e);
			
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
		textSelectLimit.setText( "" + GetPreferenceGeneral.getSelectLimitCount() ); //$NON-NLS-1$
		textResultPage.setText( "" + GetPreferenceGeneral.getPageCount() ); //$NON-NLS-1$
		textQueryTimeout.setText( "" + GetPreferenceGeneral.getQueryTimeOut() );
		
		textOraclePlan.setText( GetPreferenceGeneral.getPlanTableName() );
		comboRDBNumberComma.setText(GetPreferenceGeneral.getRDBNumberISComma());
		
		String strFontInfo = GetPreferenceGeneral.getRDBResultFont();
		lblUserFont.setText(strFontInfo);
	}
	
	/** ORACLE PLAN TABLE */
	private static final String ORACLE_PLAN_TABLE = 
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
	private Text textQueryTimeout;
}

