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
package com.hangum.tadpole.rdb.erd.core.dnd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.erd.core.Messages;
import com.hangum.tadpole.rdb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbFactory;
import com.hangum.tadpole.rdb.model.Table;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Explorer의 테이블 명을 넘겨 받아서 erd에 테이블을 그려줍니다.
 * 
 * @author hangum
 *
 */
public class TableTransferDropTargetListener extends AbstractTransferDropTargetListener {
	private static final Logger logger = Logger.getLogger(TableTransferDropTargetListener.class);
	private RdbFactory tadpoleFactory = RdbFactory.eINSTANCE;
	
	private TableTransferFactory transferFactory = new TableTransferFactory();
	private UserDBDAO userDB;
	private DB db;

	public TableTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}
	
	public TableTransferDropTargetListener(EditPartViewer viewer, UserDBDAO userDB, DB db) {
		super(viewer, TextTransfer.getInstance());
		
		this.userDB = userDB;
		this.db = db;
	}
	
	@Override
	protected Request createTargetRequest() {
		CreateRequest request = new CreateRequest();
		request.setFactory(transferFactory);
		return request;
	}

	@Override
	protected void updateTargetRequest() {		
		((CreateRequest)getTargetRequest()).setLocation(getDropLocation());
	}
	
	@Override
	protected void handleDragOver() {
		getCurrentEvent().detail = DND.DROP_COPY;
		super.handleDragOver();
	}
	
	@Override
	protected void handleDrop() {
		String[] arrayDragSourceData = null;
		try {
			arrayDragSourceData = StringUtils.splitByWholeSeparator(((String)getCurrentEvent().data), PublicTadpoleDefine.DELIMITER);

			int sourceDBSeq = Integer.parseInt(arrayDragSourceData[0]);
			if(userDB.getSeq() != sourceDBSeq) {
				MessageDialog.openError(null, "Error", Messages.TableTransferDropTargetListener_1); //$NON-NLS-1$
				return;
			}
		} catch(Exception e) {
			logger.error("dragger error", e); //$NON-NLS-1$
			MessageDialog.openError(null, "Error", "Draging exception : " + e.getMessage()); //$NON-NLS-1$
			return;
		}
		
		String tableName = arrayDragSourceData[1];		
		String refTableNames = "'" + tableName + "',"; //$NON-NLS-1$ //$NON-NLS-2$
		
		// 이미 editor 상에 테이블 정보를 가져온다.
		Map<String, Table> mapDBTables = new HashMap<String, Table>();
		for (Table table : db.getTables()) {
			mapDBTables.put(table.getName(), table);
			refTableNames += "'" + table.getName() + "',"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		refTableNames = StringUtils.chompLast(refTableNames, ","); //$NON-NLS-1$
		
		// 이미 등록되어 있는 것이 아니라면
		if(mapDBTables.get(tableName) == null) {
			// 테이블 모델 생성
			Table tableModel = tadpoleFactory.createTable();
			tableModel.setName(tableName);
			tableModel.setDb(db);
			tableModel.setConstraints(new Rectangle(getDropLocation().x, getDropLocation().y, -1, -1));
			
			try {
				// 컬럼 모델 생성
				for (TableColumnDAO columnDAO : getColumns(tableName)) {
					Column column = tadpoleFactory.createColumn();
					
					column.setDefault(columnDAO.getDefault());
					column.setExtra(columnDAO.getExtra());
					column.setField(columnDAO.getField());
					column.setNull(columnDAO.getNull());
					column.setKey(columnDAO.getKey());
					column.setType(columnDAO.getType());
					
					column.setTable(tableModel);
					tableModel.getColumns().add(column);
				}
				mapDBTables.put(tableName, tableModel);
				RelationUtil.calRelation(userDB, mapDBTables, db, refTableNames);//RelationUtil.getReferenceTable(userDB, refTableNames));
				
			} catch(Exception e) {
				logger.error("GEF Table Drag and Drop Exception", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", Messages.TadpoleModelUtils_2, errStatus); //$NON-NLS-1$
			}
			
			transferFactory.setTable(tableModel);
		} else {
			transferFactory.setTable(mapDBTables.get(tableName));
		}
		
		super.handleDrop();
	}
	
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public List<TableColumnDAO> getColumns(String strTBName) throws Exception {
//		if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("db", userDB.getDb()); //$NON-NLS-1$
			param.put("table", strTBName);			 //$NON-NLS-1$
			
			return sqlClient.queryForList("tableColumnList", param); //$NON-NLS-1$
//		} else if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {
//			
//			Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()) );
//			com.mongodb.DB mongoDB = mongo.getDB(userDB.getDb());
//			DBCollection coll = mongoDB.getCollection(strTBName);
//										
//			return MongoDBTableColumn.tableColumnInfo(coll.getIndexInfo(), coll.findOne());
//		} 
		
//		return null;
	}

}
