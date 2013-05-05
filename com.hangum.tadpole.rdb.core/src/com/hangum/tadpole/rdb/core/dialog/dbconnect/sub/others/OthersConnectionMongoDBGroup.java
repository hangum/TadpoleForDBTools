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

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

/**
 * Others connection info
 * 
 * @author hangum
 *
 */
public class OthersConnectionMongoDBGroup extends Group {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OthersConnectionMongoDBGroup.class);
	
	private OthersConnectionInfoDAO otherConnectionDAO = new OthersConnectionInfoDAO();
	
	private Button btnTableFilters;
	private Button btnReadOnlyConnection;
	private Button btnTunneling;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionMongoDBGroup(Composite parent, int style) {
		super(parent, style);
		setText(Messages.OthersConnectionMongoDBGroup_0);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnReadOnlyConnection = new Button(this, SWT.CHECK);
		btnReadOnlyConnection.setText(Messages.OthersConnectionMongoDBGroup_1);
		
		btnTableFilters = new Button(this, SWT.NONE);
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
		btnTableFilters.setText(Messages.OthersConnectionMongoDBGroup_2);
		
		Button btnTunneling = new Button(this, SWT.NONE);
		btnTunneling.setEnabled(false);
		btnTunneling.setText(Messages.OthersConnectionMongoDBGroup_3);
		btnTunneling.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SSHTunnelingDialog sshTunnelingDialog = new SSHTunnelingDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				sshTunnelingDialog.open();
			}
		});
	}
	
	/**
	 * other db connection info
	 * 
	 * @return
	 */
	public OthersConnectionInfoDAO getOthersConnectionInfo() {
		otherConnectionDAO.setReadOnlyConnection(getBtnReadOnlyConnection());
		
		return otherConnectionDAO;
	}
	
	/**
	 * @return the btnReadOnlyConnection
	 */
	public boolean getBtnReadOnlyConnection() {
		return btnReadOnlyConnection.getSelection();
	}

	/**
	 * @param btnReadOnlyConnection the btnReadOnlyConnection to set
	 */
	public void setBtnReadOnlyConnection(boolean btnReadOnlyConnection) {
		this.btnReadOnlyConnection.setSelection(btnReadOnlyConnection);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
