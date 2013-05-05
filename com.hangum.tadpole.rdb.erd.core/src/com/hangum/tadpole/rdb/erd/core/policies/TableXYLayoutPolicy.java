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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.hangum.tadpole.rdb.erd.core.command.TableChangeConstraintCommand;
import com.hangum.tadpole.rdb.erd.core.command.TableCreateCommand;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.Table;

public class TableXYLayoutPolicy extends XYLayoutEditPolicy {
	
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		TableChangeConstraintCommand command = new TableChangeConstraintCommand();
		command.setModel((Table)child.getModel());
		command.setNewConstraint((Rectangle)constraint);
		
		return command;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Command retVal = null;
		if(request.getNewObjectType().equals(Table.class)) {
			TableCreateCommand command = new TableCreateCommand();
			command.setLocation(request.getLocation());
			command.setParent((DB)getHost().getModel());
			command.setTable((Table)(request.getNewObject()));
			retVal = command;
		}
		return retVal;
	}

}
