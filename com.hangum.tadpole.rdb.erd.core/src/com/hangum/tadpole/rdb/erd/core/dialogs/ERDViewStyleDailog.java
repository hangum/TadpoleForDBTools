/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

/**
 * 에디터의 뷰 스타일을 선택합니다.
 *  
 * @author hangum
 *
 */
public class ERDViewStyleDailog extends Dialog {

	Combo comboTitle;
	Button btnPrimaryKey;
	Button btnColumnName;
	Button btnColumnComent;
	Button btnColumnType;
	Button btnNullCheck;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ERDViewStyleDailog(Shell parentShell) {
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
		
		Label lblTitle = new Label(container, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText("Title");
		
		comboTitle = new Combo(container, SWT.NONE);
		comboTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		comboTitle.add("Table Name");
		comboTitle.add("Table Comment");
		comboTitle.add("Table Name + Table Comment");
		comboTitle.select(0);
		
		new Label(container, SWT.NONE);
		
		Group grpColumn = new Group(container, SWT.NONE);
		grpColumn.setLayout(new GridLayout(1, false));
		grpColumn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpColumn.setText("Column ");
		
		btnPrimaryKey = new Button(grpColumn, SWT.CHECK);
		btnPrimaryKey.setText("Primary Key");
		
		btnColumnName = new Button(grpColumn, SWT.CHECK);
		btnColumnName.setText("Name");
		
		btnColumnComent = new Button(grpColumn, SWT.CHECK);
		btnColumnComent.setText("Coment");
		
		btnColumnType = new Button(grpColumn, SWT.CHECK);
		btnColumnType.setText("Type");
		
		btnNullCheck = new Button(grpColumn, SWT.CHECK);
		btnNullCheck.setText("Null Check");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
