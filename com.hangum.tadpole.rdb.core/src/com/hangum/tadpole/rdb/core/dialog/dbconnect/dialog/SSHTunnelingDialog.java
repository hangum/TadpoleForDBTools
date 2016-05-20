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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.mongodb.core.Messages;

/**
 * SSH Tunneling
 * 
 * @author hangum
 *
 */
public class SSHTunnelingDialog extends Dialog {
	private Text textIP;
	private Text textPort;
	private Text textID;
	private Text textPasswd;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SSHTunnelingDialog(Shell parentShell) {
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
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnUseSsh = new Button(composite, SWT.CHECK);
		btnUseSsh.setText("Use SSH");
		new Label(composite, SWT.NONE);
		
		Label lblIp = new Label(composite, SWT.NONE);
		lblIp.setText("IP");
		
		textIP = new Text(composite, SWT.BORDER);
		textIP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setText("Port");
		
		textPort = new Text(composite, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setText("ID");
		
		textID = new Text(composite, SWT.BORDER);
		textID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setText("Password");
		
		textPasswd = new Text(composite, SWT.BORDER);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,  Messages.get().Add, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(341, 282);
	}
}
