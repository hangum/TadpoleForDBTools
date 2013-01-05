/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.browserfunction.JavaScriptBrowserFunctionService;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.editor.RDBShortcutHelpDialog;
import com.hangum.tadpole.rdb.core.editors.main.browserfunction.EditorBrowserFunctionService;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.util.RequestInfoUtils;
import com.hangum.tadpole.util.download.DownloadServiceHandler;
import com.hangum.tadpole.util.download.DownloadUtils;
import com.swtdesigner.ResourceManager;

/**
 * MongoDB ServerSide Java Script editor
 * 
 * @author hangum
 *
 */
public class ServerSideJavaScriptEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServerSideJavaScriptEditor.class);
	public static final String ID = "com.hangum.tadpole.mongodb.core.ext.editor.javascript";
	
	private UserDBDAO userDB;
	private MongoDBServerSideJavaScriptDAO javascriptDAO;

	private static final String URL = "orion/tadpole/editor/mongoDBEmbeddededitor.html"; //$NON-NLS-1$
	private Browser browserQueryEditor;
	/** browser.browserFunction의 서비스 헨들러 */
	private JavaScriptBrowserFunctionService editorService;
	private Table tableUserJavascript;
	private Table tableTadpoleMessage;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** content download를 위한 더미 composite */
    private Composite compositeDumy;
	
	public ServerSideJavaScriptEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
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
		if(this.javascriptDAO != null) {
			setPartName(this.javascriptDAO.getName());
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
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
		
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png"));
		tltmConnectURL.setToolTipText("Connection Info"); //$NON-NLS-1$
		tltmConnectURL.setText("Connect [ " +  userDB.getHost() + ":" + userDB.getDb() + " ]"); //$NON-NLS-1$		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(Messages.MainEditor_tltmExecute_toolTipText_1);
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mongodb/mongo-executable.png"));
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_QUERY_FUNCTION);
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
//		ToolItem tltmSort = new ToolItem(toolBar, SWT.NONE);
//		tltmSort.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/query_format.png"));
//		tltmSort.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_FORMAT_FUNCTION);
//			}
//		});
//		tltmSort.setToolTipText(Messages.MainEditor_4);
//		
//		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png"));
		tltmDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_DOWNLOAD_SQL);
			}
		});
		tltmDownload.setToolTipText("Download SQL"); //$NON-NLS-1$
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png"));
		tltmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
				dialog.open();
			}
		});
		tltmHelp.setToolTipText("Editor Shortcut Help"); //$NON-NLS-1$
		
		browserQueryEditor = new Browser(compositeBody, SWT.NONE);
		browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browserQueryEditor.setUrl(URL);
		
		Composite composite = new Composite(compositeBody, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		addBrowserHandler();
		
		Composite compositeTail = new Composite(sashForm, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTail.setLayout(new GridLayout(1, false));
		
		CTabFolder tabFolder = new CTabFolder(compositeTail, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmResult = new CTabItem(tabFolder, SWT.NONE);
		tbtmResult.setText(Messages.ServerSideJavaScriptEditor_tbtmResult_text_1);
		
		TreeViewer treeViewerResult = new TreeViewer(tabFolder, SWT.BORDER);
		Tree tree = treeViewerResult.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tbtmResult.setControl(tree);
		
		CTabItem tbtmUserJavascript = new CTabItem(tabFolder, SWT.NONE);
		tbtmUserJavascript.setText(Messages.ServerSideJavaScriptEditor_tbtmUserJavascript_text);
		
		TableViewer tableViewerUserJavaScript = new TableViewer(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tableUserJavascript = tableViewerUserJavaScript.getTable();
		tableUserJavascript.setHeaderVisible(true);
		tableUserJavascript.setLinesVisible(true);
		tbtmUserJavascript.setControl(tableUserJavascript);
		
		CTabItem tbtmTadpoleMessage = new CTabItem(tabFolder, SWT.NONE);
		tbtmTadpoleMessage.setText(Messages.ServerSideJavaScriptEditor_tbtmTadpoleMessage_text);
		
		TableViewer tableViewerTadpoleMessage = new TableViewer(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tableTadpoleMessage = tableViewerTadpoleMessage.getTable();
		tableTadpoleMessage.setHeaderVisible(true);
		tableTadpoleMessage.setLinesVisible(true);
		tbtmTadpoleMessage.setControl(tableTadpoleMessage);

		sashForm.setWeights(new int[] {65, 35});
	}

	/**
	 * browser initialize 
	 */
	private void addBrowserHandler() {
		browserQueryEditor.addProgressListener( new ProgressListener() {
			public void completed( ProgressEvent event ) {
				try {
					registerBrowserFunctions();
					browserEvaluate(JavaScriptBrowserFunctionService.JAVA_SCRIPT_GET_INITCONTAINER);
				} catch(Exception e) {
					logger.error("set register browser function and content initialize", e);
				}
			}
			public void changed( ProgressEvent event ) {}
		});
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
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", SessionManager.getEMAIL()), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * register browser function
	 */
	private void registerBrowserFunctions() {
		editorService = new JavaScriptBrowserFunctionService(browserQueryEditor, JavaScriptBrowserFunctionService.EDITOR_SERVICE_HANDLER, this);
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
	public void setFocus() {
	}
	
	@Override
	public void dispose() {
		unregisterBrowserFunctions();
		unregisterServiceHandler();
		super.dispose();
	}
	
	/**
	 * init javascript name
	 * @return
	 */
	public String getInputJavaScriptName() {
		return javascriptDAO.getName();
	}

	/**
	 * init javascript content
	 * @return
	 */
	public String getInputJavaScriptContent() {
		return javascriptDAO.getContent();
	}

	/**
	 * download javascript
	 * 
	 * @param string
	 */
	public void downloadJavaScript(String newContents) {
		downloadServiceHandler.setName(userDB.getDisplay_name() + ".sql"); //$NON-NLS-1$
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

}
