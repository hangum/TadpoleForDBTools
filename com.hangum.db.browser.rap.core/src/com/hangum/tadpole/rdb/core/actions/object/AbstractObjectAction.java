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
package com.hangum.tadpole.rdb.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * Object view를 컨트롤 하기위한 Abstract action
 * 
 * @author hangumNote
 * 
 */
public abstract class AbstractObjectAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AbstractObjectAction.class);

	protected UserDBDAO userDB = null;
	protected IWorkbenchWindow window;				  
	protected IStructuredSelection sel;
	protected Define.DB_ACTION actionType;
	
	/**
	 * 
	 * @param window
	 * @param actionType view의 작업 타입
	 * @param userDB
	 */
	public AbstractObjectAction(IWorkbenchWindow window, Define.DB_ACTION actionType) {
		this.window = window;
		this.actionType = actionType;
		
		setEnabled(userDB != null);
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
	 * table 최신정보로 갱신
	 */
	protected void refreshTable() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshTable("Table");		
	}
	
	/**
	 * View 최신정보로 갱신
	 */
	protected void refreshView() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshView();		
	}
	
	/**
	 * Indexes 최신정보로 갱신
	 */
	protected void refreshIndexes() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshIndexes();		
	}
	
	/**
	 * Procedure 최신정보로 갱신
	 */
	protected void refreshProcedure() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshProcedure();		
	}
	
	/**
	 * Function 최신정보로 갱신
	 */
	protected void refreshFunction() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshFunction();		
	}
	
	/**
	 * Trigger 최신정보로 갱신
	 */
	protected void refreshTrigger() {
		ExplorerViewer ev = getExplorerView();
		if(ev != null) ev.refreshTrigger();		
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			this.sel = (IStructuredSelection)selection;
			
			if(userDB != null) setEnabled(true);
			else setEnabled(false);
		}
	}
	
	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}

}
