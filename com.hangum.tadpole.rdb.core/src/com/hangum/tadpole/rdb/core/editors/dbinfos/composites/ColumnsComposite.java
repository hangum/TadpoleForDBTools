/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - column information.
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;
import com.hangum.tadpole.sql.dao.rdb.RDBInfomationforColumnDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tables composite
 * 
 * @author nilriri
 * 
 */
public class ColumnsComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ColumnsComposite.class);

	private UserDBDAO userDB;
	private TableViewer tvColumnInform;

	private DefaultTableColumnFilter columnFilter;
	private Text textFilter;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ColumnsComposite(Composite parent, int style, UserDBDAO userDB) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		this.userDB = userDB;

		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));

		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Filter");

		textFilter = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.Selection) {
					columnFilter.setSearchString(textFilter.getText());
					tvColumnInform.refresh();
				}
			}
		});

		Button btnRefresh = new Button(compositeHead, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		btnRefresh.setText("Refresh");

		tvColumnInform = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvColumnInform.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTableColumn();

		columnFilter = new DefaultTableColumnFilter();
		tvColumnInform.addFilter(columnFilter);

		initUI();
	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {

		TableViewColumnDefine [] tableColumnDef = new TableViewColumnDefine [] {};
		if (DBDefine.getDBDefine(userDB) == DBDefine.MYSQL_DEFAULT || DBDefine.getDBDefine(userDB) == DBDefine.MARIADB_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) //
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) //
			};

		} else if (DBDefine.getDBDefine(userDB) == DBDefine.CUBRID_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) //
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("PARTITIONED", "Paritioned", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_PRECISION", "Data Precision", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_SCALE", "Data Scale", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) //
			};

		} else if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT || DBDefine.getDBDefine(userDB) == DBDefine.POSTGRE_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) //
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 150, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 120, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 150, SWT.LEFT).assignEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, 3) )  //
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 60, SWT.CENTER) //
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 120, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 60, SWT.CENTER) //
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 60, SWT.CENTER) //
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.RIGHT) //
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.RIGHT) //
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.RIGHT) //
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 120, SWT.LEFT) //
			};

		} else {
			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) //
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT).assignEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, 3) ) //
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 60, SWT.CENTER) //
					, new TableViewColumnDefine ("PK", "PK", 60, SWT.CENTER) //
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) //
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) //
			};
		}

		ColumnHeaderCreator.createColumnHeader(tvColumnInform, tableColumnDef);

		tvColumnInform.setContentProvider(new ArrayContentProvider());
		tvColumnInform.setLabelProvider(new DefaultLabelProvider(tvColumnInform));

	}


	/**
	 * 
	 */
	private void initUI() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<RDBInfomationforColumnDAO> listTableInform = null;
			if (DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT) {

				List<HashMap<String, String>> sqliteTableList = sqlClient.queryForList("tableInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$
				listTableInform = new ArrayList<RDBInfomationforColumnDAO>();
				for (HashMap<String, String> table : sqliteTableList) {

					List<HashMap<String, String>> sqliteColumnList = sqlClient.queryForList("columnInformation", table.get("name")); //$NON-NLS-1$ //$NON-NLS-2$


					for (HashMap<String, String> sqliteMap : sqliteColumnList) {//
						RDBInfomationforColumnDAO dao = new RDBInfomationforColumnDAO(table.get("name"), ""//
								, sqliteMap.get("name"), ""//
								, sqliteMap.get("type")//
								, ("0".equals(String.valueOf(sqliteMap.get("notnull"))) ? "No" : "")//
								, String.valueOf(sqliteMap.get("dflt_value") == null ? "" : sqliteMap.get("dflt_value"))//
								, ("1".equals(String.valueOf(sqliteMap.get("pk"))) ? "PK" : "") //
						);
						listTableInform.add(dao);
					}
				}

			} else {
				listTableInform = sqlClient.queryForList("columnInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			tvColumnInform.setInput(listTableInform);
			tvColumnInform.refresh();
			
			// google analytic
			AnalyticCaller.track(RDBDBInfosEditor.ID, "ColumnsComposite");
		} catch (Exception e) {
			logger.error("initialize column summary", e);

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}
	}

	@Override
	protected void checkSubclass() {
	}

}