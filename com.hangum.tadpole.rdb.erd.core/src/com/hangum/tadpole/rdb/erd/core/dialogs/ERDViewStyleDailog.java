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

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.rdb.erd.core.Messages;
import com.hangum.tadpole.rdb.model.Style;

/**
 * 에디터의 뷰 스타일을 선택합니다.
 *  
 * @author hangum
 *
 */
public class ERDViewStyleDailog extends Dialog {
	
	Style erdStyle;
	
	Button btnPrimaryKey;
	Button btnColumnName;
	Button btnColumnComent;
	Button btnColumnType;
	Button btnNullCheck;
	private Group grpTableTitle;
	private Button btnTableName;
	private Button btnTableComment;
	private Button btnTableNameComment;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ERDViewStyleDailog(Shell parentShell, Style erdStyle) {
		super(parentShell);
		
		this.erdStyle = erdStyle;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ERDViewStyleDailog_0);
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
		
		grpTableTitle = new Group(container, SWT.NONE);
		grpTableTitle.setLayout(new GridLayout(3, false));
		grpTableTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpTableTitle.setText(Messages.get().ERDViewStyleDailog_1);
		
		btnTableName = new Button(grpTableTitle, SWT.RADIO);
		btnTableName.setText(Messages.get().ERDViewStyleDailog_2);
		btnTableName.setData("name"); //$NON-NLS-1$
		
		btnTableComment = new Button(grpTableTitle, SWT.RADIO);
		btnTableComment.setText(Messages.get().ERDViewStyleDailog_4);
		btnTableComment.setData("comment"); //$NON-NLS-1$
		
		btnTableNameComment = new Button(grpTableTitle, SWT.RADIO);
		btnTableNameComment.setText(Messages.get().ERDViewStyleDailog_6);
		btnTableNameComment.setData("nameComment"); //$NON-NLS-1$
		
		Group grpColumn = new Group(container, SWT.NONE);
		grpColumn.setLayout(new GridLayout(1, false));
		grpColumn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpColumn.setText(Messages.get().ERDViewStyleDailog_8);
		
		btnPrimaryKey = new Button(grpColumn, SWT.CHECK);
		btnPrimaryKey.setText(Messages.get().ERDViewStyleDailog_9);
		
		btnColumnName = new Button(grpColumn, SWT.CHECK);
		btnColumnName.setText(Messages.get().ERDViewStyleDailog_10);
		
		btnColumnComent = new Button(grpColumn, SWT.CHECK);
		btnColumnComent.setText(Messages.get().ERDViewStyleDailog_11);
		
		btnColumnType = new Button(grpColumn, SWT.CHECK);
		btnColumnType.setText(Messages.get().ERDViewStyleDailog_12);
		
		btnNullCheck = new Button(grpColumn, SWT.CHECK);
		btnNullCheck.setText(Messages.get().ERDViewStyleDailog_13);
		
		initUI();

		return container;
	}
	
	private void initUI() {
		String tableTitle = erdStyle.getTableTitle();
		if("name".equals(tableTitle)) 		btnTableName.setSelection(true); //$NON-NLS-1$
		else if("comment".equals(tableTitle)) btnTableComment.setSelection(true); //$NON-NLS-1$
		else 									btnTableNameComment.setSelection(true);
		
		if("YES".equals(erdStyle.getColumnPrimaryKey())) 	btnPrimaryKey.setSelection(true); //$NON-NLS-1$
		if("YES".equals(erdStyle.getColumnName())) 			btnColumnName.setSelection(true); //$NON-NLS-1$
		if("YES".equals(erdStyle.getColumnComment())) 		btnColumnComent.setSelection(true); //$NON-NLS-1$
		if("YES".equals(erdStyle.getColumnType())) 			btnColumnType.setSelection(true); //$NON-NLS-1$
		if("YES".equals(erdStyle.getColumnNullCheck())) 	btnNullCheck.setSelection(true); //$NON-NLS-1$
	}
	
	@Override
	protected void okPressed() {
		if(btnTableName.getSelection()) 		erdStyle.setTableTitle(btnTableName.getData().toString());
		else if(btnTableComment.getSelection()) erdStyle.setTableTitle(btnTableComment.getData().toString());
		else 									erdStyle.setTableTitle(btnTableNameComment.getData().toString());
	
		if(btnPrimaryKey.getSelection()) 	erdStyle.setColumnPrimaryKey("YES"); //$NON-NLS-1$
		else 								erdStyle.setColumnPrimaryKey("NO"); //$NON-NLS-1$
		
		if(btnColumnName.getSelection()) 	erdStyle.setColumnName("YES"); //$NON-NLS-1$
		else 								erdStyle.setColumnName("NO"); //$NON-NLS-1$
		
		if(btnColumnComent.getSelection()) 	erdStyle.setColumnComment("YES"); //$NON-NLS-1$
		else 								erdStyle.setColumnComment("NO"); //$NON-NLS-1$
		
		if(btnColumnType.getSelection()) 	erdStyle.setColumnType("YES"); //$NON-NLS-1$
		else 								erdStyle.setColumnType("NO"); //$NON-NLS-1$
		
		if(btnNullCheck.getSelection()) 	erdStyle.setColumnNullCheck("YES"); //$NON-NLS-1$
		else 								erdStyle.setColumnNullCheck("NO"); //$NON-NLS-1$
		
		// scale 
		erdStyle.setScale(100.0d);
		
		super.okPressed();
	}
	
	public Style getErdStyle() {
		return erdStyle;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().ERDViewStyleDailog_3, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().ERDViewStyleDailog_32, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(470, 340);
	}

}
