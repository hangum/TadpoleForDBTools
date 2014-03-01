///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.editor.core.rdb.texteditor.function;
//
//import org.apache.log4j.Logger;
//import org.eclipse.swt.browser.Browser;
//import org.eclipse.swt.browser.BrowserFunction;
//
//import com.hangum.tadpole.editor.core.rdb.texteditor.EditorExtension;
//
///**
// * query editor browser function
// * 
// * @author hangum
// *
// */
//public abstract class EditorBrowserFunctionService extends BrowserFunction implements IEditorBrowserFunction {
//	private static final Logger logger = Logger.getLogger(EditorBrowserFunctionService.class);
//	protected EditorExtension editor;
//
//	public EditorBrowserFunctionService(Browser browser, String name, EditorExtension editor) {
//		super(browser, name);
//		this.editor = editor;
//	}
//	
//	@Override
//	public Object function(Object[] arguments) {
//		
//		int action = Integer.parseInt(arguments[0].toString());
//		
//		switch (action) {
//			case DIRTY_CHANGED:
//				return doDirtyChanged(arguments);
//
//			case GET_INITIAL_CONTENT:
//				return doGetInitialContent(arguments);
//
//			case SAVE:
//				return doSave(arguments);
//				
//			case SAVE_S:
//				return doSaveS(arguments);
//			
//			case EXECUTE_QUERY:
//				doExecuteQuery(arguments);
//				break;
//				
//			case EXECUTE_ALL_QUERY:
//				doExecuteAllQuery(arguments);
//				break;
//				
//			case EXECUTE_PLAN:
//				doExecutePlan(arguments);
//				break;
//				
//			case EXECUTE_FORMAT:
//				return doExecuteFormat(arguments);
//				
//			case APPEND_QUERY_TEXT:
//				return appendQueryText(arguments);
//				
//			case APPEND_QUERY_TEXT_AT_POSITION:
//				return appendQueryText(arguments);
//				
//			case SQL_TO_APPLICATION:
//				sqlToApplication(arguments);
//				break;
//				
//			case DOWNLOAD_SQL:
//				downloadSQL(arguments);
//				break;
//				
//			case MOVE_HISTORY_PAGE:
//				moveHistoryPage();
//				break;
//				
//			case HELP_POPUP:
//				helpPopup();
//				break;
//				
//			default:
//				return null;
//		}
//		
//		return null;
//	}
//	
//	/**
//	 * editor initialize
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract Object doGetInitialContent(Object[] arguments);
//	
//	/**
//	 * short cut save
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract Object doSave(Object[] arguments);
//
//	/**
//	 * short cut save called 
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract Object doSaveS(Object[] arguments);
//
//	/**
//	 * ediror dirty change event
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract Object doDirtyChanged(Object[] arguments);
//
//	/**
//	 * execute query
//	 * 
//	 * @param arguments
//	 */
//	protected abstract void doExecuteQuery(Object[] arguments);
//
//	/**
//	 * execute all query
//	 * 
//	 * @param arguments
//	 */
//	protected abstract void doExecuteAllQuery(Object[] arguments);
//	
//	/**
//	 * execute plan
//	 * 
//	 * @param arguments
//	 */
//	protected abstract void doExecutePlan(Object[] arguments);
//	
//	/**
//	 * query format
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract String doExecuteFormat(Object[] arguments);
//	
//	/**
//	 * append query text
//	 * 
//	 * @param arguments
//	 * @return
//	 */
//	protected abstract  String appendQueryText(Object[] arguments);
//	
//	/**
//	 * sql to application text
//	 * 
//	 * @param arguments
//	 */
//	protected abstract void sqlToApplication(Object[] arguments);
//
//	/**
//	 * download sql
//	 * @param arguments
//	 */
//	protected abstract void downloadSQL(Object[] arguments);
//	
//	/**
//	 * 쿼리 히스토리 페이지로 이동합니다.
//	 */
//	protected abstract void moveHistoryPage();
//	
//	/**
//	 * help popup
//	 */
//	protected abstract  void helpPopup();
//}
