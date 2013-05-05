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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.editor.core.rdb.texteditor.function.EditorBrowserFunctionService;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.sql.format.SQLFormater;

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
	 * @param lowSQL
	 */
	public static void run(UserDBDAO userDB, String lowSQL) {
		
		if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
			try {
				ServerSideJavaScriptEditorInput input = new ServerSideJavaScriptEditorInput(userDB);
				page.openEditor(input, ServerSideJavaScriptEditor.ID, false);
				
			} catch (PartInitException e) {
				logger.error("Mongodb javascirpt", e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "GridFS Open Exception", errStatus); //$NON-NLS-1$
			}
		} else {
			IEditorReference reference = EditorUtils.findSQLEditor(userDB);
			
//			포멧팅 한것이 잘 보이지 않아서 우선 블럭 해 놓습니다.
//			try {
//				lowSQL = SQLFormater.format(lowSQL);
//			} catch(Exception e) {}
			
			if(reference == null) {				
				try {
					MainEditorInput mei = new MainEditorInput(userDB, lowSQL);
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
					
					editor.setAppendQueryText(lowSQL); //$NON-NLS-1$
					editor.browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_APPEND_QUERY_TEXT);
					
				} catch (Exception e) {
					logger.error("find editor open", e); //$NON-NLS-1$
					
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
				}
				
			}	// end reference
		}	// end db
	}	// end method
	
}
