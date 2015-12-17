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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.admin.core.editors.system.AdminSystemSettingEditor;
import com.hangum.tadpole.commons.admin.core.editors.system.AdminSystemSettingEditorInput;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.preference.Activator;
import com.swtdesigner.ResourceManager;

/**
 * Admin System setting action
 * 
 * @author hangum
 *
 */
public class AdminSystemSettingAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AdminSystemSettingAction.class);
	private final static String ID = "com.hangum.db.browser.rap.core.actions.preference.admin.system.setting"; //$NON-NLS-1$
	
	private final IWorkbenchWindow window;
	
	public AdminSystemSettingAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText("Admin system setting");
		setToolTipText("Admin system setting");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/user.png")); //$NON-NLS-1$
		setEnabled(true);
	}
	
	@Override
	public void run() {
		try {
			AdminSystemSettingEditorInput adminSystemSettingInput = new AdminSystemSettingEditorInput();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(adminSystemSettingInput, AdminSystemSettingEditor.ID);
		} catch (PartInitException e) {
			logger.error("Admin System setting editor", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Admin System setting editor", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}
