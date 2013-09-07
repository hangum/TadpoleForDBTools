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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.util.NumberFormatUtils;
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

	private String[] selectColumns;

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
				if (e.keyCode == SWT.Selection){
					columnFilter.setSearchString(selectColumns, textFilter.getText());
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

		createColumn();

		columnFilter = new ColumnInfoFilter();
		tvColumnInform.addFilter(columnFilter);

		initUI();
	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createColumn() {

		String[] name;
		int[] size;
		int[] align;
		if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT || DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT) {
			selectColumns = new String[] { "TABLE_NAME", "TABLE_COMMENT", "COLUMN_NAME", "COLUMN_COMMENT" };
			name = new String[] { "Table Name", "Table Comment", "Column Name", "Column Comment" };
			size = new int[] { 120, 180, 150, 180 };
			align = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		} else if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			selectColumns = new String[] { "TABLE_NAME", "TABLE_COMMENT", "COLUMN_NAME", "COLUMN_COMMENT" };
			name = new String[] { "Table Name", "Table Comment", "Column Name", "Column Comment" };
			size = new int[] { 120, 180, 150, 180 };
			align = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		} else {
			selectColumns = new String[] { "TABLE_NAME", "TABLE_COMMENT", "COLUMN_NAME", "COLUMN_COMMENT" };
			name = new String[] { "Table Name", "Table Comment", "Column Name", "Column Comment" };
			size = new int[] { 120, 180, 150, 180 };
			align = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		}

		// sorter
		AllColumnComparator tableComparator = new AllColumnComparator(selectColumns);
		tvColumnInform.setSorter(tableComparator);

		createColumn(tableComparator, name, size, align);

		tvColumnInform.setContentProvider(new ArrayContentProvider());
		tvColumnInform.setLabelProvider(new ColumnInformLabelProvider(userDB, selectColumns));

	}

	/**
	 * 실제 컬럼의 헤더를 생성합니다.
	 * 
	 * @param name
	 * @param size
	 */
	private void createColumn(AllColumnComparator tableComparator, String[] name, int[] size, int[] align) {
		for (int i = 0; i < name.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvColumnInform, align[i]);

			TableColumn tableColumn = tableViewerColumn.getColumn();

			tableColumn.setText(name[i]);
			tableColumn.setWidth(size[i]);

			tableColumn.addSelectionListener(getSelectionAdapter(tvColumnInform, tableComparator, tableColumn, i));

		}
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
			List listTableInform = sqlClient.queryForList("columnInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$

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

/**
 * sort를 위한 최상위 클래서(기본으로 table의 column 사용)
 * 
 * @author hangum
 * 
 */
class AllColumnComparator extends ObjectComparator {

	String[] keyColumns;

	public AllColumnComparator(String[] key) {
		this.propertyIndex = 0;
		direction = DESCENDING;

		this.keyColumns = key;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Map tc1 = (HashMap) e1;
		Map tc2 = (HashMap) e2;

		int rc = StringValueCompare(tc1, tc2, keyColumns[propertyIndex]);

		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

	private int StringValueCompare(Map map1, Map map2, String key) {
		String value1 = "";
		String value2 = "";

		value1 = map1.get(key) == null ? "" : map1.get(key).toString().trim();
		value2 = map2.get(key) == null ? "" : map2.get(key).toString().trim();

		return value1.compareToIgnoreCase(value2);
	}

}

/**
 * mysql, mariadb label provider
 * 
 * @author hangum
 * 
 */
class ColumnInformLabelProvider extends LabelProvider implements ITableLabelProvider {
	private UserDBDAO userDB;
	private String[] displayColumns;

	public ColumnInformLabelProvider(UserDBDAO userDB, String[] columns) {
		this.userDB = userDB;
		this.displayColumns = columns;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map resultMap = (HashMap) element;

		if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT || DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT) {
			switch (columnIndex) {
			case 0:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 1:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 2:
				return NumberFormatUtils.commaFormat("" + resultMap.get(displayColumns[columnIndex]));
			case 3:
				return NumberFormatUtils.commaFormat(StringUtils.replace("" + resultMap.get(displayColumns[columnIndex]), "null", ""));
			case 4:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 5:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 6:
				return "" + resultMap.get(displayColumns[columnIndex]);
			}
		} else if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			switch (columnIndex) {
			case 0:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 1:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 2:
				return "" + resultMap.get(displayColumns[columnIndex]);
			case 3:
				return "" + resultMap.get(displayColumns[columnIndex]);
			}
		} else {
			switch (columnIndex) {
			case 0:
				return "" + resultMap.get("name");
			case 1:
				return StringUtils.replace("" + resultMap.get("comment"), "null", "");
			}
		}

		return "*** not set column ***";
	}

}

/**
 * name filetr
 * 
 * @author hangum
 * 
 */
class ColumnInfoFilter extends ViewerFilter {

	private static final Logger logger = Logger.getLogger(ColumnInfoFilter.class);

	String searchString;
	String[] keys;

	public void setSearchString(String[] keys, String s) {
		this.searchString = ".*" + s + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
		this.keys = keys;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		} else {

			Map resultMap = (HashMap) element;
			for (String key : keys) {
				String targetString = (String) resultMap.get(key);
				targetString = targetString == null ? "" : targetString;

				if (targetString.matches(searchString)) {
					return true;
				}else if (targetString.toLowerCase().matches(searchString.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

}
