package com.hangum.tadpole.erd.core.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.hangum.tadpole.erd.core.command.RelationCreateBendpointCommand;
import com.hangum.tadpole.erd.core.command.RelationMoveBendpointCommand;
import com.hangum.tadpole.model.Relation;

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
