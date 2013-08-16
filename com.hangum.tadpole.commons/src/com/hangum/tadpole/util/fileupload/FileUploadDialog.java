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
package com.hangum.tadpole.util.fileupload;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.lifecycle.UICallBack;
import org.eclipse.rap.rwt.supplemental.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadEvent;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadHandler;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadListener;
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

import com.hangum.tadpole.Messages;

/**
 * File Upload
 * 
 * @author hangum
 *
 */
public class FileUploadDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileUploadDialog.class);

	private FileUpload fileUpload;
	private Label fileNameLabel;
	
	private Label lblUploadComplet;
	
	/** uploaded file list */
	private List<String> listFiles = new ArrayList<String>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FileUploadDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.FileUploadDialog_0);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(4, false));
		
		Label lblFileupload = new Label(compositeHead, SWT.NONE);
		lblFileupload.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileupload.setText(Messages.FileUploadDialog_1);
		
		fileNameLabel = new Label(compositeHead, SWT.NONE);
		fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileNameLabel.setText(""); //$NON-NLS-1$
		
		fileUpload = new FileUpload(compositeHead, SWT.BORDER);
		fileUpload.setText(Messages.FileUploadDialog_3);
		fileUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = fileUpload.getFileName();
				fileNameLabel.setText(fileName == null ? "" : fileName); //$NON-NLS-1$
			}
		});
		final String url = startUploadReceiver();
		
		Button uploadButton = new Button(compositeHead, SWT.NONE);
		uploadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UICallBack.activate("upload"); //$NON-NLS-1$
				fileUpload.submit(url);
			}
		});
		uploadButton.setText(Messages.FileUploadDialog_4);
		
		Composite compositeTail = new Composite(container, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTail.setLayout(new GridLayout(2, false));
		
		lblUploadComplet = new Label(compositeTail, SWT.NONE);
		lblUploadComplet.setText("");
		lblUploadComplet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		return container;
	}
	
	/**
	 * 저장 이벤트 
	 * 
	 * @return
	 */
	private String startUploadReceiver() {
		listFiles.clear();
		final DiskFileUploadReceiver receiver = new DiskFileUploadReceiver();
		FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
		uploadHandler.addUploadListener(new FileUploadListener() {

			public void uploadProgress(FileUploadEvent event) {
			}

			public void uploadFailed(FileUploadEvent event) {
				addToLog( "upload failed: " + event.getFileName() ); //$NON-NLS-1$
			}

			public void uploadFinished(FileUploadEvent event) {				
				addToLog( "received: " + event.getFileName() ); //$NON-NLS-1$
				listFiles.add(receiver.getTargetFile().getAbsolutePath());
			}			
		});
		
		return uploadHandler.getUploadUrl();
	}

	/**
	 * add upload
	 * 
	 * @param message
	 */
	private void addToLog(final String message) {
		fileNameLabel.getDisplay().asyncExec(new Runnable() {
			public void run() {
				UICallBack.deactivate("upload"); //$NON-NLS-1$
				lblUploadComplet.setText(Messages.FileUploadDialog_6);
			}
		});
	}
	
	@Override
	protected void okPressed() {
		if(listFiles.size() == 0) {
			MessageDialog.openError(null, "Error", Messages.FileUploadDialog_8); //$NON-NLS-1$
			return;
//		} else {
//			if(!MessageDialog.openConfirm(null, "Confirm", "파일을 업로드 하시겠습니까?")) return; 
		}

		super.okPressed();
	}
	
	public List<String> getListFiles() {
		return listFiles;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(452, 200);
	}

}
