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
package com.hangum.tadpole.rdb.core.actions.object;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * Object view를 컨트롤 하기위한 Abstract action
 * 
 * @author hangum
 * 
 */
public abstract class AbstractObjectAction extends Action implements ISelectionListener, IWorkbenchAction {

	private IWorkbenchWindow window;				  
	UserDBDAO userDB = null;
	IStructuredSelection selection;

	private PublicTadpoleDefine.DB_ACTION actionType;
	
	public AbstractObjectAction() {
	}
	
	/**
	 * 
	 * @param window
	 * @param actionType view의 작업 타입
	 * @param userDB
	 */
	public AbstractObjectAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType) {
		this.window = window;
		this.actionType = actionType;
		
		setEnabled(false);//userDB != null);
		window.getSelectionService().addSelectionListener(this);
	}
	
	/**
	 * explorer viewe
	 * @return
	 */
	protected ExplorerViewer getExplorerView() {
		try {
			return (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * delete message
	 *  
	 * @param msgHead
	 * @param e
	 */
	protected void exeMessage(String msgHead, Exception e) {
		Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
		ExceptionDetailsErrorDialog.openError(null, "Error", msgHead + Messages.ObjectDeleteAction_25, errStatus); //$NON-NLS-1$
	}
	
	/**
	 * table 최신정보로 갱신
	 */
	protected void refreshTable() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshTable(true);		
	}
	
	/**
	 * View 최신정보로 갱신
	 */
	protected void refreshView() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshView(true);		
	}
	
	/**
	 * Indexes 최신정보로 갱신
	 */
	protected void refreshIndexes() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshIndexes(true);		
	}
	
	/**
	 * Procedure 최신정보로 갱신
	 */
	protected void refreshProcedure() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshProcedure(true);		
	}
	
	/**
	 * Procedure 최신정보로 갱신
	 */
	protected void refreshPackage() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshPackage(true);		
	}
	
	/**
	 * Function 최신정보로 갱신
	 */
	protected void refreshFunction() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshFunction(true);		
	}
	
	/**
	 * Trigger 최신정보로 갱신
	 */
	protected void refreshTrigger() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshTrigger(true);		
	}
	
	/**
	 * mongodb javascript 최신정보로 갱신
	 */
	protected void refreshJS() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshJS(true);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			this.selection = (IStructuredSelection)selection;
			
			if(userDB != null) {
				setEnabled(true);
				return;
			}
		}
		
		setEnabled(false);
	}
	
	@Override
	public void run() {
		if (null == this.selection) {
			run(selection, userDB, actionType);
		}
	}
	
	/**
	 * Convenience method for Deduplication.<br>
	 * There is no need to check null. If selection is null, don't execute this
	 * method.<br>
	 * <br>
	 * 
	 * @param selection selection of ExplorerViewer
	 * @param actionType
	 */
	abstract public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType);
	
	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	public IWorkbenchWindow getWindow() {
		return window;
	}

	public void setWindow(IWorkbenchWindow window) {
		this.window = window;
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}
	
}
