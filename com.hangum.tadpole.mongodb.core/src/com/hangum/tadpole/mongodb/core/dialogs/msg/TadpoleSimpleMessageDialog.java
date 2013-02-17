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
package com.hangum.tadpole.mongodb.core.dialogs.msg;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.editor.core.widgets.editor.TadpoleOrionHubEditor;

/**
 * tadpole message dialog
 * 
 * @author hangum
 *
 */
public class TadpoleSimpleMessageDialog extends TitleAreaDialog {
	private static final Logger logger = Logger.getLogger(TadpoleSimpleMessageDialog.class);
	private TadpoleOrionHubEditor textMessage;
	
	String title;
	String message;
	private Label lblMessage;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TadpoleSimpleMessageDialog(Shell parentShell, String title, String message) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.title = title;
		this.message = message;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Tadpole Dialog"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(title);
		setTitle("Tadpole Message");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		lblMessage = new Label(container, SWT.NONE);
		lblMessage.setText("Message");
		new Label(container, SWT.NONE);
		
		textMessage = new TadpoleOrionHubEditor(container, SWT.BORDER, message);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Ok", true); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 422);
	}
}
