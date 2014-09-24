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
import java.io.IOException;
import java.sql.Statement;
import java.util.Arrays;
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
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
	
	private int ID_BTN_INSERT		 = IDialogConstants.CLIENT_ID 	+ 1;
	
	private UserDBDAO userDB;
	private static final String INITIAL_TEXT = "No files uploaded."; //$NON-NLS-1$
	
	// file upload
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;
	
	private Text fileNameLabel;
	private Text textSeprator;
	
	private int batchSize = 1000;

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
		lblFileName.setText(Messages.CsvToRDBImportDialog_1);
		
		fileNameLabel = new Text(compositeHead, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();
		
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
			
//		Button btnInsert = new Button(compositeHead, SWT.NONE);
//		btnInsert.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				insert();
//			}
//		});
//		btnInsert.setText(Messages.SQLToDBImportDialog_btnInsert_text);
		
		return container;
	}
	
	private void insert() {
		File[] arryFiles = receiver.getTargetFiles();
		if(arryFiles.length == 0) {
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_21);
			return ;
		}
		
		if(!MessageDialog.openConfirm(null, Messages.CsvToRDBImportDialog_4, "Do you want upload?")) return;
		
		File userUploadFile = arryFiles[arryFiles.length-1];
		try {
			String strTxtFile = FileUtils.readFileToString(userUploadFile);
			String [] strArrySQL = StringUtils.split(strTxtFile, textSeprator.getText());
			runSQLExecuteBatch(Arrays.asList(strArrySQL));
			
			MessageDialog.openInformation(null, "Confirm", "Store the data.");
		} catch (IOException e) {
			logger.error("File read error", e);
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, "SQL file load exception." + e.getMessage());
			
		} catch (Exception e) {
			logger.error("SQL File import exception", e);
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, "SQL file load exception." + e.getMessage());
		}
	}
	
	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @throws Exception
	 */
	private void runSQLExecuteBatch(List<String> listQuery) throws Exception {
		java.sql.Connection conn = null;
		Statement statement = null;
		
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
					statement.executeBatch();
				}
			}
			statement.executeBatch();
			conn.commit();
			
			conn.setAutoCommit(true);
		} catch(Exception e) {
			conn.rollback();
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { if(statement != null) statement.close();} catch(Exception e) {}
			try { if(conn != null) conn.close(); } catch(Exception e){}
		}
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
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, ID_BTN_INSERT, "Insert", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 170);
	}

}
