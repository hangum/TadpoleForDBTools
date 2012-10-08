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

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * database disconnect
 * 
 * @author hangumNote
 * @deprecated
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

//		// 실제 db connection 삭제 
//		try {
//			TadpoleSQLManager.removeInstance(userDB);			
//		} catch (Exception e) {
//			logger.error("remove Instnce db", e);
//		}
		
		// Connection tree 삭제
//		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
//		Display.getCurrent().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				managerView.removeTreeList(userDB.getTypes(), userDB);
//			}
//		});	// end display
		
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
//				explorerView.initObjectHead(new ManagerListDTO(userDB.getDisplay_name(), DBDefine.getDBDefine(userDB.getTypes()) ));
			}
		});	// end display
		
		// realdb disconnect
		try {
			TadpoleSQLManager.removeInstance(userDB);			
		} catch (Exception e) { 
			logger.error("disconnection exception", e);			
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Disconnection Exception", errStatus); //$NON-NLS-1$
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
