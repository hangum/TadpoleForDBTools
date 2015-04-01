/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.auth.dialogs;

import java.util.List;

import org.apache.log4j.Logger;
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

import com.hangum.tadpole.engine.query.dao.system.TableFilterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_TableColumnFilter;

/**
 * Add Table Filter Dialog
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class AddTableFilterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AddTableFilterDialog.class);
	
	private UserDBDAO userDB;
	
	private Text textTableName;
	private Text textDescription;
	private Text textColumnNames;
	private Button btnTableFullLock;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddTableFilterDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(userDB.getDisplay_name() + " add Table, Column Filter dialog"); //$NON-NLS-1$
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
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblTableName = new Label(compositeBody, SWT.NONE);
		lblTableName.setText("Table Name");
		
		textTableName = new Text(compositeBody, SWT.NONE | SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(compositeBody, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_textDescription.heightHint = 60;
		gd_textDescription.minimumHeight = 30;
		textDescription.setLayoutData(gd_textDescription);
		new Label(compositeBody, SWT.NONE);
		
		btnTableFullLock = new Button(compositeBody, SWT.CHECK);
		btnTableFullLock.setText("Table full Lock");
		btnTableFullLock.setEnabled(false);
		
		Label lblColumnName = new Label(compositeBody, SWT.NONE);
		lblColumnName.setText("Column Name");
		
		Label lblSeparate = new Label(compositeBody, SWT.NONE);
		lblSeparate.setText("ex) Column1,Column2");
		new Label(compositeBody, SWT.NONE);
		
		textColumnNames = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_textColumnNames = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textColumnNames.heightHint = 70;
		textColumnNames.setLayoutData(gd_textColumnNames);

		initUI();
		
		textTableName.setFocus();
		
		return container;
	}

	/**
	 * initialize ui
	 */
	private void initUI() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		String strTableName 	= textTableName.getText();
		String strColumnNames 	= textColumnNames.getText();
		String strDescription 	= textDescription.getText();
		
		if("".equals(strTableName)) {
			MessageDialog.openError(getShell(), "Error", "Table name is empty.");
			textTableName.setFocus();
			return;
		} else if("".equals(strColumnNames)) {
			MessageDialog.openError(getShell(), "Error", "Column name is empty.");
			textColumnNames.setFocus();
			return;
		}
		
		TableFilterDAO tableColumnFilter = new TableFilterDAO();
		tableColumnFilter.setDb_seq(userDB.getSeq());
		tableColumnFilter.setTable_name(strTableName);
		tableColumnFilter.setColumn_names(strColumnNames);
		tableColumnFilter.setDescription(strDescription);

		try {
			// duplication check
			List<TableFilterDAO> listTableFilter = TadpoleSystem_TableColumnFilter.getTableColumnFilters(tableColumnFilter);
			if(listTableFilter.isEmpty()) {
				TadpoleSystem_TableColumnFilter.insertTableColumnFilter(tableColumnFilter);
			} else {
				MessageDialog.openError(getShell(), "Error", "Already exist table name.");
				textTableName.setFocus();
				
				return;
			}
		} catch (Exception e) {
			logger.error("tablecolumnfilter table insert exception", e);
		}
		
		super.okPressed();
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
		return new Point(450, 350);
	}

}
