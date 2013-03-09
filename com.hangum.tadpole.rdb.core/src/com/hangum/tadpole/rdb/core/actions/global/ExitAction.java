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
package com.hangum.tadpole.rdb.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.swtdesigner.ResourceManager;

/**
 * <pre>
 * 	Application(server, tomcat, jetty)을 종료합니다.
 * 	
 * </pre>
 * 
 * @author hangum
 *
 */
public class ExitAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.ExitAction"; //$NON-NLS-1$
	
	public ExitAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.ExitAction_0);
		if(ApplicationArgumentUtils.isStandaloneMode()) { 
			setToolTipText(Messages.ExitAction_1);		
		} else {
			setToolTipText("Log out");
		}
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/exit.png"));
	}

	@Override
	public void run() {
		// standalone 모드일경우에는 프로그램 종료한다.
		if(ApplicationArgumentUtils.isStandaloneMode()) {
			if( MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.ExitAction_2, Messages.ExitAction_3) ) {
				System.exit(0);
			}
		// 서버모드 일 경우 프로그램 로그아웃한다.
		} else {
			if( MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.ExitAction_2, Messages.ExitAction_3) ) {
				SessionManager.logout();
			}
		}
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
