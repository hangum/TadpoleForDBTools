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
package com.hangum.tadpole.rdb.core.actions.connections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * database disconnect
 * 
 * @author hangum
 */
public class DisconnectDBAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DisconnectDBAction.class);

	private IStructuredSelection sel;
	
	public DisconnectDBAction() {
		super();
	}

	@Override
	public void run(IAction action) {

		final UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		final String strUserID = SessionManager.getEMAIL();
		
		// editor 삭제
		MainEditorInput mei = new MainEditorInput(userDB);		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] editroRefreence = page.getEditorReferences();

		String titleName = mei.getName();
		for (IEditorReference iEditorReference : editroRefreence) {
			if(iEditorReference.getTitle().indexOf(titleName) != -1) {
				page.closeEditor(iEditorReference.getEditor(false), true);
			}
		}
		
		// object view를 초기화합니다.
		final ExplorerViewer explorerView = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				explorerView.initObjectHead(new ManagerListDTO(null));
			}
		});	// end display
		
		// session 에 접근 가능한 디비임을 표시한다.
		SessionManager.removeUnlockDB(userDB);
		
		// realdb disconnect
		try {
			TadpoleSQLManager.removeInstance(userDB);
		} catch (Exception e) { 
			logger.error("disconnection exception", e);			
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), CommonMessages.get().Error, "Disconnection Exception", errStatus); //$NON-NLS-1$
		}
		
		try {
			TadpoleSQLTransactionManager.rollbackAll(strUserID, userDB);
		} catch (Exception e) { 
			logger.error("disconnection exception", e);			
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), CommonMessages.get().Error, "Disconnection Exception", errStatus); //$NON-NLS-1$
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
