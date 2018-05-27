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
import org.eclipse.jface.dialogs.MessageDialog;
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
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * delete database action.
 * 
 * @author hangum
 *
 */
public class DeleteDBAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DeleteDBAction.class);

	private IStructuredSelection sel;

	public DeleteDBAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		final UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		removeDatabase(userDB);
	}
	
	public boolean removeDatabase(final UserDBDAO userDB) {
		if(userDB.getUser_seq() != SessionManager.getUserSeq()) {
			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().DeleteDBAction_2);
			return false;
		}
		
		if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, "[" + userDB.getDisplay_name() + "] " + Messages.get().DeleteDBAction_1)) return false; //$NON-NLS-2$ //$NON-NLS-3$
		
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
		
		// connection view를 초기화 합니다.
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				try { managerView.init(); } catch(Exception e) {}
			}
		});	// end display
		
		// object view를 초기화합니다.
		final ExplorerViewer explorerView = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				if(null != explorerView) explorerView.initObjectHead(null);
			}
		});	// end display
		
		// realdb disconnect
		try {
			TadpoleSystem_UserDBQuery.removeUserDB(userDB.getSeq());
			TadpoleSQLManager.removeInstance(userDB);			
		} catch (Exception e) { 
			logger.error("disconnection exception", e);			 //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), CommonMessages.get().Error, "Disconnection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return false;
		}
		
		return true;
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
