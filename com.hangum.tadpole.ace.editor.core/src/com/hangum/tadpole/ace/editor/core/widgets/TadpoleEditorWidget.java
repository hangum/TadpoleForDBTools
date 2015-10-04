/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.ace.editor.core.widgets;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.IEditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.ace.editor.core.utils.EvaluateWidgets;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * tadpole editor 
 * 
 * @author hangum
 *
 */
public class TadpoleEditorWidget extends EvaluateWidgets implements IEditorExtension {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleEditorWidget.class);
	
	private String initExt = "";
	
	/** 초기설정 텍스트 */
	private String initContent = "";
	
	/**
	 * 초기 content assist 
	 */
	private String initAssist = "";
	
	/**
	 * browser function 서비스 핸들러.
	 */
	private TadpoleEditorFunctionService editorService;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param initExt
	 * @param initContent
	 * @param initAssist
	 */
	public TadpoleEditorWidget(Composite parent, int style, String initExt, String initContent, String initAssist) {
		super(parent, style);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		setBackgroundMode(SWT.INHERIT_FORCE);
		
		this.initExt = initExt;
		if(initExt == EditorDefine.EXT_JSON) {
			if(initContent == null || "".equals(initContent)) {
				initContent = EditorDefine.JSON_INITIALIZE_TXT;
			}
		}
		this.initContent = initContent;
		this.initAssist = initAssist;
		
		Composite compositeServerStatus = new Composite(this, SWT.NONE);
		compositeServerStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeServerStatus = new GridLayout(1, false);
		gl_compositeServerStatus.verticalSpacing = 1;
		gl_compositeServerStatus.horizontalSpacing = 1;
		gl_compositeServerStatus.marginHeight = 1;
		gl_compositeServerStatus.marginWidth = 1;
		compositeServerStatus.setLayout(gl_compositeServerStatus);
		
		browserEditor = new Browser(compositeServerStatus, SWT.NONE);
		browserEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		addBrowserHandler();
		
		super.initWidget();
	}
	
	/**
	 * add browser function
	 */
	private void addBrowserHandler() {
		browserEditor.setUrl(DEV_DB_URL);
		registerBrowserFunctions();
		
		final String varTheme 		= PublicTadpoleDefine.getMapTheme().get(GetPreferenceGeneral.getEditorTheme());
	    final String varFontSize 	= GetPreferenceGeneral.getEditorFontSize();
	    final String varIsWrap 		= ""+GetPreferenceGeneral.getEditorIsWarp();
	    final String varWarpLimit 	= GetPreferenceGeneral.getEditorWarpLimitValue();
	    final String varIsShowGutter = ""+GetPreferenceGeneral.getEditorShowGutter();
		
		browserEditor.addProgressListener( new ProgressListener() {
			public void completed( ProgressEvent event ) {
				try {
					getBfUtils().browserEvaluate(IEditorFunction.MONGO_INITIALIZE, 
							initExt, initContent,
							varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter
					);
				} catch(Exception e) {
					logger.error("browser initialize", e);	  
				}
			}
			public void changed( ProgressEvent event ) {}
		 });
	}

	/**
	 * register browser function
	 */
	private void registerBrowserFunctions() {
		editorService = new TadpoleEditorFunctionService(browserEditor, EditorFunctionService.EDITOR_SERVICE_HANDLER, this);
	}

	/**
	 * unregister browser function
	 */
	private void unregisterBrowserFunctions() {
		if(editorService != null && editorService instanceof BrowserFunction) {
			editorService.dispose();
		}
	}
	
	@Override
	public void dispose() {
		unregisterBrowserFunctions();
		
		super.dispose();
	}

	/**
	 * set text
	 * 
	 * @param strDdl
	 */
	public void setText(String strDdl) {
		try {
			getBfUtils().browserEvaluate(EditorFunctionService.RE_NEW_TEXT, strDdl);
		} catch(Exception e) {
			logger.error("setText()", e);
		}
	}

	/**
	 * get Text
	 * @return
	 */
	public String getText() {
		try {
			return getBfUtils().browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
		} catch(Exception e) {
			logger.error("getText()", e);
		}
		return "";
	}
	
//	@Override
//	public boolean setFocus() {
//		browserEvaluate(IEditorFunction.SET_FOCUS);
//		return true;
//	}
	
	public void setBrowserFocus() {
		getBfUtils().browserEvaluate(IEditorFunction.SET_FOCUS);
	}

}
