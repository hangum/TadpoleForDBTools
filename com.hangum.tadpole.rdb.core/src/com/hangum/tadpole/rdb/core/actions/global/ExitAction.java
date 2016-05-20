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

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
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
	private static final Logger logger = Logger.getLogger(ExitAction.class);
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.ExitAction"; //$NON-NLS-1$
	
	public ExitAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().Exit);
		setToolTipText(Messages.get().Exit);

		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/exit.png")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if(ApplicationArgumentUtils.isStandaloneMode()) {
			MessageDialog dialog = new MessageDialog(shell, Messages.get().Confirm, null, Messages.get().ExitAction_4, 
										MessageDialog.QUESTION, new String[]{Messages.get().ExitAction_5, Messages.get().Logout, Messages.get().CANCEL}, 1);
			int intResult = dialog.open();
			if(intResult == 0) {
				serverLogout();
				System.exit(0);
			} else if(intResult == 1) {
				serverLogout();
			}
			
//		// tomcat 에서 실행했는지 어떻게 검사하지?
//		} else if(!ApplicationArgumentUtils.isStandaloneMode() && TadpoleApplicationContextManager.isPersonOperationType()) {
//			if( MessageDialog.openConfirm(shell, Messages.get().ExitAction_2, Messages.get().ExitAction_4) ) {
//				serverLogout();
//				System.exit(0);
//			}
		} else {
			if( MessageDialog.openConfirm(shell, Messages.get().Confirm, Messages.get().ExitAction_3) ) {
				serverLogout();
			}
		}
	}
	
	private void serverLogout() {
		// https://github.com/hangum/TadpoleForDBTools/issues/157 (종료하기 전에 에디터에 내용이 있다면 묻도록 수정.)
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();	
		IEditorReference[] references = page.getEditorReferences();
		for (IEditorReference iEditorReference : references) {
			page.closeEditor(iEditorReference.getEditor(false), true);
		}
		
		beforeLogoutAction();
		SessionManager.logout();
	}
	
	/**
	 * 로그아웃 전처리를 합니다 
	 * 
	 * <pre>
	 * 1. 사용자의 Transaction connection 이 있을 경우 rollbak 처리한다.
	 * 2. 페이지를 다른 쪽으로 이동합니다.
	 * 
	 * </pre> 
	 */
	private void beforeLogoutAction() {
		TadpoleSQLTransactionManager.executeRollback(SessionManager.getEMAIL());
		
//		HttpServletResponse hsr = RWT.getResponse();
//		try {
////			hsr.setHeader("Pragma","no-cache");
////			hsr.setHeader("Cache-Control","no-cache");
////			hsr.addHeader("Cache-Control","no-store");
////			hsr.setDateHeader("Expires", 0);
//			
////			hsr.sendRedirect("http://daum.net");
//			
//			Client client = RWT.getClient();
//			JavaScriptExecutor executor = client.getService( JavaScriptExecutor.class );
//			if( executor != null ) {
//			  executor.execute("parent.window.location.href=\"" + "http://daum.net" + "\";");
//			}
//		} catch (Exception e) {
//			logger.error("logout redirect", e);
//			
//			// ignore message
//		}
		
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

}
