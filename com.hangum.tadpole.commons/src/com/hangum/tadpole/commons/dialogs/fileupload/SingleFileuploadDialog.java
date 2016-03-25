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
package com.hangum.tadpole.commons.dialogs.fileupload;

import java.io.File;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
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

import com.hangum.tadpole.commons.Messages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;

/**
 * SQL to db import dialog
 * 
 * @author hangum
 *
 */
public class SingleFileuploadDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SingleFileuploadDialog.class);
	private String strTitle = ""; //$NON-NLS-1$
	
	private static final String INITIAL_TEXT = "No files uploaded."; //$NON-NLS-1$
	
	// file upload
	private FileUpload fileUpload;
	private DiskFileUploadReceiver receiver;
	private ServerPushSession pushSession;
	
	private Text fileNameLabel;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SingleFileuploadDialog(Shell parentShell, String strTitle) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.strTitle = strTitle;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(strTitle);
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
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblFileName = new Label(compositeHead, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText(Messages.get().SingleFileuploadDialog_1);
		
		fileNameLabel = new Text(compositeHead, SWT.BORDER);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileNameLabel.setEditable(false);

		final String url = startUploadReceiver();
		pushSession = new ServerPushSession();

		/* fileUpload 주석 후 디자인을 위한 임시 컨트롤 */
		/*
		Label lblDumy = new Label(compositeHead, SWT.NONE);
		lblDumy.setText(Messages.get().CsvToRDBImportDialog_2);
		lblDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		*/		
		fileUpload = new FileUpload(compositeHead, SWT.NONE);
		fileUpload.setText(Messages.get().FileUploadDialog_fileSelect);
		fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				if("".equals(fileName) || null == fileName) return; //$NON-NLS-1$
				
//				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, "Do you want file upload?")) return;
				fileNameLabel.setText(fileName == null ? "" : fileName); //$NON-NLS-1$
				
				pushSession.start();
				fileUpload.submit(url);
			}
		});		
			
		return container;
	}
	
	@Override
	protected void okPressed() {
		try {
			if(!insert()) return;
		} catch(Exception e) {
			MessageDialog.openError(null, Messages.get().Error, e.getMessage());
			return;
		}
		
		super.okPressed();
	}
	
	public String getStrTxtFile() {
		return strTxtFile;
	}

	String strTxtFile = ""; //$NON-NLS-1$
	private boolean insert() throws Exception {
		BOMInputStream bomInputStream ;
		
		File[] arryFiles = receiver.getTargetFiles();
		if(arryFiles.length == 0) {
			throw new Exception(Messages.get().SingleFileuploadDialog_5);
		}
		
		File userUploadFile = arryFiles[arryFiles.length-1];
		try {
			// bom마크가 있는지 charset은 무엇인지 확인한다.
			bomInputStream = new BOMInputStream(FileUtils.openInputStream(FileUtils.getFile(userUploadFile) ) );
			ByteOrderMark bom = bomInputStream.getBOM();
			String charsetName = bom == null ? "CP949" : bom.getCharsetName(); //$NON-NLS-1$

			strTxtFile = FileUtils.readFileToString(userUploadFile, charsetName);			
			if (bom != null){ 
				// ByteOrderMark.UTF_8.equals(strTxtFile.getBytes()[0]);
				//데이터의 첫바이트에 위치하는 BOM마크를 건너뛴다.
				strTxtFile = strTxtFile.substring(1);
			}
						
		} catch (Exception e) {
			logger.error("file read error", e); //$NON-NLS-1$
			throw new Exception(Messages.get().SingleFileuploadDialog_7 + e.getMessage());
		}
		
		return true;
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
		
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Confirm, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancle, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 130);
	}

}
