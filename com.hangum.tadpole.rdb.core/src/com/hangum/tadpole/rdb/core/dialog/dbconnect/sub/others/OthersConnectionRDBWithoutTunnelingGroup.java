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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.DBConnectTablesFilterDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.dao.DBConnectionTableFilterDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

/**
 * Others connection info
 * 
 * @author hangum
 *
 */
public class OthersConnectionRDBWithoutTunnelingGroup extends Group {
	
	private OthersConnectionInfoDAO otherConnectionDAO = new OthersConnectionInfoDAO();
	
	/** read only connection */
	private Button btnReadOnlyConnection;
	/** auto commit */
	private Button btnAutoCommit;
	private Button btnProfiler;
	private Button btnExecuteQuestionDml;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionRDBWithoutTunnelingGroup(Composite parent, int style) {
		super(parent, style);
		setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_0);
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnReadOnlyConnection = new Button(this, SWT.CHECK);
		btnReadOnlyConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnReadOnlyConnection.setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_1);
		
		btnAutoCommit = new Button(this, SWT.CHECK);
		btnAutoCommit.setSelection(true);
		btnAutoCommit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAutoCommit.setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_2);
		
		Button btnTableFilters = new Button(this, SWT.NONE);
		btnTableFilters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
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
		btnTableFilters.setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_3);
		new Label(this, SWT.NONE);
		
		btnProfiler = new Button(this, SWT.CHECK);
		btnProfiler.setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_4);
		
		btnExecuteQuestionDml = new Button(this, SWT.CHECK);
		btnExecuteQuestionDml.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnExecuteQuestionDml.setText(Messages.OthersConnectionRDBWithoutTunnelingGroup_5);
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

	public boolean getBtnProfiler() {
		return btnProfiler.getSelection();
	}
	
	public boolean getBtnExecuteQuestionDml() {
		return btnExecuteQuestionDml.getSelection();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
