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

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

import com.hangum.tadpole.rdb.erd.core.actions.ERDViewStyleAction;
import com.hangum.tadpole.rdb.erd.core.actions.ERDViewStyleRetargetAction;

/**
 * ERD Action BAR Contributor
 * 
 * @author hangum
 *
 */
public class TadpoleERDActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		
		// Show/hide grid
//		addRetargetAction(new RetargetAction(
//				GEFActionConstants.TOGGLE_RULER_VISIBILITY,
//				GEFMessages.get().ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX));
//		addRetargetAction(new RetargetAction(
//				GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
//				GEFMessages.get().ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));
		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_GRID_VISIBILITY,
				GEFMessages.get().ToggleGrid_Label, IAction.AS_CHECK_BOX));
     
        // styled action
		addRetargetAction(new ERDViewStyleRetargetAction());
	}
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		
		toolBarManager.add(new Separator());
//		toolBarManager.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
//		toolBarManager.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		toolBarManager.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(ERDViewStyleAction.ID));
	}
	
//	@Override
//	public void contributeToMenu(IMenuManager menuManager) {
//		super.contributeToMenu(menuManager);
//		MenuManager viewMenu = new MenuManager("View");
//		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
//		viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
//		viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
//		
//		menuManager.add(viewMenu);
//	}

	@Override
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
	}

}
