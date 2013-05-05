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
package com.hangum.tadpole.rdb.erd.core.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.hangum.tadpole.rdb.erd.core.command.RelationCreateBendpointCommand;
import com.hangum.tadpole.rdb.erd.core.command.RelationMoveBendpointCommand;
import com.hangum.tadpole.rdb.model.Relation;

public class RelationBendpointEditPolicy extends BendpointEditPolicy {

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		RelationCreateBendpointCommand command = new RelationCreateBendpointCommand();
		
		Point p = request.getLocation();
		
		command.setRelation( (Relation)request.getSource().getModel());
		command.setLocation(p);
		command.setIndex(request.getIndex());
		
		return command;
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		return null;
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		RelationMoveBendpointCommand command = new RelationMoveBendpointCommand();
		
		Point p = request.getLocation();
		
		command.setRelation( (Relation)request.getSource().getModel());
		command.setLocation(p);
		command.setIndex(request.getIndex());
		
		return command;
	}

}
