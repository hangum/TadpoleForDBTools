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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.preference.internal.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * rdb preference
 * 
 * @author hangum
 *
 */
public class RDBPreferencePage extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(RDBPreferencePage.class);
	
	private Button btnQueryProfilling;
	private Combo comboRDBResultType;
	private Combo comboRDBNumberComma;
	private Text textSelectLimit;
	private Text textResultPage;
	private Text textNull;
	private Label lblUserFont;
	
	private Text textQueryTimeout;
	private Text textCommitCount;
	private Text textShowInTheColumn;
	
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
		new Label(container, SWT.NONE);
		
		btnQueryProfilling = new Button(container, SWT.CHECK);
		btnQueryProfilling.setText(Messages.get().QueryProfilling);
		
		Label lblResultType = new Label(container, SWT.NONE);
		lblResultType.setText(Messages.get().RDBPreferencePage_resultType);
		
		comboRDBResultType = new Combo(container, SWT.READ_ONLY);
		comboRDBResultType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRDBResultType.add("Table");
//		comboRDBResultType.add("Text");
//		comboRDBResultType.add("JSON");
		comboRDBResultType.select(0);
		
		Label lblNumberColumnAdd = new Label(container, SWT.NONE);
		lblNumberColumnAdd.setText(Messages.get().RDBPreferencePage_lblNumberColumnAdd_text);
		
		comboRDBNumberComma = new Combo(container, SWT.READ_ONLY);
		comboRDBNumberComma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.YES.name());
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.NO.name());
		comboRDBNumberComma.select(0);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.get().DefaultPreferencePage_0);
		
		textSelectLimit = new Text(container, SWT.BORDER);
		textSelectLimit.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textSelectLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().DefaultPreferencePage_other_labelText_1);
		
		textResultPage = new Text(container, SWT.BORDER);
		textResultPage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textResultPage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNull = new Label(container, SWT.NONE);
		lblNull.setText(Messages.get().ShowNullCharacters);
		
		textNull = new Text(container, SWT.BORDER);
		textNull.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblResultViewFont = new Label(container, SWT.NONE);
		lblResultViewFont.setText(Messages.get().RDBPreferencePage_lblResultViewFont_text);
		
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
		btnNewButton.setText(Messages.get().RDBPreferencePage_btnNewButton_text);
		
		Label lblQueryTimeout = new Label(container, SWT.NONE);
		lblQueryTimeout.setText(Messages.get().RDBPreferencePage_lblQueryTimeout_text);
		
		textQueryTimeout = new Text(container, SWT.BORDER);
		textQueryTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textQueryTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCommitCount = new Label(container, SWT.NONE);
		lblCommitCount.setText(Messages.get().RDBPreferencePage_lblCommitCount_text);
		
		textCommitCount = new Text(container, SWT.BORDER);
		textCommitCount.setText(Messages.get().RDBPreferencePage_text_text);
		textCommitCount.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textCommitCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCharacterShownIn = new Label(container, SWT.NONE);
		lblCharacterShownIn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCharacterShownIn.setText(Messages.get().RDBPreferencePage_lblCharacterShownIn_text);
		
		textShowInTheColumn = new Text(container, SWT.BORDER);
		textShowInTheColumn.setText(Messages.get().RDBPreferencePage_text_text_1);
		textShowInTheColumn.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textShowInTheColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		label.setText(""); //$NON-NLS-1$
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText(Messages.get().DefaultPreferencePage_other_labelText);
		
		textOraclePlan = new Text(container, SWT.BORDER);
		textOraclePlan.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
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
		btnCreatePlanTable.setText(Messages.get().RDBPreferencePage_btnCreatePlanTable_text);
		
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
	public boolean isValid() {
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtQueryTimtout = textQueryTimeout.getText();
		String txtOraclePlan = textOraclePlan.getText();
		String txtCommitCount = textCommitCount.getText();
		String txtShownInTheColumn = textShowInTheColumn.getText();
		
		if(!NumberUtils.isNumber(txtSelectLimit)) {
			textSelectLimit.setFocus();
			setValid(false);
			setErrorMessage(Messages.get().DefaultPreferencePage_0 + Messages.get().RDBPreferencePage_0);
			return false;
		} else if(!(NumberUtils.toInt(txtSelectLimit) >= 100 && NumberUtils.toInt(txtSelectLimit) <= 5000)) {
			textSelectLimit.setFocus();

			setValid(false);
			setErrorMessage(String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().DefaultPreferencePage_0, "100", "5,000"));
			return false;
		} else if(!NumberUtils.isNumber(txtResultPage)) {
			textResultPage.setFocus();
			setValid(false);
			setErrorMessage(Messages.get().DefaultPreferencePage_other_labelText_1 + Messages.get().RDBPreferencePage_0);
			return false;
		} else if(!(NumberUtils.toInt(txtResultPage) >= 100 && NumberUtils.toInt(txtResultPage) <= 1000)) {
			textResultPage.setFocus();

			setValid(false);
			setErrorMessage(String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().DefaultPreferencePage_other_labelText_1, "100", "1,000"));
			return false;
		} else if(!NumberUtils.isNumber(txtQueryTimtout)) {
			textQueryTimeout.setFocus();

			setValid(false);
			setErrorMessage(Messages.get().RDBPreferencePage_lblQueryTimeout_text + Messages.get().RDBPreferencePage_0);
			return false;
		} else if(!(NumberUtils.toInt(txtQueryTimtout) >= 5 && NumberUtils.toInt(txtQueryTimtout) <= 60000)) {
			textQueryTimeout.setFocus();

			setValid(false);
			setErrorMessage(String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().RDBPreferencePage_lblQueryTimeout_text, "5", "60,000"));
			return false;
		} else if(!NumberUtils.isNumber(txtCommitCount)) {
			textCommitCount.setFocus();

			setValid(false);
			setErrorMessage(Messages.get().RDBPreferencePage_lblCommitCount_text + Messages.get().RDBPreferencePage_0);
			return false;
		} else if(!(NumberUtils.toInt(txtCommitCount) >= 1000 && NumberUtils.toInt(txtCommitCount) <= 10000)) {
			textCommitCount.setFocus();

			setValid(false);
			setErrorMessage(String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().RDBPreferencePage_lblCommitCount_text, "1,000", "10,000"));
			return false;
		} else if(!NumberUtils.isNumber(txtShownInTheColumn)) {
			textShowInTheColumn.setFocus();

			setValid(false);
			setErrorMessage(Messages.get().RDBPreferencePage_lblCharacterShownIn_text + Messages.get().RDBPreferencePage_0);
			return false;
//			
//		} else if(!(NumberUtils.toInt(txtShownInTheColumn) >= 5 && NumberUtils.toInt(txtShownInTheColumn) <= 1000)) {
//			textShowInTheColumn.setFocus();
//
//			setValid(false);
//			setErrorMessage(String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().RDBPreferencePage_lblCharacterShownIn_text, "5", "1,000"));
//			return false;
		} else if("".equals(txtOraclePlan)) { //$NON-NLS-1$

			setValid(false);
			setErrorMessage(Messages.get().RDBPreferencePage_3);
			return false;
		}
		
		setErrorMessage(null);
		setValid(true);
		
		return true;
	}
	
	@Override
	public boolean performOk() {
		if(PublicTadpoleDefine.YES_NO.NO.name().equals(SessionManager.getIsModifyPerference())) {
			MessageDialog.openWarning(null, CommonMessages.get().Warning, CommonMessages.get().CantModifyPreferenc);
			return false;
		}
		if(!isValid()) return false;
		
		boolean txtQueryProfilling = btnQueryProfilling.getSelection();
		String txtResultType = comboRDBResultType.getText();
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtNull		= textNull.getText();
		String txtQueryTimtout = textQueryTimeout.getText();
		String txtOraclePlan = textOraclePlan.getText();
		String txtRDBNumberColumnIsComman = comboRDBNumberComma.getText();
		String txtFontInfo = lblUserFont.getText();
		String txtCommitCount = textCommitCount.getText();
		String txtShownInTheColumn = textShowInTheColumn.getText();
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateRDBUserInfoData(txtQueryProfilling,
					txtSelectLimit, txtResultPage, txtQueryTimtout, txtOraclePlan, txtRDBNumberColumnIsComman, txtFontInfo, txtCommitCount, txtShownInTheColumn, txtResultType, txtNull);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.RDB_QUERY_PROFILLING, ""+txtQueryProfilling);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_TYPE, txtResultType);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, txtSelectLimit);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, txtResultPage);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_NULL, txtNull);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_QUERY_TIMEOUT, txtQueryTimtout);
			
			SessionManager.setUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, txtOraclePlan);		
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, txtRDBNumberColumnIsComman);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_FONT, txtFontInfo);
			SessionManager.setUserInfo(PreferenceDefine.RDB_COMMIT_COUNT, txtCommitCount);
			SessionManager.setUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, txtShownInTheColumn);
			
		} catch(Exception e) {
			logger.error("RDBPreference saveing", e);
			
			MessageDialog.openError(getShell(), CommonMessages.get().Confirm, Messages.get().RDBPreferencePage_5 + e.getMessage()); //$NON-NLS-1$
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
		btnQueryProfilling.setSelection(GetPreferenceGeneral.getRDBQueryProfilling());
		comboRDBResultType.setText(GetPreferenceGeneral.getResultType());
		textSelectLimit.setText( "" + GetPreferenceGeneral.getSelectLimitCount() ); //$NON-NLS-1$
		textNull.setText(GetPreferenceGeneral.getResultNull());
		textResultPage.setText( "" + GetPreferenceGeneral.getPageCount() ); //$NON-NLS-1$
		textQueryTimeout.setText( "" + GetPreferenceGeneral.getQueryTimeOut() );
		
		textOraclePlan.setText( GetPreferenceGeneral.getPlanTableName() );
		comboRDBNumberComma.setText(GetPreferenceGeneral.getRDBNumberISComma());
		
		textCommitCount.setText(GetPreferenceGeneral.getRDBCommitCount());
		
		String strFontInfo = GetPreferenceGeneral.getRDBResultFont();
		lblUserFont.setText(strFontInfo);
		
		textShowInTheColumn.setText(GetPreferenceGeneral.getRDBShowInTheColumn());
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
}

