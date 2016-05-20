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
package com.hangum.tadpole.application.start.update.checker;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.rap.rwt.RWT;
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

import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.util.CookieUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;

/**
 * new version view diloag
 * 
 * @author hangum
 *
 */
public class NewVersionViewDialog extends TitleAreaDialog {
	private NewVersionObject newVersionObj;
	private Button btnDonotShow;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewVersionViewDialog(Shell parentShell, NewVersionObject newVersionObj) {
		super(parentShell);
		
		this.newVersionObj = newVersionObj;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(SystemDefine.NAME);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(Messages.get().NewVersionViewDialog_0); //$NON-NLS-1$
		setTitle(Messages.get().NewVersionViewDialog_1); //$NON-NLS-1$
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblCurrentVersion = new Label(container, SWT.NONE);
		lblCurrentVersion.setText(Messages.get().NewVersionViewDialog_2); //$NON-NLS-1$
		
		Label lblCurrentversionvalue = new Label(container, SWT.NONE);
		lblCurrentversionvalue.setText(SystemDefine.MAJOR_VERSION + " " + SystemDefine.SUB_VERSION + " - " + SystemDefine.RELEASE_DATE);
		new Label(container, SWT.NONE);
		
		Label labelRight = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewVersion = new Label(container, SWT.NONE);
		lblNewVersion.setText(Messages.get().NewVersionViewDialog_NewVersion);
		
		Label lblNewversionvalue = new Label(container, SWT.NONE);
		lblNewversionvalue.setText(newVersionObj.getMajorVer() + " " + newVersionObj.getSubVer() + " - " + newVersionObj.getDate());
		
		Label lblInformationUrl = new Label(container, SWT.NONE);
		lblInformationUrl.setText(Messages.get().NewVersionViewDialog_7); //$NON-NLS-1$
		
		Label lblInformationurlvalue = new Label(container, SWT.NONE);
		lblInformationurlvalue.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblInformationurlvalue.setText(String.format(Messages.get().NewVersionViewDialog_8, newVersionObj.getInfoUrl())); //$NON-NLS-1$

		Label lblDownloadUrl = new Label(container, SWT.NONE);
		lblDownloadUrl.setText(Messages.get().NewVersionViewDialog_5); //$NON-NLS-1$
		
		Label lblDownloadurlvalue = new Label(container, SWT.NONE);
		lblDownloadurlvalue.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblDownloadurlvalue.setText(String.format(Messages.get().NewVersionViewDialog_6, newVersionObj.getDownloadUrl())); //$NON-NLS-1$
		new Label(container, SWT.NONE);
		
		btnDonotShow = new Button(container, SWT.CHECK);
		btnDonotShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_UPDATE_CHECK, ""+btnDonotShow.getSelection());
			}
		});
		btnDonotShow.setText(Messages.get().NewVersionViewDialog_DoesnotCheck);
		
		initUI();
		
		return area;
	}
	
	/**
	 * Initialize UI
	 */
	private void initUI() {
		btnDonotShow.setSelection(CookieUtils.isUpdateChecker());
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, true); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(502, 290);
	}
}
