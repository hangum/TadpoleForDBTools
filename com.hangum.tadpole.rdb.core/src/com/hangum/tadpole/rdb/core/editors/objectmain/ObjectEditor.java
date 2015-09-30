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
package com.hangum.tadpole.rdb.core.editors.objectmain;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.fileupload.SingleFileuploadDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.swtdesigner.ResourceManager;

/**
 * Object Editor
 * 
 * 
 * @author hangum
 *
 */
public class ObjectEditor extends MainEditor {
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.main.procedure";
	
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ObjectEditor.class);
	
	public ObjectEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		ObjectEditorInput qei = (ObjectEditorInput)input;
		userDB = qei.getUserDB();
		initDefaultEditorStr = qei.getDefaultStr();
		dbAction = qei.getDbAction();

		strRoleType = userDB.getRole_id();//SessionManager.getRoleType(userDB);
		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else  setPartName(dBResource.getName());
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		// 에디터 확장을 위한 기본 베이스 위젲을 설정합니다.
		sashFormExtension = new SashForm(parent, SWT.NONE);
		sashFormExtension.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		SashForm sashForm = new SashForm(sashFormExtension, SWT.VERTICAL);
		sashForm.setSashWidth(4);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final Composite compositeEditor = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeEditor = new GridLayout(1, false);
		gl_compositeEditor.verticalSpacing = 0;
		gl_compositeEditor.horizontalSpacing = 0;
		gl_compositeEditor.marginHeight = 0;
		gl_compositeEditor.marginWidth = 0;
		compositeEditor.setLayout(gl_compositeEditor);
		
		ToolBar toolBar = new ToolBar(compositeEditor, SWT.NONE | SWT.FLAT | SWT.RIGHT);
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setToolTipText(Messages.MainEditor_37);
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png")); //$NON-NLS-1$
		tltmConnectURL.setText(userDB.getDisplay_name());
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(getSite().getShell(), userDB);
				dialog.open();
				setFocus();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// fileupload 
		ToolItem tltmOpen = new ToolItem(toolBar, SWT.NONE);
		tltmOpen.setToolTipText(Messages.MainEditor_35);
		tltmOpen.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/file-open.png")); //$NON-NLS-1$
		tltmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SingleFileuploadDialog dialog = new SingleFileuploadDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.MainEditor_36);
				if(Dialog.OK == dialog.open()) {
					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrTxtFile()); //$NON-NLS-1$
					appendText(dialog.getStrTxtFile());
				}
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmCompile = new ToolItem(toolBar, SWT.NONE);
		tltmCompile.setToolTipText(String.format("Compile", STR_SHORT_CUT_PREFIX));
		tltmCompile.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/compile.png")); //$NON-NLS-1$
		tltmCompile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, PublicTadpoleDefine.SQL_DELIMITER);
				
				EditorDefine.EXECUTE_TYPE executeType = EditorDefine.EXECUTE_TYPE.NONE;
				if( Boolean.parseBoolean( browserEvaluateToStr(EditorFunctionService.IS_BLOCK_TEXT) ) ) {
					executeType = EditorDefine.EXECUTE_TYPE.BLOCK;
				}
				
				RequestQuery reqQuery = new RequestQuery(strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, executeType, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
//		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
//		tltmExecute.setToolTipText("Execute");
//		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/play.png")); //$NON-NLS-1$
//		tltmExecute.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//
//			}
//		});
//		new ToolItem(toolBar, SWT.SEPARATOR);
//		
//		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
//		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png")); //$NON-NLS-1$
//		tltmHelp.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
//				dialog.open();
//				
//				setFocus();
//			}
//		});
//		tltmHelp.setToolTipText(String.format(Messages.MainEditor_27, STR_SHORT_CUT_PREFIX));
	    ////// tool bar end ///////////////////////////////////////////////////////////////////////////////////
	    
	    ////// orion editor start /////////////////////////////////////////////////////////////////////////////
	    browserQueryEditor = new Browser(compositeEditor, SWT.BORDER);
	    browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));	    
	    
	    addBrowserService();
	    
	    resultMainComposite = new ResultMainComposite(sashForm, SWT.BORDER);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
	    resultMainComposite.setLayout(gl_compositeResult);
	    resultMainComposite.setMainEditor(this);
		
		sashForm.setWeights(new int[] {65, 35});
		initEditor();
	}
	/**
	 * initialize editor
	 */
	private void initEditor() {
		// 과거에 실행했던 쿼리 정보 가져오기.
//		resultMainComposite.initMainComposite();
		
		// google analytic
		AnalyticCaller.track(MainEditor.ID, userDB.getDbms_type());
	}
	
}