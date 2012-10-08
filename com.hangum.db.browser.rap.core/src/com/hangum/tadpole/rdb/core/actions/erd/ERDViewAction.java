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
package com.hangum.tadpole.rdb.core.actions.erd;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.erd.core.editor.TadpoleEditor;
import com.hangum.tadpole.erd.core.editor.TadpoleEditorInput;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;

public class ERDViewAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ERDViewAction.class);
	private IStructuredSelection sel;

	public ERDViewAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		run(userDB);
	}
	
	public void run(UserDBDAO userDB) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			TadpoleEditorInput input = new TadpoleEditorInput(userDB.getDisplay_name() + "(" + userDB.getDb() + ")", userDB, false);
			page.openEditor(input, TadpoleEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("erd editor opend", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ERDAllTableViewAction_3, errStatus); //$NON-NLS-1$
		}
	}
	
	public void run(UserDBResourceDAO userDBErd) {
		UserDBDAO userDB = userDBErd.getParent();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			TadpoleEditorInput input = new TadpoleEditorInput(userDB.getDisplay_name() + "(" + userDB.getDb() + ")", userDBErd);
			page.openEditor(input, TadpoleEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("erd editor opend", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ERDAllTableViewAction_3, errStatus); //$NON-NLS-1$
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
