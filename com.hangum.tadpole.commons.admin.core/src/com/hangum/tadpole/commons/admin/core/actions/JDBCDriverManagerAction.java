/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.driver.JDBCDriverManageDialog;
import com.swtdesigner.ResourceManager;

/**
 * JDBC Driver manager action
 * 
 * @author hangum
 *
 */
public class JDBCDriverManagerAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JDBCDriverManagerAction.class);
	private final static String ID = "com.hangum.tadpole.commons.admin.core.actions.global.JDBCDriverManagerAction"; //$NON-NLS-1$
	private final IWorkbenchWindow window;
	
	public JDBCDriverManagerAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().JDBCDriverManagerAction_0);
		setToolTipText(Messages.get().JDBCDriverManagerAction_0);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/manager.png")); //$NON-NLS-1$
		setEnabled(true);
	}
	
	@Override
	public void run() {
		JDBCDriverManageDialog dialog = new JDBCDriverManageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		if(Dialog.OK ==  dialog.open()) {
			if(dialog.isUploaded()) {
				MessageDialog.openInformation(null, Messages.get().Information, Messages.get().jdbcdriver);
			}
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
