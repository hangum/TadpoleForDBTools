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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpold.commons.libs.core.dao.KeyValueDAO;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * RDB 디비 summary 정보를 출력하는 composite.
 * 
 * @author hangum
 *
 */
public class RDBInformationComposite extends Composite {
	
	private UserDBDAO userDB;
	private TableViewer tvInformation;
	
	/** information dao */
	private List<KeyValueDAO> listInfo = new ArrayList<KeyValueDAO>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RDBInformationComposite(Composite parent, int style, final UserDBDAO userDb) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		this.userDB = userDb;
		
		tvInformation = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvInformation.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvInformation, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(130);
		tblclmnName.setText("Name"); //$NON-NLS-1$
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvInformation, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
		tblclmnValue.setWidth(300);
		tblclmnValue.setText("Value"); //$NON-NLS-1$
		
		tvInformation.setContentProvider(new ArrayContentProvider());
		tvInformation.setLabelProvider(new RDBInformationLabelProvider());
		initUI();
		tvInformation.setInput(listInfo);
	}
	
	/**
	 * 초기데이터를 쌓습니다.
	 */
	private void initUI() {
		listInfo = new ArrayList<KeyValueDAO>();
		
		listInfo.add(new KeyValueDAO("Operation type", 	userDB.getOperation_type())); //$NON-NLS-1$
		listInfo.add(new KeyValueDAO("Group Name", 		userDB.getGroup_name())); //$NON-NLS-1$
		listInfo.add(new KeyValueDAO("Display Name", 	userDB.getDisplay_name())); //$NON-NLS-1$
		
		listInfo.add(new KeyValueDAO("JDBC URL", 		userDB.getShowUrl(SessionManager.getRepresentRole()))); //$NON-NLS-1$
		if(DBDefine.getDBDefine(userDB) != DBDefine.SQLite_DEFAULT) {
			listInfo.add(new KeyValueDAO("Host/IP", 		userDB.getShowHost(SessionManager.getRepresentRole()) + "/" + userDB.getShowPort(SessionManager.getRepresentRole()))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		listInfo.add(new KeyValueDAO("Database", 		userDB.getShowDb(SessionManager.getRepresentRole()))); //$NON-NLS-1$
		if(DBDefine.getDBDefine(userDB) != DBDefine.SQLite_DEFAULT) {
			listInfo.add(new KeyValueDAO("User",	 		userDB.getShowUsers(SessionManager.getRepresentRole()))); //$NON-NLS-1$
		}
		
		listInfo.add(new KeyValueDAO("Read Only", 		userDB.getIs_readOnlyConnect())); //$NON-NLS-1$
		listInfo.add(new KeyValueDAO("Table Filter",	userDB.getIs_table_filter())); //$NON-NLS-1$
		if("YES".equals(userDB.getIs_table_filter())) { //$NON-NLS-1$
			listInfo.add(new KeyValueDAO("\tInclude Filter",	userDB.getTable_filter_include())); //$NON-NLS-1$
			listInfo.add(new KeyValueDAO("\tExclude Filter",	userDB.getTable_filter_exclude())); //$NON-NLS-1$
		}
		// 몽고디비는 없으므로.. 
		if(DBDefine.getDBDefine(userDB) != DBDefine.MONGODB_DEFAULT) {
			listInfo.add(new KeyValueDAO("Auto Commit",		userDB.getIs_autocommit())); //$NON-NLS-1$
			listInfo.add(new KeyValueDAO("Profile", 		userDB.getIs_profile())); //$NON-NLS-1$
			listInfo.add(new KeyValueDAO(Messages.RDBInformationComposite_17, 	userDB.getQuestion_dml()));
		}
	}

	@Override
	protected void checkSubclass() {
	}
}

/**
 * rdb information label provider
 * 
 * @author hangum
 *
 */
class RDBInformationLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		KeyValueDAO dao = (KeyValueDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getName();
		case 1: return dao.getValue();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}