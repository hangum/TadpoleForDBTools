package com.hangum.tadpole.erd.core.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.model.Relation;

public final class RelationCreateBendpointCommand extends Command {
	private int index;
	private Point location;
	private Relation relation;
	
	@Override
	public void execute() {
		relation.getBendpoint().add(index, location);
	}
	
	@Override
	public void undo() {
		relation.getBendpoint().remove(index);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setRelation(Relation relation) {
		this.relation = relation;
	}

}
