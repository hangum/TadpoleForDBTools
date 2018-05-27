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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.dao.KeyValueDAO;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;

/**
 * RDB 디비 summary 정보를 출력하는 composite.
 * 
 * @author hangum
 *
 */
public class RDBInformationComposite extends DBInfosComposite {
	private static final Logger logger = Logger.getLogger(RDBInformationComposite.class);
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
		
		tvInformation = new TableViewer(this, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvInformation.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvInformation, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(130);
		tblclmnName.setText(CommonMessages.get().Name);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvInformation, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
		tblclmnValue.setWidth(300);
		tblclmnValue.setText(Messages.get().Value);
		
		tvInformation.setContentProvider(ArrayContentProvider.getInstance());
		tvInformation.setLabelProvider(new RDBInformationLabelProvider());
		initUI(true);
	}
	
	/**
	 * 초기데이터를 쌓습니다.
	 */
	public void initUI(boolean isRefresh) {
		if(isRefresh) {
			listInfo.clear();
		} else {
			if(listInfo.size() != 0) return;
		}
		
		// db information
		if(DBGroupDefine.MONGODB_GROUP != userDB.getDBGroup()) {
			rdbInfo();
		}
		
		listInfo.add(new KeyValueDAO(Messages.get().OperationType, 	userDB.getOperation_type()));
		listInfo.add(new KeyValueDAO(Messages.get().GroupName, 	userDB.getGroup_name()));
		listInfo.add(new KeyValueDAO(Messages.get().DisplayName, 	userDB.getDisplay_name()));
		
		listInfo.add(new KeyValueDAO(Messages.get().JDBCURL, 	userDB.getUrl(userDB.getRole_id())));
		if(DBGroupDefine.SQLITE_GROUP != userDB.getDBGroup()) {
			listInfo.add(new KeyValueDAO("Host/IP", 		userDB.getHost(userDB.getRole_id()) + "/" + userDB.getPort(userDB.getRole_id()))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		listInfo.add(new KeyValueDAO(Messages.get().Database, 	userDB.getDb(userDB.getRole_id())));
		if(userDB.getDBDefine() != DBDefine.SQLite_DEFAULT) {
			listInfo.add(new KeyValueDAO("User",	 		userDB.getUsers(userDB.getRole_id()))); //$NON-NLS-1$
		}
		
		listInfo.add(new KeyValueDAO(Messages.get().ReadOnly, 	userDB.getIs_readOnlyConnect()));

		// 몽고디비는 없으므로.. 
		if(DBGroupDefine.MONGODB_GROUP != userDB.getDBGroup()) {
			listInfo.add(new KeyValueDAO(Messages.get().AutoCommit,		userDB.getIs_autocommit()));
			listInfo.add(new KeyValueDAO(Messages.get().Profile, 		userDB.getIs_profile()));
			listInfo.add(new KeyValueDAO(Messages.get().RDBInformationComposite_17, 	userDB.getQuestion_dml()));
		}
		
		tvInformation.setInput(listInfo);
		
		// google analytic
		AnalyticCaller.track(RDBDBInfosEditor.ID, "RDBInformationComposite"); //$NON-NLS-1$
	}
	
	/**
	 * add rdb info
	 */
	private void rdbInfo() {
		Connection conn = null;
		try {
			conn = TadpoleSQLManager.getConnection(userDB);
			DatabaseMetaData dmd = conn.getMetaData();
			
			listInfo.add(new KeyValueDAO(Messages.get().DatabaseInformation, dmd.getDatabaseProductName() + " " + dmd.getDatabaseProductVersion()));
			listInfo.add(new KeyValueDAO(Messages.get().DriverInformation, dmd.getDriverName() + " " + dmd.getDriverVersion()));
		} catch(Exception e) {
			logger.error("RDB info", e);
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) {}
		}
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