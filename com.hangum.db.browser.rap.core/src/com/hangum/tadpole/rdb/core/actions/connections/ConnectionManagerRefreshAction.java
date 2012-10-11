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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;

/**
 * connection manager의 뷰어 리프레쉬 액션
 * 
 * @author hangum
 */
public class ConnectionManagerRefreshAction implements IViewActionDelegate {
	protected IStructuredSelection sel;
	
	public ConnectionManagerRefreshAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				managerView.init();
			}
		});	// end display
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void init(IViewPart view) {
	}

}
