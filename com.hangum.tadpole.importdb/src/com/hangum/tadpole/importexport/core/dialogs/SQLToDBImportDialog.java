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
package com.hangum.tadpole.importexport.core.dialogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.importexport.core.Messages;
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
		newShell.setText(userDB.getDisplay_name() + Messages.get().SQLToDBImportDialog_1);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		lblFileName.setText(Messages.get().CsvToRDBImportDialog_1);
		
		fileNameLabel = new Text(compositeHead, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();

		/* fileUpload 주석 후 디자인을 위한 임시 컨트롤 */
		/*
		Label lblDumy = new Label(compositeHead, SWT.NONE);
		lblDumy.setText(Messages.get().CsvToRDBImportDialog_2);
		lblDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		*/		
		fileUpload = new FileUpload(compositeHead, SWT.NONE);
		fileUpload.setText(Messages.get().CsvToRDBImportDialog_2);
		fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				if("".equals(fileName) || null == fileName) return; //$NON-NLS-1$
				
				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().CsvToRDBImportDialog_5)) return;
				fileNameLabel.setText(fileName == null ? "" : fileName); //$NON-NLS-1$
				
				pushSession.start();
				fileUpload.submit(url);
			}
		});		
		
		Label lblSeprator = new Label(compositeHead, SWT.NONE);
		lblSeprator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeprator.setText(Messages.get().SQLToDBImportDialog_lblSeprator_text);
		
		textSeprator = new Text(compositeHead, SWT.BORDER);
		textSeprator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblBatchSize = new Label(compositeHead, SWT.NONE);
		lblBatchSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBatchSize.setText(Messages.get().SQLToDBImportDialog_0);
		
		textBatchSize = new Text(compositeHead, SWT.BORDER | SWT.RIGHT);		
		if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT ) {
			//SQLite 는 BatchExecute작업이 한번에 200건 이상 처리시 database logic에러가 발생하고 있어서 1건마다 executeBatch 및 commit을 하도록 한다.
			textBatchSize.setEditable(false);
			textBatchSize.setText(Messages.get().SQLToDBImportDialog_2);
		}else{
			textBatchSize.setEditable(true);
			textBatchSize.setText(Messages.get().SQLToDBImportDialog_BatchSize);
		}
		textBatchSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblException = new Label(compositeHead, SWT.NONE);
		lblException.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblException.setText(Messages.get().SQLToDBImportDialog_Exception);
		
		Composite composite = new Composite(compositeHead, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		btnIgnore = new Button(composite, SWT.RADIO);
		GridData gd_btnIgnore = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnIgnore.widthHint = 98;
		btnIgnore.setLayoutData(gd_btnIgnore);
		btnIgnore.setText(Messages.get().SQLToDBImportDialog_Ignore);
		
		btnStop = new Button(composite, SWT.RADIO);
		btnStop.setSelection(true);
		GridData gd_btnStop = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnStop.widthHint = 92;
		btnStop.setLayoutData(gd_btnStop);
		btnStop.setText(Messages.get().SQLToDBImportDialog_Stop);
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
	
	private void insert() throws IOException {
		int ret;
		BOMInputStream bomInputStream = null ;
		
		File[] arryFiles = receiver.getTargetFiles();
		if(arryFiles.length == 0) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().CsvToRDBImportDialog_21);
			return ;
		}
		
		if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLToDBImportDialog_UploadQuestion)) return;
		bufferBatchResult = new StringBuffer();
		
		try{
			batchSize = Integer.valueOf(textBatchSize.getText());
		} catch (Exception e) {
			batchSize = 1000;
		}
		
		File userUploadFile = arryFiles[arryFiles.length-1];
		try {
			// bom마크가 있는지 charset은 무엇인지 확인한다.
			bomInputStream = new BOMInputStream(FileUtils.openInputStream(FileUtils.getFile(userUploadFile)));//`, false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32BE);
			
			String charsetName = "utf-8";  //$NON-NLS-1$
			String strSQLData = ""; //$NON-NLS-1$
			if(bomInputStream.getBOM() == null) {
				strSQLData = FileUtils.readFileToString(userUploadFile, charsetName);
			}else{
				charsetName = bomInputStream.getBOMCharsetName();
				strSQLData = FileUtils.readFileToString(userUploadFile, charsetName).substring(1);
			}
			
			String [] strArrySQL = StringUtils.split(strSQLData, textSeprator.getText());
			ret = runSQLExecuteBatch(Arrays.asList(strArrySQL));
			
			if (ret == 0 ) 
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().SQLToDBImportDialog_StoreData); //$NON-NLS-1$
		} catch (IOException e) {
			logger.error(Messages.get().SQLToDBImportDialog_ReadError, e);
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().SQLToDBImportDialog_LoadException + e.getMessage());
			
		} catch (Exception e) {
			logger.error(Messages.get().SQLToDBImportDialog_ImportException, e);
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().SQLToDBImportDialog_LoadException + e.getMessage());
		} finally {
			if(bomInputStream != null) bomInputStream.close();
		}
	}
	
	private void saveLog(){
		try {
			if(bufferBatchResult == null || "".equals(bufferBatchResult.toString())) { //$NON-NLS-1$
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().SQLToDBImportDialog_LogEmpty);
				return;
			}
			String filename = PublicTadpoleDefine.TEMP_DIR + userDB.getDisplay_name() + "_SQLImportResult.log"; //$NON-NLS-1$
			
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter bw = new OutputStreamWriter(fos, "UTF-8"); //$NON-NLS-1$
			
			bw.write(bufferBatchResult.toString());
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
				if("".equals(StringUtils.trimToEmpty(strQuery))) continue; //$NON-NLS-1$
				
				statement.addBatch(strQuery);
				if (++count % batchSize == 0) {
					try{
						statement.executeBatch();
					} catch(SQLException e) {
						logger.error("Execute Batch error", e); //$NON-NLS-1$
						bufferBatchResult.append(e.getMessage()+"\n"); //$NON-NLS-1$
						
						SQLException ne = e.getNextException();						
						while (ne != null){
							logger.error("NEXT SQLException is ", ne);//$NON-NLS-1$
							bufferBatchResult.append(ne.getMessage()+"\n"); //$NON-NLS-1$
							ne = ne.getNextException();
						}
						
						if (btnIgnore.getSelection()) {
							conn.commit();
							continue;
						} else {
							conn.rollback();
							result = -1;
							break;
						}							
					}
				}
			}
			
			statement.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			
			if (result < 0 && !"".equals(bufferBatchResult.toString())) { //$NON-NLS-1$
				MessageDialog.openWarning(null, Messages.get().Warning, bufferBatchResult.toString());
			}
		} catch (SQLException e) {
			logger.error("Execute Batch error", e); //$NON-NLS-1$
			bufferBatchResult.append(e.getMessage()+"\n"); //$NON-NLS-1$
			if (btnIgnore.getSelection()) {
				conn.commit();
			}else{
				conn.rollback();
			}
			
			SQLException ne = e.getNextException();
			while (ne != null){
				logger.error("Execute Batch error", e); //$NON-NLS-1$
				bufferBatchResult.append(e.getMessage()+"\n"); //$NON-NLS-1$
				ne = ne.getNextException();
			}
		} catch(Exception e) {
			result = -1;
			logger.error("Execute Batch error", e); //$NON-NLS-1$
			bufferBatchResult.append(e.getMessage()+"\n"); //$NON-NLS-1$
			conn.rollback();
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
			try {
				insert();
			} catch (IOException e) {
				logger.error("Execute batch insert to db.", e); //$NON-NLS-1$
			}
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
		Button button1 = createButton(parent, ID_BTN_EXPORT, Messages.get().SQLToDBImportDialog_SaveLog, false);
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		Button button2 = createButton(parent, ID_BTN_INSERT, Messages.get().SQLToDBImportDialog_Insert, false);
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 220);
	}

}
