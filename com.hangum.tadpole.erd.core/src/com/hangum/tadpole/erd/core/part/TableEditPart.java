package com.hangum.tadpole.erd.core.part;

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

import com.hangum.tadpole.erd.core.figures.ColumnFigure;
import com.hangum.tadpole.erd.core.figures.TableFigure;
import com.hangum.tadpole.erd.core.figures.TableFigure.COLUMN_TYPE;
import com.hangum.tadpole.erd.core.policies.TableComponentEditPolicy;
import com.hangum.tadpole.model.Column;
import com.hangum.tadpole.model.Table;

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
		Table table = (Table)getModel();
		DBEditPart parent = (DBEditPart)getParent();

		super.refreshVisuals();
		updateFigure((TableFigure)getFigure());
		refreshChildren();
		
		parent.setLayoutConstraint(this, figure, table.getConstraints());
	}
	
	private void updateFigure(TableFigure figure) {
		Table tableModel = (Table)getModel();
		
		figure.setTableName(tableModel.getName());
		figure.removeAllColumns();

		EList<Column> columns = tableModel.getColumns();
		for (Column column : columns) {
			ColumnFigure[] figures = createColumnFigure(tableModel, column);
			figure.add(figures[0]);
			figure.add(figures[1]);
			figure.add(figures[2]);
			figure.add(figures[3]);
		}
	}
	
	private ColumnFigure[] createColumnFigure(Table tableModel, Column model){
		ColumnFigure labelKey = new ColumnFigure(COLUMN_TYPE.KEY);
		ColumnFigure labelName = new ColumnFigure(COLUMN_TYPE.NAME);
		ColumnFigure labelType = new ColumnFigure(COLUMN_TYPE.TYPE);
		ColumnFigure labelNotNull = new ColumnFigure(COLUMN_TYPE.NULL);
		
		labelKey.setText( StringUtils.substring(model.getKey(), 0, 1));
		labelName.setText(model.getField());
		labelType.setText(model.getType());
		labelNotNull.setText(StringUtils.substring(model.getNull(), 0, 1));

		return new ColumnFigure[]{labelKey, labelName, labelType, labelNotNull};
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
