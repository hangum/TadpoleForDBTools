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
package com.hangum.tadpole.erd.core.part;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.hangum.tadpole.erd.core.figures.decoration.relation.RelationDecorator;
import com.hangum.tadpole.erd.core.policies.RelationBendpointEditPolicy;
import com.hangum.tadpole.rdb.model.Relation;

public class RelationEditPart extends AbstractConnectionEditPart {
	private static final Logger logger = Logger.getLogger(RelationEditPart.class);
	private RelationAdapter adapter;
	private Label labelSource;
	
	private Label labelTarget;

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
		
		// source
		// text에는 target 이름을 넣는다.
		labelSource = new Label();
		labelSource.setText(relation.getReferenced_column_name());
		labelSource.setLabelAlignment(PositionConstants.CENTER);
		labelSource.setOpaque(true);
		labelSource.setBackgroundColor(ColorConstants.white);
		labelSource.setForegroundColor(ColorConstants.darkBlue);
		conn.add(labelSource, new ConnectionEndpointLocator(conn, true));

		// target
		// text에서 source이름을 넣는다.
		//
		labelTarget = new Label();
		labelTarget.setText(relation.getColumn_name());
		labelTarget.setLabelAlignment(PositionConstants.CENTER);
		labelTarget.setOpaque(true);
		labelTarget.setBackgroundColor(ColorConstants.white);
		labelTarget.setForegroundColor(ColorConstants.darkBlue);
		conn.add(labelTarget, new ConnectionEndpointLocator(conn, false));
		
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
