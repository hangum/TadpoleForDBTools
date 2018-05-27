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
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.TDBResultCodeDefine;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.export.SQLExporter;
import com.hangum.tadpole.engine.sql.util.export.XMLExporter;
import com.hangum.tadpole.engine.sql.util.export.all.AllDataExporter;
import com.hangum.tadpole.engine.sql.util.export.all.QueryDataExportFactory;
import com.hangum.tadpole.engine.sql.util.export.dto.ExportResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.AbstractExportComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportExcelComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportHTMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportJSONComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportSQLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportTextComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportXMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.AbstractExportDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportExcelDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportHtmlDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportJsonDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportSqlDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportTextDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportXmlDAO;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.EXPORT_METHOD;

/**
 * User resultset download dialog
 * 
 * @author hangum
 */
public class ResultSetDownloadDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResultSetDownloadDialog.class);
	
	/** null 기본값 */
	private String strDefaultNullValue = "";
	
	/** 다운로드 실행시에 사용할 쿼리 지정 */
	private String strSQL = "";

	/** define max download limit */
	final int intMaxDownloadCnt = Integer.parseInt(GetAdminPreference.getQueryResultDownloadLimit());

	/** button status */
	public enum USER_BTN_CLICK_STATUS {PREVIEW, SENDEDITOR, DOWNLOAD};
	public USER_BTN_CLICK_STATUS userBtnClickStatus = USER_BTN_CLICK_STATUS.PREVIEW;
	
	/** user seq */
	private int userSeq = SessionManager.getUserSeq();
	
	/** 배열이 0부터 시작하므로 실제로는 5건. */ 
	private final int SHOW_PREVIEW_DATA_COUNT = 4;
	private final int BTN_PREVIEW_ID = IDialogConstants.CLIENT_ID + 1;
	private final int BTN_DATA_SENDE_DITOR_ID = IDialogConstants.CLIENT_ID + 2;
	
	private String defaultTargetName;
	private QueryExecuteResultDTO queryExecuteResultDTO;
	
	private CTabFolder tabFolder;
	private AbstractExportComposite compositeText;
	private AbstractExportComposite compositeExcel;
	private AbstractExportComposite compositeHTML;
	private AbstractExportComposite compositeJSON;
	private AbstractExportComposite compositeXML;
	private AbstractExportComposite compositeSQL;
	
	// preview 
	private Text textPreview;
	protected DownloadServiceHandler downloadServiceHandler;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param requestQuery
	 * @param queryExecuteResultDTO 
	 * @param strDefTableName 
	 */
	public ResultSetDownloadDialog(Shell parentShell, RequestQuery requestQuery, String strDefTableName, QueryExecuteResultDTO queryExecuteResultDTO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.defaultTargetName = strDefTableName;
		this.queryExecuteResultDTO = queryExecuteResultDTO;
		
		if(requestQuery.getSqlStatementType() == PublicTadpoleDefine.SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
			strSQL = requestQuery.getSqlAddParameter();
		} else {
			strSQL = requestQuery.getSql();
		}
		
		strSQL = SQLConvertCharUtil.toServer(queryExecuteResultDTO.getUserDB(), strSQL);
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
		
		compositeExcel = new ExportExcelComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeExcel.setLayout(new GridLayout(1, false));
		
		compositeHTML = new ExportHTMLComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeHTML.setLayout(new GridLayout(1, false));
		
		compositeJSON = new ExportJSONComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeJSON.setLayout(new GridLayout(1, false));
		
		compositeXML = new ExportXMLComposite(tabFolder, SWT.NONE, defaultTargetName);
		compositeXML.setLayout(new GridLayout(1, false));
		
		compositeSQL = new ExportSQLComposite(tabFolder, SWT.NONE, defaultTargetName, queryExecuteResultDTO.getColumnLabelName());
		compositeSQL.setLayout(new GridLayout(1, false));
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
		
		initUIData();
		
		return container;
	}
	
	private void initUIData() {
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.CANCEL_ID) {
			super.buttonPressed(buttonId);			
		} else {
			if(buttonId == BTN_PREVIEW_ID) {
				userBtnClickStatus = USER_BTN_CLICK_STATUS.PREVIEW;
				this.textPreview.setText("");
				
			} else if(buttonId == BTN_DATA_SENDE_DITOR_ID) {
				userBtnClickStatus = USER_BTN_CLICK_STATUS.SENDEDITOR;
				
			} else if(buttonId == IDialogConstants.OK_ID) {
				userBtnClickStatus = USER_BTN_CLICK_STATUS.DOWNLOAD;
			}
			
			executeButton();
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BTN_PREVIEW_ID, Messages.get().Preview, true);
		createButton(parent, BTN_DATA_SENDE_DITOR_ID, Messages.get().SendEditor, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Download, false);
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false);
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

	/** execute button */
	private void executeButton() {
		final EXPORT_METHOD selectionTab = (EXPORT_METHOD)tabFolder.getSelection().getData();
		AbstractExportDAO exportDAO = null;
		
		if(EXPORT_METHOD.TEXT == selectionTab) {
			if(!compositeText.isValidate()) return;
			exportDAO = compositeText.getLastData();
		} else if(EXPORT_METHOD.EXCEL == selectionTab) {
			if(!compositeText.isValidate()) return;
			exportDAO = compositeExcel.getLastData();
		} else if(EXPORT_METHOD.HTML == selectionTab) {
			if(!compositeHTML.isValidate()) return;
			exportDAO = compositeHTML.getLastData();
		} else if(EXPORT_METHOD.JSON == selectionTab) {
			if(!compositeJSON.isValidate()) return;
			exportDAO = compositeJSON.getLastData();
		} else if(EXPORT_METHOD.XML == selectionTab) {
			if(!compositeXML.isValidate()) return;
			exportDAO = compositeXML.getLastData();
		} else if(EXPORT_METHOD.SQL == selectionTab) {			
			if(!compositeSQL.isValidate()) return;
			exportDAO = compositeSQL.getLastData();
		} else {
			if(logger.isDebugEnabled()) logger.debug("selection tab is " + selectionTab);	
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().ResultSetDownloadDialog_notSelect); 
			return;
		}
		
		// 요청 쿼리를 생성한다.
		final RequestResultDAO reqResultDAO = new RequestResultDAO();
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(SessionManager.getLoginIp());
		reqResultDAO.setSql_text(strSQL);
		reqResultDAO.setEXECUSTE_SQL_TYPE(PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDIT_DOWN);
		
		// job
		final String MSG_LoadingData = CommonMessages.get().LoadingData;;
		final AbstractExportDAO _dao = exportDAO;
		
		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(MSG_LoadingData, IProgressMonitor.UNKNOWN);
				
				 ExportResultDTO exportResult = null;
				try {
					if(EXPORT_METHOD.TEXT == selectionTab) {		
						ExportTextDAO dao = (ExportTextDAO)_dao;
						exportResult = exportResultCSVType(dao.isIsncludeHeader(), dao.getTargetName(), dao.getSeparatorType(), dao.getComboEncoding());
					} else if(EXPORT_METHOD.EXCEL == selectionTab) {
						ExportExcelDAO dao = (ExportExcelDAO)_dao;
						exportResult = exportResultExcelType(dao.getTargetName());
					} else if(EXPORT_METHOD.HTML == selectionTab) {
						ExportHtmlDAO dao = (ExportHtmlDAO)_dao;
						exportResult = exportResultHtmlType(dao.getTargetName(), dao.getComboEncoding());
					} else if(EXPORT_METHOD.JSON == selectionTab) {			
						ExportJsonDAO dao = (ExportJsonDAO)_dao;
						exportResult = exportResultJSONType(dao.isIsncludeHeader(), dao.getTargetName(), dao.getSchemeKey(), dao.getRecordKey(), dao.getComboEncoding(), dao.isFormat());
					} else if(EXPORT_METHOD.XML == selectionTab) {
						ExportXmlDAO dao = (ExportXmlDAO)_dao;
						exportResult = exportResultXmlType(dao.getTargetName(), dao.getComboEncoding());
					} else if(EXPORT_METHOD.SQL == selectionTab) {			
						ExportSqlDAO dao = (ExportSqlDAO)_dao;
						exportResult = exportResultSqlType(dao.getTargetName(), dao.getComboEncoding(), dao.getListWhere(),  dao.getStatementType(), dao.getCommit());
					}
					reqResultDAO.setResultData(exportResult.getResultData());
					reqResultDAO.setRows(exportResult.getRowCount());
					reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.S.name()); //$NON-NLS-1$
				} catch(Exception e) {
					logger.error("Resultset download Download type : " + selectionTab.name(), e);
					
					reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
					reqResultDAO.setTdb_result_code(TDBResultCodeDefine.BAD_REQUEST);
					reqResultDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage(), e);
				} finally {
					// 메시지에 다운로드 타입을 넣어준다.
					reqResultDAO.setMesssage("Download type : " + selectionTab.name() + PublicTadpoleDefine.LINE_SEPARATOR + reqResultDAO.getMesssage());
					
					reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
					monitor.done();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				
				final Display display = getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
//							
							if (userBtnClickStatus == USER_BTN_CLICK_STATUS.DOWNLOAD) {
								TadpoleSystem_ExecutedSQL.insertExecuteHistory(
										SessionManager.getUserSeq(), 
										queryExecuteResultDTO.getUserDB(), 
										reqResultDAO
									);
							}
						} else {
							MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, jobEvent.getResult().getMessage());
						}
					}	// end run
				});	// end display.asyncExec
				
			}	// end done
		});	// end job
		
		job.setName(Messages.get().DownloadQueryResult);
		job.setUser(true);
		job.schedule();
	}

	/**
	 * export csv type
	 * 
	 * @param isAddHead
	 * @param targetName
	 * @param seprator
	 * @param encoding
	 * @return
	 */
	protected ExportResultDTO exportResultCSVType(boolean isAddHead, String targetName, char separator, String encoding) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
			previewDataLoad(targetName, CSVExpoter.makeContent(isAddHead, queryExecuteResultDTO, separator, SHOW_PREVIEW_DATA_COUNT, strDefaultNullValue), encoding);
		} else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
			targetEditor(CSVExpoter.makeContent(isAddHead, queryExecuteResultDTO, separator, strDefaultNullValue));
		} else {
			exportDto = QueryDataExportFactory.createCSV(separator, isAddHead, targetName, "csv", queryExecuteResultDTO.getUserDB(), strSQL, intMaxDownloadCnt);
			exportDto = downloadFile(targetName, exportDto, encoding);
		}
		
		return exportDto;
	}
	
	/**
	 * export excel type
	 * 
	 * @param targetName
	 * @return
	 * @throws Exception
	 */
	protected ExportResultDTO exportResultExcelType(String targetName) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		
		if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
			previewDataLoad(targetName, "", "UTF-8");
		}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
