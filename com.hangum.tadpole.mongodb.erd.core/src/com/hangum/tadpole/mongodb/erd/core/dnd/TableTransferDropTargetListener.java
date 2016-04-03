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
package com.hangum.tadpole.mongodb.erd.core.dnd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.erd.core.Messages;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBERDEditor;
import com.hangum.tadpole.mongodb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.mongodb.erd.core.utils.TadpoleModelUtils;
import com.hangum.tadpole.mongodb.erd.stanalone.Activator;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.Table;

/**
 * Explorer의 테이블 명을 넘겨 받아서 erd에 테이블을 그려줍니다.
 * 
 * @author hangum
 *
 */
public class TableTransferDropTargetListener extends AbstractTransferDropTargetListener {
	private static final Logger logger = Logger.getLogger(TableTransferDropTargetListener.class);
	private MongodbFactory tadpoleFactory = MongodbFactory.eINSTANCE;
	/** object view에서 넘어오는 테이블 네임 인텍스, 0번은 스키마 이름(rdb에서만 사용하는.) */
	private int IDX_TABLE_NAME = 1;
	
	private TadpoleMongoDBERDEditor mongoEditor = null;
	private TableTransferFactory transferFactory = new TableTransferFactory();
	private UserDBDAO userDB;
	private DB db;

	public TableTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}
	
	public TableTransferDropTargetListener(TadpoleMongoDBERDEditor mongoEditor, EditPartViewer viewer, UserDBDAO userDB, DB db) {
		super(viewer, TextTransfer.getInstance());
		
		this.mongoEditor = mongoEditor;
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
		String strDragSource = (String)getCurrentEvent().data;
		try {
			String[] arrayDragSourceData = StringUtils.splitByWholeSeparator(strDragSource, PublicTadpoleDefine.DELIMITER);

			int sourceDBSeq = Integer.parseInt(arrayDragSourceData[0]);
			if(userDB.getSeq() != sourceDBSeq) {
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().TableTransferDropTargetListener_1); //$NON-NLS-1$
				return;
			}
		} catch(Exception e) {
			logger.error("dragger error", e); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.get().Error, "Draging exception : " + e.getMessage()); //$NON-NLS-1$
			return;
		}
		
		final int nextTableX = getDropLocation().x;
		final int nextTableY = getDropLocation().y;
		
		final String strFullData = StringUtils.substringAfter(strDragSource, PublicTadpoleDefine.DELIMITER);
		final String [] arryTables = StringUtils.splitByWholeSeparator(strFullData, PublicTadpoleDefine.DELIMITER_DBL);
		final Map<String, List<CollectionFieldDAO>> mapTable = new HashMap<>();
		
		Job job = new Job("Painting model") {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Painting table object", IProgressMonitor.UNKNOWN);
		
				try {
					for (int i=0; i<arryTables.length; i++) {
						String strTable = arryTables[i];
						monitor.subTask(String.format("Working %s/%s", i, arryTables.length));
						String[] arryTable = StringUtils.splitByWholeSeparator(strTable, PublicTadpoleDefine.DELIMITER);
						if(arryTable.length == 0) continue;
						
						String tableName = arryTable[IDX_TABLE_NAME];
						mapTable.put(tableName, getColumns(tableName));
					}
					
				} catch(Exception e) {
					logger.error("ERD Initialize excepiton", e);
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}
						
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
	
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event;
				Display display = mongoEditor.getEditorSite().getShell().getDisplay();
				display.syncExec(new Runnable() {
					public void run() {
				
						if(jobEvent.getResult().isOK()) {
							paintingModel(nextTableX, nextTableY, arryTables, mapTable);
						} else {
							MessageDialog.openError(null, Messages.get().Confirm, jobEvent.getResult().getMessage());
						}
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
		
//		super.handleDrop();
	}
	

	/**
	 * painting model 
	 * 
	 * @param nextTableX
	 * @param nextTableY
	 * @param arryTables
	 * @param mapTable
	 */
	private void paintingModel(int nextTableX, int nextTableY, String [] arryTables, Map<String, List<CollectionFieldDAO>> mapTable) {
		Rectangle prevRectangle = null;
		
		int originalX = nextTableX;
		int intCount = 1;
		for (String strTable : arryTables) {
			String[] arryTable = StringUtils.splitByWholeSeparator(strTable, PublicTadpoleDefine.DELIMITER);
			if(arryTable.length == 0) continue;
			
			String tableName = arryTable[IDX_TABLE_NAME];
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
				
				if(prevRectangle == null) {
					prevRectangle = new Rectangle(nextTableX, 
							nextTableY, 
							TadpoleModelUtils.END_TABLE_WIDTH, 
							TadpoleModelUtils.END_TABLE_HIGHT); 
				} else {
					// 테이블의 좌표를 잡아줍니다. 
					prevRectangle = new Rectangle(nextTableX, 
												nextTableY, 
												TadpoleModelUtils.END_TABLE_WIDTH, 
												TadpoleModelUtils.END_TABLE_HIGHT);
				}
				tableModel.setConstraints(prevRectangle);
				// 다음 좌표를 계산합니다.
				nextTableX = originalX + (TadpoleModelUtils.GAP_WIDTH * intCount);
				intCount++;
				
				try {
					// 컬럼 모델 생성
					for (CollectionFieldDAO columnDAO : mapTable.get(tableName)) {										
						Column column = tadpoleFactory.createColumn();					
						column.setField(columnDAO.getField());
						column.setKey(columnDAO.getKey());
						column.setType(columnDAO.getType());
						if("BasicDBObject".equals(columnDAO.getType())) {
							makeSubDoc(tableModel, column, columnDAO);
						}						
						
						column.setTable(tableModel);
					}
					mapDBTables.put(tableName, tableModel);
					RelationUtil.calRelation(userDB, mapDBTables, db, refTableNames);//RelationUtil.getReferenceTable(userDB, refTableNames));
					
				} catch(Exception e) {
					logger.error("GEF Table Drag and Drop Exception", e); //$NON-NLS-1$
					
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.get().Error, Messages.get().TadpoleModelUtils_2, errStatus); //$NON-NLS-1$
				}
				
				transferFactory.setTable(tableModel);
			} else {
				transferFactory.setTable(mapDBTables.get(tableName));
			}
		}
	}
	
	/**
	 * add sub document
	 * 
	 * @param tableModel
	 * @param parentColumn
	 * @param columnDAO
	 */
	private void makeSubDoc(Table tableModel, Column parentColumn, CollectionFieldDAO columnDAO) {
		
		for (CollectionFieldDAO cfDAO : columnDAO.getChildren()) {
			Column column = tadpoleFactory.createColumn();
			
			column.setField(cfDAO.getField());
			column.setKey(cfDAO.getKey());
			column.setType(cfDAO.getType());
			if("BasicDBObject".equals(cfDAO.getType())) {
				makeSubDoc(tableModel, column, cfDAO);
			}						
			
			//
			//	column.setTable(tableModel); 
			// 위의 커럼안에 테이블이 존재하면 서브 컬럼마다 테이블이 중복으로 표시된다.
			// 그러나 위의 코드를 삭제하면 The object 'com.hangum.tadpole.mongodb.model.impl.ColumnImpl@b98167 (field: serverStatus, type: Integer, default: null, logicalField: null, key: NO, comment: null)' is not contained in a resource.
			// 오류가 발생하고
			//  참고합니다 (http://andy.ekiwi.de/?p=1006)
			
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
		return MongoDBQuery.collectionColumn(userDB, strTBName);
	}

}
