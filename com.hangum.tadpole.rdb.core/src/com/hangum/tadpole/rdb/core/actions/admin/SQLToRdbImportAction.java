/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.admin;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.importexport.core.dialogs.SQLToDBImportDialog;
import com.hangum.tadpole.rdb.core.Messages;

/**
 *  SQL file Import
 * 
 * @author hangum
 *
 */
public class SQLToRdbImportAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLToRdbImportAction.class);

	private IStructuredSelection sel;
	
	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.get().Information, Messages.get().MainEditor_DoesnotSupport);
		} else {
			boolean isPossible = false;
			if(PermissionChecker.isDBAdminRole(userDB)) isPossible = true;
			else {
				if(!PermissionChecker.isProductBackup(userDB)) isPossible = true;
			}
			
			if(isPossible) {
				SQLToDBImportDialog dialog = new SQLToDBImportDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
			} else {
				MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.get().Information, Messages.get().MainEditor_21);
			}
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
