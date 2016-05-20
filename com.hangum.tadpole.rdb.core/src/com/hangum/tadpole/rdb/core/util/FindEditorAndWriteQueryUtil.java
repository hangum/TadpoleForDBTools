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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.editors.objectmain.ObjectEditor;
import com.hangum.tadpole.rdb.core.editors.objectmain.ObjectEditorInput;

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
	
	public static void run(String strObject) {
		IEditorPart editor = EditorUtils.findSQLEditor();
		if(editor != null) {				
			appendSQLEditorOpen(editor, strObject);				
		}	// end reference
	}
	
	/**
	 * 쿼리 스트링을 에디터로 엽니다.
	 * 
	 * @param userDB
	 * @param strObjectName
	 * @param strScript 
	 * @param isNewEditor 항상 새로운 창으로 엽니다.
	 * @param initAction action이 호출된곳.
	 */
	public static void run(UserDBDAO userDB, String strObjectName, String strScript, boolean isNewEditor, PublicTadpoleDefine.OBJECT_TYPE initAction) {
		
		if(userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
			newMongoDBEditorOpen(userDB, strScript);
		} else {

//			if(isFormating) {
//				try {
//					lowSQL = SQLFormater.format(lowSQL);
//				} catch(Exception e) {
//					// ignore exception 쿼리 파싱을 잘 못하거나 틀리면 exception 나오는데, 걸려줍니다.
//				}
//			}

			/** select, view 등이 아니면 무조 새로운 에디터로 오픈합니다 */
			if(SQLUtil.isSELECTEditor(initAction)) {
				IEditorPart editor = EditorUtils.findSQLEditor(userDB);
				if(editor == null || isNewEditor) {				
					newSQLEditorOpen(userDB, strScript, initAction);		
				} else {
					appendSQLEditorOpen(editor, strScript);				
				}	// end reference
			} else {
				newObjectEditorOpen(userDB, strObjectName, strScript, initAction);
			}
		}	// end db
	}
	

	/**
	 * 쿼리 스트링으로 엽니다.
	 * 
	 * @param userDB
	 * @param lowSQL
	 * @param initAction
	 */
	public static void run(UserDBDAO userDB, String lowSQL, PublicTadpoleDefine.OBJECT_TYPE initAction) {
		run(userDB, "", lowSQL, false, initAction);
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
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "GridFS Open Exception", errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * new window open
	 * 
	 * @param userDB
	 * @param objectName
	 * @param strScript 
	 * @param initAction
	 */
	private static void newObjectEditorOpen(UserDBDAO userDB, String objectName, String strScript, PublicTadpoleDefine.OBJECT_TYPE initAction) {
		try {
			ObjectEditorInput mei = new ObjectEditorInput(userDB, objectName, strScript, initAction);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, ObjectEditor.ID, false);
		} catch (PartInitException e) {
			logger.error("new sql editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().AbstractQueryAction_1, errStatus); //$NON-NLS-1$
		}	
	}
	
	/**
	 * new window open
	 * 
	 * @param userDB
	 * @param lowSQL
	 * @param initAction
	 */
	private static void newSQLEditorOpen(UserDBDAO userDB, String lowSQL, PublicTadpoleDefine.OBJECT_TYPE initAction) {
		try {
			MainEditorInput mei = new MainEditorInput(userDB, lowSQL, initAction);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, MainEditor.ID, false);
		} catch (PartInitException e) {
			logger.error("new object editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().AbstractQueryAction_1, errStatus); //$NON-NLS-1$
		}	
	}
	
	/**
	 * 같은 디비의 에디터가 열려 있을 경우, 기존 에디터에 더한다.
	 * 
	 * @param reference
	 * @param lowSQL
	 */
	private static void appendSQLEditorOpen(IEditorPart editorPart, String lowSQL) {
		try {
			MainEditor editor = (MainEditor)editorPart;
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editor.getEditorInput(), MainEditor.ID, false);
			
			editor.appendText(lowSQL);
		} catch (Exception e) {
			logger.error("find editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().AbstractQueryAction_1, errStatus); //$NON-NLS-1$
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
