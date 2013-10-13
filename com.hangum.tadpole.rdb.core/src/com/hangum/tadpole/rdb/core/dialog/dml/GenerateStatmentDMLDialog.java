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
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class GenerateStatmentDMLDialog extends Dialog {
	/** generation SQL string */
	private String genSQL = "";
	
	private UserDBDAO userDB;
	private String tableName;
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
	public GenerateStatmentDMLDialog(Shell parentShell, UserDBDAO userDB, String tableName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.tableName = tableName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.tableName + " DML Generator"); //$NON-NLS-1$
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

		TableViewerColumn tvColumnDataType = new TableViewerColumn(tableViewer, SWT.CENTER);
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
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put("db", userDB.getDb());
			parameter.put("table", tableName);

			lblTableName.setText(tableName);
			this.textTableAlias.setText("a");

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
			List<ExtendTableColumnDAO> newTableColumns = new ArrayList<ExtendTableColumnDAO>();

			ExtendTableColumnDAO newTableDAO = new ExtendTableColumnDAO("*", "", "", textTableAlias.getText().trim());// (ExtendTableColumnDAO)tableColumnDAO;
			newTableDAO.setCheck(true);

			newTableColumns.add(newTableDAO);
			for (TableColumnDAO tableColumnDAO : showTableColumns) {
				newTableDAO = new ExtendTableColumnDAO(tableColumnDAO.getField(), tableColumnDAO.getType(), tableColumnDAO.getKey(), textTableAlias.getText().trim());// (ExtendTableColumnDAO)tableColumnDAO;
				newTableColumns.add(newTableDAO);
			}

			tableViewer.setInput(newTableColumns);

			tableViewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String buildSelectSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection())
			resultSQL.append("/* Tadpole SQL Generator */").append(PublicTadpoleDefine.LINE_SEPARATOR);

		int cnt = 0;

		resultSQL.append("SELECT " + PublicTadpoleDefine.LINE_SEPARATOR);

		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if (allDao.isCheck()) {
				if (cnt == 0) {
					resultSQL.append("\t");
				} else {
					resultSQL.append("\t, ");
				}
				if ("*".equals(allDao.getField())) {
					resultSQL.append(allDao.getColumnNamebyTableAlias()).append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" as ").append(allDao.getColumnAlias()).append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				cnt++;
			}
		}
		resultSQL.append("  FROM " + this.tableName + " " + this.textTableAlias.getText().trim()).append(PublicTadpoleDefine.LINE_SEPARATOR);
		cnt = 0;
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) {
					resultSQL.append(" WHERE ");
				} else {
					resultSQL.append("\t AND ");
				}
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				cnt++;
			}
		}
		resultSQL.append(PublicTadpoleDefine.SQL_DILIMITER);

		return resultSQL.toString();
	}

	private String buildUpdateSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection())
			resultSQL.append("/* Tadpole SQL Generator */").append(PublicTadpoleDefine.LINE_SEPARATOR);

		int cnt = 0;

		resultSQL.append("UPDATE " + this.tableName + " " + this.textTableAlias.getText().trim() + PublicTadpoleDefine.LINE_SEPARATOR);

		ExtendTableColumnDAO firstDao = (ExtendTableColumnDAO) tableViewer.getElementAt(0);
		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getField())) {
					continue;
				}
				if (cnt == 0) {
					resultSQL.append("\t SET ");
				} else {
					resultSQL.append("\t, ");
				}
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				cnt++;

			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt == 0) {
						resultSQL.append("\t SET ");
					} else {
						resultSQL.append("\t, ");
					}
					resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
					if (chkComment.getSelection()) {
						resultSQL.append("/* " + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
					} else {
						resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
					}
					cnt++;

				}
			}
		}
		cnt = 0;
		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) {
					resultSQL.append(" WHERE ");
				} else {
					resultSQL.append("\t AND ");
				}
				resultSQL.append(allDao.getColumnNamebyTableAlias()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				cnt++;
			}
		}
		resultSQL.append(PublicTadpoleDefine.SQL_DILIMITER);

		return resultSQL.toString();
	}

	private String buildInsertSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection())
			resultSQL.append("/* Tadpole SQL Generator */").append(PublicTadpoleDefine.LINE_SEPARATOR);

		int cnt = 0;

		resultSQL.append("INSERT INTO " + this.tableName + " ( " + PublicTadpoleDefine.LINE_SEPARATOR);

		ExtendTableColumnDAO firstDao = (ExtendTableColumnDAO) tableViewer.getElementAt(0);
		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getField())) {
					continue;
				}
				if (cnt > 0) {

					resultSQL.append(", ");
				}
				resultSQL.append(allDao.getField()).append(PublicTadpoleDefine.LINE_SEPARATOR);
				;
				cnt++;

			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt > 0) {

						resultSQL.append(", ");
					}
					resultSQL.append(allDao.getField()).append(PublicTadpoleDefine.LINE_SEPARATOR);
					;
					cnt++;
				}
			}
		}
		resultSQL.append(")" + PublicTadpoleDefine.LINE_SEPARATOR + " VALUES ( ").append(PublicTadpoleDefine.LINE_SEPARATOR);
		cnt = 0;

		if (firstDao.isCheck()) {
			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if ("*".equals(allDao.getField())) {
					continue;
				}
				if (cnt > 0) {

					resultSQL.append(", ");
				}
				resultSQL.append("?");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getField() + ":" + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}

				cnt++;

			}

		} else {

			for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
				if (allDao.isCheck()) {
					if (cnt > 0) {

						resultSQL.append(", ");
					}
					resultSQL.append("?");
					if (chkComment.getSelection()) {
						resultSQL.append("/* " + allDao.getField() + ":" + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
					} else {
						resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
					}
					cnt++;
				}
			}
		}

		resultSQL.append(")");
		resultSQL.append(PublicTadpoleDefine.SQL_DILIMITER);

		return resultSQL.toString();
	}

	private String buildDeleteSQL() {
		StringBuffer resultSQL = new StringBuffer();
		if (chkComment.getSelection())
			resultSQL.append("/* Tadpole SQL Generator */").append(PublicTadpoleDefine.LINE_SEPARATOR);

		int cnt = 0;

		resultSQL.append("DELETE FROM  " + this.tableName + PublicTadpoleDefine.LINE_SEPARATOR);

		for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) tableViewer.getInput()) {
			if ("PK".equals(allDao.getKey())) {

				if (cnt == 0) {
					resultSQL.append(" WHERE ");
				} else {
					resultSQL.append("\t AND ");
				}
				resultSQL.append(allDao.getField()).append(" = ? ");
				if (chkComment.getSelection()) {
					resultSQL.append("/* " + allDao.getType() + " */").append(PublicTadpoleDefine.LINE_SEPARATOR);
				} else {
					resultSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
				}
				cnt++;
			}
		}
		resultSQL.append(PublicTadpoleDefine.SQL_DILIMITER);

		return resultSQL.toString();
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
			return dao.getColumnAlias();
		}

		return "*** not set column value ***";
	}

}
