/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.erd.core.Messages;
import com.hangum.tadpole.rdb.erd.core.editor.TadpoleRDBEditor;
import com.hangum.tadpole.rdb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.rdb.erd.core.utils.TDBDataHandler;
import com.hangum.tadpole.rdb.erd.core.utils.TadpoleModelUtils;
import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbFactory;
import com.hangum.tadpole.rdb.model.Table;
import com.swtdesigner.ResourceManager;

/**
 * ERD view refresh action
 * 
 * @author hangum
 *
 */
public class ERDRefreshAction extends SelectionAction {
	public final static String ID = "com.hangum.tadpole.rdb.erd.actions.global.ERDViewRefreshAction"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(ERDRefreshAction.class);
	private GraphicalViewer viewer;
	private TadpoleRDBEditor rdbEditor;
	
	public ERDRefreshAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
		super(part);
		setLazyEnablementCalculation(false);
		
		rdbEditor = (TadpoleRDBEditor)part;
		this.viewer = graphicalViewer;
		
		setId(ID);
		setText(Messages.get().ERDRefreshAction_0);
		setToolTipText(Messages.get().ERDRefreshAction_0);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
	}
	
	public GraphicalViewer getViewer() {
		return viewer;
	}
	
	@Override
	public void run() {
		if(!MessageDialog.openConfirm(getWorkbenchPart().getSite().getShell(), CommonMessages.get().Confirm, Messages.get().ERDRefreshAction_4)) return;
		
		DB dbModel = rdbEditor.getDb();
		
		// 현재 보여지는 table 목록과 위치를 가져옵니다.
		Map<String, Rectangle> mapOldTables = new HashMap<String, Rectangle>();
		for(Table table : dbModel.getTables()) {
			mapOldTables.put(table.getName(), table.getConstraints());
		}
		
		// remove tables
		int intTableCnt = dbModel.getTables().size();
		for(int i=0; i<intTableCnt; i++) {
			Table table = dbModel.getTables().get(0);
			table.setDb(null);
		}
		
		List<String> strTableNames = new ArrayList<String>(mapOldTables.keySet());
		
		// refresh ui
		try {
			final RdbFactory factory = RdbFactory.eINSTANCE;
			final UserDBDAO userDB = rdbEditor.getUserDB();
			// 전체 참조 테이블 목록.
			Map<String, Table> mapDBTables = new HashMap<String, Table>();
			
			List<TableDAO> listTAbles = TadpoleModelUtils.INSTANCE.getTable(userDB, strTableNames);
			
			for(TableDAO table : listTAbles) {
				Table tableModel = factory.createTable();
				tableModel.setDb(dbModel);
				tableModel.setName(table.getName());
				
				if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
					tableModel.setComment("");	 //$NON-NLS-1$
				} else {
					String tableComment = table.getComment();
					tableComment = StringUtils.substring(""+tableComment, 0, 10); //$NON-NLS-1$
					
					tableModel.setSchema(table.getSchema_name());
					tableModel.setComment(tableComment);
				}
				
				mapDBTables.put(tableModel.getName(), tableModel);
				tableModel.setConstraints(mapOldTables.get(table.getName()));
				// column add
				List<TableColumnDAO> columnList = TDBDataHandler.getColumns(userDB, table);
				for (TableColumnDAO columnDAO : columnList) {
					
					Column column = factory.createColumn();
					column.setDefault(columnDAO.getDefault());
					column.setExtra(columnDAO.getExtra());
					column.setField(columnDAO.getField());
					column.setNull(columnDAO.getNull());
					column.setKey(columnDAO.getKey());
					column.setType(columnDAO.getType());
					
					String strComment = columnDAO.getComment();
					if(strComment == null) strComment = ""; //$NON-NLS-1$
					strComment = StringUtils.substring(""+strComment, 0, 10); //$NON-NLS-1$
					column.setComment(strComment);
					
					column.setTable(tableModel);
					tableModel.getColumns().add(column);
				}
				
			}	// end table list
		
			// 관계를 만듭니다.
			RelationUtil.calRelation(userDB, mapDBTables, dbModel);
			
		} catch (Exception e) {
			logger.error("Get all table list", e); //$NON-NLS-1$
		}
	}
	
	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}
