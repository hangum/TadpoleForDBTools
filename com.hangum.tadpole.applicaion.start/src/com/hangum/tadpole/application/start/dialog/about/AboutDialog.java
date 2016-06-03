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

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
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
		newShell.setText(Messages.get().AboutDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		lblNewLabelImage.setImage(ResourceManager.getPluginImage(BrowserActivator.APPLICTION_ID, "resources/icons/TadpoleForDBTools.png")); //$NON-NLS-1$
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Thanks, stariki
		Text txtVersion = new Text(composite_1, SWT.NONE) ;
		txtVersion.setText(Messages.get().AboutAction_3 + " V" + SystemDefine.MAJOR_VERSION + " " + SystemDefine.SUB_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
		txtVersion.setEditable(false);

		Text txtRleaseDate = new Text(composite_1, SWT.NONE) ;
		txtRleaseDate.setText(Messages.get().AboutDialog_lblReleaseDate_text + " " + SystemDefine.RELEASE_DATE); //$NON-NLS-1$
		txtRleaseDate.setEditable(false);
		
		new Label(composite_1, SWT.NONE);
		
		Label lblNewLabel0 = new Label(composite_1, SWT.NONE);
		lblNewLabel0.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel0.setText(Messages.get().AboutDialog_5);

		Label lblNewLabel2 = new Label(composite_1, SWT.NONE);
		lblNewLabel2.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel2.setText(Messages.get().AboutDialog_6);
		
		Label lblNewLabel3 = new Label(composite_1, SWT.NONE);
		lblNewLabel3.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel3.setText(Messages.get().AboutDialog_7);
		
		Label lblLicenseLgpl = new Label(composite_1, SWT.NONE);
		lblLicenseLgpl.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblLicenseLgpl.setText(Messages.get().AboutDialog_8);
		
		Label lblNewLabel4 = new Label(composite_1, SWT.NONE);
		lblNewLabel4.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel4.setText(Messages.get().AboutAction_5);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(635, 308);
	}
}
