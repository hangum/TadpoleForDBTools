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
package com.hangum.tadpole.rdb.erd.core.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.rdb.model.Relation;

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
