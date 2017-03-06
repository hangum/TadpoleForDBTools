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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.parser.UpdateDeleteParser;
import com.hangum.tadpole.engine.sql.parser.dto.QueryDMLInfoDTO;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.SQLResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * Update delete confirm dialog
 * 
 * @author hangum
 *
 */
public class UpdateDeleteConfirmDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(UpdateDeleteConfirmDialog.class);
	private UserDBDAO userDB;
	private RequestQuery reqQuery;
	private TableViewer tvQueryResult;
	private Text textQuery;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param reqQuery 
	 * @param userDB 
	 */
	public UpdateDeleteConfirmDialog(Shell parentShell, UserDBDAO userDB, RequestQuery reqQuery) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.reqQuery = reqQuery;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().GrantCheckerUtils_0);
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
		label_1.setText(CommonMessages.get().UserRequestQuery);
		
		textQuery = new Text(compositeInfo, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textQuery = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textQuery.minimumHeight = 80;
		gd_textQuery.heightHint = 80;
		textQuery.setLayoutData(gd_textQuery);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		Label label = new Label(compositeBody, SWT.NONE);
		label.setText(Messages.get().CheckDataAndRunQeury);
		
		tvQueryResult = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvQueryResult.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initData();

		return container;
	}

	/**
	 * initialize data
	 */
	private void initData() {
		String strSQL = reqQuery.getSql();
		if(reqQuery.getSqlStatementType() == PublicTadpoleDefine.SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
			strSQL = reqQuery.getSqlAddParameter();
		}
		
		textQuery.setText(strSQL);
		try {
			QueryDMLInfoDTO dmlInfoDto = new QueryDMLInfoDTO();
			UpdateDeleteParser parser = new UpdateDeleteParser();
			parser.parseQuery(strSQL, dmlInfoDto);
			
			String strObjecName = dmlInfoDto.getObjectName();
			String strWhereAfter = StringUtils.substringAfterLast(strSQL.toLowerCase(), "where");
			
			if(logger.isDebugEnabled()) {
				logger.debug("=============================================================================");
				logger.debug("object name : " + strObjecName);
				logger.debug("where after query: " + strWhereAfter);
				logger.debug("=============================================================================");
			}
			
			String sqlSelect = "select * from " + strObjecName + " where " + strWhereAfter;
			QueryExecuteResultDTO rsDAO = QueryUtils.executeQuery(userDB, sqlSelect, 0, GetPreferenceGeneral.getPageCount());
			createTableColumn(reqQuery, tvQueryResult, rsDAO, false);
			
			tvQueryResult.setLabelProvider(new SQLResultLabelProvider(reqQuery.getMode(), rsDAO));
			tvQueryResult.setContentProvider(new ArrayContentProvider());
			final TadpoleResultSet trs = rsDAO.getDataList();
			tvQueryResult.setInput(trs.getData());
			TableUtil.packTable(tvQueryResult.getTable());
			
			tvQueryResult.getTable().setToolTipText(sqlSelect);
		} catch(Exception e) {
			logger.error("initialize sql", e);
		}
	}
	
	/**
	 * table의 Column을 생성한다.
	 */
	public static void createTableColumn(final RequestQuery reqQuery,
										final TableViewer tableViewer,
										final ResultSetUtilDTO rsDAO,
										final boolean isEditable) {
		// 기존 column을 삭제한다.
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		if(rsDAO.getColumnName() == null) return;
			
		try {			
			for(int i=0; i<rsDAO.getColumnName().size(); i++) {
				final int columnAlign = RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i))?SWT.RIGHT:SWT.LEFT;
				String strColumnName = rsDAO.getColumnLabelName().get(i);
		
				/** 표시 되면 안되는 컬럼을 제거 합니다 */
				if(StringUtils.startsWithIgnoreCase(strColumnName, PublicTadpoleDefine.SPECIAL_USER_DEFINE_HIDE_COLUMN)) continue;
				
				final TableViewerColumn tv = new TableViewerColumn(tableViewer, columnAlign);
				final TableColumn tc = tv.getColumn();
				
				tc.setText(strColumnName);
				tc.setResizable(true);
				tc.setMoveable(true);
			}	// end for
			
		} catch(Exception e) { 
			logger.error("SQLResult TableViewer", e);
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
