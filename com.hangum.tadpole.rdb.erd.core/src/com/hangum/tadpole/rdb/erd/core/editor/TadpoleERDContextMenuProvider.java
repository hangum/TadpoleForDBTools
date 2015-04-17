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
package com.hangum.tadpole.rdb.erd.core.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.hangum.tadpole.rdb.erd.core.actions.AutoLayoutAction;
import com.hangum.tadpole.rdb.erd.core.actions.ERDRefreshAction;

/**
 * mouse right click context menu
 * 
 * @author hangum
 *
 */
public class TadpoleERDContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	
	public TadpoleERDContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		IAction action;
		
		GEFActionConstants.addStandardActionGroups(menu);
		
		action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		
		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		
		action = getActionRegistry().getAction(AutoLayoutAction.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		// refresh action
		action = getActionRegistry().getAction(ERDRefreshAction.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
	}
	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}
	
	private void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}

	
}
