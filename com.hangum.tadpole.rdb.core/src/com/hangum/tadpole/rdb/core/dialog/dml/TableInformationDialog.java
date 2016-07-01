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
package com.hangum.tadpole.rdb.core.dialog.dml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.sql.format.SQLFormater;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class TableInformationDialog extends Dialog {
	private static final Logger logger = Logger
			.getLogger(TableInformationDialog.class);
	private boolean isEditorAdd = false;

	/** generation SQL string */
	private String genSQL = ""; //$NON-NLS-1$

	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private TableViewer tableViewer;
	private TableViewer tableViewer_ext;
	private Text textTBNameCmt;
	private Label lblTableName;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TableInformationDialog(Shell parentShell, boolean isEditorAdd,
			UserDBDAO userDB, TableDAO tableDAO) {
		super(parentShell);
		setBlockOnOpen(isEditorAdd);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.isEditorAdd = isEditorAdd;
		this.userDB = userDB;
		this.tableDAO = tableDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(tableDAO.getName() + Messages.get().TableInformation);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;

		Composite compositeBody = new Composite(container, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeTable = new Composite(compositeBody, SWT.NONE);
		compositeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		compositeTable.setLayout(new GridLayout(2, false));

		lblTableName = new Label(compositeTable, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblTableName.setText(SQLUtil.getTableName(userDB, tableDAO));

		textTBNameCmt = new Text(compositeTable, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textTBNameCmt = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textTBNameCmt.heightHint = 33;
		textTBNameCmt.setLayoutData(gd_textTBNameCmt);
		textTBNameCmt.setText(tableDAO.getComment());

		Composite compositeDML = new Composite(compositeBody, SWT.NONE);
		compositeDML.setLayout(new GridLayout(5, false));
		compositeDML.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,	false, 1, 1));

		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumHeight = 300;
		table.setLayoutData(gd_table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvColumnName = new TableViewerColumn(tableViewer,	SWT.NONE);
		TableColumn tcColumnName = tvColumnName.getColumn();
		tcColumnName.setWidth(150);
		tcColumnName.setText(Messages.get().ColumnName);
		// tvColumnName.setEditingSupport(new
		// DMLColumnEditingSupport(tableViewer, 0, this));

		TableViewerColumn tvColumnDataType = new TableViewerColumn(tableViewer,	SWT.LEFT);
		TableColumn tcDataType = tvColumnDataType.getColumn();
		tcDataType.setWidth(100);
		tcDataType.setText(Messages.get().DataType);

		TableViewerColumn tvColumnKey = new TableViewerColumn(tableViewer, SWT.CENTER);
		TableColumn tcKey = tvColumnKey.getColumn();
		tcKey.setWidth(50);
		tcKey.setText(Messages.get().Key);

		TableViewerColumn tvColumnCmt = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcCmt = tvColumnCmt.getColumn();
		tcCmt.setWidth(300);
		tcCmt.setText(Messages.get().Description);

		Composite composite_3 = new Composite(compositeBody, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.verticalSpacing = 2;
		gl_composite_3.horizontalSpacing = 2;
		gl_composite_3.marginHeight = 2;
		gl_composite_3.marginWidth = 2;
		composite_3.setLayout(gl_composite_3);

		Composite previewComposite = new Composite(compositeBody, SWT.BORDER);
		previewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_previewComposite = new GridLayout(1, false);
		gl_previewComposite.verticalSpacing = 2;
		gl_previewComposite.horizontalSpacing = 2;
		gl_previewComposite.marginHeight = 2;
		gl_previewComposite.marginWidth = 2;
		previewComposite.setLayout(gl_previewComposite);

		tableViewer_ext = new TableViewer(previewComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_ext = tableViewer_ext.getTable();
		GridData gd_table_ext = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_ext.minimumHeight = 150;
		table_ext.setLayoutData(gd_table_ext);
		table_ext.setLinesVisible(true);
		table_ext.setHeaderVisible(true);

		TableViewerColumn tvPropertyName = new TableViewerColumn(tableViewer_ext, SWT.NONE);
		TableColumn tcPropertyName = tvPropertyName.getColumn();
		tcPropertyName.setWidth(180);
		tcPropertyName.setText("Property");

		TableViewerColumn tvPropertyValue = new TableViewerColumn(tableViewer_ext, SWT.NONE);
		TableColumn tcPropertyValue = tvPropertyValue.getColumn();
		tcPropertyValue.setWidth(300);
		tcPropertyValue.setText("Value");

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableInformationLabelProvider());

		initData();

		tableViewer_ext.setContentProvider(new ArrayContentProvider());
		tableViewer_ext.setLabelProvider(new TableStatisticsLabelProvider());
		initExtendedData();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	private void initData() {
		try {
			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery
					.getTableColumns(userDB, tableDAO);
			List<ExtendTableColumnDAO> newTableColumns = new ArrayList<ExtendTableColumnDAO>();

			ExtendTableColumnDAO newTableDAO;// = new ExtendTableColumnDAO("*", "", "", lblTableName.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			// newTableDAO.setCheck(true);
			// newTableColumns.add(newTableDAO);

			tableDAO = TadpoleObjectQuery.getTable(userDB, tableDAO.getName());
			textTBNameCmt.setText(tableDAO.getComment());

			for (TableColumnDAO tableColumnDAO : showTableColumns) {
				String strSysName = SQLUtil.makeIdentifierName(userDB, tableColumnDAO.getField());
				newTableDAO = new ExtendTableColumnDAO(tableColumnDAO.getField(), tableColumnDAO.getType(),	tableColumnDAO.getKey(), lblTableName.getText());
				newTableDAO.setSysName(strSysName);
				newTableDAO.setComment(tableColumnDAO.getComment());

				newTableColumns.add(newTableDAO);

			}

			tableViewer.setInput(newTableColumns);

			tableViewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initExtendedData() {
		try {
			Map<String, String> sizeInfoMap = (Map<String, String>) TadpoleObjectQuery.getTableSizeInfo(userDB, tableDAO);
			Map<String, String> statInfoMap = (Map<String, String>) TadpoleObjectQuery.getStatisticsInfo(userDB, tableDAO);

			List<Map<String, String>> extendsInfoList = new ArrayList<Map<String, String>>();

			for (String key : sizeInfoMap.keySet()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", key);
				map.put("value", String.valueOf(sizeInfoMap.get(key)));
				extendsInfoList.add(map);
			}

			for (String key : statInfoMap.keySet()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", key);
				map.put("value", String.valueOf(statInfoMap.get(key)));
				extendsInfoList.add(map);
			}

			tableViewer_ext.setInput(extendsInfoList);
			tableViewer_ext.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 쿼리 생성 후 후반작업을 합니다.
	 * 
	 * @param strSQL
	 * @return
	 */
	private String lastSQLGen(String strSQL) {
		String retSQL = strSQL + PublicTadpoleDefine.SQL_DELIMITER;
		try {
			retSQL = SQLFormater.format(retSQL);
		} catch (Exception e) {
			logger.error("SQL Formatting", e); //$NON-NLS-1$
		}

		return retSQL;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (isEditorAdd) {
			createButton(parent, IDialogConstants.OK_ID, Messages.get().GenerateStatmentDMLDialog_2, false);
		}
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}
}
