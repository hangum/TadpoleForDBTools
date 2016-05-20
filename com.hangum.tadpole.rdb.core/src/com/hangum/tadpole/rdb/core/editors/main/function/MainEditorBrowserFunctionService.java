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
package com.hangum.tadpole.rdb.core.editors.main.function;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine.EXECUTE_TYPE;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.util.DialogUtil;
import com.hangum.tadpole.sql.format.SQLFormater;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class MainEditorBrowserFunctionService extends EditorFunctionService {
	private static final Logger logger = Logger.getLogger(MainEditorBrowserFunctionService.class);
	protected UserDBDAO userDB;
	protected MainEditor editor;

	public MainEditorBrowserFunctionService(UserDBDAO userDB, Browser browser, String name, MainEditor editor) {
		super(browser, name, editor);
		
		this.userDB = userDB;
		this.editor = editor;
	}
	
	@Override
	protected Object doSave(Object[] arguments) {
		boolean result = false;
		try {
			String newContents = (String) arguments[1];
			result = editor.calledDoSave(newContents);
		} catch(Exception e) {
			logger.error("do not save", e);
		}
		
		return result;
	}
	

	@Override
	protected Object doAutoSave(Object[] arguments) {
		boolean result = false;
		try {
			String newContents = (String) arguments[1];
			result = editor.calledDoAutoSave(newContents);
		} catch(Exception e) {
			logger.error("do not save", e);
		}
		
		return result;
	}
	
	@Override
	protected void doDirtyChanged(Object[] arguments) {
		editor.setDirty(true);
	}
	
	@Override
	protected String getContentAssist(Object[] arguments) {
		String strSQL = (String) arguments[1];
		int intPosition = ((Double)arguments[2]).intValue();
		
		return editor.getContentAssist(strSQL, intPosition);
	}
	
	@Override
	protected void doExecuteQuery(Object[] arguments) {
		String strSQL = (String) arguments[1];
		EditorDefine.EXECUTE_TYPE exeType = EXECUTE_TYPE.NONE;
		if((Boolean) arguments[2]) exeType = EXECUTE_TYPE.BLOCK;
		
		RequestQuery rq = new RequestQuery(userDB, strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.QUERY, exeType, editor.isAutoCommit());
		editor.executeCommand(rq);
	}
	
	/**
	 * execute plan
	 */
	@Override
	protected void doExecutePlan(Object[] arguments) {
		String strSQL = (String) arguments[1];
		RequestQuery rq = new RequestQuery(userDB, strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, editor.isAutoCommit());
		editor.executeCommand(rq);
	}
	
	/**
	 * SQL formatting
	 */
	@Override
	protected String doExecuteFormat(Object[] arguments) {
		String newContents = (String) arguments[1];
		
		try {
			return SQLFormater.format(newContents );						
		} catch (Exception e) {
			logger.error("sql format", e);
		}
		
		return newContents;
	}
	
	/**
	 * help popup
	 */
	@Override
	protected void helpPopup() {
		RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
		
		editor.setFocus();
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService#f4DMLOpen(java.lang.Object[])
	 */
	@Override
	protected void f4DMLOpen(Object[] arguments) {
		String strObject = parseLastObject((String) arguments[1]);
		if(StringUtils.contains(strObject, ".")) strObject = StringUtils.substringAfter(strObject, ".");
		if(logger.isDebugEnabled()) logger.debug("select editor content is '" + strObject + "'");
		
		DialogUtil.popupDMLDialog(editor.getUserDB(), strObject);
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService#generateSelect(java.lang.Object[])
	 */
	@Override
	protected void generateSelect(Object[] arguments) {
		String strObject = parseLastObject((String) arguments[1]);
		String strSQL = "select * from " + strObject;
		EditorDefine.EXECUTE_TYPE exeType = EXECUTE_TYPE.NONE;
		exeType = EXECUTE_TYPE.BLOCK;
		
		RequestQuery rq = new RequestQuery(userDB, strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.QUERY, exeType, editor.isAutoCommit());
		editor.executeCommand(rq);
	}

	/**
	 * parse last object 
	 */
	private String parseLastObject(String obj) {
		String strObject = StringUtils.remove((String)obj, ",");
		if(StringUtils.contains(strObject, '(')) {
			strObject = StringUtils.substringBefore(strObject, "(");	
		}
		
		if(StringUtils.contains(strObject, ')')) {
			strObject = StringUtils.substringAfter(strObject, ")");	
		}
		
		return strObject;
	}
}
