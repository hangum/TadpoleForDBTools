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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.hangum.tadpole.rdb.erd.core.command.TableDeleteCommand;
import com.hangum.tadpole.rdb.model.Table;

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
