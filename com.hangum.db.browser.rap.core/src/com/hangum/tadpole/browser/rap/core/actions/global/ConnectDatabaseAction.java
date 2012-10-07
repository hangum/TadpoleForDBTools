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
package com.hangum.tadpole.browser.rap.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.browser.rap.core.Activator;
import com.hangum.tadpole.browser.rap.core.Messages;
import com.hangum.tadpole.browser.rap.core.dialog.dbconnect.DBLoginDialog;
import com.hangum.tadpole.browser.rap.core.dialog.dbconnect.DBLoginDialog.WORK_TYPE;
import com.hangum.tadpole.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.swtdesigner.ResourceManager;

/**
 * 전체 영역에서 사용하기 위한 action 
 * 
 * @author hangum
 *
 */
public class ConnectDatabaseAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.ConnectDatabaseAction"; //$NON-NLS-1$
	private IStructuredSelection sel;
	
	public ConnectDatabaseAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.ConnectDatabaseAction_1);
		setToolTipText(Messages.ConnectDatabaseAction_2);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/connect.png"));
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		runConnectionDialog(sel);
	}
	
	public void runConnectionDialog(IStructuredSelection sel) {
		String selGroupName = null;
		
		if(sel != null) {
			if(sel.getFirstElement() instanceof ManagerListDTO) {
				ManagerListDTO mana = (ManagerListDTO)sel.getFirstElement();
				selGroupName = mana.getName();
			} else if(sel.getFirstElement() instanceof UserDBDAO) {
				UserDBDAO user =(UserDBDAO)sel.getFirstElement();
				selGroupName = user.getParent().getName();
			}
		}
		
		final DBLoginDialog dialog = new DBLoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), selGroupName);
		int ret = dialog.open();
		
		if(ret == Dialog.OK || ret == dialog.DELETE_BTN_ID) {
			final UserDBDAO userDB = dialog.getDTO();
			final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);			
			final WORK_TYPE workType = dialog.getWorkType();
			
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					// 입력
					if(WORK_TYPE.INSERT == workType) {
						managerView.addUserDB(userDB, true);
//					// 수정 삭제
//					} else if(WORK_TYPE.MODIFY == workType) {
//						managerView.modifyUserDB(userDB);
//					} else if(WORK_TYPE.DELETE == workType) {
//						managerView.deleteUserDB(userDB);
					} else {
						managerView.init();
					}
				}
			});	// end display
		}	// end if
	}
	

	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		sel = (IStructuredSelection)selection;

	}

}
