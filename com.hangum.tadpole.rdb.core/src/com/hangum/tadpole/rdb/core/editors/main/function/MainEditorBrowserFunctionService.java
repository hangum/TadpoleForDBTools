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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine.EXECUTE_TYPE;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.rdb.core.dialog.dml.GenerateStatmentDMLDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.sql.format.SQLFormater;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class MainEditorBrowserFunctionService extends EditorFunctionService {
	private static final Logger logger = Logger.getLogger(MainEditorBrowserFunctionService.class);
	protected MainEditor editor;

	public MainEditorBrowserFunctionService(Browser browser, String name, MainEditor editor) {
		super(browser, name, editor);
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
	protected void doDirtyChanged(Object[] arguments) {
		editor.setDirty(true);
	}
	
	@Override
	protected void doExecuteQuery(Object[] arguments) {
		String strSQL = (String) arguments[1];
		EditorDefine.EXECUTE_TYPE exeType = EXECUTE_TYPE.NONE;
		if((Boolean) arguments[2]) exeType = EXECUTE_TYPE.BLOCK;
		
		RequestQuery rq = new RequestQuery(strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.QUERY, exeType, editor.isAutoCommit());
		editor.executeCommand(rq);
	}
	
	/**
	 * execute plan
	 */
	@Override
	protected void doExecutePlan(Object[] arguments) {
		String strSQL = (String) arguments[1];
		RequestQuery rq = new RequestQuery(strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, editor.isAutoCommit());
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
		String strObject = StringUtils.remove((String) arguments[1], ",");
		strObject = StringUtils.remove(strObject, "(");
		strObject = StringUtils.remove(strObject, ")");
		
		if(StringUtils.contains(strObject, ".")) strObject = StringUtils.substringAfter(strObject, ".");
		
		if(logger.isDebugEnabled()) logger.debug("select editor content is '" + strObject + "'");
		
		try {
			TableDAO tableDao = null;
			List<TableDAO> listTable = editor.getUserDB().getListTable();
			if(listTable.isEmpty()) { 
				if(DBDefine.POSTGRE_DEFAULT != editor.getUserDB().getDBDefine()) { 
					tableDao = TadpoleObjectQuery.getTable(editor.getUserDB(), StringUtils.trim(strObject));
				} else {
					tableDao = new TableDAO(strObject, "");
				}
			} else {
				for (TableDAO tmpTableDAO : listTable) {
					if(strObject.equals(tmpTableDAO.getName())) {
						tableDao = tmpTableDAO;
					}
				}
			}
			
			if(tableDao != null) {
				GenerateStatmentDMLDialog dialog = new GenerateStatmentDMLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, 
									editor.getUserDB(), tableDao);
				dialog.open();
			}
			
		} catch (Exception e) {
			logger.error("f4 function", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService#generateSelect(java.lang.Object[])
	 */
	@Override
	protected void generateSelect(Object[] arguments) {
		String strObject = StringUtils.remove((String) arguments[1], ",");
		strObject = StringUtils.remove(strObject, "(");
		strObject = StringUtils.remove(strObject, ")");
		
		String strSQL = "select * from " + strObject;
		EditorDefine.EXECUTE_TYPE exeType = EXECUTE_TYPE.NONE;
		exeType = EXECUTE_TYPE.BLOCK;
		
		RequestQuery rq = new RequestQuery(strSQL, editor.getDbAction(), EditorDefine.QUERY_MODE.QUERY, exeType, editor.isAutoCommit());
		editor.executeCommand(rq);
	}
	

}
