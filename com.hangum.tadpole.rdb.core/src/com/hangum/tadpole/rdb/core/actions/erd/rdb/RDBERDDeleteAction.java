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
package com.hangum.tadpole.rdb.core.actions.erd.rdb;

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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.session.manager.SessionManager;

public class RDBERDDeleteAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBERDDeleteAction.class);
	private IStructuredSelection sel;
	
	public RDBERDDeleteAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBResourceDAO userDB = (UserDBResourceDAO)sel.getFirstElement();
		if(userDB.getUser_seq() != SessionManager.getUserSeq()) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().DeleteDBAction_2);
			return;
		}
		if(MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().ERDDeleteAction_1)) run(userDB);
	}
	
	public void run(UserDBResourceDAO userDBErd) {
		try {
			TadpoleSystem_UserDBResource.delete(userDBErd);
			
			ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ManagerViewer.ID);
			mv.deleteResource(userDBErd);
		} catch (Exception e) {
			logger.error(Messages.get().ERDDeleteAction_2, e);
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
