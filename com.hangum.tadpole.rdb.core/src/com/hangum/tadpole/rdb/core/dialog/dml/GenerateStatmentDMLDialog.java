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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.TadpoleObjectQuery;
import com.hangum.tadpole.sql.format.SQLFormater;
import com.swtdesigner.ResourceManager;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class GenerateStatmentDMLDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(GenerateStatmentDMLDialog.class);
	
	/** generation SQL string */
	private String genSQL = "";
	
	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private TableViewer tableViewer;
	private Text textTableAlias;
	private Text textQuery;
	private Label lblTableName;

	private Button chkComment;

	private Button rdoSelect;
	private Button rdoUpdate;
	private Button rdoInsert;
	private Button rdoDelete;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public GenerateStatmentDMLDialog(Shell parentShell, UserDBDAO userDB, TableDAO tableDAO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.tableDAO = tableDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("DML Generator"); //$NON-NLS-1$
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

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDml = new Label(composite, SWT.NONE);
		lblDml.setText("DML");

		rdoSelect = new Button(composite, SWT.RADIO);
		rdoSelect.setSelection(true);
		rdoSelect.setText("SELECT");

		rdoUpdate = new Button(composite, SWT.RADIO);
		rdoUpdate.setText("UPDATE");

		rdoInsert = new Button(composite, SWT.RADIO);
		rdoInsert.setText("INSERT");

		rdoDelete = new Button(composite, SWT.RADIO);
		rdoDelete.setText("DELETE");

		assignSelectionAdapter(rdoSelect);
		assignSelectionAdapter(rdoUpdate);
		assignSelectionAdapter(rdoInsert);
		assignSelectionAdapter(rdoDelete);

		Composite composite_1 = new Composite(container, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.verticalSpacing = 2;
		gl_composite_1.horizontalSpacing = 2;
		gl_composite_1.marginHeight = 2;
		gl_composite_1.marginWidth = 2;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_2.heightHint = 28;
		composite_2.setLayoutData(gd_composite_2);
		composite_2.setLayout(new GridLayout(4, false));

		Label lblTable = new Label(composite_2, SWT.NONE);
		lblTable.setText("Table");

		lblTableName = new Label(composite_2, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText("table name");

		Label lblAs = new Label(composite_2, SWT.NONE);
		lblAs.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAs.setText(" as ");

		textTableAlias = new Text(composite_2, SWT.BORDER);
		textTableAlias.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if (tableViewer.getInput() != null) {
					for (ExtendTableColumnDAO dao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {

						dao.setTableAlias(textTableAlias.getText());
					}
					tableViewer.refresh();
					queryGenetation();
				}
			}
		});
		textTableAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvColumnName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcColumnName = tvColumnName.getColumn();
		tcColumnName.setWidth(150);
		tcColumnName.setText("Column Name");
		tvColumnName.setEditingSupport(new DMLColumnEditingSupport(tableViewer, 0, this));

		TableViewerColumn tvColumnDataType = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcDataType = tvColumnDataType.getColumn();
		tcDataType.setWidth(120);
		tcDataType.setText("Data Type");

		TableViewerColumn tvColumnKey = new TableViewerColumn(tableViewer, SWT.CENTER);
		TableColumn tcKey = tvColumnKey.getColumn();
		tcKey.setWidth(50);
		tcKey.setText("Key");

		TableViewerColumn tvColumnAlias = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcAlias = tvColumnAlias.getColumn();
		tcAlias.setWidth(100);
		tcAlias.setText("Alias");
		tvColumnAlias.setEditingSupport(new DMLColumnEditingSupport(tableViewer, 3, this));

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.verticalSpacing = 2;
		gl_composite_3.horizontalSpacing = 2;
		gl_composite_3.marginHeight = 2;
		gl_composite_3.marginWidth = 2;
		composite_3.setLayout(gl_composite_3);

		final Button btnAllCheck = new Button(composite_3, SWT.CHECK);
		btnAllCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (ExtendTableColumnDAO dao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
					if ("*".equals(dao.getField())) {
						dao.setCheck(!btnAllCheck.getSelection());
					} else {
						dao.setCheck(btnAllCheck.getSelection());
					}
				}
				tableViewer.refresh();
				queryGenetation();
			}
		});
		btnAllCheck.setText("All Columns");

		chkComment = new Button(composite_3, SWT.CHECK);
		chkComment.setText("Include Comment");
		assignSelectionAdapter(chkComment);
		
		Composite previewComposite = new Composite(composite_1, SWT.BORDER);
		previewComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_previewComposite = new GridLayout(1, false);
		gl_previewComposite.verticalSpacing = 2;
		gl_previewComposite.horizontalSpacing = 2;
		gl_previewComposite.marginHeight = 2;
		gl_previewComposite.marginWidth = 2;
		previewComposite.setLayout(gl_previewComposite);

		textQuery = new Text(previewComposite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textQuery = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textQuery.minimumHeight = 120;
		textQuery.setLayoutData(gd_textQuery);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new GenerateLabelProvider());

		initData();
		queryGenetation();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	private void assignSelectionAdapter(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryGenetation();
			}
		});
	}

	public String queryGenetation() {
		String sql = "";

		if (rdoSelect.getSelection()) {
			sql = buildSelectSQL();
		} else if (rdoUpdate.getSelection()) {
			sql = buildUpdateSQL();
		} else if (rdoInsert.getSelection()) {
			sql = buildInsertSQL();
		} else if (rdoDelete.getSelection()) {
			sql = buildDeleteSQL();
		} else {
			sql = "/* DML generation error. */";
		}
		genSQL = sql;

		this.textQuery.setText(sql);

		return sql;

	}
	
	public String getDML() {
		return genSQL;
	}

	private void initData() {
		try {
			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.makeShowTableColumns(userDB, tableDAO);
			List<ExtendTableColumnDAO> newTableColumns = new ArrayList<ExtendTableColumnDAO>();

			ExtendTableColumnDAO newTableDAO = new ExtendTableColumnDAO("*", "", "", textTableAlias.getText().trim());
			newTableDAO.setCheck(true);

			newTableColumns.add(newTableDAO);
			for (TableColumnDAO tableColumnDAO : showTableColumns) {
				String strSysName = SQLUtil.makeIdentifierName(userDB, tableColumnDAO.getField());
				newTableDAO = new ExtendTableColumnDAO(tableColumnDAO.getField(), tableColumnDAO.getType(), tableColumnDAO.getKey(), textTableAlias.getText().trim());
				newTableDAO.setSysName(strSysName);
				
				newTableColumns.add(newTableDAO);
			}
			
			tableViewer.setInput(newTableColumns);

			tableViewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Generate SELECT statment
	 * 
	 * @return
	 */
	private String buildSelectSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection()) {
			resultSQL.append("/* Tadpole SQL Generator */");
		}
		resultSQL.append("SELECT ");
		
		int cnt = 0;

		StringBuffer sbColumn = new StringBuffer();
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if (allDao.isCheck()) {
				if (cnt != 0) sbColumn.append("\t, ");
				
				if ("*".equals(allDao.getField())) {
					sbColumn.append(allDao.getSysName());
				} else {
					sbColumn.append(allDao.getSysName()).append(" as ").append(allDao.getColumnAlias());
				}
				cnt++;
			}
		}
		if(StringUtils.isEmpty(StringUtils.trim(sbColumn.toString()))) sbColumn.append(" * " );
		
		resultSQL.append(sbColumn.toString());
		resultSQL.append("  FROM " + this.tableDAO.getSysName() + " " + this.textTableAlias.getText().trim());
		cnt = 0;
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) {
					resultSQL.append(" WHERE ");
				} else {
					resultSQL.append(" AND ");
				}
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */");
				}
				cnt++;
			}
		}

		return lastSQLGen(resultSQL.toString());
	}

	/**
	 * Generate UPDATE statement
	 * @return
	 */
	private String buildUpdateSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection()) resultSQL.append("/* Tadpole SQL Generator */");

		int cnt = 0;

		resultSQL.append("UPDATE " + this.tableDAO.getSysName() + " " + this.textTableAlias.getText().trim());

		ExtendTableColumnDAO firstDao = (ExtendTableColumnDAO) tableViewer.getElementAt(0);
		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getField())) continue;
				
				if (cnt == 0) resultSQL.append(" SET ");
				else resultSQL.append(" , ");
				
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */");
				}
				cnt++;
			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt == 0) resultSQL.append(" SET ");
					else resultSQL.append(", ");
					
					resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
					if (chkComment.getSelection()) {
						resultSQL.append("/* " + allDao.getType() + " */");
					}
					cnt++;

				}
			}
		}
		cnt = 0;
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) resultSQL.append(" WHERE ");
				else resultSQL.append(" AND ");
				
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */");
				}
				cnt++;
			}
		}
		
		return lastSQLGen(resultSQL.toString());
	}

	/**
	 * Generate INSERT statement
	 * @return
	 */
	private String buildInsertSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection()) resultSQL.append("/* Tadpole SQL Generator */");

		int cnt = 0;

		resultSQL.append("INSERT INTO " + tableDAO.getSysName() + " ( " );

		ExtendTableColumnDAO firstDao = (ExtendTableColumnDAO) tableViewer.getElementAt(0);
		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getField())) continue;

				if (cnt > 0) resultSQL.append(", ");
				resultSQL.append(allDao.getSysName());

				cnt++;
			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt > 0) resultSQL.append(", ");
					resultSQL.append(allDao.getSysName());
					
					cnt++;
				}
			}
		}
		resultSQL.append(")" + PublicTadpoleDefine.LINE_SEPARATOR + " VALUES ( ");
		cnt = 0;

		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getSysName())) continue;
				
				if (cnt > 0) resultSQL.append(", ");
				resultSQL.append("?");
				
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getField() + ":" + allDao.getType() + " */");
				}

				cnt++;
			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt > 0) resultSQL.append(", ");
					
					resultSQL.append("?");
					if (chkComment.getSelection()) {
						resultSQL.append("/* " + allDao.getField() + ":" + allDao.getType() + " */");
					}
					cnt++;
				}
			}
		}

		resultSQL.append(")");
		
		return lastSQLGen(resultSQL.toString());
	}

	private String buildDeleteSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection()) resultSQL.append("/* Tadpole SQL Generator */");

		int cnt = 0;

		resultSQL.append("DELETE FROM  " + tableDAO.getSysName());
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) resultSQL.append(" WHERE ");
				else resultSQL.append("\t AND ");
				
				resultSQL.append(allDao.getSysName()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */");
				}
				cnt++;
			}
		}

		return lastSQLGen(resultSQL.toString());
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
			logger.error("SQL Formatting", e);
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
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 550);
	}
}

class GenerateLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final Image CHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/checked.png"); //$NON-NLS-1$;
	private static final Image UNCHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/unchecked.png"); //$NON-NLS-1$;

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (columnIndex == 0) {
			ExtendTableColumnDAO columnDao = (ExtendTableColumnDAO) element;
			if (columnDao.isCheck()) {
				return CHECKED;
			} else {
				return UNCHECKED;
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ExtendTableColumnDAO dao = (ExtendTableColumnDAO) element;

		switch (columnIndex) {
		case 0:
			return dao.getColumnNamebyTableAlias();
		case 1:
			return dao.getType();
		case 2:
			return dao.getKey();
		case 3:
			return dao.getSysName();
		}

		return "*** not set column value ***";
	}

}
