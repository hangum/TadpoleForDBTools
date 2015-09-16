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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;
import com.ibatis.sqlmap.client.SqlMapClient;

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
		lblNewLabel.setText(Messages.TablesComposite_0);
		
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
		btnRefresh.setText(Messages.TablesComposite_1);
		
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
		if(DBDefine.getDBDefine(userDB) == DBDefine.MYSQL_DEFAULT ||
			DBDefine.getDBDefine(userDB) == DBDefine.MARIADB_DEFAULT
		) {
			String[] name = {"Name", "Engine", "Rows", "Auto Increment", "collation", "Size(MB)", "Created", "Comment"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			int[] size = {120, 70, 70, 100, 80, 80, 120, 220};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else if(DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {
			String[] name = {"Name", "Rows", "Lock"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int[] size = {120, 70, 70};
			int[] align = {SWT.LEFT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else {
			String[] name = {"Name", "comment", "Index", "Shared", "Primary Key", "Triggers", "Sub Class", "Rules", "Option"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
			int[] size = {120, 150, 60, 60, 100, 80, 80, 60, 60};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
			
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
			List listTableInform = sqlClient.queryForList("tableInformation", userDB.getDb()); //$NON-NLS-1$
			
			tvTableInform.setInput(listTableInform);
			tvTableInform.refresh();
		} catch (Exception e) {
			logger.error("initialize session list", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}
		
		// google analytic
		AnalyticCaller.track(RDBDBInfosEditor.ID, "TablesComposite"); //$NON-NLS-1$
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
		
		if(DBDefine.getDBDefine(userDB) == DBDefine.MYSQL_DEFAULT ||
				DBDefine.getDBDefine(userDB) == DBDefine.MARIADB_DEFAULT
		) {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$
			case 1: return ""+resultMap.get("ENGINE"); //$NON-NLS-1$ //$NON-NLS-2$
			case 2: return NumberFormatUtils.commaFormat(""+resultMap.get("TABLE_ROWS")); //$NON-NLS-1$ //$NON-NLS-2$
			case 3: return NumberFormatUtils.commaFormat(StringUtils.replace(""+resultMap.get("AUTO_INCREMENT"), "null", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			case 4: return ""+resultMap.get("TABLE_COLLATION"); //$NON-NLS-1$ //$NON-NLS-2$
			case 5: return ""+resultMap.get("SIZEOFMB"); //$NON-NLS-1$ //$NON-NLS-2$
			case 6: return ""+resultMap.get("CREATE_TIME"); //$NON-NLS-1$ //$NON-NLS-2$
			case 7: return ""+resultMap.get("TABLE_COMMENT"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if(DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$
			case 1: return NumberFormatUtils.commaFormat(""+resultMap.get("NUM_ROWS")); //$NON-NLS-1$ //$NON-NLS-2$
			case 2: return ""+resultMap.get("TABLE_LOCK"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			switch(columnIndex) {
		    case 0: return ""+resultMap.get("name"); //$NON-NLS-1$ //$NON-NLS-2$
			case 1: return StringUtils.replace(""+resultMap.get("comment"), "null", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			case 2: return "true".equals(""+resultMap.get("has_index")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 3: return "true".equals(""+resultMap.get("is_shared")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 4: return "true".equals(""+resultMap.get("has_pk")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 5: return "true".equals(""+resultMap.get("has_triggers")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 6: return "true".equals(""+resultMap.get("has_subclass")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 7: return "true".equals(""+resultMap.get("has_rules")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 8: return StringUtils.replace(""+resultMap.get("options"), "null", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
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
		String tbName = ""; //$NON-NLS-1$
		if(resultMap.get("TABLE_NAME") != null) tbName = ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		else tbName = ""+resultMap.get("name"); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(tbName.matches(searchString)) return true;
		return false;
	}
	
}
