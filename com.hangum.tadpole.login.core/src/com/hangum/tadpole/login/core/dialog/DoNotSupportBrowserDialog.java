/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.login.core.dialog;

import java.util.Properties;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.login.core.message.LoginDialogMessages;

/**
 * Do not support dialog
 * 
 * @author hangum
 *
 */
public class DoNotSupportBrowserDialog extends Dialog {
	private String strDwnContextMac = "";
	private String strDwnContextWin = "";
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DoNotSupportBrowserDialog(Shell parentShell) {
		super(parentShell);

		Properties prop = LoadConfigFile.getConfig();
		strDwnContextMac = prop.getProperty("tadpole.browser.download.context.mac", "https://www.mozilla.org/firefox/new/?scene=2");
		strDwnContextWin = prop.getProperty("tadpole.browser.download.context.win", "https://www.mozilla.org/firefox/new/?scene=2");
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CommonMessages.get().Information);
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
		
		Label lblDo = new Label(container, SWT.NONE);
		lblDo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String errMsg = LoginDialogMessages.get().LoginDialog_30 + RequestInfoUtils.getUserBrowser() + ". " + LoginDialogMessages.get().UserInformationDialog_5 + "\n" + LoginDialogMessages.get().LoginDialog_support_browserip;  //$NON-NLS-1$//$NON-NLS-2$
		lblDo.setText(errMsg);
		
		Group grpDownload = new Group(container, SWT.NONE);
		grpDownload.setLayout(new GridLayout(2, false));
		grpDownload.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpDownload.setText(CommonMessages.get().Download);
		
		Label label = new Label(grpDownload, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 20;
		label.setLayoutData(gd_label);
		label.setText("    ");
		
		Label lblWinDownload = new Label(grpDownload, SWT.NONE);
		lblWinDownload.setText("<a href='" + strDwnContextWin + "' target='_blank'>Windown Firefox Download</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblWinDownload.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		new Label(grpDownload, SWT.NONE);
		Label lblMacDownload = new Label(grpDownload, SWT.NONE);
		lblMacDownload.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblMacDownload.setText("<a href='" + strDwnContextMac + "' target='_blank'>Mac Firefox Download</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblMacDownload.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 213);
	}
}
