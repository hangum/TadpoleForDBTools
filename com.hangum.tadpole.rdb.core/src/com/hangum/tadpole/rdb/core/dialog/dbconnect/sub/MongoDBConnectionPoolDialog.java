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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub;

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
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.mongodb.core.Messages;

/**
 * MongoDB Connection Poool Dialog
 * 
 * @author hangum
 *
 */
public class MongoDBConnectionPoolDialog extends Dialog {
	private Text textConnectionPerHost;
	private Text textBlockingThreadMultiplier;
	private Text textMaxWiatTime;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MongoDBConnectionPoolDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblConnectionsPerHost = new Label(container, SWT.NONE);
		lblConnectionsPerHost.setText("Connections Per Host");
		
		textConnectionPerHost = new Text(container, SWT.BORDER);
		textConnectionPerHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBlockingThreadMultiplier = new Label(container, SWT.NONE);
		lblBlockingThreadMultiplier.setText("Blocking Thread Multiplier");
		
		textBlockingThreadMultiplier = new Text(container, SWT.BORDER);
		textBlockingThreadMultiplier.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMaxWaitTime = new Label(container, SWT.NONE);
		lblMaxWaitTime.setText("Max Wait Time");
		
		textMaxWiatTime = new Text(container, SWT.BORDER);
		textMaxWiatTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,  Messages.get().Add, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
