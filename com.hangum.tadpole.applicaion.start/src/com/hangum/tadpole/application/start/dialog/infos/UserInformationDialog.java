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
package com.hangum.tadpole.application.start.dialog.infos;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.application.start.Messages;
import com.swtdesigner.SWTResourceManager;

/**
 * When your browser shows incorrect information.
 * 
 * @author hangum
 *
 */
public class UserInformationDialog extends Dialog {
	
	private String userBroser = "";

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public UserInformationDialog(Shell parentShell, String userBrowser) {
		super(parentShell);
		
		this.userBroser = userBrowser;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Check Information");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		// 
		Label lblInformation = new Label(container, SWT.NONE);
		lblInformation.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		lblInformation.setText("User browser is " + userBroser + ".");
		
		Label lblRecommand = new Label(container, SWT.NONE);
		lblRecommand.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblRecommand.setText(Messages.LoginDialog_lblNewLabel_text);

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
		return new Point(450, 150);
	}

}
