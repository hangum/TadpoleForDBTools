/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.rdb.erd.core.dialogs.ERDViewStyleDailog;
import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.swtdesigner.ResourceManager;

/**
 * ERD view styled select action
 * 
 * @author hangum
 *
 */
public class ERDViewStyleAction extends SelectionAction {
	
	public final static String ID = "com.hangum.tadpole.rdb.erd.actions.global.ERDViewStyleAction"; //$NON-NLS-1$
	
	public ERDViewStyleAction(IWorkbenchPart part) {
		super(part);
		
		setId(ID);
		setText("View Style Select");
		setToolTipText("View Style Select");
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/viewStyle.png"));
	}
	
	@Override
	public void run() {
		ERDViewStyleDailog dialog = new ERDViewStyleDailog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
		
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}
