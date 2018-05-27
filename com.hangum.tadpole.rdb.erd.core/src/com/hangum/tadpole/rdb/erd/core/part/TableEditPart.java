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
package com.hangum.tadpole.rdb.erd.core.part;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.erd.core.figures.ColumnFigure;
import com.hangum.tadpole.rdb.erd.core.figures.TableFigure;
import com.hangum.tadpole.rdb.erd.core.figures.TableFigure.COLUMN_TYPE;
import com.hangum.tadpole.rdb.erd.core.policies.TableComponentEditPolicy;
import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.Style;
import com.hangum.tadpole.rdb.model.Table;

public class TableEditPart extends AbstractGraphicalEditPart implements NodeEditPart {
	private static final Logger logger = Logger.getLogger(TableEditPart.class);
	
	private TableAdapter adapter;
	public TableEditPart() {
		super();
		adapter = new TableAdapter();
	}
	
	@Override
	protected IFigure createFigure() {
		TableFigure tf = new TableFigure();
		updateFigure(tf);
		return tf;
	}

	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new RelationNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableComponentEditPolicy());
	}
	
	/*
	 * SELECT 스트립트를 에디터에 보여주면 좋겠다. 
	 */
//	@Override
//	public void performRequest(Request req) {
//	    if(req.getType() == RequestConstants.REQ_OPEN) {
//	    	Table tableModel = (Table)getModel();
//	    }
//	}
	
	@Override
	protected void refreshVisuals() {
		Table table = (Table)getModel();
		DBEditPart parent = (DBEditPart)getParent();

		super.refreshVisuals();
		updateFigure((TableFigure)getFigure());
		refreshChildren();
		
		parent.setLayoutConstraint(this, figure, table.getConstraints());
	}
	
	private void updateFigure(TableFigure figure) {
		Table tableModel = (Table)getModel();
		
		final String strSchemaName = StringUtils.trimToEmpty(tableModel.getSchema());
		
		DB db = tableModel.getDb();
		if(db == null) {
			figure.removeAllColumns();
		} else {
			Style style = db.getStyle();
			String strTableTitle = style.getTableTitle();
			
			if(db.getDbType().startsWith(DBDefine.SQLite_DEFAULT.toString())) {
				figure.setTableName(tableModel.getName() );
			} else {
				if("name".equals(strTableTitle)) 		{
					if("".equals(strSchemaName)) figure.setTableName(tableModel.getName());
					else figure.setTableName(String.format("%s.%s", tableModel.getSchema(), tableModel.getName()));
				} else if("comment".equals(strTableTitle)) {
					figure.setTableName(tableModel.getComment());
				} else {
					if("".equals(tableModel.getComment())) {
						if("".equals(strSchemaName)) figure.setTableName(tableModel.getName());
						else figure.setTableName(String.format("%s.%s", tableModel.getSchema(), tableModel.getName()));
					} else {
						if("".equals(strSchemaName)) figure.setTableName(tableModel.getName() + "(" + tableModel.getComment() + ")");
						else figure.setTableName(String.format("%s.%s(%s)", tableModel.getSchema(), tableModel.getName(), tableModel.getComment()));
					}
				}
			}
			
			figure.removeAllColumns();
			
			// 모든 컬럼을 보여 주지 않아야 하는지 ..
			boolean isShowColumn = "NO".equals(style.getColumnPrimaryKey()) & "NO".equals(style.getColumnName()) & "NO".equals(style.getColumnComment()) & "NO".equals(style.getColumnType()) & "NO".equals(style.getColumnNullCheck());
			if(!isShowColumn) {
				EList<Column> columns = tableModel.getColumns();
				for (Column column : columns) {
					ColumnFigure[] figures = createColumnFigure(tableModel, column);
					
					if("YES".equals(style.getColumnPrimaryKey())) 	figure.add(figures[0]);
					if("YES".equals(style.getColumnName())) 		figure.add(figures[1]);
					if("YES".equals(style.getColumnComment())) 		figure.add(figures[2]);
					if("YES".equals(style.getColumnType())) 		figure.add(figures[3]);
					if("YES".equals(style.getColumnNullCheck())) 	figure.add(figures[4]);
				}
			}
		}
	}
	
	private ColumnFigure[] createColumnFigure(Table tableModel, Column model){
		ColumnFigure labelKey = new ColumnFigure(COLUMN_TYPE.KEY);
		ColumnFigure labelName = new ColumnFigure(COLUMN_TYPE.NAME);
		ColumnFigure labelComment = new ColumnFigure(COLUMN_TYPE.COMMENT);
		ColumnFigure labelType = new ColumnFigure(COLUMN_TYPE.TYPE);
		ColumnFigure labelNotNull = new ColumnFigure(COLUMN_TYPE.NULL);
		
		labelKey.setText( StringUtils.substring(model.getKey(), 0, 1));
		labelName.setText(model.getField());
		labelComment.setText(model.getComment());
		labelType.setText(model.getType());
		labelNotNull.setText(StringUtils.substring(model.getNull(), 0, 1));

		return new ColumnFigure[]{labelKey, labelName, labelComment, labelType, labelNotNull};
	}
	
	@Override
	protected List getModelSourceConnections() {
		Table model = (Table)getModel();
		return model.getOutgoingLinks();
	}
	
	@Override
	protected List getModelTargetConnections() {
		Table model = (Table)getModel();
		return model.getIncomingLinks();
	}
	
	@Override
	public void activate() {
		if(!isActive()) {
			((Table)getModel()).eAdapters().add(adapter);
		}
		super.activate();
	}
	
	@Override
	public void deactivate() {
		if(isActive()) {
			((Table)getModel()).eAdapters().remove(adapter);
		}
		super.deactivate();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return ((TableFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return ((TableFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return ((TableFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return ((TableFigure)getFigure()).getConnectionAnchor();
	}
	
	public class TableAdapter implements Adapter {

		@Override
		public void notifyChanged(Notification notification) {
//			Table tableModel = (Table)getModel();
//			logger.debug("\t\t ######################## [table] " + tableModel.getName());
			
			refreshVisuals();
			refreshSourceConnections();
			refreshTargetConnections();
		}

		@Override
		public Notifier getTarget() {
			return (Table)getModel();
		}

		@Override
		public void setTarget(Notifier newTarget) {
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type.equals(Table.class);
		}

	}

}
