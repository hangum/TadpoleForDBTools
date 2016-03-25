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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.dialogs.help.MongoDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.browserfunction.JavaScriptBrowserFunctionService;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.dialog.EvalInputDialog;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.util.FindTadpoleViewerOrEditor;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;

/**
 * MongoDB ServerSide Java Script editor
 * 
 * @author hangum
 *
 */
public class ServerSideJavaScriptEditor extends EditorExtension {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServerSideJavaScriptEditor.class);
	public static final String ID = "com.hangum.tadpole.mongodb.core.ext.editor.javascript";
	
	private UserDBDAO userDB;
	private MongoDBServerSideJavaScriptDAO javascriptDAO;
	
	/** save field */
	private boolean isFirstLoad = false;
	private boolean isDirty = false;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** content download를 위한 더미 composite */
    private Composite compositeDumy;
	private String save_id;
	private Text textResultJavaScript;
	private CTabFolder tabFolder;
	
	public ServerSideJavaScriptEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ToolBar toolBar = new ToolBar(compositeBody, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// Shortcut prefix
		String prefixOSShortcut = ShortcutPrefixUtils.getCtrlShortcut();
		
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png"));
		tltmConnectURL.setToolTipText("Connection Info"); //$NON-NLS-1$
		if(PermissionChecker.isShow(userDB.getRole_id())) {
			tltmConnectURL.setText("Connect [ " +  userDB.getHost() + ":" + userDB.getDb() + " ]"); //$NON-NLS-1$
		} else {
			tltmConnectURL.setText("Connect Information"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
				
				setFocus();
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.get().MainEditor_tltmExecute_toolTipText_1, prefixOSShortcut));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mongodb/mongo-executable.png"));
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String strContent = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					executeEval(strContent);
				} catch(Exception ee) {
					logger.error("Execute JS", ee);
				}
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png"));
		tltmDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!MessageDialog.openConfirm(null, "Conforim", "Do you want SQL Download?")) return;
				
				try {
					String strJSContent = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					downloadJavaScript(getUserDB().getDisplay_name() + ".js",  strJSContent);
				} catch(Exception ee) {
					logger.error("Download JS", ee);
				}
			}
		});
		tltmDownload.setToolTipText("Download JavaScript"); //$NON-NLS-1$
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png"));
		tltmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MongoDBShortcutHelpDialog dialog = new MongoDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
				dialog.open();
				
				setFocus();
			}
		});
		tltmHelp.setToolTipText("Editor Shortcut Help"); //$NON-NLS-1$
		
		browserQueryEditor = new Browser(compositeBody, SWT.BORDER);
		browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		addBrowserService();
		
		Composite compositeTail = new Composite(sashForm, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeTail = new GridLayout(1, false);
		gl_compositeTail.verticalSpacing = 1;
		gl_compositeTail.horizontalSpacing = 1;
		gl_compositeTail.marginHeight = 1;
		gl_compositeTail.marginWidth = 1;
		compositeTail.setLayout(gl_compositeTail);
		
		tabFolder = new CTabFolder(compositeTail, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmEvalJavaScript = new CTabItem(tabFolder, SWT.NONE);
		tbtmEvalJavaScript.setText(Messages.get().ServerSideJavaScriptEditor_tbtmEvalJavaScript_text_1);
		
		Composite compositeTabJS = new Composite(tabFolder, SWT.NONE);
		tbtmEvalJavaScript.setControl(compositeTabJS);
		GridLayout gl_compositeTabJS = new GridLayout(1, false);
		gl_compositeTabJS.verticalSpacing = 1;
		gl_compositeTabJS.horizontalSpacing = 1;
		gl_compositeTabJS.marginHeight = 1;
		gl_compositeTabJS.marginWidth = 1;
		compositeTabJS.setLayout(gl_compositeTabJS);

		textResultJavaScript = new Text(compositeTabJS, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textResultJavaScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeDumy = new Composite(compositeTabJS, SWT.NONE);
		compositeDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDumy.setLayout(new GridLayout(1, false));
	
		sashForm.setWeights(new int[] {7, 3});
		
		initEditor();
	}
	
	/**
	 * 데이터 초기화 합니다.
	 */
	private void initEditor() {
		registerServiceHandler();
		tabFolder.setSelection(0);		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		if(javascriptDAO == null) {
			save_id = getJSName();
		}
	
		if(!"".equals(save_id)) {
			try {
//				Object resultObj = browserQueryEditor.evaluate(EditorFunctionService.JAVA_SCRIPT_SAVE_FUNCTION);
//				if(!(resultObj instanceof Boolean && (Boolean) resultObj)) {
//					monitor.setCanceled(true);
//				}
			} catch(SWTException e) {
				logger.error(RequestInfoUtils.requestInfo("doSave exception", SessionManager.getEMAIL()), e); //$NON-NLS-1$
				monitor.setCanceled(true);
			}
		}		
	}
	
	/**
	 * new java script name
	 * 
	 * @return
	 */
	private String getJSName() {
		MongoJSNameValidator fv = new MongoJSNameValidator(userDB);
		
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Save", "Java Script Name", userDB.getDisplay_name(), fv); //$NON-NLS-1$
		if (dlg.open() == Window.OK) {
			return fv.getFileName();
		} else {
			return "";
		}
	}
	
	/**
	 * 실제 저장
	 * @param newContents
	 * @return
	 */
	public boolean performSave(String newContents) {
		if(javascriptDAO == null) {
			save_id = getJSName();
	
			if(!"".equals(save_id)) {
				MongoDBServerSideJavaScriptDAO javaScriptDAO = new MongoDBServerSideJavaScriptDAO(save_id, newContents);
				try {
					MongoDBQuery.insertJavaScript(userDB, javaScriptDAO);
					
					setPartName(save_id);
					setDirty(false);
					
					// explorer refresh합니다.
					referExplorer();
				} catch(Exception e) {
					logger.error("save javascript", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
					
					return false;
				}
			}
		} else {
			try {
				MongoDBQuery.updateJavaScript(userDB, javascriptDAO.getName(), newContents);
				setDirty(false);
				
				// explorer refresh합니다.
				referExplorer();
			} catch(Exception e) {
				logger.error("save javascript", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
				
				return false;
			}
		}
		return true;
	}
	
	/**
	 * refresh explorer
	 * 
	 */
	private void referExplorer() {
		// explorer refresh합니다.
		ExplorerViewer ev = FindTadpoleViewerOrEditor.getExplorerView(userDB);
		if(ev != null) ev.refreshJS(true, "");
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		ServerSideJavaScriptEditorInput qei = (ServerSideJavaScriptEditorInput)input;
		this.userDB = qei.getUserDB();
		this.javascriptDAO = qei.getJavascriptDAO();
		if(this.javascriptDAO == null) {
			setPartName(qei.getName());
		} else {
			setPartName(javascriptDAO.getName());
			isFirstLoad = true;
		} 
	}
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/**
	 * browser initialize 
	 */
	private void addBrowserService() {
		browserQueryEditor.setUrl(DEV_DB_URL);

		final String varTheme 		= PublicTadpoleDefine.getMapTheme().get(GetPreferenceGeneral.getEditorTheme());
	    final String varFontSize 	= GetPreferenceGeneral.getEditorFontSize();
	    final String varIsWrap 		= ""+GetPreferenceGeneral.getEditorIsWarp();
	    final String varWarpLimit 	= GetPreferenceGeneral.getEditorWarpLimitValue();
	    final String varIsShowGutter = ""+GetPreferenceGeneral.getEditorShowGutter();
		
		registerBrowserFunctions();
		browserQueryEditor.addProgressListener( new ProgressListener() {
			public void completed( ProgressEvent event ) {
				browserEvaluate(IEditorFunction.MONGO_INITIALIZE, 
						EditorDefine.EXT_JAVASCRIPT, getInputJavaScriptContent(),
						varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter
				);
			}
			public void changed( ProgressEvent event ) {}
		});
	}
	
	/** 
	 * browser function call
	 * 
	 *  @param command browser
	 */
	public void browserEvaluate(String command) {
		try {
			browserQueryEditor.evaluate(command);
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", SessionManager.getEMAIL()), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * register browser function
	 */
	protected void registerBrowserFunctions() {
		editorService = new JavaScriptBrowserFunctionService(browserQueryEditor, EditorFunctionService.EDITOR_SERVICE_HANDLER, this);
	}
	
	@Override
	public void setFocus() {
		setOrionTextFocus();
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}
	
	/**
	 * init javascript name
	 * @return
	 */
	public String getInputJavaScriptName() {
		if(javascriptDAO == null) return "";
		return javascriptDAO.getName();
	}

	/**
	 * init javascript content
	 * @return
	 */
	public String getInputJavaScriptContent() {
		if(javascriptDAO == null) return "";
		return javascriptDAO.getContent();
	}

	/**
	 * download javascript
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadJavaScript(String fileName, String newContents) {
		downloadServiceHandler.setName(fileName); //$NON-NLS-1$
		downloadServiceHandler.setByteContent(newContents.getBytes());
		
		DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}

	/**
	 * save property change
	 * @param boolean1
	 */
	public void setDirty(Boolean boolean1) {
		if(!isFirstLoad) {
			if(isDirty != boolean1) {
				isDirty = boolean1;
				firePropertyChange(PROP_DIRTY);
			}
		}		
		
		isFirstLoad = false;
	}

	/**
	 * execute javascript eval 
	 * 
	 * @param strJavaScript
	 */
	private void executeEval(String strJavaScript) {
		Object[] arryArgs = null;//{25, 34};
		logger.debug("[original javascript]" + strJavaScript);
		
		// argument 갯수를 입력받기 
		String strArgument = StringUtils.substringBetween(strJavaScript, "function", "{");
		if(strArgument != null) {
			int intArgumentCount = StringUtils.countMatches(strArgument, ",")+1;
			EvalInputDialog dialog = new EvalInputDialog(getSite().getShell(), intArgumentCount);
			if(Dialog.OK == dialog.open()) {
				arryArgs = dialog.getInputObject();
				
				try {
					Object objResult = MongoDBQuery.executeEval(getUserDB(), strJavaScript, arryArgs);			
					textResultJavaScript.setText(objResult == null?"":objResult.toString());
				} catch (Exception e) {
					textResultJavaScript.setText("");
					logger.error("execute javascript", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
				}
			}
		}
				
	}
}
