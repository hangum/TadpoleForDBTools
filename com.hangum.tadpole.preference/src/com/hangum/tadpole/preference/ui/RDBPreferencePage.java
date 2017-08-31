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
import com.hangum.tadpole.commons.libs.core.define.TadpoleProperties;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.message.WarningMessages;
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
	
	private Combo comboRDBNumberComma;
	private Text textSelectLimit;
	private Text textResultPage;
	private Text textNull;
	private Label lblUserFont;
	
	private Text textQueryTimeout;
	private Text textCommitCount;
	private Text textShowInTheColumn;
	
	private Combo comboResultHeadClick;
	
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
		
		Label lblBasic = new Label(container, SWT.NONE);
		lblBasic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBasic.setText("Basic");
		
		Label labelBasic = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelBasic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNumberColumnAdd = new Label(container, SWT.NONE);
		lblNumberColumnAdd.setText(Messages.get().RDBPreferencePage_lblNumberColumnAdd_text);
		
		comboRDBNumberComma = new Combo(container, SWT.READ_ONLY);
		comboRDBNumberComma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.YES.name());
		comboRDBNumberComma.add(PublicTadpoleDefine.YES_NO.NO.name());
		comboRDBNumberComma.select(0);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.get().MaxNumOfRowsBySelect);
		
		textSelectLimit = new Text(container, SWT.BORDER);
		textSelectLimit.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textSelectLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().RowsPerPage);
		
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
		lblResultViewFont.setText(Messages.get().FontForResultPages);
		
		Composite compositeFont = new Composite(container, SWT.NONE);
		compositeFont.setLayout(new GridLayout(2, false));
		compositeFont.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblUserFont = new Label(compositeFont, SWT.NONE);
		lblUserFont.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblUserFont.setText("");
		
		Button btnNewButton = new Button(compositeFont, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setFontData();
			}
		});
		btnNewButton.setText(Messages.get().Font);
		
		Label lblQueryTimeout = new Label(container, SWT.NONE);
		lblQueryTimeout.setText(Messages.get().QueryTimeout);
		
		textQueryTimeout = new Text(container, SWT.BORDER);
		textQueryTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textQueryTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCommitCount = new Label(container, SWT.NONE);
		lblCommitCount.setText(Messages.get().BatchSize);
		
		textCommitCount = new Text(container, SWT.BORDER);
		textCommitCount.setText(Integer.toString(TadpoleProperties.ROWS_PER_PAGE_DEF));
		textCommitCount.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textCommitCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCharacterShownIn = new Label(container, SWT.NONE);
		lblCharacterShownIn.setText(Messages.get().MaximumNumberOfCharactersPerColumn);
		
		textShowInTheColumn = new Text(container, SWT.BORDER);
		textShowInTheColumn.setText(Messages.get().RDBPreferencePage_text_text_1);
		textShowInTheColumn.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		textShowInTheColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelClickResultHead = new Label(container, SWT.NONE);
		labelClickResultHead.setText(Messages.get().WhenClickingOnColumnName);
		
		comboResultHeadClick = new Combo(container, SWT.READ_ONLY);
		comboResultHeadClick.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboResultHeadClick.add(Messages.get().SortData);
		comboResultHeadClick.add(Messages.get().CopyColumnNameToEditor);
		comboResultHeadClick.select(1);
		
		Label lblOracleBar = new Label(container, SWT.NONE);
		lblOracleBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOracleBar.setText("Oracle");
		
		Label lblOracle = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblOracle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText(Messages.get().OraclePlanTable);
		
		Composite compositeOraclePlan = new Composite(container, SWT.NONE);
		compositeOraclePlan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeOraclePlan.setLayout(new GridLayout(2, false));
		
		textOraclePlan = new Text(compositeOraclePlan, SWT.BORDER);
		textOraclePlan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnCreatePlanTable = new Button(compositeOraclePlan, SWT.NONE);
		btnCreatePlanTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSimpleMessageDialog planDialog = new TadpoleSimpleMessageDialog(getShell(), textOraclePlan.getText(), ORACLE_PLAN_TABLE);
				planDialog.open();
			}
		});
		btnCreatePlanTable.setText(Messages.get().CreatePlanTable);
		textOraclePlan.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				isValid();
			}
		});
		
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
			setErrorMessage(String.format(WarningMessages.get().EnterNumbersOnlyWithItem, Messages.get().MaxNumOfRowsBySelect));
			return false;
		} else if(!((NumberUtils.toInt(txtSelectLimit) >= TadpoleProperties.NUMBER_OF_ROWS_BY_SELECT_MIN) 
				&& (NumberUtils.toInt(txtSelectLimit) <= TadpoleProperties.NUMBER_OF_ROWS_BY_SELECT_MAX))) {
			textSelectLimit.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().InvalidRange_GEAndLEWithItem, 
					                      Messages.get().MaxNumOfRowsBySelect, 
					                      TadpoleProperties.NUMBER_OF_ROWS_BY_SELECT_MIN, 
					                      TadpoleProperties.NUMBER_OF_ROWS_BY_SELECT_MAX));
			return false;
		} else if(!NumberUtils.isNumber(txtResultPage)) {
			textResultPage.setFocus();
			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().EnterNumbersOnlyWithItem, Messages.get().RowsPerPage));
			return false;
		} else if(!((NumberUtils.toInt(txtResultPage) >= TadpoleProperties.ROWS_PER_PAGE_MIN)
				 && (NumberUtils.toInt(txtResultPage) <= TadpoleProperties.ROWS_PER_PAGE_MAX))) {
			textResultPage.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().InvalidRange_GEAndLEWithItem, 
					                      Messages.get().RowsPerPage, 
					                      TadpoleProperties.ROWS_PER_PAGE_MIN, 
					                      TadpoleProperties.ROWS_PER_PAGE_MAX));
			return false;
		} else if(!NumberUtils.isNumber(txtQueryTimtout)) {
			textQueryTimeout.setFocus();

			setValid(false);
			setErrorMessage(Messages.get().QueryTimeout + WarningMessages.get().EnterNumbersOnlyWithItem);
			return false;
		} else if(!((NumberUtils.toInt(txtQueryTimtout) >= TadpoleProperties.QUERY_TIMEOUT_MIN)
				 && (NumberUtils.toInt(txtQueryTimtout) <= TadpoleProperties.QUERY_TIMEOUT_MAX))) {
			textQueryTimeout.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().InvalidRange_GEAndLEWithItem, 
					                      Messages.get().QueryTimeout, 
					                      TadpoleProperties.QUERY_TIMEOUT_MIN, 
					                      TadpoleProperties.QUERY_TIMEOUT_MAX));
			return false;
		} else if(!NumberUtils.isNumber(txtCommitCount)) {
			textCommitCount.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().EnterNumbersOnlyWithItem, Messages.get().BatchSize));
			return false;
		} else if(!((NumberUtils.toInt(txtCommitCount) >= TadpoleProperties.BATCH_SIZE_MIN) 
				 && (NumberUtils.toInt(txtCommitCount) <= TadpoleProperties.BATCH_SIZE_MAX))) {
			textCommitCount.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().InvalidRange_GEAndLEWithItem, 
					                      Messages.get().BatchSize, 
					                      TadpoleProperties.BATCH_SIZE_MIN, 
					                      TadpoleProperties.BATCH_SIZE_MAX));
			return false;
		} else if(!NumberUtils.isNumber(txtShownInTheColumn)) {
			textShowInTheColumn.setFocus();

			setValid(false);
			setErrorMessage(String.format(WarningMessages.get().EnterNumbersOnlyWithItem, Messages.get().BatchSize));
			return false;
//		} else if(!((NumberUtils.toInt(txtShownInTheColumn) >= TadpoleProperties.NUMBER_OF_CHARACTERS_PER_COLUMN_MIN)
//				 && (NumberUtils.toInt(txtShownInTheColumn) <= TadpoleProperties.NUMBER_OF_CHARACTERS_PER_COLUMN_MAX))) {
//			textShowInTheColumn.setFocus();
			
//			setValid(false);
//			setErrorMessage(String.format(WarningMessages.get().InvalidRange_GEAndLEWithItem, 
//					                      Messages.get().MaximumNumberOfCharactersPerColumn,
//					                      TadpoleProperties.NUMBER_OF_CHARACTERS_PER_COLUMN_MIN,
//					                      TadpoleProperties.NUMBER_OF_CHARACTERS_PER_COLUMN_MAX));
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
		
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtNull		= textNull.getText();
		String txtQueryTimtout = textQueryTimeout.getText();
		String txtOraclePlan = textOraclePlan.getText();
		String txtRDBNumberColumnIsComman = comboRDBNumberComma.getText();
		String txtFontInfo = lblUserFont.getText();
		String txtCommitCount = textCommitCount.getText();
		String txtShownInTheColumn = textShowInTheColumn.getText();
		
		String txtResultHeadClick = PreferenceDefine.RDB_RESULT_SET_HEAD_CLICK_SORTDATA_VALUE;
		if(comboResultHeadClick.getSelectionIndex() == 1) {
			txtResultHeadClick = PreferenceDefine.RDB_RESULT_SET_HEAD_CLICK_COPYCOLUMNNAME_VALUE;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateRDBUserInfoData(
					txtSelectLimit, txtResultPage, txtQueryTimtout, txtOraclePlan, txtRDBNumberColumnIsComman, txtFontInfo, txtCommitCount, txtShownInTheColumn, txtResultHeadClick, txtNull);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, txtSelectLimit);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, txtResultPage);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_NULL, txtNull);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_QUERY_TIMEOUT, txtQueryTimtout);
			
			SessionManager.setUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, txtOraclePlan);		
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, txtRDBNumberColumnIsComman);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_FONT, txtFontInfo);
			SessionManager.setUserInfo(PreferenceDefine.RDB_COMMIT_COUNT, txtCommitCount);
			SessionManager.setUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, txtShownInTheColumn);
			SessionManager.setUserInfo(PreferenceDefine.RDB_RESULT_SET_HEAD_CLICK, txtResultHeadClick);
			
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
		
		if(PreferenceDefine.RDB_RESULT_SET_HEAD_CLICK_SORTDATA_VALUE.equals(GetPreferenceGeneral.getRDBResultHeadClick())) {
			comboResultHeadClick.select(0);
		} else {
			comboResultHeadClick.select(1);
		}
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

