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
