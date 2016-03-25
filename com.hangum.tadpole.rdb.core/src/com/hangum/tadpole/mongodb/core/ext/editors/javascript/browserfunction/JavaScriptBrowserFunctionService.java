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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript.browserfunction;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.dialogs.help.MongoDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class JavaScriptBrowserFunctionService extends EditorFunctionService {
	private static final Logger logger = Logger.getLogger(JavaScriptBrowserFunctionService.class);
        
	private ServerSideJavaScriptEditor editor;

	public JavaScriptBrowserFunctionService(Browser browser, String name, ServerSideJavaScriptEditor editor) {
		super(browser, name, editor);
		this.editor = editor;
	}

	/**
	 * help popup
	 */
	protected void helpPopup() {
		MongoDBShortcutHelpDialog dialog = new MongoDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
	}

	@Override
	protected Object doSave(Object[] arguments) {
		boolean result = false;
		try {
			String newContents = (String) arguments[1];
			result = editor.performSave(newContents);
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
		if(logger.isDebugEnabled()) logger.debug("======= doExecuteQuery*** === ***");
	}

	@Override
	protected void doExecutePlan(Object[] arguments) {
		
	}

	@Override
	protected String doExecuteFormat(Object[] arguments) {
		return "";
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService#f4DMLOpen(java.lang.Object[])
	 */
	@Override
	protected void f4DMLOpen(Object[] argument) {
		if(logger.isDebugEnabled()) logger.debug("======= f4DMLOpen*** === ***");
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService#generateSelect(java.lang.Object[])
	 */
	@Override
	protected void generateSelect(Object[] argument) {
		if(logger.isDebugEnabled()) logger.debug("======= generateSelect*** === ***");
	}

	@Override
	protected String getContentAssist(Object[] arguments) {
		return "";
	}

	@Override
	protected Object doAutoSave(Object[] arguments) {
		return null;
	}
}
