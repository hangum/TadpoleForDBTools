/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.db;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.dao.SQLStatementStruct;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDMLCommand;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.QueryResultLabelProvider;
import com.hangum.tadpole.sql.parse.UpdateDeleteStatementParser;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Update delete confirm dialog
 * 
 * @author hangum
 *
 */
public class UpdateDeleteConfirmDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(UpdateDeleteConfirmDialog.class);
	
	private RequestQuery reqQuery;
	private TableViewer tvQueryResult;
	private Text textQuery;
	
	// 결과를 출력하기 위한 테이블 
	private boolean isWhere = false;
	private Composite compositeData;
	private Label labelSummaryText;
	private Button btnAllDataDelete;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param reqQuery 
	 * @param userDB 
	 */
	public UpdateDeleteConfirmDialog(Shell parentShell, RequestQuery reqQuery) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.reqQuery = reqQuery;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().CheckDataAndRunQeury);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		
		Composite compositeInfo = new Composite(container, SWT.NONE);
		compositeInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeInfo.setLayout(new GridLayout(1, false));
		
		Label label_1 = new Label(compositeInfo, SWT.NONE);
		label_1.setText(Messages.get().GrantCheckerUtils_0);
		
		textQuery = new Text(compositeInfo, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textQuery = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textQuery.minimumHeight = 80;
		gd_textQuery.heightHint = 80;
		textQuery.setLayoutData(gd_textQuery);

		
		btnAllDataDelete = new Button(compositeInfo, SWT.CHECK);
		btnAllDataDelete.setText(Messages.get().AreYouModifyAllData);

		// 수정 예상 데이터 리스트 ui
		compositeData = new Composite(container, SWT.NONE);
		compositeData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeData.setLayout(new GridLayout(1, false));
		
		labelSummaryText = new Label(compositeData, SWT.NONE);
		labelSummaryText.setText(Messages.get().CheckDataAndRunQeury);
		
		tvQueryResult = new TableViewer(compositeData, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvQueryResult.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblAaa = new Label(compositeData, SWT.NONE);
		lblAaa.setText(String.format(Messages.get().UpdateDeleteConfirmDialog_Message, 500));
		
		initData();

		return container;
	}
	
	@Override
	protected void okPressed() {
		if(!isWhere) {
			if(!btnAllDataDelete.getSelection()) {
				MessageDialog.openInformation(getShell(), CommonMessages.get().Information, Messages.get().UpdateDeleteConfirmDialog_PleaseSelect);
				btnAllDataDelete.setFocus();
				return;
			}
		}
		super.okPressed();
	}

	/**
	 * initialize data
	 */
	private void initData() {
		String strSQL = reqQuery.getOriginalSql();
		if(reqQuery.getSqlStatementType() == PublicTadpoleDefine.SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
			strSQL = reqQuery.getSqlAddParameter();
		}
		
		textQuery.setText(strSQL);
		try {
			SQLStatementStruct sqlStatement = UpdateDeleteStatementParser.getParse(reqQuery.getUserDB(), strSQL);
			String sqlSelect = "select * from " + sqlStatement.getObjectName();
			if(!StringUtils.trimToEmpty(sqlStatement.getWhere()).equals("")) {
				sqlSelect += " where " + sqlStatement.getWhere();
				
				isWhere = true;
			}
			if(logger.isDebugEnabled()) logger.debug("[change select statement]" + sqlSelect);
			
			if(isWhere) {
				RequestQuery _cloneRequestQuery = (RequestQuery)reqQuery.clone();
				_cloneRequestQuery.setSql(sqlSelect);
				QueryExecuteResultDTO rsDAO = ExecuteDMLCommand.executSQL(_cloneRequestQuery, 0, 500);
				TableUtil.createTableColumn(tvQueryResult, rsDAO);
				
				tvQueryResult.setLabelProvider(new QueryResultLabelProvider(reqQuery.getMode(), rsDAO));
				tvQueryResult.setContentProvider(ArrayContentProvider.getInstance());
				final TadpoleResultSet trs = rsDAO.getDataList();
				tvQueryResult.setInput(trs.getData());
				TableUtil.packTable(tvQueryResult.getTable());
				
				if(trs.getData().size() < GetPreferenceGeneral.getPageCount()) {
					labelSummaryText.setText(Messages.get().CheckDataAndRunQeury + " (" + String.format(Messages.get().UpdateDeleteConfirmDialog_findData, trs.getData().size()) + ")");
				} else {
					labelSummaryText.setText(Messages.get().CheckDataAndRunQeury + " (" + String.format(Messages.get().UpdateDeleteConfirmDialog_findDataOver, trs.getData().size()) + ")");
				}
				
				tvQueryResult.getTable().setToolTipText(sqlSelect);
			} else {
				if(logger.isDebugEnabled()) logger.debug("mabe all data delete");
			}
		
			// 젠처 ui를 초기화 한다.
			compositeData.setVisible(isWhere);
			btnAllDataDelete.setEnabled(!isWhere);
			
			tvQueryResult.getTable().setFocus();
			
		} catch(Exception e) {
			logger.error("initialize sql", e);
			
			MessageDialog.openError(getShell(), CommonMessages.get().Error, e.getMessage() + "\n" + Messages.get().CheckSQLStatement);
			
			this.close();
		}
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Yes, false);
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().No, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(714, 484);
	}
}
