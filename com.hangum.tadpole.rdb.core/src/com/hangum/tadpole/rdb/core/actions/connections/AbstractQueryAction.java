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
package com.hangum.tadpole.rdb.core.actions.connections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.MongoDBInfosEditor;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.MongoDBInfosInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.util.EditorUtils;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.QueryTemplateUtils;

/**
 * query editor관련된 최상위 abstract class
 * 
 * @author hangum
 *
 */
public abstract class AbstractQueryAction implements IViewActionDelegate {
	private static final Logger logger = Logger.getLogger(AbstractQueryAction.class);
	protected IStructuredSelection sel;

	public AbstractQueryAction() {
		super();
	}
	
	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		run(userDB);
	}
	
	/**
	 * 디비의 화면을 오픈합니다.
	 * 
	 * @param userDB
	 */
	public void run(UserDBDAO userDB) {
		
		// mongodb인지 검사하여..
		if(DBDefine.getDBDefine(userDB.getDbms_types()) != DBDefine.MONGODB_DEFAULT) {
			MainEditorInput mei = new MainEditorInput(userDB);
			
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, MainEditor.ID);
			} catch (PartInitException e) {
				logger.error("open editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MONGODB_DEFAULT) {
			MongoDBInfosInput mongoInput = new MongoDBInfosInput(userDB, MongoDBInfosEditor.PAGES.COLLECTION_SUMMERY);
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mongoInput, MongoDBInfosEditor.ID);
			} catch (PartInitException e) {
				logger.error("open editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
		}
		
	}
	
	/**
	 * 기존 리소스 호출
	 * 
	 * @param userDB
	 * @param dao
	 */
	public void run(UserDBResourceDAO dao) {
		IEditorReference reference = EditorUtils.findSQLEditor(dao);
		
		if(reference == null) {
					
			try {
				MainEditorInput mei = new MainEditorInput(dao);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, MainEditor.ID);
			} catch (Exception e) {
				logger.error("new editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MainEditorInput_0, errStatus); //$NON-NLS-1$
			}
		} else {
			try {
				MainEditor editor = (MainEditor)reference.getEditor(true);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editor.getEditorInput(), MainEditor.ID, false);
				editor.setFocus();
			} catch (Exception e) {
				logger.error("findEditor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * 특정 쿼리를 생성합니다.
	 * 
	 * @param userDB
	 * @param actionType
	 */
	public void run(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType) {
		FindEditorAndWriteQueryUtil.run(userDB, QueryTemplateUtils.getQuery(userDB, actionType));
	}
	
	/**
	 * 특정 쿼리를 생성합니다.
	 * 
	 * @param userDB
	 * @param actionType
	 */
	public void run(UserDBDAO userDB, String strSql) {
		FindEditorAndWriteQueryUtil.run(userDB, strSql);
	}
	

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}
}
