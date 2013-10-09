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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

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
	 * 쿼리 스트링을 에디터로 엽니다.
	 * 
	 * @param userDB
	 * @param lowSQL
	 * @param isNewEditor 항상 새로운 창으로 엽니다.
	 * @param isFormating sql formatting
	 */
	public static void run(UserDBDAO userDB, String lowSQL, boolean isNewEditor) {
		
		if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {
			newMongoDBEditorOpen(userDB, lowSQL);
		} else {

//			if(isFormating) {
//				try {
//					lowSQL = SQLFormater.format(lowSQL);
//				} catch(Exception e) {
//					// ignore exception 쿼리 파싱을 잘 못하거나 틀리면 exception 나오는데, 걸려줍니다.
//				}
//			}
						
			IEditorReference reference = EditorUtils.findSQLEditor(userDB);
			if(reference == null || isNewEditor) {				
				newSQLEditorOpen(userDB, lowSQL);		
			} else {
				appendSQLEditorOpen(reference, userDB, lowSQL);				
			}	// end reference
		}	// end db
	}
	

	/**
	 * 쿼리 스트링으로 엽니다.
	 * 
	 * @param userDB
	 * @param lowSQL
	 */
	public static void run(UserDBDAO userDB, String lowSQL) {
		run(userDB, lowSQL, false);
	}
	
	/**
	 * Open mongodB editor
	 * 
	 * @param userDB
	 * @param lowSQL
	 */
	private static void newMongoDBEditorOpen(UserDBDAO userDB, String lowSQL) { 
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		try {
			ServerSideJavaScriptEditorInput input = new ServerSideJavaScriptEditorInput(userDB);
			page.openEditor(input, ServerSideJavaScriptEditor.ID, false);
			
		} catch (PartInitException e) {
			logger.error("Mongodb javascirpt", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "GridFS Open Exception", errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * new window open
	 * 
	 * @param userDB
	 * @param lowSQL
	 */
	private static void newSQLEditorOpen(UserDBDAO userDB, String lowSQL) {
		try {
			MainEditorInput mei = new MainEditorInput(userDB, lowSQL);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, MainEditor.ID, false);
		} catch (PartInitException e) {
			logger.error("new editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
		}	
	}
	
	/**
	 * 같은 디비의 에디터가 열려 있을 경우, 기존 에디터에 더한다.
	 * 
	 * @param reference
	 * @param userDB
	 * @param lowSQL
	 */
	private static void appendSQLEditorOpen(IEditorReference reference, UserDBDAO userDB, String lowSQL) {
		try {
			MainEditor editor = (MainEditor)reference.getEditor(false);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editor.getEditorInput(), MainEditor.ID, false);
			
			editor.appendText(lowSQL);
		} catch (Exception e) {
			logger.error("find editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.AbstractQueryAction_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * 현재 오픈되어 있는 에디터의 커서 포지션에 데이터를 뿌려줍니다.
	 * 만약에 에디터가 없다면 무시합니다.
	 * 
	 * @param userDB
	 * @param strAppendText
	 */
	public static void runAtPosition(String strAppendText) {

		IEditorPart iep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(iep != null) {
			if(iep instanceof MainEditor) {
				MainEditor editor = (MainEditor)iep;
				editor.appendTextAtPosition(strAppendText);
			}
		}
		
	}
	
}
