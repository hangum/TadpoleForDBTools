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
package com.hangum.tadpole.rdb.erd.core.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.rdb.model.Table;

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
