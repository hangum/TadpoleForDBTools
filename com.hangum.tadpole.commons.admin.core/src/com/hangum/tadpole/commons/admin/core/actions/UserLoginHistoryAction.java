/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.dialogs.UserLoginHistoryDialog;
import com.swtdesigner.ResourceManager;

/**
 * User Login history
 * 
 * @author hangum
 *
 */
public class UserLoginHistoryAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserLoginHistoryAction.class);
	private final static String ID = "com.hangum.tadpole.commons.admin.core.actions.global.UserLoginHistoryAction"; //$NON-NLS-1$
	private final IWorkbenchWindow window;
	
	public UserLoginHistoryAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText("User Login History");
		setToolTipText("User Login History");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/userHistory.png"));
		setEnabled(true);
	}
	
	@Override
	public void run() {
		UserLoginHistoryDialog dialog = new UserLoginHistoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
