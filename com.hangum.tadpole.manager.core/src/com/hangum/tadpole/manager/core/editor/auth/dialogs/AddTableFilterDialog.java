/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.auth.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

/**
 * Add Table Filter Dialog
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class AddTableFilterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AddTableFilterDialog.class);
	private Text textDescription;
	private Text textColumnNames;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddTableFilterDialog(Shell parentShell) {
		super(parentShell);
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
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblTableName = new Label(compositeBody, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText("Table Name");
		
		Combo comboTableName = new Combo(compositeBody, SWT.NONE);
		comboTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(compositeBody, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_textDescription.heightHint = 60;
		gd_textDescription.minimumHeight = 30;
		textDescription.setLayoutData(gd_textDescription);
		new Label(compositeBody, SWT.NONE);
		
		Button btnTableFullLock = new Button(compositeBody, SWT.CHECK);
		btnTableFullLock.setText("Table full Lock");
		
		Label lblColumnName = new Label(compositeBody, SWT.NONE);
		lblColumnName.setText("Column Name");
		
		Label lblSeparate = new Label(compositeBody, SWT.NONE);
		lblSeparate.setText("ex) Separate ,");
		new Label(compositeBody, SWT.NONE);
		
		textColumnNames = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_textColumnNames = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textColumnNames.heightHint = 70;
		textColumnNames.setLayoutData(gd_textColumnNames);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
