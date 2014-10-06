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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 * 에디터의 뷰 스타일을 선택합니다.
 *  
 * @author hangum
 *
 */
public class ERDViewStyleDailog extends Dialog {
	Button btnPrimaryKey;
	Button btnColumnName;
	Button btnColumnComent;
	Button btnColumnType;
	Button btnNullCheck;
	private Group grpTableTitle;
	private Button btnTab;
	private Button btnTableComment;
	private Button btnTableName;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ERDViewStyleDailog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("ERD Viewing style");
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
		
		grpTableTitle = new Group(container, SWT.NONE);
		grpTableTitle.setLayout(new GridLayout(3, false));
		grpTableTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpTableTitle.setText("Table Title");
		
		btnTab = new Button(grpTableTitle, SWT.RADIO);
		btnTab.setText("Table Name");
		
		btnTableComment = new Button(grpTableTitle, SWT.RADIO);
		btnTableComment.setText("Table Comment ");
		
		btnTableName = new Button(grpTableTitle, SWT.RADIO);
		btnTableName.setText("Table Name + Comment");
		
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
		return new Point(470, 320);
	}

}
