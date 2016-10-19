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
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.sql.format.SQLFormater;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class IndexInformationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(IndexInformationDialog.class);
	private boolean isEditorAdd = false;

	/** generation SQL string */
	private String genSQL = ""; //$NON-NLS-1$

	private UserDBDAO userDB;
	private InformationSchemaDAO indexDAO;
	private TableViewer indexTableViewer;
	private TableViewer tableViewer_ext;
	private Text textTBNameCmt;
	private Label lblTableName;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public IndexInformationDialog(Shell parentShell, boolean isEditorAdd, UserDBDAO userDB, InformationSchemaDAO indexDAO) {
		super(parentShell);
		setBlockOnOpen(isEditorAdd);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.isEditorAdd = isEditorAdd;
		this.userDB = userDB;
		this.indexDAO = indexDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(indexDAO.getINDEX_NAME() + " " + CommonMessages.get().Information);
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
		lblTableName.setText(SQLUtil.getIndexName(indexDAO));

		textTBNameCmt = new Text(compositeTable, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textTBNameCmt = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textTBNameCmt.heightHint = 33;
		textTBNameCmt.setLayoutData(gd_textTBNameCmt);
		textTBNameCmt.setText(indexDAO.getCOMMENT());

		Composite compositeDML = new Composite(compositeBody, SWT.NONE);
		compositeDML.setLayout(new GridLayout(5, false));
		compositeDML.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,	false, 1, 1));

		indexTableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = indexTableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumHeight = 160;
		table.setLayoutData(gd_table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvColumnName = new TableViewerColumn(indexTableViewer, SWT.NONE);
		TableColumn tcColumnName = tvColumnName.getColumn();
		tcColumnName.setWidth(120);
		tcColumnName.setText(Messages.get().ColumnName);

		TableViewerColumn tvColumnDataType = new TableViewerColumn(indexTableViewer, SWT.CENTER);
		TableColumn tcDataType = tvColumnDataType.getColumn();
		tcDataType.setWidth(60);
		tcDataType.setText("Order");

		TableViewerColumn tvColumnKey = new TableViewerColumn(indexTableViewer, SWT.RIGHT);
		TableColumn tcKey = tvColumnKey.getColumn();
		tcKey.setWidth(60);
		tcKey.setText("Position");

		TableViewerColumn tvColumnCmt = new TableViewerColumn(indexTableViewer, SWT.RIGHT);
		TableColumn tcCmt = tvColumnCmt.getColumn();
		tcCmt.setWidth(50);
		tcCmt.setText("Length");

		TableViewerColumn tvTableName = new TableViewerColumn(indexTableViewer, SWT.NONE);
		TableColumn tcTableName = tvTableName.getColumn();
		tcTableName.setWidth(90);
		tcTableName.setText("Table Name");

		TableViewerColumn tvTableOwner = new TableViewerColumn(indexTableViewer, SWT.NONE);
		TableColumn tcTableOwner = tvTableOwner.getColumn();
		tcTableOwner.setWidth(90);
		tcTableOwner.setText("Table Owner");

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
		tcPropertyName.setWidth(160);
		tcPropertyName.setText("Property");

		TableViewerColumn tvPropertyValue = new TableViewerColumn(tableViewer_ext, SWT.NONE);
		TableColumn tcPropertyValue = tvPropertyValue.getColumn();
		tcPropertyValue.setWidth(250);
		tcPropertyValue.setText("Value");

		indexTableViewer.setContentProvider(new ArrayContentProvider());
		indexTableViewer.setLabelProvider(new IndexInformationLabelProvider());

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
			List<InformationSchemaDAO> showIndexColumns = TadpoleObjectQuery.getIndexColumns(userDB, indexDAO);

			indexTableViewer.setInput(showIndexColumns);

			indexTableViewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initExtendedData() {
		try {
			Map<String, String> statInfoMap = (Map<String, String>) TadpoleObjectQuery.getIndexStatisticsInfo(userDB, indexDAO);

			List<Map<String, String>> extendsInfoList = new ArrayList<Map<String, String>>();

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
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false);

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}
}
