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
package com.hangum.tadpole.rdb.core.dialog.table.mysql;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.rdb.core.Messages;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;

/**
 * mysql table relation
 * 
 * @author hangum
 *
 */
public class MySQLTableRelationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MySQLTableRelationDialog.class);
	
	private Text textRefName;
	private Combo comboOriColumn;
	private Combo comboRefTableName;
	private Combo comboRefColumnName;
	private Combo comboOnUpdate;
	private Combo comboOnDelete;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MySQLTableRelationDialog(Shell parentShell) {
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
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		textRefName = new Text(container, SWT.BORDER);
		textRefName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpTableEmployees = new Group(container, SWT.NONE);
		grpTableEmployees.setLayout(new GridLayout(2, false));
		grpTableEmployees.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpTableEmployees.setText("Table : employees");
		
		Label lblColumn = new Label(grpTableEmployees, SWT.NONE);
		lblColumn.setText("Column");
		
		comboOriColumn = new Combo(grpTableEmployees, SWT.READ_ONLY);
		comboOriColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpReference = new Group(container, SWT.NONE);
		grpReference.setLayout(new GridLayout(2, false));
		grpReference.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpReference.setText("Reference");
		
		Label lblTableName = new Label(grpReference, SWT.NONE);
		lblTableName.setText("Table Name");
		
		comboRefTableName = new Combo(grpReference, SWT.READ_ONLY);
		comboRefTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblColumnName = new Label(grpReference, SWT.NONE);
		lblColumnName.setText("Column Name");
		
		comboRefColumnName = new Combo(grpReference, SWT.READ_ONLY);
		comboRefColumnName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpAction = new Group(container, SWT.NONE);
		grpAction.setLayout(new GridLayout(2, false));
		grpAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpAction.setText("Action");
		
		Label lblOnUpdate = new Label(grpAction, SWT.NONE);
		lblOnUpdate.setText("On update");
		
		comboOnUpdate = new Combo(grpAction, SWT.READ_ONLY);
		comboOnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOnUpdate.add("RESTRICT");
		comboOnUpdate.add("CASCADE");
		comboOnUpdate.add("SET NULL");
		comboOnUpdate.add("NO ACTION");
		comboOnUpdate.setText("NO ACTION");
		
		Label lblOnDelete = new Label(grpAction, SWT.NONE);
		lblOnDelete.setText("On delete");
		
		comboOnDelete = new Combo(grpAction, SWT.READ_ONLY);
		comboOnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOnDelete.add("RESTRICT");
		comboOnDelete.add("CASCADE");
		comboOnDelete.add("SET NULL");
		comboOnDelete.add("NO ACTION");
		comboOnDelete.setText("NO ACTION");

		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * initialize UI
	 */
	private void initUI() {
		
	}
	
	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 359);
	}

}
