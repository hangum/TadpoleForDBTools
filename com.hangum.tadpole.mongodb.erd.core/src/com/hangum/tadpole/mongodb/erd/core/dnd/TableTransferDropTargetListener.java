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
package com.hangum.tadpole.mongodb.erd.core.dnd;

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
import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.hangum.tadpole.mongodb.erd.core.Messages;
import com.hangum.tadpole.mongodb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.mongodb.erd.stanalone.Activator;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.Table;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * Explorer의 테이블 명을 넘겨 받아서 erd에 테이블을 그려줍니다.
 * 
 * @author hangum
 *
 */
public class TableTransferDropTargetListener extends AbstractTransferDropTargetListener {
	private static final Logger logger = Logger.getLogger(TableTransferDropTargetListener.class);
	private MongodbFactory tadpoleFactory = MongodbFactory.eINSTANCE;
	
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
				for (CollectionFieldDAO columnDAO : getColumns(tableName)) {										
					Column column = tadpoleFactory.createColumn();					
					column.setField(columnDAO.getField());
					column.setKey(columnDAO.getKey());
					column.setType(columnDAO.getType());
					if("BasicDBObject".equals(columnDAO.getType())) {
//						logger.debug("[columnDAO.getType()]" + columnDAO.getType());
						makeSubDoc(column, columnDAO);
					}						
					
					column.setTable(tableModel);
//					tableModel.getColumns().add(column);
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
	 * add sub document
	 * 
	 * @param columnDAO
	 */
	private void makeSubDoc(Column parentColumn, CollectionFieldDAO columnDAO) {
		
		for (CollectionFieldDAO cfDAO : columnDAO.getChildren()) {
			Column column = tadpoleFactory.createColumn();					
			column.setField(cfDAO.getField());
			column.setKey(cfDAO.getKey());
			column.setType(cfDAO.getType());
			if("BasicDBObject".equals(cfDAO.getType())) {
				makeSubDoc(column, cfDAO);
			}						
			parentColumn.getSubDoc().add(column);
		}
	}
	
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public List<CollectionFieldDAO> getColumns(String strTBName) throws Exception {
		Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()) );
		com.mongodb.DB mongoDB = mongo.getDB(userDB.getDb());
		DBCollection coll = mongoDB.getCollection(strTBName);
									
		return MongoDBTableColumn.tableColumnInfo(coll.getIndexInfo(), coll.findOne());
	}

}
