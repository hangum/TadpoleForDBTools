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
package com.hangum.tadpole.rdb.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDDeleteAction;
import com.swtdesigner.ResourceManager;

/**
 * 현재 선택된 디비의 릴레이션 화면을 오픈합니다.
 * 
 * @author hangum
 *
 */
public class DeleteResourceAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.DeleteResourceAction"; //$NON-NLS-1$
	private IStructuredSelection iss;
	
	public DeleteResourceAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().DeleteResourceAction_0);
		setToolTipText(Messages.get().DeleteResourceAction_0);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/delete-relation.png")); //$NON-NLS-1$
		setEnabled(false);
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		UserDBResourceDAO userResourceDB = (UserDBResourceDAO)iss.getFirstElement();
		if(!MessageDialog.openConfirm(window.getShell(), Messages.get().Confirm, Messages.get().DeleteResourceAction_4)) return;
		
		RDBERDDeleteAction erdDelete = new RDBERDDeleteAction();
		erdDelete.run(userResourceDB);
	}
	

	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IStructuredSelection sel = (IStructuredSelection)selection;
		if(sel != null) {
			if( sel.getFirstElement() instanceof UserDBResourceDAO ) {
				iss = sel;
				
				setEnabled(true);
			} else setEnabled(false);
		} else setEnabled(false);
	}

}
