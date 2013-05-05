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
package com.hangum.tadpole.mongodb.erd.core.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.mongodb.model.Table;

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
