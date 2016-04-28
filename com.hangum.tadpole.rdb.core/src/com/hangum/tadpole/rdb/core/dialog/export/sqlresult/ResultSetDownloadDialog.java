/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.utils.zip.util.ZipUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.export.SQLExporter;
import com.hangum.tadpole.engine.sql.util.export.XMLExporter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.AbstractExportComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportHTMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportJSONComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportSQLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportTextComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportXMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportHtmlDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportJsonDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportSqlDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportTextDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportXmlDAO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.AbstractResultDetailComposite;
import com.hangum.tadpole.rdb.core.util.EditorUtils;

/**
 * Resultset to download
 * 
 * @author hangum
 */
public class ResultSetDownloadDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResultSetDownloadDialog.class);
	/** 배열이 0부터 시작하므로 시리제로는 5건. */
	private final int PREVIEW_COUNT = 4;
	private final int PREVIEW_ID = IDialogConstants.CLIENT_ID + 1;
	private final int SENDEDITOR_ID = IDialogConstants.CLIENT_ID + 2;
	
	private String defaultTargetName;
	private QueryExecuteResultDTO queryExecuteResultDTO;
	
	private CTabFolder tabFolder;
	private AbstractExportComposite compositeText;
	private AbstractExportComposite compositeHTML;
	private AbstractExportComposite compositeJSON;
	private AbstractExportComposite compositeXML;
	private AbstractExportComposite compositeSQL;
	
	// preview 
	private Text textPreview;
	private boolean isPreview;
	
	protected DownloadServiceHandler downloadServiceHandler;
	private boolean isSendEditor;
	private MainEditor targetEditor;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param queryExecuteResultDTO 
	 * @param strDefTableName 
	 */
	public ResultSetDownloadDialog(Shell parentShell, String strDefTableName, QueryExecuteResultDTO queryExecuteResultDTO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.defaultTargetName = strDefTableName;
		this.queryExecuteResultDTO = queryExecuteResultDTO;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.get().ExportData);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolder = new CTabFolder(sashForm, SWT.NONE);
		tabFolder.setBorderVisible(false);
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		compositeText = new ExportTextComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeHTML = new ExportHTMLComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeJSON = new ExportJSONComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeXML = new ExportXMLComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeSQL = new ExportSQLComposite(tabFolder, SWT.NONE, defaultTargetName, queryExecuteResultDTO.getColumnLabelName());
		compositeText.setLayout(new GridLayout(1, false));
		//--[tail]----------------------------------------------------------------------------------------
		Group groupPreview = new Group(sashForm, SWT.NONE);
		groupPreview.setText(Messages.get().PreviewMsg);
		groupPreview.setLayout(new GridLayout(1, false));
		
		textPreview = new Text(groupPreview, SWT.BORDER | SWT.MULTI);
		textPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//--[start]----------------------------------------------------------------------------------------
		sashForm.setWeights(new int[] {7,3});
		tabFolder.setSelection(0);
		//--[end]----------------------------------------------------------------------------------------
		
		registerServiceHandler();
		
		return container;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == PREVIEW_ID) {
			isPreview = true;
			this.textPreview.setText("");
			okPressed();
		}else if(buttonId == SENDEDITOR_ID) {
			isPreview = false;
			isSendEditor = true;
			
			QueryEditorAction qea = new QueryEditorAction();
			IEditorPart activeEditor = qea.open(queryExecuteResultDTO.getUserDB());
			
			if (activeEditor instanceof MainEditor) {
				targetEditor = (MainEditor) activeEditor;
				targetEditor.setDirty(true); // 저장버튼 활성
				targetEditor.setFocus();
			}
			okPressed();
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, PREVIEW_ID, Messages.get().Preview, true);
		createButton(parent, SENDEDITOR_ID, "Send Editor", true);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Download, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}
	
	@Override
	public boolean close() {
		unregisterServiceHandler();
		return super.close();
	}

	@Override
	protected void okPressed() {
		String selectionTab = ""+tabFolder.getSelection().getData();

		if("text".equalsIgnoreCase(selectionTab)) {			
			if(compositeText.isValidate()) {
				ExportTextDAO dao = (ExportTextDAO)compositeText.getLastData();
				exportResultCSVType( dao.isIsncludeHeader(), dao.getTargetName(), dao.getSeparatorType(), dao.getComboEncoding());
			}else{
				return;
			}
		}else if("html".equalsIgnoreCase(selectionTab)) {			
			if(compositeHTML.isValidate()) {
				ExportHtmlDAO dao = (ExportHtmlDAO)compositeHTML.getLastData();
				exportResultHtmlType(dao.getTargetName(), dao.getComboEncoding());
			}else{
				return;
			}
		}else if("json".equalsIgnoreCase(selectionTab)) {			
			if(compositeJSON.isValidate()) {
				ExportJsonDAO dao = (ExportJsonDAO)compositeJSON.getLastData();
				exportResultJSONType( dao.isIsncludeHeader(), dao.getTargetName(), dao.getSchemeKey(), dao.getRecordKey(), dao.getComboEncoding(), dao.isFormat());
			}else{
				return;
			}
		}else if("xml".equalsIgnoreCase(selectionTab)) {			
			if(compositeXML.isValidate()) {
				ExportXmlDAO dao = (ExportXmlDAO)compositeXML.getLastData();
				exportResultXmlType(dao.getTargetName(), dao.getComboEncoding());
			}else{
				return;
			}
		}else if("sql".equalsIgnoreCase(selectionTab)) {			
			if(compositeSQL.isValidate()) {
				ExportSqlDAO dao = (ExportSqlDAO)compositeSQL.getLastData();
				exportResultSqlType(dao.getTargetName(), dao.getComboEncoding(), dao.getListWhere(),  dao.getStatementType(), dao.getCommit());
			}else{
				return;
			}
		}else{
			if(logger.isDebugEnabled()) logger.debug("selection tab is " + selectionTab);	
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ResultSetDownloadDialog_notSelect); 
			return;
		}
	}

	protected void exportResultCSVType(boolean isAddHead, String targetName, char seprator, String encoding) {
		try {
			if (isPreview) {
				previewDataLoad(targetName, CSVExpoter.makeContent(isAddHead, targetName, queryExecuteResultDTO, seprator, PREVIEW_COUNT), encoding);
			}else if (this.isSendEditor) {
				targetEditor.appendText(CSVExpoter.makeContent(isAddHead, targetName, queryExecuteResultDTO, seprator));
			}else{
				downloadFile(targetName, CSVExpoter.makeCSVFile(isAddHead, targetName, queryExecuteResultDTO, seprator), encoding);
			}
		} catch(Exception ee) {
			logger.error("Text type export error", ee); //$NON-NLS-1$
		}
	}
	
	protected void exportResultHtmlType(String targetName, String encoding) {
		try {
			if (isPreview) {
				previewDataLoad(targetName, HTMLExporter.makeContent(targetName, queryExecuteResultDTO, PREVIEW_COUNT), encoding);
			}else if (isSendEditor) {
				targetEditor.appendText(HTMLExporter.makeContent(targetName, queryExecuteResultDTO));
			}else{
				downloadFile(targetName, HTMLExporter.makeContentFile(targetName, queryExecuteResultDTO), encoding);
			}
		} catch(Exception ee) {
			logger.error("Text type export error", ee); //$NON-NLS-1$
		}
	}
	
	protected void exportResultJSONType(boolean isAddHead, String targetName, String schemeKey, String recordKey, String encoding, boolean isFormat) {
		try {
			if (isAddHead){
				if (isPreview) {
					previewDataLoad(targetName, JsonExpoter.makeContent(targetName, queryExecuteResultDTO, schemeKey, recordKey, isFormat, PREVIEW_COUNT), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(JsonExpoter.makeContent(targetName, queryExecuteResultDTO, schemeKey, recordKey, isFormat, -1));
				}else{
					downloadFile(targetName, JsonExpoter.makeContentFile(targetName, queryExecuteResultDTO, schemeKey, recordKey, isFormat), encoding);
				}
			}else{
				if (isPreview) {
					previewDataLoad(targetName, JsonExpoter.makeContent(targetName, queryExecuteResultDTO, isFormat, PREVIEW_COUNT), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(JsonExpoter.makeContent(targetName, queryExecuteResultDTO, isFormat, -1));
				}else{
					downloadFile(targetName, JsonExpoter.makeContentFile(targetName, queryExecuteResultDTO, isFormat), encoding);
				}
			}
		} catch(Exception ee) {
			logger.error("Text type export error", ee); //$NON-NLS-1$
		}
	}
	
	protected void exportResultXmlType(String targetName, String encoding) {
		try {
			if (isPreview) {
				previewDataLoad(targetName, XMLExporter.makeContent(targetName, queryExecuteResultDTO, PREVIEW_COUNT), encoding);
			}else if (isSendEditor) {
				targetEditor.appendText(XMLExporter.makeContent(targetName, queryExecuteResultDTO));
			}else{
				downloadFile(targetName, XMLExporter.makeContentFile(targetName, queryExecuteResultDTO), encoding);
			}
		} catch(Exception ee) {
			logger.error("Text type export error", ee); //$NON-NLS-1$
		}
	}
	
	protected void exportResultSqlType(String targetName, String encoding, List<String> listWhere, String stmtType, int commit) {
		try {
			
			if ("batch".equalsIgnoreCase(stmtType)) {
				if (isPreview) {
					previewDataLoad(targetName, SQLExporter.makeFileBatchInsertStatment(targetName, queryExecuteResultDTO, PREVIEW_COUNT, commit), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(SQLExporter.makeFileBatchInsertStatment(targetName, queryExecuteResultDTO, -1, commit));
				}else{
					downloadFile(targetName, SQLExporter.makeFileBatchInsertStatment(targetName, queryExecuteResultDTO, commit), encoding);
				}
			}else if ("insert".equalsIgnoreCase(stmtType)) {
				if (isPreview) {
					previewDataLoad(targetName, SQLExporter.makeFileInsertStatment(targetName, queryExecuteResultDTO, PREVIEW_COUNT, commit), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(SQLExporter.makeFileInsertStatment(targetName, queryExecuteResultDTO, -1, commit));
				}else{
					downloadFile(targetName, SQLExporter.makeFileInsertStatment(targetName, queryExecuteResultDTO, commit), encoding);
				}
			}else if ("update".equalsIgnoreCase(stmtType)) {
				if (isPreview) {
					previewDataLoad(targetName, SQLExporter.makeFileUpdateStatment(targetName, queryExecuteResultDTO, listWhere, PREVIEW_COUNT, commit), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(SQLExporter.makeFileUpdateStatment(targetName, queryExecuteResultDTO, listWhere, -1, commit));
				}else{
					downloadFile(targetName, SQLExporter.makeFileUpdateStatment(targetName, queryExecuteResultDTO, listWhere, commit), encoding);
				}
			}else if ("merge".equalsIgnoreCase(stmtType)) {
				if (isPreview) {
					previewDataLoad(targetName, SQLExporter.makeFileMergeStatment(targetName, queryExecuteResultDTO, listWhere, PREVIEW_COUNT, commit), encoding);
				}else if (isSendEditor) {
					targetEditor.appendText(SQLExporter.makeFileMergeStatment(targetName, queryExecuteResultDTO, listWhere, -1, commit));
				}else{
					downloadFile(targetName, SQLExporter.makeFileMergeStatment(targetName, queryExecuteResultDTO, listWhere, commit), encoding);
				}
			}else{
				// not support type;
			}
			
			isPreview = false;
		} catch(Exception ee) {
			logger.error("Text type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * preview data 
	 * @param fileName
	 * @param previewData
	 * @param encoding
	 * @throws Exception
	 */
	protected void previewDataLoad(String fileName, String previewData, String encoding) throws Exception {
		this.textPreview.setText(previewData);
		
		this.isPreview = false;
	}
	
	/**
	 * download file
	 * @param strFileLocation
	 * @throws Exception
	 */
	protected void downloadFile(String fileName, String strFileLocation, String encoding) throws Exception {
		//TODO: 결과 파일 인코딩 하기...
		String strZipFile = ZipUtils.pack(strFileLocation);
		byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
		
		_downloadExtFile(fileName +".zip", bytesZip); //$NON-NLS-1$
	}
	
	/** registery service handler */
	protected void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/** download service handler call */
	protected void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	protected void _downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(getShell(), downloadServiceHandler.getId());
	}
	
}
