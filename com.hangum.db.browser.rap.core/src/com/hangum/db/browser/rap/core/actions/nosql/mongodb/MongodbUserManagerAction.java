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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.users.UserManagerDialog;

/**
 * mongodb user manager action
 * 	
 * @author hangum
 *
 */
public class MongodbUserManagerAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongodbUserManagerAction.class);

	private IStructuredSelection sel;

	public MongodbUserManagerAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		UserManagerDialog dialog = new UserManagerDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
