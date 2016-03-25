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
package com.hangum.tadpole.rdb.core.actions.erd.mongodb;

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
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBERDEditor;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;

public class MongoDBERDViewAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBERDViewAction.class);
	private IStructuredSelection sel;

	public MongoDBERDViewAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		run(userDB);
	}
	
	public void run(UserDBDAO userDB) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			TadpoleMongoDBEditorInput input = new TadpoleMongoDBEditorInput(userDB.getDisplay_name() + "(" + userDB.getDb() + ")", userDB, false);
			page.openEditor(input, TadpoleMongoDBERDEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("erd editor opend", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ERDAllTableViewAction_3, errStatus); //$NON-NLS-1$
		}
	}
	
	public void run(UserDBResourceDAO userDBErd) {
		UserDBDAO userDB = userDBErd.getParent();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			TadpoleMongoDBEditorInput input = new TadpoleMongoDBEditorInput(userDB.getDisplay_name() + "(" + userDB.getDb() + ")", userDBErd);
			page.openEditor(input, TadpoleMongoDBERDEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("erd editor opend", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ERDAllTableViewAction_3, errStatus); //$NON-NLS-1$
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
