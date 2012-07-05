package com.hangum.tadpole.erd.core.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.model.Table;

public class TableChangeConstraintCommand extends Command {
	private Rectangle oldConstraint;
	private Rectangle newConstraint;
	private Table model;
	
	@Override
	public void execute() {
		if(oldConstraint == null) oldConstraint = model.getConstraints();
		model.setConstraints(newConstraint);
	}

	@Override
	public void undo() {
		model.setConstraints(oldConstraint);
	}
	
	public void setModel(Table model) {
		this.model = model;
	}
	
	public void setNewConstraint(Rectangle newConstraint) {
		this.newConstraint = newConstraint;
	}
}
