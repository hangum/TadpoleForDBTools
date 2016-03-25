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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.help.HelpDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.HelpUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailDialog;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.DBObject;

/**
 * Collection validate
 * 
 * @author hangum
 *
 */
public class CollValidateDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CollValidateDialog.class);
	private UserDBDAO userDB;
	private String collName;
	
	private Button btnFull;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CollValidateDialog(Shell parentShell, UserDBDAO userDB, String collName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.collName = collName;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(collName + " - validate Dialog");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		btnFull = new Button(container, SWT.CHECK);
		btnFull.setSelection(true);
		btnFull.setText("Full");
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	@Override
	protected void okPressed() {
		try {
			DBObject retDBObj = MongoDBQuery.collValidate(userDB, collName, btnFull.getSelection());
			
			if(null != retDBObj) {
				FindOneDetailDialog resultViewDialog = new FindOneDetailDialog(null, userDB, "Validate Message", retDBObj);
				resultViewDialog.open();
			}
		} catch (Exception e) {
			logger.error("mongodb collection validate", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "collection validate Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.HELP_ID) {
			HelpUtils.showHelp(HelpDefine.MONGODB_VALIDATE);
		}
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.HELP_ID, "Help", false);
		createButton(parent, IDialogConstants.OK_ID, "Validate", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(470, 140);
	}

}
