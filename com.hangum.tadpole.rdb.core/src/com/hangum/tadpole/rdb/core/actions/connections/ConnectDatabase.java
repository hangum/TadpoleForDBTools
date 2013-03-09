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
package com.hangum.tadpole.rdb.core.actions.connections;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.DBLoginDialog;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;

/**
 * db 연결 action
 * @author hangumNote
 *
 */
public class ConnectDatabase implements IViewActionDelegate {
	public static final String ID = "com.hangum.tadpole.browser.rap.core.action.connect.database";
	private IStructuredSelection sel;
	private String selGroupName = "";

	@Override
	public void run(IAction action) {
		
		if(sel != null) {
			if(sel.getFirstElement() instanceof ManagerListDTO) {
				ManagerListDTO mana = (ManagerListDTO)sel.getFirstElement();
				selGroupName = mana.getName();
			} else if(sel.getFirstElement() instanceof UserDBDAO) {
				UserDBDAO user =(UserDBDAO)sel.getFirstElement();
				selGroupName = user.getParent().getName();
			}
		}
		
		run();
		
	}
	
	public void run() {
		final DBLoginDialog dialog = new DBLoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), selGroupName);
		final int ret = dialog.open();

		final UserDBDAO userDB = dialog.getDTO();
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);			
		
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				if(ret == Dialog.OK) managerView.addUserDB(userDB, true);
				else managerView.init();
			}
		});	// end display

	}
	

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
		
	}

	@Override
	public void init(IViewPart view) {
	}
}
