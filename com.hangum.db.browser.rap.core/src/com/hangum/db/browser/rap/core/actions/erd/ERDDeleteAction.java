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
package com.hangum.db.browser.rap.core.actions.erd;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.hangum.db.define.Define;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystem_UserDBResource;

public class ERDDeleteAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ERDDeleteAction.class);
	private IStructuredSelection sel;
	
	public ERDDeleteAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBResourceDAO userDB = (UserDBResourceDAO)sel.getFirstElement();
		
		if(MessageDialog.openConfirm(null, Messages.ERDDeleteAction_0, Messages.ERDDeleteAction_1)) run(userDB);
	}
	
	public void run(UserDBResourceDAO userDBErd) {
		try {
			TadpoleSystem_UserDBResource.delete(userDBErd);
			
			ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ManagerViewer.ID);
			mv.deleteErd(userDBErd);
		} catch (Exception e) {
			logger.error(Messages.ERDDeleteAction_2, e);
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ERDDeleteAction_3, errStatus); //$NON-NLS-1$
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}
}
