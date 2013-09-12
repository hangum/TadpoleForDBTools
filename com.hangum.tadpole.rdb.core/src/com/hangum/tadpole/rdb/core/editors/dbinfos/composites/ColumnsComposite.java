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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.rdb.RDBInfomationforColumnDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.ColumnCommentEditorSupport;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableCommentEditorSupport;
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

	private ColumnInfoFilter columnFilter;
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

		columnFilter = new ColumnInfoFilter();
		tvColumnInform.addFilter(columnFilter);

		initUI();
	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {

		TableColumnDef[] tableColumnDef = new TableColumnDef[] {};
		if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT || DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT) {

			tableColumnDef = new TableColumnDef[] { //
			new TableColumnDef("TABLE_NAME", "Table Name", 100, SWT.LEFT) //
					, new TableColumnDef("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) //
					, new TableColumnDef("COLUMN_NAME", "Column Name", 100, SWT.LEFT) //
					, new TableColumnDef("NULLABLE", "Nullable", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_TYPE", "Data Type", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableColumnDef("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableColumnDef("CHAR_USED", "Char Used", 100, SWT.LEFT) //
					, new TableColumnDef("HISTOGRAM", "Histogram", 100, SWT.LEFT) //
					, new TableColumnDef("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) //
					, new TableColumnDef("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) //
					, new TableColumnDef("DENSITY", "Density", 100, SWT.LEFT) //
					, new TableColumnDef("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) //
			};

		} else if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT||DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.POSTGRE_DEFAULT) {

			tableColumnDef = new TableColumnDef[] { //
			new TableColumnDef("TABLE_NAME", "Table Name", 100, SWT.LEFT) //
					, new TableColumnDef("TABLE_COMMENT", "Table Comment", 150, SWT.LEFT) //
					, new TableColumnDef("COLUMN_NAME", "Column Name", 120, SWT.LEFT) //
					, new TableColumnDef("COLUMN_COMMENT", "Column Comment", 150, SWT.LEFT) //
					, new TableColumnDef("NULLABLE", "Nullable", 60, SWT.CENTER) //
					, new TableColumnDef("DATA_TYPE", "Data Type", 120, SWT.LEFT) //
					, new TableColumnDef("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableColumnDef("CHAR_USED", "Char Used", 60, SWT.CENTER) //
					, new TableColumnDef("HISTOGRAM", "Histogram", 60, SWT.CENTER) //
					, new TableColumnDef("NUM_DISTINCT", "Num Distinct", 100, SWT.RIGHT) //
					, new TableColumnDef("NUM_NULLS", "Num Nulls", 100, SWT.RIGHT) //
					, new TableColumnDef("DENSITY", "Density", 100, SWT.RIGHT) //
					, new TableColumnDef("LAST_ANALYZED", "Last Analyzed", 120, SWT.LEFT) //
			};

		} else {
			tableColumnDef = new TableColumnDef[] { //
			new TableColumnDef("TABLE_NAME", "Table Name", 100, SWT.LEFT) //
					, new TableColumnDef("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) //
					, new TableColumnDef("COLUMN_NAME", "Column Name", 100, SWT.LEFT) //
					, new TableColumnDef("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) //
					, new TableColumnDef("NULLABLE", "Nullable", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_TYPE", "Data Type", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) //
					, new TableColumnDef("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) //
					, new TableColumnDef("CHAR_USED", "Char Used", 100, SWT.LEFT) //
					, new TableColumnDef("HISTOGRAM", "Histogram", 100, SWT.LEFT) //
					, new TableColumnDef("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) //
					, new TableColumnDef("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) //
					, new TableColumnDef("DENSITY", "Density", 100, SWT.LEFT) //
					, new TableColumnDef("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) //
			};
		}

		createColumnHeader(tableColumnDef);

		tvColumnInform.setContentProvider(new ArrayContentProvider());
		tvColumnInform.setLabelProvider(new ColumnInformLabelProvider(tvColumnInform));

	}

	/**
	 * 실제 컬럼의 헤더를 생성합니다.
	 * 
	 * @param name
	 * @param size
	 */
	private void createColumnHeader(TableColumnDef[] colDef) {
		AllColumnComparator tableComparator = new AllColumnComparator();

		for (int i = 0; i < colDef.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvColumnInform, colDef[i].align);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setText(colDef[i].caption);
			tableColumn.setData("column", colDef[i].column);
			tableColumn.setWidth(colDef[i].width);

			tableColumn.addSelectionListener(getSelectionAdapter(tvColumnInform, tableComparator, tableColumn, i));
			
			if ("TABLE_COMMENT".equals(colDef[i].column)){
				//table is multi line display...(table object explorer)
				//tableViewerColumn.setEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, i));
			}else if ("COLUMN_COMMENT".equals(colDef[i].column)) {
				tableViewerColumn.setEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, i));
			}
		}

		tvColumnInform.setSorter(tableComparator);
	}

	protected SelectionAdapter getSelectionAdapter(final TableViewer viewer, final ObjectComparator comparator, final TableColumn column, final int index) {
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};

		return adapter;
	}

	/**
	 * 
	 */
	private void initUI() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<RDBInfomationforColumnDAO> listTableInform = sqlClient.queryForList("columnInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$

			tvColumnInform.setInput(listTableInform);
			tvColumnInform.refresh();
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

class TableColumnDef {
	String column;
	String caption;
	int width;
	int align;

	public TableColumnDef(String column) {
		this(column, StringUtils.capitalize(column.toLowerCase().replace("_", "")));
	}

	public TableColumnDef(String column, String caption) {
		this(column, caption, 100);
	}

	public TableColumnDef(String column, String caption, int width) {
		this(column, caption, width, SWT.LEFT);
	}

	TableColumnDef(String column, String caption, int width, int align) {
		this.column = column;
		this.caption = caption == null ? StringUtils.capitalize(column.toLowerCase().replace("_", "")) : caption;
		this.width = width == 0 ? 100 : width;
		this.align = align <= 0 ? SWT.LEFT : align;
	}
}

/**
 * sort를 위한 최상위 클래서(기본으로 table의 column 사용)
 * 
 * @author hangum
 * 
 */
class AllColumnComparator extends ObjectComparator {
	public AllColumnComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		RDBInfomationforColumnDAO tc1 = (RDBInfomationforColumnDAO) e1;
		RDBInfomationforColumnDAO tc2 = (RDBInfomationforColumnDAO) e2;

		String column = (String) ((TableViewer) viewer).getTable().getColumn(propertyIndex).getData("column");
		int rc = tc1.compareToIgnoreCase(tc2, column);

		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}

/**
 * mysql, mariadb label provider
 * 
 * @author hangum
 * 
 */
class ColumnInformLabelProvider extends LabelProvider implements ITableLabelProvider {
	private TableViewer tableViewer;

	public ColumnInformLabelProvider(TableViewer tv) {
		this.tableViewer = tv;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String columnName = (String) tableViewer.getTable().getColumn(columnIndex).getData("column");
		RDBInfomationforColumnDAO resultMap = (RDBInfomationforColumnDAO) element;
		return resultMap.getColumnValuebyName(columnName);
	}

}

/**
 * name filetr
 * 
 * @author hangum
 * 
 */
class ColumnInfoFilter extends ViewerFilter {
	private String searchString;

	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		} else {

			RDBInfomationforColumnDAO dao = (RDBInfomationforColumnDAO) element;
			String targetString = "";

			for (TableColumn tc : ((TableViewer) viewer).getTable().getColumns()) {
				targetString = dao.getColumnValuebyName((String) tc.getData("column")).toLowerCase();

				if (targetString.matches(searchString)) {
					return true;
				}
			}
		}
		return false;
	}
}
