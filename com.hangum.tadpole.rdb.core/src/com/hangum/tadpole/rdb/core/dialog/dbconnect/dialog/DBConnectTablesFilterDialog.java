///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog;
//
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//
//import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.dao.DBConnectionTableFilterDAO;
//
///**
// * Table Filter Dialog
// * 
// * ex)
// * 		".*" + test + ".*"
// * 
// * @author hangum
// *
// */
//public class DBConnectTablesFilterDialog extends Dialog {
//	private DBConnectionTableFilterDAO tableFilterDAO = new DBConnectionTableFilterDAO();
//	
//	private Button btnEnable;
//	private Text textInclude;
//	private Text textExclude;
//
//	/**
//	 * Create the dialog.
//	 * @param parentShell
//	 */
//	public DBConnectTablesFilterDialog(Shell parentShell) {
//		super(parentShell);
//	}
//	
//	@Override
//	public void configureShell(Shell newShell) {
//		super.configureShell(newShell);
//		newShell.setText("Table Filter Dialog");
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		
//		btnEnable = new Button(container, SWT.CHECK);
//		btnEnable.setSelection(true);
//		btnEnable.setText("Enable");
//		
//		Group grpInclude = new Group(container, SWT.NONE);
//		grpInclude.setLayout(new GridLayout(1, false));
//		grpInclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpInclude.setText("Include");
//		
//		textInclude = new Text(grpInclude, SWT.BORDER | SWT.MULTI);
//		textInclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		Group grpExclude = new Group(container, SWT.NONE);
//		grpExclude.setLayout(new GridLayout(1, false));
//		grpExclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpExclude.setText("Exclude");
//		
//		textExclude = new Text(grpExclude, SWT.BORDER | SWT.MULTI);
//		textExclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		btnEnable.setFocus();
//
//		return container;
//	}
//	
//	@Override
//	protected void okPressed() {
//		tableFilterDAO.setEnable(btnEnable.getSelection());
//		tableFilterDAO.setIncludeFilter(textInclude.getText());
//		tableFilterDAO.setExcludeFilter(textExclude.getText());
//		
//		super.okPressed();
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public DBConnectionTableFilterDAO getTableFilterDAO() {
//		return tableFilterDAO;
//	}
//
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, "OK", true);
//		createButton(parent, IDialogConstants.CANCEL_ID, "Cancle", false);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(501, 454);
//	}
//
//	/**
//	 * @return the btnEnable
//	 */
//	public boolean getBtnEnable() {
//		return btnEnable.getSelection();
//	}
//
//	/**
//	 * @param btnEnable the btnEnable to set
//	 */
//	public void setBtnEnable(boolean btnEnable) {
//		this.btnEnable.setSelection(btnEnable);
//	}
//
//	/**
//	 * @return the textInclude
//	 */
//	public String getTextInclude() {
//		return textInclude.getText();
//	}
//
//	/**
//	 * @param textInclude the textInclude to set
//	 */
//	public void setTextInclude(String textInclude) {
//		this.textInclude.setText(textInclude);
//	}
//
//	/**
//	 * @return the textExclude
//	 */
//	public String getTextExclude() {
//		return textExclude.getText();
//	}
//
//	/**
//	 * @param textExclude the textExclude to set
//	 */
//	public void setTextExclude(String textExclude) {
//		this.textExclude.setText(textExclude);
//	}
//}
