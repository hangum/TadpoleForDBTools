///*******************************************************************************
// * Copyright (c) 2014 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.dialog.ddl;
//
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//
///**
// * SQL Source view dialog
// * 
// * @author hangum
// *
// */
//public class SQLSourceViewDialog extends Dialog {
//	private String strSQL = "";
//	private Text textSQL;
//
//	/**
//	 * Create the dialog.
//	 * 
//	 * @param parentShell
//	 * @param strSQL
//	 */
//	public SQLSourceViewDialog(Shell parentShell, String strSQL) {
//		super(parentShell);
//		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
//		this.strSQL = strSQL;
//	}
//	
//	@Override
//	protected void configureShell(Shell newShell) {
//		super.configureShell(newShell);
//		newShell.setText("Execute SQL Dialog");
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		GridLayout gridLayout = (GridLayout) container.getLayout();
//		gridLayout.verticalSpacing = 5;
//		gridLayout.horizontalSpacing = 5;
//		gridLayout.marginHeight = 5;
//		gridLayout.marginWidth = 5;
//		
//		textSQL = new Text(container, SWT.BORDER | SWT.MULTI);
//		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		textSQL.setText(strSQL);
//
//		return container;
//	}
//
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, "Close", true);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(450, 300);
//	}
//
//}
