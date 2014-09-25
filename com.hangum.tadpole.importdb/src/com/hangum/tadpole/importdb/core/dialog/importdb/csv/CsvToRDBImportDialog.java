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
import java.sql.Connection;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.csv.CSVLoader;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.importdb.core.Messages;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

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
	
	private Button btnTruncateBeforeInsert;
	
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
		lblTableName.setText(Messages.CsvToRDBImportDialog_0);
		
		textTableName = new Text(compositeHead, SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
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
		lblSeprator.setText(Messages.CsvToRDBImportDialog_6);
		
		textSeprator = new Text(compositeHead, SWT.BORDER);
		textSeprator.setText(","); //$NON-NLS-1$
		textSeprator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnGenrateSql = new Button(compositeHead, SWT.NONE);
		btnGenrateSql.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate()) return;
				
				File[] arryFiles = receiver.getTargetFiles();
				File userDBFile = arryFiles[arryFiles.length-1];
				
				CSVLoader loader = new CSVLoader(textSeprator.getText());
				try {
					String strInsertSQL = loader.generateSQL(userDBFile, textTableName.getText());
					if(logger.isDebugEnabled()) logger.debug(strInsertSQL);
					textSQL.setText(strInsertSQL);
				} catch (Exception e1) {
					logger.error("CSV load error", e1); //$NON-NLS-1$
					MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_10 + e1.getMessage());
				}
			}
		});
		btnGenrateSql.setText(Messages.CsvToRDBImportDialog_11);
		new Label(compositeHead, SWT.NONE);
		
		btnTruncateBeforeInsert = new Button(compositeHead, SWT.CHECK);
		btnTruncateBeforeInsert.setText(Messages.CsvToRDBImportDialog_12);
		
		Group grpSqlTemplate = new Group(container, SWT.NONE);
		grpSqlTemplate.setLayout(new GridLayout(1, false));
		grpSqlTemplate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSqlTemplate.setText(Messages.CsvToRDBImportDialog_16);
		
		textSQL = new Text(grpSqlTemplate, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		textTableName.setFocus();
		
		return container;
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
		
		CSVLoader loader = new CSVLoader(textSeprator.getText());
		Connection conn =  null;
		try {
			conn = TadpoleSQLManager.getInstance(userDB).getDataSource().getConnection();
			int count = loader.loadCSV(conn, userDBFile, textTableName.getText(), btnTruncateBeforeInsert.getSelection());
			
			MessageDialog.openInformation(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_26 + "\n count is "+ count);
		} catch (Exception e1) {
			logger.error("CSV load error", e1); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.CsvToRDBImportDialog_4, Messages.CsvToRDBImportDialog_29 + e1.getMessage());
			
			return;
		} finally {
			if(conn != null) try { conn.close(); } catch(Exception e) {} 
		}
		
		super.okPressed();
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
		createButton(parent, ID_BTN_INSERT, "Insert", false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.CsvToRDBImportDialog_30, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 500);
	}

}
