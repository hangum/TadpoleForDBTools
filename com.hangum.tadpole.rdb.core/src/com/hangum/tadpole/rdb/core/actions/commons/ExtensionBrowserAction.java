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
package com.hangum.tadpole.rdb.core.actions.commons;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExternalBrowser;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.externalbrowser.ExternalBrowserEditor;
import com.hangum.tadpole.rdb.core.editors.externalbrowser.ExternalBrowserInput;

/**
 * Extension browser Action
 * 
 * @author hangum
 *
 */
public class ExtensionBrowserAction implements IViewActionDelegate {
	private static final Logger logger = Logger.getLogger(ExtensionBrowserAction.class);
	private IStructuredSelection sel;

	public ExtensionBrowserAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();

		// 외부 확장 브라우저 리스트를 가져옵니다. 
		if(null != userDB) {
			try {
				List<ExternalBrowserInfoDAO> listExternalBrowser = TadpoleSystem_ExternalBrowser.getExternalBrowser(userDB);
				
				if(listExternalBrowser.isEmpty()) {
					MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().ExtensionBrowserAction_1);					 //$NON-NLS-1$
				} else {
					ExternalBrowserInput exi = new ExternalBrowserInput(userDB, listExternalBrowser);
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(exi, ExternalBrowserEditor.ID, false);
				}
				
			} catch(Exception e) {
				logger.error("Get external browser exception", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ExtensionBrowserAction_3, errStatus); //$NON-NLS-1$
			}
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
