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

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.editors.main.browserfunction.EditorBrowserFunctionService;

/**
 * 쿼리 생성관련 유틸입니다.
 * 
 * @author hangum
 *
 */
public class FindEditorAndWriteQueryUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindEditorAndWriteQueryUtil.class);

	/**
	 * 쿼리 스트링으로 엽니다.
	 * 
	 * @param userDB
	 * @param str
	 */
	public static void run(UserDBDAO userDB, String str) {
		IEditorReference reference = EditorUtils.findSQLEditor(userDB);
		
		if(reference == null) {
			
			try {
				MainEditorInput mei = new MainEditorInput(userDB, str);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, MainEditor.ID, false);
			} catch (PartInitException e) {
				logger.error("new editor open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
			
		} else {
			
			try {
				MainEditor editor = (MainEditor)reference.getEditor(false);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editor.getEditorInput(), MainEditor.ID, false);
				
				editor.setAppendQueryText(str); //$NON-NLS-1$
				editor.browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_APPEND_QUERY_TEXT);
				
			} catch (Exception e) {
				logger.error("find editor open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
			
		}
	}
	
}
