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
package com.hangum.tadpole.mongodb.core.dialogs.resultview;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.mongodb.DBObject;
import org.eclipse.swt.widgets.Label;

/**
 * MongoDB의 결과 result dialog
 * 
 * @author hangum
 *
 */
public class FindResultViewerDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindResultViewerDialog.class);
	private Iterable<DBObject> iteResult = null;
	private String strTitle;
	private UserDBDAO userDB;
	private String initColName;
	
	
	private Label lblInfo;
	/** 쿼리 결과 출력 */
	private MongodbResultComposite compositeResult ;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FindResultViewerDialog(Shell parentShell, String strTitle, UserDBDAO userDB, String initColName, Iterable<DBObject> iteResult) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.strTitle = strTitle;
		this.userDB = userDB;
		this.initColName = initColName;
		
		this.iteResult = iteResult;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText(strTitle); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lblInfo = new Label(composite, SWT.BORDER);
		lblInfo.setText(strTitle);
		
		compositeResult = new MongodbResultComposite(composite, SWT.NONE, userDB, initColName, false);
		compositeResult.setLayout(new GridLayout(1, false));
		
		try {
			compositeResult.refreshDBView(iteResult, 0);
			compositeResult.setResult();
		} catch(Exception e) {
			logger.error("Show Cursor", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Show Cursor", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(581, 503);
	}

}
