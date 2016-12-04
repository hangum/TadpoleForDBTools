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
package com.hangum.tadpole.engine.security;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.otp.core.GetOTPCode;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.Messages;

/**
 * OTP input Dialog
 * 
 * @author hangum
 *
 */
public class OTPInputDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(OTPInputDialog.class);
	private String userID;
	private String secretKey;
	private Text textOTPCode;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param secretKey 
	 * @param userID 
	 */
	public OTPInputDialog(Shell parentShell, String userID, String secretKey) {
		super(parentShell);
		
		this.userID = userID;
		this.secretKey = secretKey;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().OTPLoginDialog_0);
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
		
		Label lblOtpCode = new Label(container, SWT.NONE);
		lblOtpCode.setText(Messages.get().OTP); //$NON-NLS-1$
		
		textOTPCode = new Text(container, SWT.BORDER);
		textOTPCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textOTPCode.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		String strOTPCode = StringUtils.trim(textOTPCode.getText());
		if("".equals(strOTPCode)) {
			MessageDialog.openError(getShell(), CommonMessages.get().Error, Messages.get().OTPEmpty);//"OTP 값을 입력해 주십시오.");
			textOTPCode.setFocus();
			return;
		}
		
		try {
			GetOTPCode.isValidate(userID, secretKey, strOTPCode);
		} catch(Exception e) {
			logger.error("OTP check", e);
			MessageDialog.openError(getShell(), CommonMessages.get().Error, e.getMessage());
			textOTPCode.setFocus();
			return;
		}
		
		super.okPressed();
	}
	
	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Confirm, true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 115);
	}
}
