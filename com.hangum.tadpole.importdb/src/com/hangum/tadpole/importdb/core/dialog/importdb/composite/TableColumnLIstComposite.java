/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.importdb.Activator;
import com.hangum.tadpole.importdb.core.Messages;
import com.hangum.tadpole.importdb.core.dialog.importdb.dao.ModTableDAO;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * <pre>
 * Table
 * 	- column : type  	
 * 을 구조를 갖는 콤포짖
 * (현재(12.11.18)는 테이블 정보만 표시합니다.
 * </pre>
 * 
 * @author hangum
 *
 */
public class TableColumnLIstComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnLIstComposite.class);
	private UserDBDAO userDB = null;
	
	private TableViewer tableViewer = null;
	private List<ModTableDAO> listTables = new ArrayList<ModTableDAO>();
	
	private static final Image CHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/checked.png"); //$NON-NLS-1$;
	private static final Image UNCHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/unchecked.png"); //$NON-NLS-1$;
			
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TableColumnLIstComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final TableViewerColumn tableColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		tableColumn.getColumn().setWidth(30);
		tableColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				ModTableDAO modDao = (ModTableDAO)element;
				if (modDao.isModify()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});
		tableColumn.setEditingSupport(new ModifyEditingSupport(tableViewer));
		
		final TableViewerColumn tableColumnName = new TableViewerColumn(tableViewer, SWT.LEFT);
		tableColumnName.getColumn().setText("Table Name"); //$NON-NLS-1$
		tableColumnName.getColumn().setWidth(400);
		tableColumnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ModTableDAO modDao = (ModTableDAO)element;
				return modDao.getName();						
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(listTables);		
	}
	
	public void init(UserDBDAO userDB) {
		if(userDB == null) {
			MessageDialog.openError(null, "Data Import", Messages.TableColumnLIstComposite_1); //$NON-NLS-1$
			
			return;
		}
		listTables.clear();
		this.userDB = userDB;
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableDAO> showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
			for (TableDAO tableDAO : showTables) {
				listTables.add( new ModTableDAO(tableDAO.getName()) );
			}			
		} catch (Exception e) {
			logger.error("DB Connecting... ", e); //$NON-NLS-1$
			MessageDialog.openError(null, "Data Import", e.getMessage()); //$NON-NLS-1$
		}
		
		tableViewer.setInput(listTables);
		tableViewer.refresh();
	}
	
	public List<ModTableDAO> getListTables() {
		return listTables;
	}
	
	/**
	 * 사용자가 선택한 테이블 정보만 넘겨줍니다.
	 * @return
	 */
	public List<ModTableDAO> getSelectListTables() {
		List<ModTableDAO> listSelectTable = new ArrayList<ModTableDAO>();
		for (ModTableDAO modTableDAO : getListTables()) {
			if(modTableDAO.isModify()) listSelectTable.add(modTableDAO);			
		}
		
		return listSelectTable;
	}
	
	/**
	 * select all
	 */
	public void selectAll() {
		for(ModTableDAO modDAO : getListTables()) {
			modDAO.setModify(true);
		}
		tableViewer.refresh();
	}
	
	/**
	 * 
	 */
	public void selectNotAll() {
		for(ModTableDAO modDAO : getListTables()) {
			modDAO.setModify(false);
		}
		tableViewer.refresh();	
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
}
