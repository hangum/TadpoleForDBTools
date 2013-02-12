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
package com.hangum.tadpole.mongodb.erd.core.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.mongodb.model.Relation;

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
