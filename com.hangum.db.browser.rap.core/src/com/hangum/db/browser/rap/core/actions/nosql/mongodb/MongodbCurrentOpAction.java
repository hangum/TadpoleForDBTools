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
package com.hangum.db.browser.rap.core.actions.nosql.mongodb;

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

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.ext.editors.currentOp.CurrentOpEditorInput;
import com.hangum.tadpole.mongodb.core.ext.editors.currentOp.CurrentOperationEditor;

/**
 * mongodb currentOp action
 * 	
 * @author hangum
 *
 */
public class MongodbCurrentOpAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongodbCurrentOpAction.class);

	private IStructuredSelection sel;

	public MongodbCurrentOpAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			CurrentOpEditorInput input = new CurrentOpEditorInput(userDB);
			page.openEditor(input, CurrentOperationEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("Mongodb current operation", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "current operation Exception", errStatus); //$NON-NLS-1$
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
