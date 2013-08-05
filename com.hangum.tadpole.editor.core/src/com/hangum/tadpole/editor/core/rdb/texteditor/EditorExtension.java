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
package com.hangum.tadpole.editor.core.rdb.texteditor;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.editor.core.IEditorExtension;
import com.hangum.tadpole.editor.core.rdb.texteditor.function.EditorBrowserFunctionService;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.util.RequestInfoUtils;

/**
 * Editor의 확장을 정의한다.
 * 
 * @author hangum
 *
 */
public abstract class EditorExtension extends EditorPart implements IEditorExtension {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EditorExtension.class);

	///[browser editor]/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Browser browserQueryEditor;
	/** browser.browserFunction의 서비스 헨들러 */
	protected EditorBrowserFunctionService editorService;
	
//	protected static final String DEV_DB_URL = "orion/tadpole/editor/RDBEmbeddededitor.html"; //$NON-NLS-1$
//	protected static final String REAL_DB_URL = "orion/tadpole/editor/REAL_RDBEmbeddededitor.html"; //$NON-NLS-1$

	/** session에서 사용자 정보를 가져다 놓습니다.
	 * No context available outside of the request service lifecycle.
	 */
	protected final String strUserEMail = SessionManager.getEMAIL();
	protected String strRoleType = "";
	protected final int user_seq = SessionManager.getSeq();
	
	/** 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */
	protected int queryResultCount 	= GetPreferenceGeneral.getQueryResultCount();
	/** 쿼리 결과를 page당 처리 하는 카운트 */
	protected int queryPageCount 		= GetPreferenceGeneral.getPageCount();
	/** oracle plan table 이름 */
	protected String planTableName 	= GetPreferenceGeneral.getPlanTableName();
	/** export delimit */
	protected String exportDelimit = GetPreferenceGeneral.getExportDelimit().toLowerCase().equals("tab")?"	":GetPreferenceGeneral.getExportDelimit(); //$NON-NLS-1$ //$NON-NLS-2$
	/** 결과 컬럼이 숫자이면 ,를 찍을 것인지 */
	protected boolean isResultComma = GetPreferenceGeneral.getISRDBNumberIsComma();
	
	/** 에디터의 모든 쿼리를 수행합니다. */
	public static int ALL_QUERY_EXECUTE 	= -998;
	/** 부분 쿼리 블럭을 수행합니다 */
	public static int BLOCK_QUERY_EXECUTE 	= -999;

    /** 에디터의 텍스트 */
    protected String queryText = ""; //$NON-NLS-1$
    /** 에디터의 커서 포인트 */
    protected int queryEditorCursorPoistion = 0;
    /** query append 텍스트 */
    protected String appendQueryText = ""; //$NON-NLS-1$
	
	/** 현재 에디터에서 처리해야하는 디비 정보. */
	protected UserDBDAO userDB;
	
	public EditorExtension() {
		super();
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	/** 
	 * browser function call
	 * 
	 *  @param command brower command
	 */
	public void browserEvaluate(String command) {
		try {
			browserQueryEditor.evaluate(command);
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", strUserEMail), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * orion text setfocus
	 */
	protected void setOrionTextFocus() {
		try {
			browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SET_FOCUS_FUNCTION);
		} catch(Exception e) {
			// ignore exception
		}
	}
	
	/**
	 * 사용자 role_type을 리턴한다.
	 * 
	 * @return
	 */
	protected String getUserType() {
		return strRoleType;
	}
	
//	/**
//	 * browser handler
//	 */
//	protected void addBrowserService() {
//		if(DBOperationType.valueOf(userDB.getOperation_type()) == DBOperationType.REAL) {
//	    	browserQueryEditor.setUrl(REAL_DB_URL);
//	    } else {
//	    	browserQueryEditor.setUrl(DEV_DB_URL);
//	    }
//		
//		registerBrowserFunctions();
//		
//		browserQueryEditor.addProgressListener(new ProgressListener() {
//			public void completed( ProgressEvent event ) {
//				try {
//					browserEvaluate(IEditorBrowserFunction.JAVA_SCRIPT_GET_INITCONTAINER);
//				} catch(Exception e) {
//					logger.error("Error add browser function", e);
//				}
//			}
//			public void changed( ProgressEvent event ) {}			
//		});
//	}
	
	/**
	 * register browser function
	 */
	protected abstract void registerBrowserFunctions();
	
	/**
	 * unregister browser function
	 */
	private void unregisterBrowserFunctions() {
		if(editorService != null && editorService instanceof BrowserFunction) {
			editorService.dispose();
		}
	}
	
	/////[query 추가]/////////////////////////////////////////////////////////////////////////////
	public String getAppendQueryText() {
		return appendQueryText;
	}
	public void setAppendQueryText(String appendQueryText) {
		this.appendQueryText = appendQueryText;
	}
	//////////////////////////////////////////////////////////////////////////////////	
	
	@Override
	public void dispose() {
		unregisterBrowserFunctions();
		super.dispose();
	}

}
