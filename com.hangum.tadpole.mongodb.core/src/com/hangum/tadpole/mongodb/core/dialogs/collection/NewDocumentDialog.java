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
package com.hangum.tadpole.mongodb.core.dialogs.collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

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
	private static int FORMAT_BTN_ID = IDialogConstants.CLIENT_ID + 1;

	protected UserDBDAO userDB;
	protected String collectionName;
	
	protected Text textName;
	protected TadpoleEditorWidget textContent;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewDocumentDialog(Shell parentShell, UserDBDAO userDB, String collectionName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
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
		
		textContent = new TadpoleEditorWidget(container, SWT.BORDER, EditorDefine.EXT_JSON, "", "");		
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		textContent.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textContent.getText().trim())) { //$NON-NLS-1$
			
			textContent.setFocus();
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().NewCollectionDialog_8);
			return;
		}

		try {
			MongoDBQuery.insertDocument(userDB, textName.getText().trim(), textContent.getText().trim());
		} catch (Exception e) {
			logger.error("mongodb create collection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Create Collection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return;
		}
		
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().NewDocumentDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {		
		Button button = createButton(parent, FORMAT_BTN_ID, Messages.get().NewDocumentDialog_1, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textContent.setText( JSONUtil.getPretty(textContent.getText()) );
			}
		});
		
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Insert, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 476);
	}

}
