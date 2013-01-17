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
package com.hangum.tadpole.erd.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.hangum.tadpole.erd.core.command.TableDeleteCommand;
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
