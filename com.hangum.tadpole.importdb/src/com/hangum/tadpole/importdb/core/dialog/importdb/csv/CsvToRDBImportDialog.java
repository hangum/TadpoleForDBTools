///*******************************************************************************
// * Copyright (c) 2014 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.importdb.core.dialog.importdb.csv;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.rap.rwt.service.ServerPushSession;
//import org.eclipse.rap.rwt.widgets.FileUpload;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//
//import com.hangum.tadpole.sql.dao.system.UserDBDAO;
//
///**
// * CSV to RDB Import dialog
// * 
// * @author hangum
// *
// */
//public class CsvToRDBImportDialog extends Dialog {
//	private static final Logger logger = Logger.getLogger(CsvToRDBImportDialog.class);
//	private UserDBDAO userDB;
//	
//	// file upload
//	private FileUpload fileUpload;
//	private DiskFileUploadReceiver receiver;
//	private ServerPushSession pushSession;
//	
//	/**
//	 * Create the dialog.
//	 * @param parentShell
//	 */
//	public CsvToRDBImportDialog(Shell parentShell, UserDBDAO userDB) {
//		super(parentShell);
//		
//		this.userDB = userDB;
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		GridLayout gridLayout = (GridLayout) container.getLayout();
//		gridLayout.horizontalSpacing = 5;
//		gridLayout.verticalSpacing = 5;
//		gridLayout.marginHeight = 5;
//		gridLayout.marginWidth = 5;
//		
//		Composite compositeHead = new Composite(container, SWT.NONE);
//		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeHead.setLayout(new GridLayout(1, false));
//		
//		Label lblFileName = new Label(compositeHead, SWT.NONE);
//		lblFileName.setText("File Name");
//		
//		Label lblDelimiter = new Label(compositeHead, SWT.NONE);
//		lblDelimiter.setText("Delimiter");
//		
//		Composite compositeBody = new Composite(container, SWT.NONE);
//		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		compositeBody.setLayout(new GridLayout(1, false));
//
//		return container;
//	}
//
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, "OK", true);
//		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(661, 533);
//	}
//
//}
