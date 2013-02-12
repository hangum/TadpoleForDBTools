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
package com.hangum.tadpole.rdb.core.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.erd.core.editor.TadpoleRDBEditor;

/**
 * editor utils
 * 
 * @author hangum
 *
 */
public class EditorUtils {

	/**
	 * find sql editor
	 * 
	 * @param dao UserDBDAO
	 * @return
	 */
	public static IEditorReference findSQLEditor(final UserDBDAO dao) {
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		
		for (IEditorReference iEditorReference : editors) {
			
			//  에디터를 검색한다.
			if(MainEditor.ID.equals( iEditorReference.getId() )) {
				
				MainEditor editor = (MainEditor)iEditorReference.getEditor(false);
				if(editor.getUserDB().getSeq() == dao.getSeq()) {
					return iEditorReference;
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
					
					if(editor.getdBResource().getSeq() == dao.getSeq()) return iEditorReference;
				} else if(ier instanceof TadpoleRDBEditor) {
					TadpoleRDBEditor editor = (TadpoleRDBEditor)iEditorReference.getEditor(true);
					if(editor.getUserDBErd() == null) continue;
					
					if(editor.getUserDBErd().getSeq() == dao.getSeq()) return iEditorReference;
				}
			}
		}
		
		return null;
	}
}
