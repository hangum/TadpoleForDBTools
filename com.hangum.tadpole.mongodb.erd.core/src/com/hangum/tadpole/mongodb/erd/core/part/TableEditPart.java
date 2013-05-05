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
package com.hangum.tadpole.mongodb.erd.core.part;

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

import com.hangum.tadpole.mongodb.erd.core.figures.ColumnFigure;
import com.hangum.tadpole.mongodb.erd.core.figures.SubTableFigure;
import com.hangum.tadpole.mongodb.erd.core.figures.TableFigure;
import com.hangum.tadpole.mongodb.erd.core.figures.TableFigure.COLUMN_TYPE;
import com.hangum.tadpole.mongodb.erd.core.policies.TableComponentEditPolicy;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.Table;

/**
 * table edit part
 * 
 * @author hangum
 *
 */
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
	
	@Override
	protected void refreshVisuals() {
		Table tableModel = (Table)getModel();
		DBEditPart parent = (DBEditPart)getParent();

		super.refreshVisuals();
		updateFigure((TableFigure)getFigure());
		refreshChildren();
		
		parent.setLayoutConstraint(this, figure, tableModel.getConstraints());
	}
	
	private void updateFigure(TableFigure tableFigure) {
		Table tableModel = (Table)getModel();
		
		tableFigure.setTableName(tableModel.getName());
		tableFigure.removeAllColumns();

		EList<Column> columns = tableModel.getColumns();
		for (Column column : columns) {
			if("BasicDBObject".equals(column.getType())) {
				addSubDocument(tableFigure, column);				
			} else {				
				addColumnFigure(tableFigure, column);
			}
		}		
	}
	
	/**
	 * Add sub document and column 
	 * 
	 * @param parentTableFigure
	 * @param columnParent
	 */
	private void addSubDocument(TableFigure parentTableFigure, Column columnParent) {
		SubTableFigure subDocFigure = new SubTableFigure();
		subDocFigure.setTableName(columnParent.getField());
		
		EList<Column> sucColumns = columnParent.getSubDoc();
		for (Column column : sucColumns) {
			if("BasicDBObject".equals(column.getType())) {				
				addSubDocument(subDocFigure, column);
			} else {				
				addColumnFigure(subDocFigure, column);
			}			
		}
		
		parentTableFigure.add(subDocFigure);
	}	
	
	/**
	 * create column figure
	 * 
	 * @param tableFigure
	 * @param model
	 */
	private void addColumnFigure(TableFigure tableFigure, Column model){
		ColumnFigure labelKey = new ColumnFigure(COLUMN_TYPE.KEY);
		labelKey.setText(StringUtils.substring(model.getKey(), 0, 1));
		
		ColumnFigure labelName = new ColumnFigure(COLUMN_TYPE.NAME);
		labelName.setText(model.getField());
		
		ColumnFigure labelType = new ColumnFigure(COLUMN_TYPE.TYPE);
		labelType.setText(model.getType());

		tableFigure.add(labelKey);
		tableFigure.add(labelName);
		tableFigure.add(labelType);
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