//			targetEditor("에디터로 데이터를 보낼수 없습니다.");
		}else{
			exportDto = QueryDataExportFactory.createExcel(targetName, "xlsx", queryExecuteResultDTO.getUserDB(), strSQL, intMaxDownloadCnt);
			exportDto = downloadFile(targetName, exportDto, "UTF-8");
		}
		
		return exportDto;
	}
	
	/**
	 * export html type
	 * 
	 * @param targetName
	 * @param encoding
	 */
	protected ExportResultDTO exportResultHtmlType(String targetName, String encoding) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		
		if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
			previewDataLoad(targetName, HTMLExporter.makeContent(targetName, queryExecuteResultDTO, SHOW_PREVIEW_DATA_COUNT, strDefaultNullValue), encoding);
		}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
			targetEditor(HTMLExporter.makeContent(targetName, queryExecuteResultDTO, strDefaultNullValue));
		}else{
			exportDto = QueryDataExportFactory.createHTML(encoding, targetName, "html", queryExecuteResultDTO.getUserDB(), strSQL, intMaxDownloadCnt);
			exportDto = downloadFile(targetName, exportDto, encoding);
		}
		
		return exportDto;
	}
	
	/**
	 * export json type
	 * 
	 * @param isAddHead
	 * @param targetName
	 * @param schemeKey
	 * @param recordKey
	 * @param encoding
	 * @param isFormat
	 */
	protected ExportResultDTO exportResultJSONType(boolean isAddHead, String targetName, String schemeKey, String recordKey, String encoding, boolean isFormat)  throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		
		if (isAddHead){
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, JsonExpoter.makeHeadContent(targetName, queryExecuteResultDTO, schemeKey, recordKey, isFormat, SHOW_PREVIEW_DATA_COUNT), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(JsonExpoter.makeHeadContent(targetName, queryExecuteResultDTO, schemeKey, recordKey, isFormat, -1));
			}else{
				exportDto = AllDataExporter.makeJSONHeadAllResult(queryExecuteResultDTO.getUserDB(), strSQL, targetName, schemeKey, recordKey, isFormat, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}else{
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, JsonExpoter.makeContent(targetName, queryExecuteResultDTO, isFormat, SHOW_PREVIEW_DATA_COUNT), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(JsonExpoter.makeContent(targetName, queryExecuteResultDTO, isFormat, -1));
			}else{
				exportDto = AllDataExporter.makeJSONAllResult(queryExecuteResultDTO.getUserDB(), strSQL, targetName, isFormat, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}
		
		return exportDto;
	}
	
	/**
	 * export xml type
	 * 
	 * @param targetName
	 * @param encoding
	 */
	protected ExportResultDTO exportResultXmlType(String targetName, String encoding) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		
		if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
			previewDataLoad(targetName, XMLExporter.makeContent(targetName, queryExecuteResultDTO, SHOW_PREVIEW_DATA_COUNT), encoding);
		}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
			targetEditor(XMLExporter.makeContent(targetName, queryExecuteResultDTO));
		}else{
			exportDto = AllDataExporter.makeXMLResult(queryExecuteResultDTO.getUserDB(), strSQL, targetName, encoding, strDefaultNullValue, intMaxDownloadCnt);
			exportDto = downloadFile(targetName, exportDto, encoding);
		}
		
		return exportDto;
	}
	
	/**
	 * export sql type
	 * @param targetName
	 * @param encoding
	 * @param listWhere
	 * @param stmtType
	 * @param commit
	 */
	protected ExportResultDTO exportResultSqlType(String targetName, String encoding, List<String> listWhere, String stmtType, int commit) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		
		if ("batch".equalsIgnoreCase(stmtType)) {
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, SQLExporter.makeBatchInsertStatment(targetName, queryExecuteResultDTO, SHOW_PREVIEW_DATA_COUNT, commit), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(SQLExporter.makeBatchInsertStatment(targetName, queryExecuteResultDTO, -1, commit));
			}else{
				exportDto = AllDataExporter.makeFileBatchInsertStatment(queryExecuteResultDTO.getUserDB(), strSQL, targetName, commit, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}else if ("insert".equalsIgnoreCase(stmtType)) {
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, SQLExporter.makeInsertStatment(targetName, queryExecuteResultDTO, SHOW_PREVIEW_DATA_COUNT, commit), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(SQLExporter.makeInsertStatment(targetName, queryExecuteResultDTO, -1, commit));
			}else{
				exportDto = AllDataExporter.makeFileInsertStatment(queryExecuteResultDTO.getUserDB(), strSQL, targetName, commit, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}else if ("update".equalsIgnoreCase(stmtType)) {
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, SQLExporter.makeUpdateStatment(targetName, queryExecuteResultDTO, listWhere, SHOW_PREVIEW_DATA_COUNT, commit), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(SQLExporter.makeUpdateStatment(targetName, queryExecuteResultDTO, listWhere, -1, commit));
			}else{
				exportDto = AllDataExporter.makeFileUpdateStatment(queryExecuteResultDTO.getUserDB(), strSQL, targetName, listWhere, commit, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}else if ("merge".equalsIgnoreCase(stmtType)) {
			if (userBtnClickStatus == USER_BTN_CLICK_STATUS.PREVIEW) {
				previewDataLoad(targetName, SQLExporter.makeMergeStatment(targetName, queryExecuteResultDTO, listWhere, SHOW_PREVIEW_DATA_COUNT, commit), encoding);
			}else if (userBtnClickStatus == USER_BTN_CLICK_STATUS.SENDEDITOR) {
				targetEditor(SQLExporter.makeMergeStatment(targetName, queryExecuteResultDTO, listWhere, -1, commit));
			}else{
				exportDto = AllDataExporter.makeFileMergeStatment(queryExecuteResultDTO.getUserDB(), strSQL, targetName, listWhere, commit, encoding, strDefaultNullValue, intMaxDownloadCnt);
				exportDto = downloadFile(targetName, exportDto, encoding);
			}
		}
		
		return exportDto;
	}
	
	/**
	 * 에디터 오픈
	 * 
	 * @param strContetn
	 */
	private void targetEditor(final String strContetn) {
		getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				FindEditorAndWriteQueryUtil.run(queryExecuteResultDTO.getUserDB(), strContetn, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
			}
		});
	}
	
	/**
	 * preview data 
	 * @param fileName
	 * @param previewData
	 * @param encoding
	 * @throws Exception
	 */
	protected void previewDataLoad(final String fileName, final String previewData, final String encoding) throws Exception {
		getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				textPreview.setText(previewData);		
			}
		});
	}
	
	/**
	 * download file
	 * 
	 * @param fileName
	 * @param exportDto
	 * @param encoding 
	 * 
	 * @throws Exception
	 */
	protected ExportResultDTO downloadFile(final String fileName, final ExportResultDTO exportDto, final String encoding) throws Exception {
		final File file = new File(exportDto.getFileFullName());
		if(file.exists()) {
			String strExt = StringUtils.substringAfterLast(exportDto.getFileFullName(), ".");
	//		if(logger.isInfoEnabled()) {
	//			logger.info("#####[start]#####################[resource download]");
	//			logger.info("\tfile ext : " + strExt);
	//			logger.info("\tfile size : " + file.length());
	//			logger.info("#####[end]#####################[resource download]");
	//		}
			final byte[] bytesDatas = FileUtils.readFileToByteArray(file);
			
			// excel 파일의 경우 바이너리가 저장 되므로... 파일의 위치를 %user_home%/res/파일명 으로 남기도록 합니다.
			if(exportDto.getExportMethod() == EXPORT_METHOD.EXCEL) {
				
				String strNewFile = ApplicationArgumentUtils.USER_RESOURCE_DIR + userSeq + PublicTadpoleDefine.DIR_SEPARATOR + System.currentTimeMillis() + file.getName();
				if(logger.isDebugEnabled()) logger.debug("==> new file location: " + strNewFile);
				FileUtils.moveFile(file, new File(strNewFile));
				
				exportDto.setResultData("file location: " + strNewFile);
			} else {
				exportDto.setResultData(new String(bytesDatas));
			}
			
			getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						_downloadExtFile(fileName + "." + strExt, bytesDatas); //$NON-NLS-1$
						
						// 사용후 파일을 삭제한다.
						FileUtils.deleteDirectory(new File(file.getParent()));
					} catch(Exception e) {
						logger.error("download file", e);
					}
				}
			});
		} else {
			// 파일 결과가 없는 경우는 SQL문(INSERT, UPDATE 등)을 만드는데 SELECT ROW 결과가 없는 경우이다. 
			// INSERT INTO AA(A, B, C) VLAUES(?, ?, ?) 를 만들수가 없는 경우...

			getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						// Invalid thread access 에러 조심
						MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().NoResultNoFile);
					} catch(Exception e) {
						logger.error("download file", e);
					}
				}
			});
		}
		return exportDto;
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
