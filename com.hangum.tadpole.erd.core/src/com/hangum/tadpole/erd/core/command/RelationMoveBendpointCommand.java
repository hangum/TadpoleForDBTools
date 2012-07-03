package com.hangum.tadpole.erd.core.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.model.Relation;

public final class RelationMoveBendpointCommand extends Command {
	private Point oldLocation;
	private Point newLocation;
	private int index;
	private Relation relation;
	
	@Override
	public void execute() {
		if(oldLocation == null) oldLocation = relation.getBendpoint().get(index);
		relation.getBendpoint().set(index, newLocation);
	}
	
	@Override
	public void undo() {
		relation.getBendpoint().set(index, oldLocation);
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	
	public void setLocation(final Point newLocation) {
		this.newLocation = newLocation;
	}
}
