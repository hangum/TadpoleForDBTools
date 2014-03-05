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
package com.hangum.tadpole.ace.editor.core.texteditor.function;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public abstract class EditorFunctionService extends BrowserFunction implements IEditorFunction {
	private static final Logger logger = Logger.getLogger(EditorFunctionService.class);
	protected EditorExtension editor;

	public EditorFunctionService(Browser browser, String name, EditorExtension editor) {
		super(browser, name);
		this.editor = editor;
	}
	
	@Override
	public Object function(Object[] arguments) {
		
		int intActionId =  NumberUtils.toInt(arguments[0].toString());
		
		switch (intActionId) {
			case DIRTY_CHANGED:
//				logger.debug("=#################################################################===Rise dirth change");
				doDirtyChanged(arguments);
				break;

			case SAVE:
				return doSave(arguments);
				
			case EXECUTE_QUERY:
				doExecuteQuery(arguments);
				break;
				
			case EXECUTE_PLAN:
				doExecutePlan(arguments);
				break;
				
			case EXECUTE_FORMAT:
				return doExecuteFormat(arguments);
				
			case HELP_POPUP:
				helpPopup();
				break;
				
			default:
				return null;
		}
		
		return null;
	}
	
	/**
	 * short cut save
	 * 
	 * @param arguments
	 * @return
	 */
	protected abstract Object doSave(Object[] arguments);

	/**
	 * ediror dirty change event
	 * 
	 * @param arguments
	 * @return
	 */
	protected abstract void doDirtyChanged(Object[] arguments);

	/**
	 * execute query
	 * 
	 * @param arguments
	 */
	protected abstract void doExecuteQuery(Object[] arguments);
	
	/**
	 * execute plan
	 * 
	 * @param arguments
	 */
	protected abstract void doExecutePlan(Object[] arguments);
	
	/**
	 * query format
	 * 
	 * @param arguments
	 * @return
	 */
	protected abstract String doExecuteFormat(Object[] arguments);
	
	/**
	 * help popup
	 */
	protected abstract  void helpPopup();
}
