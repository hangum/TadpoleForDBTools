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
package com.hangum.tadpole.application.start.dialog.perspective;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.application.start.Perspective;
import com.swtdesigner.ResourceManager;

/**
 * select perstive dialog
 * 
 * @author hangum
 *
 */
public class SelectPerspectiveDialog extends Dialog {
	private Button btnDefault;
	private Button btnManager;
	private Button btnAdmin;
	private String result;

	public SelectPerspectiveDialog(Shell parentShell) {
		super(parentShell);
		result = Perspective.DEFAULT;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SelectPerspectiveDialog_0);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(427, 380);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		Label lblIcon = new Label(container, SWT.NONE);
		lblIcon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		lblIcon.setText("icon"); //$NON-NLS-1$
		lblIcon.setImage(ResourceManager.getPluginImageDescriptor(BrowserActivator.APPLICTION_ID, "resources/icons/TadpoleForDBTools_log.png").createImage()); //$NON-NLS-1$

		Label lblYouDoNot = new Label(container, SWT.WRAP);
		lblYouDoNot.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
		lblYouDoNot.setText(Messages.get().SelectPerspectiveDialog_3
				+ "\n\n> Preference >> Perspective"); //$NON-NLS-1$

		Group grpSelectPerspective = new Group(container, SWT.NONE);
		grpSelectPerspective.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		grpSelectPerspective.setText(Messages.get().SelectPerspectiveDialog_5);
		grpSelectPerspective.setLayout(new GridLayout(1, false));

		btnDefault = new Button(grpSelectPerspective, SWT.RADIO);
		btnDefault.setText(Messages.get().SelectPerspectiveDialog_6);

		btnManager = new Button(grpSelectPerspective, SWT.RADIO);
		btnManager.setText(Messages.get().SelectPerspectiveDialog_7);

		btnAdmin = new Button(grpSelectPerspective, SWT.RADIO);
		btnAdmin.setText(Messages.get().SelectPerspectiveDialog_8);

		btnDefault.setSelection(true);

		return container;
	}

	@Override
	protected void okPressed() {
		if (btnAdmin.getSelection()) {
			result = Perspective.ADMIN;
		} else if (btnManager.getSelection()) {
			result = Perspective.MANAGER;
		} else {
			result = Perspective.DEFAULT;
		}
		super.okPressed();
	}

	public String getResult() {
		return result;
	}

}
