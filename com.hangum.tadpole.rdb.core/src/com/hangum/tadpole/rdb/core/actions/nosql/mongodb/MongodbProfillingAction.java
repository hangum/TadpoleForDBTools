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
package com.hangum.tadpole.rdb.core.actions.nosql.mongodb;

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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.ext.editors.Profilling.ProfilingEditor;
import com.hangum.tadpole.mongodb.core.ext.editors.Profilling.ProfilingEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * mongodb profilling action
 * 	
 * @author hangum
 *
 */
public class MongodbProfillingAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongodbProfillingAction.class);

	private IStructuredSelection sel;

	public MongodbProfillingAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			ProfilingEditorInput input = new ProfilingEditorInput(userDB);
			page.openEditor(input, ProfilingEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("Mongodb profilling", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Profilling Exception", errStatus); //$NON-NLS-1$
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
