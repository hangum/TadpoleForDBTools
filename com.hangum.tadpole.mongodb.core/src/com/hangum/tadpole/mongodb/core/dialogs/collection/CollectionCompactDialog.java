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
package com.hangum.tadpole.mongodb.core.dialogs.collection;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

/**
 * Collection compact
 * reference : http://docs.mongodb.org/manual/reference/command/compact/
 * 
 * @author hangum
 *
 */
public class CollectionCompactDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CollectionCompactDialog.class);

	private UserDBDAO userDB;
	private String collName;
	
	private Button btnForce;
	private Text textCollName;
	private Text textPaddingFactor;
	private Text textPaddingBytes;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CollectionCompactDialog(Shell parentShell, UserDBDAO userDB, String collName) {
		super(parentShell);
		
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.userDB = userDB;
		this.collName = collName;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Collection compact Dialog");
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
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setText("Name");
		
		textCollName = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		textCollName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		btnForce = new Button(composite, SWT.CHECK);
		btnForce.setText("force");
		
		Label lblPaddingFactor = new Label(composite, SWT.NONE);
		lblPaddingFactor.setText("padding Factor");
		
		textPaddingFactor = new Text(composite, SWT.BORDER);
		textPaddingFactor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPaddingFactor.setText("0");
		
		Label lblPaddingBytes = new Label(composite, SWT.NONE);
		lblPaddingBytes.setText("padding Bytes");
		
		textPaddingBytes = new Text(composite, SWT.BORDER);
		textPaddingBytes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPaddingBytes.setText("0");
		
		init();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	/**
	 * initialize data
	 */
	private void init() {
		textCollName.setText(collName);
	}
	
	@Override
	protected void okPressed() {
		if(!NumberUtils.isNumber(textPaddingFactor.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "padding factor is number.");
			textPaddingFactor.setFocus();
			return;
		}
		if(!NumberUtils.isNumber(textPaddingBytes.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "padding Bytes is number.");
			textPaddingBytes.setFocus();
			return;
		}

		if(MessageDialog.openConfirm(null, "Question?", "Are you sure you want to run this command?  It can potentially lock the db for a long time.")) {
			try {
				String retMsg = MongoDBQuery.collCompact(userDB, 
						collName, 
						btnForce.getSelection(), 
						NumberUtils.createInteger(textPaddingFactor.getText()), 
						NumberUtils.createInteger(textPaddingBytes.getText()));
				
				MessageDialog.openInformation(null, "Successful", "Compact success");
					
			} catch (Exception e) {
				logger.error("mongodb compact" + collName, e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Collection compact Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				
				return;
			}
		}
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 250);
	}
}
