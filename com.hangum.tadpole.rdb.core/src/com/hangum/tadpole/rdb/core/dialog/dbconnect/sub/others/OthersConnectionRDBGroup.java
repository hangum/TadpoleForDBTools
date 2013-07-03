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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.DBConnectTablesFilterDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.SSHTunnelingDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.dao.DBConnectionTableFilterDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import org.eclipse.swt.widgets.Label;

/**
 * Others connection info
 * 
 * @author hangum
 *
 */
public class OthersConnectionRDBGroup extends Group {
	
	private OthersConnectionInfoDAO otherConnectionDAO = new OthersConnectionInfoDAO();
	
	/** read only connection */
	private Button btnReadOnlyConnection;
	/** auto commit */
	private Button btnAutoCommit;
	/** tunneling */
	private Button btnTunneling;
	private Button btnProfiler;
	private Button btnExecuteQuestionDml;
	private Button btnShowTables;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionRDBGroup(Composite parent, int style) {
		super(parent, style);
		setText(Messages.OthersConnectionRDBGroup_0);
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnReadOnlyConnection = new Button(this, SWT.CHECK);
		btnReadOnlyConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnReadOnlyConnection.setText(Messages.OthersConnectionRDBGroup_1);
		
		btnAutoCommit = new Button(this, SWT.CHECK);
		btnAutoCommit.setSelection(true);
		btnAutoCommit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAutoCommit.setText(Messages.OthersConnectionRDBGroup_2);
		
		Button btnTableFilters = new Button(this, SWT.NONE);
		btnTableFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBConnectTablesFilterDialog dialog = new DBConnectTablesFilterDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				if(Dialog.OK == dialog.open()) {
					DBConnectionTableFilterDAO tableFilterDao = dialog.getTableFilterDAO();
					
					otherConnectionDAO.setTableFilter(tableFilterDao.isEnable());
					otherConnectionDAO.setStrTableFilterInclude(tableFilterDao.getIncludeFilter());
					otherConnectionDAO.setStrTableFilterExclude(tableFilterDao.getExcludeFilter());
				}
			}
		});
		btnTableFilters.setText(Messages.OthersConnectionRDBGroup_3);
		
		btnTunneling = new Button(this, SWT.NONE);
		btnTunneling.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SSHTunnelingDialog sshTunnelingDialog = new SSHTunnelingDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				sshTunnelingDialog.open();
			}
		});
		btnTunneling.setText(Messages.OthersConnectionRDBGroup_4);
		btnTunneling.setEnabled(false);
		
		btnProfiler = new Button(this, SWT.CHECK);
		btnProfiler.setText(Messages.OthersConnectionRDBGroup_5);
		btnProfiler.setSelection(true);
		
		btnShowTables = new Button(this, SWT.CHECK);
		btnShowTables.setSelection(true);
		btnShowTables.setText(Messages.OthersConnectionRDBGroup_btnShowTables_text);
		
		btnExecuteQuestionDml = new Button(this, SWT.CHECK);
		btnExecuteQuestionDml.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnExecuteQuestionDml.setText(Messages.OthersConnectionRDBGroup_6);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}
	
	/**
	 * other db connection info
	 * 
	 * @return
	 */
	public OthersConnectionInfoDAO getOthersConnectionInfo() {
		otherConnectionDAO.setReadOnlyConnection(getBtnReadOnlyConnection());
		otherConnectionDAO.setAutoCommit(getBtnAutoCommit());
		otherConnectionDAO.setShowTables(getBtnShowTables());
		
		otherConnectionDAO.setProfiling(getBtnProfiler());
		otherConnectionDAO.setDMLStatement(getBtnExecuteQuestionDml());
		
		return otherConnectionDAO;
	}
	
	/**
	 * @return the btnReadOnlyConnection
	 */
	public boolean getBtnReadOnlyConnection() {
		return btnReadOnlyConnection.getSelection();
	}

	/**
	 * @return the btnAutoCommit
	 */
	public boolean getBtnAutoCommit() {
		return btnAutoCommit.getSelection();
	}

	/**
	 * @return the btnTunneling
	 */
	public boolean getBtnTunneling() {
		return btnTunneling.getSelection();
	}

	public boolean getBtnExecuteQuestionDml() {
		return btnExecuteQuestionDml.getSelection();
	}
	
	public boolean getBtnProfiler() {
		return btnProfiler.getSelection();
	}

	/**
	 * @param btnShowTables
	 */
	public void setBtnShowTables(boolean btnShowTables) {
		this.btnShowTables.setSelection(btnShowTables);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getBtnShowTables() {
		return btnShowTables.getSelection();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
