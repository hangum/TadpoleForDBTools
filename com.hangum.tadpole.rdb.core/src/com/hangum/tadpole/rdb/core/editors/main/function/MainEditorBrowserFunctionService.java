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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.editor.core.rdb.texteditor.function.EditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.dialog.export.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.sql.format.SQLFormater;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class MainEditorBrowserFunctionService extends EditorBrowserFunctionService {
	private static final Logger logger = Logger.getLogger(MainEditorBrowserFunctionService.class);
	protected MainEditor editor;

	public MainEditorBrowserFunctionService(Browser browser, String name, MainEditor editor) {
		super(browser, name, editor);
		this.editor = editor;
	}
	
	/**
	 * editor initialize
	 */
	@Override
	protected Object doGetInitialContent(Object[] arguments) {
		return editor.getInitExt() + ":ext:" + editor.getOrionText();
	}
	
	@Override
	protected Object doSave(Object[] arguments) {
		boolean result = false;
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			result = editor.performSave(newContents);
		}
		
		return result;
	}
	
	@Override
	protected Object doSaveS(Object[] arguments) {
		boolean result = false;
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			result = editor.performSave(newContents);
		}
		
		return result;
	}

	@Override
	protected Object doDirtyChanged(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof Boolean)) {
			editor.setDirty((Boolean) arguments[1]);
		}
		
		return editor.isDirty();
	}
	
	@Override
	protected void doExecuteQuery(Object[] arguments) {
		
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(new Integer(queryStruct[0]));
			
			editor.executeCommand(PublicTadpoleDefine.QUERY_MODE.DEFAULT);
		}
	}
	
	@Override
	protected void doExecuteAllQuery(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(MainEditor.ALL_QUERY_EXECUTE);
			
			editor.executeCommand(PublicTadpoleDefine.QUERY_MODE.DEFAULT);
		}
	}
	
	/**
	 * execute plan
	 */
	@Override
	protected void doExecutePlan(Object[] arguments) {
		
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(new Integer(queryStruct[0]));
			
			editor.executeCommand(PublicTadpoleDefine.QUERY_MODE.EXPLAIN_PLAN);
		}
	}
	
	/**
	 * SQL formatting
	 */
	@Override
	protected String doExecuteFormat(Object[] arguments) {
		String newContents = (String) arguments[1];
		
		try {
			newContents = SQLFormater.format(newContents );
			
			return newContents;						
		} catch (Exception e) {
			logger.error("sql format", e);
		}
		
		return newContents;
	}
	
	/**
	 * append sql text
	 * 
	 * @param arguments
	 * @return
	 */
	@Override
	protected String appendQueryText(Object[] arguments) {
		return editor.getAppendQueryText();
	}
	
	/**
	 * SQL to Application(java or php)
	 */
	@Override
	protected void sqlToApplication(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			// dialog open
			SQLToStringDialog dialog = new SQLToStringDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), PublicTadpoleDefine.SQL_TO_APPLICATION.Java_StringBuffer.toString(), queryStruct[1]);
			dialog.open();
			
		}
	}

	/**
	 * download sql
	 * @param arguments
	 */
	@Override
	protected void downloadSQL(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.downloadExtFile(editor.getUserDB().getDisplay_name()+".sql", queryStruct[1]);
		}
	}
	
	/**
	 * 쿼리 히스토리 페이지로 이동합니다.
	 */
	@Override
	protected void moveHistoryPage() {
		editor.selectHistoryPage();
	}
	
	/**
	 * help popup
	 */
	@Override
	protected void helpPopup() {
		RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
	}

}
