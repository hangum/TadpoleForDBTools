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
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.manager.internal.map.SQLMap;
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
		
		if( MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.ExitAction_2, Messages.ExitAction_3) ) {
			
			// 모든 데이터베이스를 클로스 합니다.
			TadpoleSQLManager.getDbManager();
			
			
			// https://github.com/hangum/TadpoleForDBTools/issues/157 (종료하기 전에 에디터에 내용이 있다면 묻도록 수정.)
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();	
			IEditorReference[] references = page.getEditorReferences();
			for (IEditorReference iEditorReference : references) {
				page.closeEditor(iEditorReference.getEditor(false), true);
			}
			
			// standalone 모드일경우에는 프로그램 종료한다.
			if(ApplicationArgumentUtils.isStandaloneMode()) {
				beforeLogoutAction();
				System.exit(0);
			// 서버모드 일 경우 프로그램 로그아웃한다.
			} else {
				beforeLogoutAction();
				SessionManager.logout();
			}
		}
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
