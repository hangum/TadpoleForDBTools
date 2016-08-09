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
package com.hangum.tadpole.rdb.core.actions.oracle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.ext.DownloadSQLiteDBAction;

/**
 * Oracle table space manager action
 * 
 * @author hangum
 *
 */
public class TableSpaceManagerAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DownloadSQLiteDBAction.class);
	private final static String ID = "com.hangum.tadpole.rdb.core.actions.oracle.TableSpaceManagerAction"; //$NON-NLS-1$

	private IStructuredSelection sel;

	public TableSpaceManagerAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		final UserDBDAO userDB = (UserDBDAO) sel.getFirstElement();

		try {
			TableSpaceManagerEditorInput userMe = new TableSpaceManagerEditorInput();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(userMe, TableSapceManageEditor.ID);
		} catch (PartInitException e) {
			logger.error("Tablespace Management editor", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Tablespace Management editor", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection) selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}