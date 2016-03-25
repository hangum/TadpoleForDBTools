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
package com.hangum.tadpole.rdb.core.util;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.actions.global.SynchronizedEditorHandler;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.erd.core.editor.TadpoleRDBEditor;

/**
 * editor utils
 * 
 * @author hangum
 *
 */
public class EditorUtils {
	private static final Logger logger = Logger.getLogger(EditorUtils.class);
	
	/**
	 * find SQL editor
	 * @return
	 */
	public static IEditorPart findSQLEditor() {
		// First check active editor
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor instanceof MainEditor) {
			return activeEditor;
		}
		
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();		
		for (IEditorReference iEditorReference : editors) {
			//  에디터를 검색한다.
			if(MainEditor.ID.equals( iEditorReference.getId() )) {
				return (MainEditor)iEditorReference.getEditor(false);
			}
		}
		
		return null;
	}

	/**
	 * find SQL editor
	 * 
	 * @param dao UserDBDAO
	 * @return
	 */
	public static IEditorPart findSQLEditor(final UserDBDAO dao) {
		// First check active editor
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor instanceof MainEditor) {
			MainEditor editor = (MainEditor) activeEditor;
			if(editor.getUserDB().equals(dao) && SQLUtil.isSELECTEditor(editor.getDbAction())) {
				return activeEditor;
			}
		}
		
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();		
		for (IEditorReference iEditorReference : editors) {
			//  에디터를 검색한다.
			if(MainEditor.ID.equals( iEditorReference.getId() )) {
				MainEditor editor = (MainEditor)iEditorReference.getEditor(false);
				if(editor.getUserDB().getSeq() == dao.getSeq()) {
					return editor;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * find sql editor
	 * 
	 * @param dao UserDBResourceDAO
	 * @return
	 */
	public static IEditorReference findSQLEditor(final UserDBResourceDAO dao) {
		return findEditor(MainEditor.ID, dao);
	}

	/**
	 * find erd editor
	 * @param dao
	 * @return
	 */
	public static IEditorReference findERDEditor(UserDBResourceDAO dao) {
		return findEditor(TadpoleRDBEditor.ID, dao);
	}

	/**
	 * find editor
	 * 
	 * @param id
	 * @param dao
	 * @return
	 */
	public static IEditorReference findEditor(String id, UserDBResourceDAO dao) {
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		
		for (IEditorReference iEditorReference : editors) {
			if(id.equals( iEditorReference.getId() )) {				
				IEditorPart ier = iEditorReference.getEditor(true);
				if(ier instanceof MainEditor) {
					MainEditor editor = (MainEditor)iEditorReference.getEditor(true);
					if(editor.getdBResource() == null) continue;
					
					if(editor.getdBResource().getResource_seq() == dao.getResource_seq()) return iEditorReference;
				} else if(ier instanceof TadpoleRDBEditor) {
					TadpoleRDBEditor editor = (TadpoleRDBEditor)iEditorReference.getEditor(true);
					if(editor.getUserDBErd() == null) continue;
					
					if(editor.getUserDBErd().getResource_seq() == dao.getResource_seq()) return iEditorReference;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * select connection manager
	 * 
	 * @param userDBDAO
	 */
	public static void selectConnectionManager(final UserDBDAO userDBDAO) {
		if(userDBDAO == null) return;
		if(!SynchronizedEditorHandler.isSynchronizedState()) return;
		
		final Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				ManagerViewer managerView =  (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
				
				ISelection is = managerView.getManagerTV().getSelection();
				if(!is.isEmpty()) {
					Object element = ((IStructuredSelection)is).getFirstElement();
					if(element instanceof ManagerListDTO) {
						setSelection(managerView, userDBDAO);

					} else if(element instanceof UserDBDAO) {
						UserDBDAO currentSelectionUserDB = (UserDBDAO)element;
						if(currentSelectionUserDB.getSeq() != userDBDAO.getSeq()) {
							setSelection(managerView, userDBDAO);
						}
					
					} else if(element instanceof UserDBResourceDAO) {
						UserDBResourceDAO dao = (UserDBResourceDAO)element;
						if(dao.getDb_seq() != userDBDAO.getSeq()) {
							setSelection(managerView, userDBDAO);
						}
					}
					
				} else {
					setSelection(managerView, userDBDAO);
				}
			}
			
			private void setSelection(ManagerViewer managerView, final UserDBDAO userDBDAO) {
				IStructuredSelection iss = new StructuredSelection(userDBDAO);
				managerView.getManagerTV().setSelection(iss);
			}
		});
	}
}
