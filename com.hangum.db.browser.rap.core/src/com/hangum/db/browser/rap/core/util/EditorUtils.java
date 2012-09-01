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
package com.hangum.db.browser.rap.core.util;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.editors.main.MainEditor;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.erd.core.editor.TadpoleEditor;

/**
 * editor utils
 * 
 * @author hangum
 *
 */
public class EditorUtils {

	/**
	 * fins sql editor
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
		return findEditor(TadpoleEditor.ID, dao);
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
				MainEditor editor = (MainEditor)iEditorReference.getEditor(true);
				if(editor.getdBResource() == null) continue;
				
				if(editor.getdBResource().getSeq() == dao.getSeq()) {
					return iEditorReference;
				}
			}
		}
		
		return null;
	}
}
