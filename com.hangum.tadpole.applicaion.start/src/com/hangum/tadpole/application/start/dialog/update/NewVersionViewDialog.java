/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start.dialog.update;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

/**
 * new version view diloag
 * 
 * @author hangum
 *
 */
public class NewVersionViewDialog extends TitleAreaDialog {
	NewVersionObject newVersionObj;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewVersionViewDialog(Shell parentShell, NewVersionObject newVersionObj) {
		super(parentShell);
		
		this.newVersionObj = newVersionObj;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("New version information");
		setTitle("New Version Checker");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblCurrentVersion = new Label(container, SWT.NONE);
		lblCurrentVersion.setText("Current Version");
		
		Label lblCurrentversionvalue = new Label(container, SWT.NONE);
		lblCurrentversionvalue.setText(newVersionObj.getMajorVer() + " " + newVersionObj.getSubVer());
		
		Label lblReleaseData = new Label(container, SWT.NONE);
		lblReleaseData.setText("Release Data");
		
		Label lblReleasedatavalue = new Label(container, SWT.NONE);
		lblReleasedatavalue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblReleasedatavalue.setText(newVersionObj.getDate());
		
		Label lblDownloadUrl = new Label(container, SWT.NONE);
		lblDownloadUrl.setText("Download URL");
		
		Label lblDownloadurlvalue = new Label(container, SWT.NONE);
		lblDownloadurlvalue.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblDownloadurlvalue.setText(String.format("<a href='%s' target='_blank'>Click</a>", newVersionObj.getDownload_url()));
		
		Label lblInformationUrl = new Label(container, SWT.NONE);
		lblInformationUrl.setText("Information URL");
		
		Label lblInformationurlvalue = new Label(container, SWT.NONE);
		lblInformationurlvalue.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblInformationurlvalue.setText(String.format("<a href='%s' target='_blank'>Click</a>", newVersionObj.getInfo_url()));

		return area;
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
		return new Point(552, 243);
	}
}
