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
package com.hangum.tadpole.importdb.core.dialog.importdb.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.importdb.core.Messages;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * SQL to db import dialog
 * 
 * @author hangum
 *
 */
public class SQLToDBImportDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SQLToDBImportDialog.class);
	
	private int ID_BTN_EXPORT		 = IDialogConstants.CLIENT_ID 	+ 1;
	private int ID_BTN_INSERT		 = IDialogConstants.CLIENT_ID 	+ 2;
	
	private UserDBDAO userDB;
	private static final String INITIAL_TEXT = "No files uploaded."; //$NON-NLS-1$
	
	// file upload
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;
	
	private Text fileNameLabel;
	private Text textSeprator;
	
	private int batchSize = 1000;
	private Text textBatchSize;
	private Button btnIgnore;
	private Button btnStop;

	private StringBuffer bufferBatchResult;

	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
    /** content download를 위한 더미 composite */
    private Composite compositeDumy;


	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SQLToDBImportDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(userDB.getDisplay_name() + " SQL File import"); //$NON-NLS-1$
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
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblFileName = new Label(compositeHead, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText(Messages.CsvToRDBImportDialog_1);
		
		fileNameLabel = new Text(compositeHead, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();

		/* fileUpload 주석 후 디자인을 위한 임시 컨트롤 */
		/*
		Label lblDumy = new Label(compositeHead, SWT.NONE);
		lblDumy.setText(Messages.CsvToRDBImportDialog_2);
		lblDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
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
		lblSeprator.setText(Messages.SQLToDBImportDialog_lblSeprator_text);
		
		textSeprator = new Text(compositeHead, SWT.BORDER);
		textSeprator.setText(Messages.SQLToDBImportDialog_text_1_text);
		textSeprator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblBatchSize = new Label(compositeHead, SWT.NONE);
		lblBatchSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBatchSize.setText(Messages.SQLToDBImportDialog_lblNewLabel_text);
		
		textBatchSize = new Text(compositeHead, SWT.BORDER | SWT.RIGHT);
		textBatchSize.setText(Messages.SQLToDBImportDialog_text_1_text_1);
		textBatchSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblException = new Label(compositeHead, SWT.NONE);
		lblException.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblException.setText(Messages.SQLToDBImportDialog_lblNewLabel_1_text);
		
		Composite composite = new Composite(compositeHead, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		btnIgnore = new Button(composite, SWT.RADIO);
		GridData gd_btnIgnore = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnIgnore.widthHint = 98;
		btnIgnore.setLayoutData(gd_btnIgnore);
		btnIgnore.setText(Messages.SQLToDBImportDialog_btnIgnore_text);
		
		btnStop = new Button(composite, SWT.RADIO);
		btnStop.setSelection(true);
		GridData gd_btnStop = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnStop.widthHint = 92;
		btnStop.setLayoutData(gd_btnStop);
		btnStop.setText(Messages.SQLToDBImportDialog_btnStop_text);
		new Label(compositeHead, SWT.NONE);
		new Label(compositeHead, SWT.NONE);
		
		compositeDumy = new Composite(compositeHead, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_composite_1.heightHint = 16;
		compositeDumy.setLayoutData(gd_composite_1);
		new Label(compositeHead, SWT.NONE);
		
		registerServiceHandler();
		
		return container;
	}
	
	private void insert() {
		int ret;
		BOMInputStream bomInputStream ;
		
		File[] arryFiles = receiver.getTargetFiles();
		if(arryFiles.length == 0) {
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_21);
			return ;
		}
		
		if(!MessageDialog.openConfirm(null, Messages.CsvToRDBImportDialog_4, "Do you want upload?")) return;
		bufferBatchResult = new StringBuffer();
		
		try{
			batchSize = Integer.valueOf(textBatchSize.getText());
		} catch (Exception e) {
			batchSize = 1000;
		}
		
		File userUploadFile = arryFiles[arryFiles.length-1];
		try {
			// bom마크가 있는지 charset은 무엇인지 확인한다.
			bomInputStream = new BOMInputStream(FileUtils.openInputStream(FileUtils.getFile(userUploadFile) ) );
			ByteOrderMark bom = bomInputStream.getBOM();
			String charsetName = bom == null ? "CP949" : bom.getCharsetName();

			String strTxtFile = FileUtils.readFileToString(userUploadFile, charsetName);			
			if (bom != null){ 
				// ByteOrderMark.UTF_8.equals(strTxtFile.getBytes()[0]);
				//데이터의 첫바이트에 위치하는 BOM마크를 건너뛴다.
				strTxtFile = strTxtFile.substring(1);
			}
			
			String [] strArrySQL = StringUtils.split(strTxtFile, textSeprator.getText());
			ret = runSQLExecuteBatch(Arrays.asList(strArrySQL));
			
			if (ret == 0 ) 
				MessageDialog.openInformation(null, "Confirm", "Store the data.");
		} catch (IOException e) {
			logger.error("File read error", e);
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, "SQL file load exception." + e.getMessage());
			
		} catch (Exception e) {
			logger.error("SQL File import exception", e);
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, "SQL file load exception." + e.getMessage());
		}
	}
	
	private void saveLog(){
		try {
			if("".equals(bufferBatchResult.toString())) {
				MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, "Result log is empty.");
				return;
			}
			String filename = PublicTadpoleDefine.TEMP_DIR + userDB.getDisplay_name() + "_SQLImportResult.log";
			
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter bw = new OutputStreamWriter(fos, "UTF-8");
			
			bw.write(bufferBatchResult.toString());
			bw.close();
			
			String strImportResultLogContent = FileUtils.readFileToString(new File(filename));
			
			downloadExtFile(userDB.getDisplay_name() + "_SQLImportResult.log", strImportResultLogContent);//sbExportData.toString()); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("log writer", ee);
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

		DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
	}

	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @throws Exception
	 */
	private int runSQLExecuteBatch(List<String> listQuery) throws Exception {
		java.sql.Connection conn = null;
		Statement statement = null;
		int result = 0;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			conn = client.getDataSource().getConnection();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			int count = 0;
			
			for (String strQuery : listQuery) {
				if("".equals(StringUtils.trimToEmpty(strQuery))) continue;
				
				statement.addBatch(strQuery);
				if (++count % batchSize == 0) {
					try{
						//TODO: SQLiteDB의 경우 한꺼번에 배치 insert가능한 건수가 200건인지 대량 배치 인서트 작업을 처리할 수 있는 다른 방법을 찾아야함.
						statement.executeBatch();
						//Thread.sleep(100);
						//statement.clearBatch();
						conn.commit();
					} catch(Exception e) {
						result = -1;
						logger.error("Execute Batch error", e);
						bufferBatchResult.append(e.getMessage()+"\n");
						if (btnIgnore.getSelection()) {
							conn.commit();
							continue;
						} else {
							conn.rollback();
							break;
						}							
					}
				}
			}
			
			try{
				statement.executeBatch();
				conn.commit();
			} catch(Exception e) {
				result = -1;
				logger.error("Execute Batch error", e);
				bufferBatchResult.append(e.getMessage()+"\n");
				if (btnIgnore.getSelection()) {
					conn.commit();
				} else {
					conn.rollback();
				}							
			}

			conn.setAutoCommit(true);
			
			if (result < 0 && !"".equals(bufferBatchResult.toString())) {
				MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, bufferBatchResult.toString());
			}
			//TODO: bufferBatchResult 파일로 저장.
		} catch(Exception e) {
			result = -1;
			conn.rollback();
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { if(statement != null) statement.close();} catch(Exception e) {}
			try { if(conn != null) conn.close(); } catch(Exception e){}			
		}
		return result;
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
			insert();
		}else if(buttonId == ID_BTN_EXPORT) {
			saveLog();
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button1 = createButton(parent, ID_BTN_EXPORT, "Save log", false);
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		Button button2 = createButton(parent, ID_BTN_INSERT, "Insert", false);
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 220);
	}

}

