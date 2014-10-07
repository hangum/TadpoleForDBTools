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
package com.hangum.tadpole.importdb.core.dialog.importdb.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.addons.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.addons.fileupload.FileDetails;
import org.eclipse.rap.addons.fileupload.FileUploadEvent;
import org.eclipse.rap.addons.fileupload.FileUploadHandler;
import org.eclipse.rap.addons.fileupload.FileUploadListener;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.csv.CSVLoader;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.importdb.core.Messages;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * CSV to RDB Import dialog
 * 
 * @author hangum
 *
 */
public class CsvToRDBImportDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(CsvToRDBImportDialog.class);
	private int ID_BTN_INSERT		 = IDialogConstants.CLIENT_ID 	+ 1;
	
	private UserDBDAO userDB;
	private static final String INITIAL_TEXT = "No files uploaded."; //$NON-NLS-1$
	
	// file upload
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;
	
	private Text fileNameLabel;
	private Text textTableName;
	private Text textSQL;
	private Text textSeprator;
	
	private Composite compositeExistsData;
	private Button btnCopyNew;
	private Button btnTruncate ;
	private Button btnDeleteAll ;
	private Button btnNotDelete;
	
	private Composite compositeDisable;
	private Button btnTrigger;
	private Button btnFk ;
	private Button btnPk ;
	
	private Button btnIgnore ;
	private Button btnStop ;
	
	private Composite compositeExecuteType;
	private Button btnInsert ;
	private Button btnUpdate ;
	private Button btnDelete ;
	
	private Text textBatchSize;
	private List<HashMap<String,String>> disableObjectResults = new ArrayList<HashMap<String,String>>();
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CsvToRDBImportDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(userDB.getDisplay_name() + " CSV File import"); //$NON-NLS-1$
	}

	@Override
	public boolean close() {
		unregisterServiceHandler();
		return super.close();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.horizontalSpacing = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblTableName = new Label(compositeHead, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText(Messages.CsvToRDBImportDialog_0);
		
		textTableName = new Text(compositeHead, SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnCopyNew = new Button(compositeHead, SWT.CHECK);
		btnCopyNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// update나 delete를 선택할 경우에는 기존 데이터에 대해 삭제또는 갱신하는 것이므로 삭제옵션은 필요하지 않음.
				btnDeleteAll.setSelection(true);
				btnTruncate.setSelection(false);
				btnNotDelete.setSelection(false);
				compositeExistsData.setEnabled(!btnCopyNew.getSelection());
				
				btnTrigger.setSelection(false);
				btnFk.setSelection(false);
				btnPk.setSelection(false);
				compositeDisable.setEnabled(!btnCopyNew.getSelection());
				
				btnInsert.setSelection(true);
				btnUpdate.setSelection(false);
				btnDelete.setSelection(false);
				compositeExecuteType.setEnabled(!btnCopyNew.getSelection());
			}
		});
		btnCopyNew.setText(Messages.CsvToRDBImportDialog_btnCopyNew_text);
		
		Label lblFileName = new Label(compositeHead, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText(Messages.CsvToRDBImportDialog_1);
		
		fileNameLabel = new Text(compositeHead, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();
	
		/* Window builder Design View bug */
		/*
		Label temp = new Label(compositeHead, SWT.NONE);
		temp.setText("Temp");
		temp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		*/	
		fileUpload = new FileUpload(compositeHead, SWT.NONE);
		fileUpload.setText(Messages.CsvToRDBImportDialog_2);
		fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				if("".equals(fileName) || null == fileName) return; //$NON-NLS-1$
				
				if(!MessageDialog.openConfirm(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_5)) return;
				fileNameLabel.setText(fileName == null ? "" : fileName); //$NON-NLS-1$
				
				pushSession.start();
				fileUpload.submit(url);
			}
		});
		
		Label lblSeprator = new Label(compositeHead, SWT.NONE);
		lblSeprator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeprator.setText(Messages.CsvToRDBImportDialog_6);
		
		Composite composite_3 = new Composite(compositeHead, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_3.setLayout(new GridLayout(3, false));
		
		textSeprator = new Text(composite_3, SWT.BORDER);
		GridData gd_textSeprator = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textSeprator.widthHint = 101;
		textSeprator.setLayoutData(gd_textSeprator);
		textSeprator.setText(",");
		
		Label lblBatchSize = new Label(composite_3, SWT.NONE);
		lblBatchSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBatchSize.setText(Messages.CsvToRDBImportDialog_lblBatchSize_text);
		
		textBatchSize = new Text(composite_3, SWT.BORDER | SWT.RIGHT);
		if(DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT ) {
			//SQLite 는 BatchExecute작업이 한번에 200건 이상 처리시 database logic에러가 발생하고 있어서 1건마다 executeBatch 및 commit을 하도록 한다.
			textBatchSize.setEditable(false);
			textBatchSize.setText("1");
		}else{
			textBatchSize.setEditable(true);
			textBatchSize.setText(Messages.CsvToRDBImportDialog_text_1_text_1);
		}
		textBatchSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblException = new Label(compositeHead, SWT.NONE);
		lblException.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblException.setText(Messages.CsvToRDBImportDialog_lblException_text);
		
		Composite composite_4 = new Composite(compositeHead, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_4.setLayout(new GridLayout(2, false));
		
		btnIgnore = new Button(composite_4, SWT.RADIO);
		GridData gd_btnIgnore = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnIgnore.widthHint = 98;
		btnIgnore.setLayoutData(gd_btnIgnore);
		btnIgnore.setToolTipText(Messages.CsvToRDBImportDialog_btnIgnore_toolTipText);
		btnIgnore.setText(Messages.CsvToRDBImportDialog_btnIgnore_text);
		
		btnStop = new Button(composite_4, SWT.RADIO);
		btnStop.setSelection(true);
		btnStop.setToolTipText(Messages.CsvToRDBImportDialog_btnStop_toolTipText);
		GridData gd_btnStop = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnStop.widthHint = 95;
		btnStop.setLayoutData(gd_btnStop);
		btnStop.setText(Messages.CsvToRDBImportDialog_btnStop_text);
		new Label(compositeHead, SWT.NONE);
		
		Label lblExecuteType = new Label(compositeHead, SWT.NONE);
		lblExecuteType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExecuteType.setText(Messages.CsvToRDBImportDialog_lblExecuteType_text);
		
		compositeExecuteType = new Composite(compositeHead, SWT.NONE);
		compositeExecuteType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeExecuteType.setLayout(new GridLayout(3, false));
		
		btnInsert = new Button(compositeExecuteType, SWT.RADIO);
		GridData gd_btnInsert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnInsert.widthHint = 97;
		btnInsert.setLayoutData(gd_btnInsert);
		btnInsert.setSelection(true);
		btnInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// update나 delete를 선택할 경우에는 기존 데이터에 대해 삭제또는 갱신하는 것이므로 삭제옵션은 필요하지 않음.
				btnDeleteAll.setSelection(true);
				btnTruncate.setSelection(false);
				btnNotDelete.setSelection(false);
				compositeExistsData.setEnabled(true);
			}
		});
		btnInsert.setText(Messages.CsvToRDBImportDialog_btnInsert_text);
		
		btnUpdate = new Button(compositeExecuteType, SWT.RADIO);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// update나 delete를 선택할 경우에는 기존 데이터에 대해 삭제또는 갱신하는 것이므로 삭제옵션은 필요하지 않음.
				btnTruncate.setSelection(false);
				btnDeleteAll.setSelection(false);				
				btnNotDelete.setSelection(true);
				compositeExistsData.setEnabled(false);
			}
		});
		GridData gd_btnUpdate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnUpdate.widthHint = 89;
		btnUpdate.setLayoutData(gd_btnUpdate);
		btnUpdate.setText(Messages.CsvToRDBImportDialog_btnUpdate_text);
		
		
		btnDelete = new Button(compositeExecuteType, SWT.RADIO);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// update나 delete를 선택할 경우에는 기존 데이터에 대해 삭제또는 갱신하는 것이므로 삭제옵션은 필요하지 않음.
				btnTruncate.setSelection(false);
				btnDeleteAll.setSelection(false);				
				btnNotDelete.setSelection(true);
				compositeExistsData.setEnabled(false);
			}
		});
		GridData gd_btnDelete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDelete.widthHint = 88;
		btnDelete.setLayoutData(gd_btnDelete);
		btnDelete.setText(Messages.CsvToRDBImportDialog_btnDelete_text);
		
		Button btnSaveLog = new Button(compositeHead, SWT.NONE);
		btnSaveLog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSaveLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate()) return;						
				saveResultLog();
			}
		});
		btnSaveLog.setText(Messages.CsvToRDBImportDialog_btnSaveLog_text);
		
		Label lblExistsData = new Label(compositeHead, SWT.NONE);
		lblExistsData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExistsData.setText(Messages.CsvToRDBImportDialog_lblExistsData_text);
		
		compositeExistsData = new Composite(compositeHead, SWT.NONE);
		compositeExistsData.setLayout(new GridLayout(3, false));
		compositeExistsData.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		btnTruncate = new Button(compositeExistsData, SWT.RADIO);
		GridData gd_btnTruncate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTruncate.widthHint = 97;
		btnTruncate.setLayoutData(gd_btnTruncate);
		btnTruncate.setText(Messages.CsvToRDBImportDialog_btnTruncate_text);
		
		btnDeleteAll = new Button(compositeExistsData, SWT.RADIO);
		GridData gd_btnDeleteAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDeleteAll.widthHint = 91;
		btnDeleteAll.setLayoutData(gd_btnDeleteAll);
		btnDeleteAll.setSelection(true);
		btnDeleteAll.setText(Messages.CsvToRDBImportDialog_btnDeleteAll_text);
		
		btnNotDelete = new Button(compositeExistsData, SWT.RADIO);
		btnNotDelete.setText(Messages.CsvToRDBImportDialog_btnRadioButton_text);
		
		Button btnDownloadSql = new Button(compositeHead, SWT.NONE);
		btnDownloadSql.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDownloadSql.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate()) return;						
				downloadSQL();
			}
		});
		btnDownloadSql.setText(Messages.CsvToRDBImportDialog_btnDownloadSql_text);
		btnDownloadSql.setVisible(false);
		
		Label lblDisable = new Label(compositeHead, SWT.NONE);
		lblDisable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDisable.setText(Messages.CsvToRDBImportDialog_lblDisable_text);
		
		compositeDisable = new Composite(compositeHead, SWT.NONE);
		compositeDisable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeDisable.setLayout(new GridLayout(3, false));
		
		btnTrigger = new Button(compositeDisable, SWT.CHECK);
		GridData gd_btnTrigger = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTrigger.widthHint = 81;
		btnTrigger.setLayoutData(gd_btnTrigger);
		btnTrigger.setText(Messages.CsvToRDBImportDialog_btnTrigger_text);
		
		btnPk = new Button(compositeDisable, SWT.CHECK);
		btnPk.setEnabled(false);
		GridData gd_btnPk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPk.widthHint = 67;
		btnPk.setLayoutData(gd_btnPk);
		btnPk.setText(Messages.CsvToRDBImportDialog_btnPk_text);
		
		btnFk = new Button(compositeDisable, SWT.CHECK);
		btnFk.setEnabled(false);
		GridData gd_btnFk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFk.widthHint = 72;
		btnFk.setLayoutData(gd_btnFk);
		btnFk.setText(Messages.CsvToRDBImportDialog_btnFk_text);
		
		Button btnGenrateSql = new Button(compositeHead, SWT.NONE);
		btnGenrateSql.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGenrateSql.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate()) return;						
				generatePreviewSQL();
			}
		});
		btnGenrateSql.setText(Messages.CsvToRDBImportDialog_11);
		
		Group grpSqlTemplate = new Group(container, SWT.NONE);
		grpSqlTemplate.setLayout(new GridLayout(1, false));
		grpSqlTemplate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSqlTemplate.setText(Messages.CsvToRDBImportDialog_16);
		
		textSQL = new Text(grpSqlTemplate, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		registerServiceHandler();
		
		textTableName.setFocus();
		
		return container;
	}
	
	private void saveLog(){
		try {
			if("".equals(textSQL.getText())) { //$NON-NLS-1$
				MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.SQLToDBImportDialog_LogEmpty);
				return;
			}
			String filename = PublicTadpoleDefine.TEMP_DIR + userDB.getDisplay_name() + "_SQLImportResult.log"; //$NON-NLS-1$
			
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter bw = new OutputStreamWriter(fos, "UTF-8"); //$NON-NLS-1$
			
			bw.write(textSQL.getText());
			bw.close();
			
			String strImportResultLogContent = FileUtils.readFileToString(new File(filename));
			
			downloadExtFile(userDB.getDisplay_name() + "_SQLImportResult.log", strImportResultLogContent);//sbExportData.toString()); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("log writer", ee); //$NON-NLS-1$
		}		
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
	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadExtFile(String fileName, String newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents.getBytes());

		DownloadUtils.provideDownload(compositeExecuteType, downloadServiceHandler.getId());
	}

	private void appendPreviewSQL(String newSQL){
		textSQL.setText(textSQL.getText() + newSQL + "\n");
	}
	
	/* ***************************************************************************************
	 * DBMS별 기능 구현 가능성 검증 필요.
	 * DBMS별로 Disable Constraint 를 지원하는지 여부와 drop 후 add 가능 여부에 따라 기능 구현이 필요함.
	 * *************************************************************************************** */
	private boolean loadObjectDiableStatements(String tableName) {
		try {
			disableObjectResults.clear();
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			if (btnTrigger.getSelection()){
				disableObjectResults = sqlClient.queryForList("triggerListInTable", tableName); //$NON-NLS-1$
			}

			if (btnPk.getSelection()){
				disableObjectResults = sqlClient.queryForList("primarykeyListInTable", tableName); //$NON-NLS-1$
			}
			
			return true;
		} catch (Exception e) {
			logger.error("loadObjectDiableStatements", e); //$NON-NLS-1$
			//appendPreviewSQL("/* Disable Object not support or select. */");
			return false;
		}
	}
	
	private HashMap<String,Object> loadPrimaryKeyColumns(String tableName){
		List<HashMap> showIndexColumns=null;
		HashMap<String,Object> result = new HashMap<String,Object>();
		String columns = "";
		try{
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showIndexColumns = sqlClient.queryForList("primarykeyListInTable", tableName); //$NON-NLS-1$
			for (HashMap dao: showIndexColumns){
				if(DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT ) {
					/* cid, name, type, notnull, dflt_value, pk */
					if ("1".equals(dao.get("pk").toString())) {
						result.put(dao.get("name").toString(), (Integer) dao.get("cid") + 1);	
						columns += dao.get("name").toString() + ",";
					}
				}else{
					result.put(dao.get("column_name").toString(), (Integer) dao.get("column_order"));	
					columns += dao.get("column_name").toString() + ",";
				}				
			}
		} catch (Exception e) {
			logger.error("loadObjectDiableStatements", e); //$NON-NLS-1$
			appendPreviewSQL("/* Disable Object not support or select. */");
		}
		
		result.put("all_key_columns", StringUtils.split(columns, ","));
		
		return result;
	}
	
	private void saveResultLog(){
		saveLog();
	}
	
	private void downloadSQL(){
		MessageDialog.openInformation(null, "Tadpole CSV Import", "not support...");
	}
	
	private void generatePreviewSQL(){
		File[] arryFiles = receiver.getTargetFiles();
		File userDBFile = arryFiles[arryFiles.length-1];
		HashMap<String,Object> keyColumns = new HashMap<String,Object>();
		String tableName = textTableName.getText().trim();
		
		//PreviewSQL Clear!!!
		textSQL.setText("");
		
		CSVLoader loader = new CSVLoader(textSeprator.getText(), textBatchSize.getText(), btnStop.getSelection());
		String stmtType = "i";
		try {
			if (this.btnInsert.getSelection()) {
				stmtType = "i";
			} else if (this.btnUpdate.getSelection()){
				stmtType = "u";
			} else if (this.btnDelete.getSelection()){
				stmtType = "d";
			}
			
			// truncate or delete
			if (this.btnCopyNew.getSelection()) { 
				tableName += "_COPY"; 
				appendPreviewSQL("CREATE TABLE " + tableName + " AS \nSELECT * FROM " + textTableName.getText().trim() + " WHERE 1=0;\n");
			}else{

				// 기존 테이블에 대해서 작업할때만 테이블 제약조건 Disable / Enable 처리 필요함.
				if (loadObjectDiableStatements(tableName)){
					if (disableObjectResults !=null && disableObjectResults.size() > 0){
						for (HashMap<String,String> map : disableObjectResults){
							appendPreviewSQL(map.get("disable_statement").toString().concat("\n"));
						}
					}else{
						appendPreviewSQL("/*Find not found disable objects...*/");
					}
				}
				
				if ("i".equals(stmtType)){
					// 기존 테이블에 insert하는 경우에만 기존자료 삭제 방법에 대해 처리한다.
					if (this.btnTruncate.getSelection()) { 
						appendPreviewSQL("TRUNCATE TABLE " + tableName + ";");
					}else if (this.btnDeleteAll.getSelection()){
						appendPreviewSQL("DELETE FROM " + tableName + ";");
					}
				}else{
					// update, delete작업이 필요한 경우 대상테이블의 PrimaryKey정보를 조회한다.
					keyColumns = loadPrimaryKeyColumns(tableName);
				}
			}
			
			String strGenerateSQL = loader.generateSQL(userDBFile, tableName, stmtType, keyColumns );
			
			// enable script...
			//loadObjectDiableStatements(textTableName.getText().trim());
			
			if(logger.isDebugEnabled()) logger.debug(strGenerateSQL);
			appendPreviewSQL(strGenerateSQL);
		} catch (Exception e1) {
			logger.error("CSV load error", e1); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_10 + e1.getMessage());
		}

	}

	/**
	 * validator
	 * 
	 * @return
	 */
	private boolean validate() {
		if("".equals(textTableName.getText())) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_19);
			textTableName.setFocus();
			return false;
		}
		
		File[] arryFiles = receiver.getTargetFiles();
		if(arryFiles.length == 0) {
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_21);
			return false;
		}
		
		if("".equals(textSeprator.getText())) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_24);
			textSeprator.setFocus();
			return false;
		}
		
		return true;
	}
	
	/**
	 * data insert
	 */
	private void insertData() {
		if(!validate()) return;
		
		File[] arryFiles = receiver.getTargetFiles();
		File userDBFile = arryFiles[arryFiles.length-1];
		
		CSVLoader loader = new CSVLoader(textSeprator.getText(), textBatchSize.getText(), btnStop.getSelection());
		Connection conn =  null;
		HashMap<String,Object> keyColumns = new HashMap<String,Object>();
		String stmtType = "i";
		String workType = "n";
		try {
			if (this.btnInsert.getSelection()) {
				stmtType = "i";
			} else if (this.btnUpdate.getSelection()){
				stmtType = "u";
			} else if (this.btnDelete.getSelection()){
				stmtType = "d";
			}
			
			if(btnCopyNew.getSelection()){
				workType = "c"; // Copy & New
				disableObjectResults.clear();
			}else{
				
				// 기존 테이블에 대해서 작업할때만 테이블 제약조건 Disable / Enable 처리 필요함.
				loadObjectDiableStatements(textTableName.getText());

				if(btnTruncate.getSelection()){
					workType = "t"; // Truncate
				}else if(btnDeleteAll.getSelection()){
					workType = "d"; // Delete
				}
			}
			

			keyColumns = loadPrimaryKeyColumns(textTableName.getText().trim());
			conn = TadpoleSQLManager.getInstance(userDB).getDataSource().getConnection();
			
			
			int count = loader.loadCSV(conn, userDBFile, textTableName.getText(), workType, stmtType, keyColumns, disableObjectResults);
			
			this.appendPreviewSQL(loader.getImportResultLog().toString());
			
			MessageDialog.openInformation(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_26 + "\n count is "+ count);
		} catch (Exception e1) {
			logger.error("CSV load error", e1); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_29 + e1.getMessage());
			
			return;
		} finally {
			if(conn != null) try { conn.close(); } catch(Exception e) {} 
		}
		
		//super.okPressed();
	}
	
	/**
	 * 저장 이벤트 
	 * 
	 * @return
	 */
	private String startUploadReceiver() {
		receiver = new DiskFileUploadReceiver();
		final FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
		uploadHandler.addUploadListener(new FileUploadListener() {

			public void uploadProgress(FileUploadEvent event) {
			}

			public void uploadFailed(FileUploadEvent event) {
				addToLog( "upload failed: " + event.getException() ); //$NON-NLS-1$
			}

			public void uploadFinished(FileUploadEvent event) {
				for( FileDetails file : event.getFileDetails() ) {
					addToLog( "uploaded : " + file.getFileName() ); //$NON-NLS-1$
				}
			}			
		});
		
		return uploadHandler.getUploadUrl();
	}
	private void addToLog(final String message) {
		if (!fileNameLabel.isDisposed()) {
			fileNameLabel.getDisplay().asyncExec(new Runnable() {
				public void run() {
					String text = fileNameLabel.getText();
					if (INITIAL_TEXT.equals(text)) {
						text = ""; //$NON-NLS-1$
					}
					fileNameLabel.setText(message);

					pushSession.stop();
				}
			});
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		
		if(buttonId == ID_BTN_INSERT) {
			if(MessageDialog.openConfirm(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_14)) {
				insertData();
			}
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, ID_BTN_INSERT, "Import", false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.CsvToRDBImportDialog_30, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 500);
	}
}
