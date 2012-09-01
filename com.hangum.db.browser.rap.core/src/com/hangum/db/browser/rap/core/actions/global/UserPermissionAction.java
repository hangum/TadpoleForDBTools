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
package com.hangum.db.browser.rap.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.tadpole.manager.core.dialogs.auth.ManageControlDialog;
import com.swtdesigner.ResourceManager;

/**
 * 유저관리
 * 
 * @author hangum
 *
 */
public class UserPermissionAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.UserPermissionAction"; //$NON-NLS-1$
	private IStructuredSelection iss;
	
	public UserPermissionAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText("User Manager");
		setToolTipText("User Manager");
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/user_group.png"));
		setEnabled(true);
	}
	
	@Override
	public void run() {
		ManageControlDialog dialog = new ManageControlDialog(window.getShell());
		dialog.open();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
