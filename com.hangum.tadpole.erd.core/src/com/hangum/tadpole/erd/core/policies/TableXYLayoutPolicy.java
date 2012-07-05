package com.hangum.tadpole.erd.core.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.hangum.tadpole.erd.core.command.TableChangeConstraintCommand;
import com.hangum.tadpole.erd.core.command.TableCreateCommand;
import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Table;

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
