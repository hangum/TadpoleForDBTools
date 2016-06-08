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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.hangum.tadpole.rdb.erd.core.figures.decoration.relation.RelationDecorator;
import com.hangum.tadpole.rdb.erd.core.policies.RelationBendpointEditPolicy;
import com.hangum.tadpole.rdb.model.Relation;
import com.hangum.tadpole.rdb.model.Table;

public class RelationEditPart extends AbstractConnectionEditPart {
	private static final Logger logger = Logger.getLogger(RelationEditPart.class);
	private RelationAdapter adapter;

//	private Label labelSource;
//	private Label labelTarget;

	public RelationEditPart() {
		super();
		adapter = new RelationAdapter();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new RelationBendpointEditPolicy());
		
		// delte
//		installEditPolicy(EditPolicy.CONNECTION_ROLE, new RelationConnectionEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		
		Relation relation = (Relation)getModel();
		conn.setSourceDecoration(new RelationDecorator(relation.getSource_kind().getName()));
		conn.setTargetDecoration(new RelationDecorator(relation.getTarget_kind().getName()));
		
		Label labelSourceTarget = new Label();
		if(StringUtils.startsWith(relation.getDb().getDbType(), "SQLite")) {
			labelSourceTarget.setText(String.format("%s:%s", relation.getReferenced_column_name(), relation.getColumn_name()));
		} else {
			labelSourceTarget.setText(String.format("%s(%s:%s)", relation.getConstraint_name(), relation.getReferenced_column_name(), relation.getColumn_name()));	
		}
		
		labelSourceTarget.setForegroundColor(ColorConstants.darkBlue());
		labelSourceTarget.setBackgroundColor(ColorConstants.white());
		labelSourceTarget.setToolTip(new Label(String.format("%s:%s", relation.getReferenced_column_name(), relation.getColumn_name())));
		
		
		Table table = relation.getTarget();
		if(table == null) {
			conn.add(labelSourceTarget, new ConnectionLocator(conn, ConnectionLocator.MIDDLE));
		} else {
			EList<Relation> list = table.getIncomingLinks();
			if(list.size() == 1) {
				conn.add(labelSourceTarget, new ConnectionLocator(conn, ConnectionLocator.MIDDLE));
			} else {
				for (Relation tmpRelation : list) {
					if(StringUtils.equals(tmpRelation.getConstraint_name(), relation.getConstraint_name())) {
						ConnectionLocator cl = new ConnectionLocator(conn, ConnectionLocator.MIDDLE);
						cl.setGap(10);
						cl.setRelativePosition(PositionConstants.SOUTH);
						conn.add(labelSourceTarget, cl);
					} else {
						ConnectionLocator cl = new ConnectionLocator(conn, ConnectionLocator.MIDDLE);
						cl.setRelativePosition(PositionConstants.WEST);
						conn.add(labelSourceTarget, cl);
					}
				}
			}
		}
		
		return conn;
	}
	
	@Override
	protected void refreshVisuals() {
		Connection connection = getConnectionFigure();
		List<Point> modelConstraint = ((Relation)getModel()).getBendpoint();
		List<AbsoluteBendpoint> figureConstraint = new ArrayList<AbsoluteBendpoint>();
		for (Point p : modelConstraint) {
			figureConstraint.add(new AbsoluteBendpoint(p));
		}
		connection.setRoutingConstraint(figureConstraint);
	}

	@Override
	public void activate() {
		if(!isActive()) ((Relation)getModel()).eAdapters().add(adapter);
		super.activate();
	}

	@Override
	public void deactivate() {
		if(!isActive()) ((Relation)getModel()).eAdapters().remove(adapter);
		super.deactivate();
	}
	
	public class RelationAdapter implements Adapter {

		@Override
		public void notifyChanged(Notification notification) {
//			Relation relation = (Relation)getModel();
//			try {
//				logger.debug("\t\t\t #############relation########## [source] " + relation.getSource().getName() );
//				logger.debug("\t\t\t #############relation########## [target] " + relation.getTarget().getName() );
//			} catch(Exception e) {}
			
			refreshVisuals();
		}

		@Override
		public Notifier getTarget() {
			return (Relation)getModel();
		}

		@Override
		public void setTarget(Notifier newTarget) {
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type.equals(Relation.class);
		}
		
	}
}
