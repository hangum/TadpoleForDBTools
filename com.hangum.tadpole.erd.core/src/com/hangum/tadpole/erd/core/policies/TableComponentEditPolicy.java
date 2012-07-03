package com.hangum.tadpole.erd.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.hangum.tadpole.erd.core.command.TableDeleteCommand;
import com.hangum.tadpole.model.Table;

/**
 * table delete command policy
 * 
 * @author hangum
 *
 */
public class TableComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		TableDeleteCommand tableDeleteCommand = new TableDeleteCommand();
		tableDeleteCommand.setTable( (Table)getHost().getModel() );
		return tableDeleteCommand;
	}
}
