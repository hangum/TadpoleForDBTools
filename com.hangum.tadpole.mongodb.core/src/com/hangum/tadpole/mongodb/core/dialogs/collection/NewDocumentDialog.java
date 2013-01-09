/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.dialogs.collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.util.JSONUtil;
import com.hangum.tadpole.util.TadpoleWidgetUtils;

/**
 * 신규 document 를 생성합니다.
 * 
 * @author hangum
 *
 */
public class NewDocumentDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewDocumentDialog.class);

	protected UserDBDAO userDB;
	protected String collectionName;
	
	protected Text textName;
	protected Text textContent;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewDocumentDialog(Shell parentShell, UserDBDAO userDB, String collectionName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
		this.collectionName = collectionName;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Collection Name"); //$NON-NLS-1$
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.setText(collectionName);
		
		Label lblDataStructure = new Label(container, SWT.NONE);
		lblDataStructure.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblDataStructure.setText("JSON Type Document"); //$NON-NLS-1$
		
		textContent = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);		
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		textContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textContent.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textContent.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		textContent.setFocus();
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textContent.getText().trim())) { //$NON-NLS-1$
			
			textContent.setFocus();
			MessageDialog.openError(null, Messages.NewDocumentDialog_3, Messages.NewDocumentDialog_4);
			return;
		}

		try {
			MongoDBQuery.insertDocument(userDB, textName.getText().trim(), textContent.getText().trim());
		} catch (Exception e) {
			logger.error("mongodb create collection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Create Collection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return;
		}
		
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("MongoDB New Document Dialog"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	private static int FORMAT_ID = 999;
	@Override
	protected void createButtonsForButtonBar(Composite parent) {		
		Button button = createButton(parent, FORMAT_ID, "Format", false); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textContent.setText( JSONUtil.getPretty(textContent.getText()) );
			}
		});
		
		createButton(parent, IDialogConstants.OK_ID, "Ok", true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 476);
	}

}
