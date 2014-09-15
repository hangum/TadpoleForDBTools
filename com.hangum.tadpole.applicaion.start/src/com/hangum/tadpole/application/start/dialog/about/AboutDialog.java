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
package com.hangum.tadpole.application.start.dialog.about;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.swtdesigner.ResourceManager;

/**
 * About dialog
 * 
 * @author hangum
 *
 */
public class AboutDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AboutDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("About...");
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
		gridLayout.numColumns = 2;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Label lblNewLabelImage = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabelImage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabelImage.heightHint = 199;
		gd_lblNewLabelImage.widthHint = 150;
		gd_lblNewLabelImage.minimumHeight = 184;
		gd_lblNewLabelImage.minimumWidth = 300;
		lblNewLabelImage.setLayoutData(gd_lblNewLabelImage);
//		lblNewLabel_1.setText(Messages.AboutDialog_lblNewLabel_1_text);
		lblNewLabelImage.setImage(ResourceManager.getPluginImage(BrowserActivator.APPLICTION_ID, "resources/icons/TadpoleForDBTools.png"));
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Thanks, stariki
		Text txtVersion = new Text(composite_1, SWT.NONE) ;
		txtVersion.setText(Messages.AboutAction_3 + " Version " + SystemDefine.MAJOR_VERSION + " SR " + SystemDefine.SUB_VERSION);
		txtVersion.setEditable(false);

		Text txtRleaseDate = new Text(composite_1, SWT.NONE) ;
		txtRleaseDate.setText(Messages.AboutDialog_lblReleaseDate_text + " " + SystemDefine.RELEASE_DATE);
		txtRleaseDate.setEditable(false);
		
		Label label = new Label(composite_1, SWT.NONE);
		
		Label lblNewLabel0 = new Label(composite_1, SWT.NONE);
		lblNewLabel0.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel0.setText("Mail : <a href=\"mailto:adi.tadpole@gmail.com\" target=\"_blank\">adi.tadpole@gmail.com</a>");//Messages.AboutAction_4);

		Label lblNewLabel2 = new Label(composite_1, SWT.NONE);
		lblNewLabel2.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel2.setText("Home : <a href=\"https://github.com/hangum/TadpoleForDBTools\" target=\"_blank\">https://github.com/hangum/TadpoleForDBTools</a>");//Messages.AboutAction_4);
		
		Label lblNewLabel3 = new Label(composite_1, SWT.NONE);
		lblNewLabel3.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel3.setText("Register Issue : <a href=\"https://github.com/hangum/TadpoleForDBTools/issues?state=open\" target=\"_blank\">https://github.com/hangum/TadpoleForDBTools/issues?state=open</a>");//Messages.AboutAction_4);
		
		Label lblLicenseLgpl = new Label(composite_1, SWT.NONE);
		lblLicenseLgpl.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblLicenseLgpl.setText("License: <a href=\"http://www.gnu.org/licenses/why-not-lgpl.en.html\" target=\"_blank\">LGPL</a>");
		
		Label lblNewLabel4 = new Label(composite_1, SWT.NONE);
		lblNewLabel4.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel4.setText(Messages.AboutAction_5);

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
		return new Point(635, 308);
	}
}
