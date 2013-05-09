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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.util.NumberFormatUtils;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Tables composite
 * 
 * @author hangum
 *
 */
public class TablesComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TablesComposite.class);
	
	private UserDBDAO userDB;
	private TableViewer tvTableInform;
	
	private TableInfoFilter tableFilter;
	private Text textFilter;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TablesComposite(Composite parent, int style, UserDBDAO userDB) {
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
			public void keyReleased(KeyEvent e) {
				tableFilter.setSearchString(textFilter.getText());
				tvTableInform.refresh();
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
		
		tvTableInform = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvTableInform.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumn();
		
		tvTableInform.setContentProvider(new ArrayContentProvider());
		tvTableInform.setLabelProvider(new TableInformLabelProvider(userDB));
		
		tableFilter = new TableInfoFilter();
		tvTableInform.addFilter(tableFilter);

		initUI();
	}
	
	/**
	 * table column head를 생성합니다.
	 */
	private void createColumn() {
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT ||
			DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT
		) {
			String[] name = {"Name", "Engine", "Rows", "Auto Increment", "collation", "Created", "Comment"};
			int[] size = {120, 70, 70, 100, 120, 120, 220};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			String[] name = {"Name", "Rows", "Lock"};
			int[] size = {120, 70, 70};
			int[] align = {SWT.LEFT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else {
			String[] name = {"Name", "comment"};
			int[] size = {120, 70};
			int[] align = {SWT.LEFT, SWT.LEFT};
			
			createColumn(name, size, align);
		}
	}
	
	/**
	 *  실제 컬럼의 헤더를 생성합니다.
	 * 
	 * @param name
	 * @param size
	 */
	private void createColumn(String[] name, int[] size, int[] align) {
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tvTableInform, align[i]);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
		}
	}
	
	/**
	 * 
	 */
	private void initUI() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List listTableInform = sqlClient.queryForList("tableInformation", userDB.getDb());
			
			tvTableInform.setInput(listTableInform);
			tvTableInform.refresh();
		} catch (Exception e) {
			logger.error("initialize session list", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}
	}
	
	@Override
	protected void checkSubclass() {
	}
}

/**
 * mysql, mariadb label provider
 * @author hangum
 *
 */
class TableInformLabelProvider extends LabelProvider implements ITableLabelProvider {
	private UserDBDAO userDB;
	
	public TableInformLabelProvider(UserDBDAO userDB) {
		this.userDB  = userDB;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map resultMap = (HashMap)element;
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT ||
				DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT
		) {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("TABLE_NAME");
			case 1: return ""+resultMap.get("ENGINE");
			case 2: return NumberFormatUtils.commaFormat(""+resultMap.get("TABLE_ROWS"));
			case 3: return NumberFormatUtils.commaFormat(StringUtils.replace(""+resultMap.get("AUTO_INCREMENT"), "null", ""));
			case 4: return ""+resultMap.get("TABLE_COLLATION");
			case 5: return ""+resultMap.get("CREATE_TIME");
			case 6: return ""+resultMap.get("TABLE_COMMENT");
			}
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("TABLE_NAME");
			case 1: return NumberFormatUtils.commaFormat(""+resultMap.get("NUM_ROWS"));
			case 2: return ""+resultMap.get("TABLE_LOCK");
			}
		} else {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("name");
			case 1: return StringUtils.replace(""+resultMap.get("comment"), "null", "");
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
class TableInfoFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
		
		Map resultMap = (HashMap)element;
		String tbName = "";
		if(resultMap.get("TABLE_NAME") != null) tbName = ""+resultMap.get("TABLE_NAME");
		else tbName = ""+resultMap.get("name");
		
		if(tbName.matches(searchString)) return true;
		return false;
	}
	
}
