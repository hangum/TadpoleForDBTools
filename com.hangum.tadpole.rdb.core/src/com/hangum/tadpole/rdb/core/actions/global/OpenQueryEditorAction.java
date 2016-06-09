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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.swtdesigner.ResourceManager;

/**
 * 현재 선택된 디비의 릴레이션 화면을 오픈합니다.
 * 
 * @author hangum
 *
 */
public class OpenQueryEditorAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.OpenQueryEditorAction"; //$NON-NLS-1$
	protected final IWorkbenchWindow window;
	protected IStructuredSelection iss;
	protected UserDBDAO userDB;
	
	public OpenQueryEditorAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().EditSQL);
		setToolTipText(Messages.get().EditSQL);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/sql-query.png"));
		setEnabled(false);
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		QueryEditorAction qea = new QueryEditorAction();
		qea.run(userDB);
	}
	

	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IStructuredSelection sel = (IStructuredSelection)selection;

		setEnabled(false);
		if(sel != null) {
			ManagerViewer ev = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
			Object obj = ((IStructuredSelection)ev.getManagerTV().getSelection()).getFirstElement();
			if( obj instanceof UserDBDAO ) {
				userDB = (UserDBDAO)obj;
				isSelectEnable();
			} else if(obj instanceof UserDBResourceDAO) {
				UserDBResourceDAO userDBResource = (UserDBResourceDAO)obj;
				userDB = userDBResource.getParent();
				isSelectEnable();
			} else if(obj instanceof ResourcesDAO) {
				ResourcesDAO resourcesDAO = (ResourcesDAO)obj;
				userDB = resourcesDAO.getUserDBDAO();
				isSelectEnable();
			}
		}
	}
	
	/**
	 * is select button enable
	 */
	private void isSelectEnable() {
		if(TadpoleSecurityManager.getInstance().isLock(userDB)) {
			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {				
				setEnabled(true);
			}
		}
	}
}
