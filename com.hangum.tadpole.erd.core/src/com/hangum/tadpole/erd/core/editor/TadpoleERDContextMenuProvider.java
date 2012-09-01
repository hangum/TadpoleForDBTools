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
package com.hangum.tadpole.erd.core.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.hangum.tadpole.erd.core.actions.AutoLayoutAction;

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
		
	}

	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}
	
	private void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}

	
}
