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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.ExtensionBrowserURLDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;

/**
 * Others connection info
 * 
 * @author hangum
 *
 */
public class OthersConnectionMongoDBGroup extends AbstractOthersConnection {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OthersConnectionMongoDBGroup.class);
	
	private String strIp = "", strPort = "";
	private OthersConnectionInfoDAO otherConnectionDAO = new OthersConnectionInfoDAO();
	
//	private Button btnTableFilters;
	private Button btnReadOnlyConnection;
	private Button btnShowTables;
	private Button btnExternalBrowser;
//	private Button btnIsVisible;
//	private Button btnSendMonitoring;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param selectDB
	 * @param strIp
	 * @param strPort
	 */
	public OthersConnectionMongoDBGroup(Composite parent, int style, DBDefine selectDB) {
		super(parent, style, selectDB);
		
		setText(Messages.get().SettingOtherInfo);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnReadOnlyConnection = new Button(this, SWT.CHECK);
		btnReadOnlyConnection.setText(Messages.get().ReadOnly);
		
//		btnTableFilters = new Button(this, SWT.NONE);
//		btnTableFilters.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				DBConnectTablesFilterDialog dialog = new DBConnectTablesFilterDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
//				if(Dialog.OK == dialog.open()) {
//					DBConnectionTableFilterDAO tableFilterDao = dialog.getTableFilterDAO();
//					
//					otherConnectionDAO.setTableFilter(tableFilterDao.isEnable());
//					otherConnectionDAO.setStrTableFilterInclude(tableFilterDao.getIncludeFilter());
//					otherConnectionDAO.setStrTableFilterExclude(tableFilterDao.getExcludeFilter());
//				}
//			}
//		});
//		btnTableFilters.setText(Messages.get().OthersConnectionMongoDBGroup_2);
		
		btnExternalBrowser = new Button(this, SWT.NONE);
		btnExternalBrowser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExtensionBrowserURLDialog dialog = new ExtensionBrowserURLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), getDefaultExternalBrowserInfo());
				if(Dialog.OK == dialog.open()) {
					otherConnectionDAO.setExterBrowser(dialog.isEnable());
					otherConnectionDAO.setListExterBroswer(dialog.getListExterBroswer());
				}
			}
		});
		btnExternalBrowser.setText(Messages.get().ExternalBrowser);
		
		btnShowTables = new Button(this, SWT.CHECK);
		btnShowTables.setSelection(true);
		btnShowTables.setText(Messages.get().ShowTables);
		
//		btnIsVisible = new Button(this, SWT.CHECK);
//		btnIsVisible.setSelection(true);
//		btnIsVisible.setText(Messages.get().OthersConnectionMongoDBGroup_btnIsVisible_text);
		
//		btnSendMonitoring = new Button(this, SWT.CHECK);
//		btnSendMonitoring.setEnabled(false);
//		btnSendMonitoring.setText(Messages.get().OthersConnectionMongoDBGroup_btnSendMonitoring_text);
	}
	
	/**
	 * Callback Initialize UI
	 * 
	 * @param strIp
	 * @param strPort
	 */
	public void callBackUIInit(String strIp, String strPort) {
		setStrIp(strIp);
		setStrPort(strPort);
	}
	
	/**
	 * other db connection info
	 * 
	 * @return
	 */
	public OthersConnectionInfoDAO getOthersConnectionInfo() {
		otherConnectionDAO.setReadOnlyConnection(getBtnReadOnlyConnection());
		otherConnectionDAO.setShowTables(getBtnShowTables());
		
//		otherConnectionDAO.setVisible(getIsVisible());
//		otherConnectionDAO.setSummaryReport(getSendMonitoring());
		
		return otherConnectionDAO;
	}
	
//	public boolean getIsVisible() {
//		return btnIsVisible.getSelection();
//	}
//	public boolean getSendMonitoring() {
//		return btnSendMonitoring.getSelection();
//	}
	
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
	public List<ExternalBrowserInfoDAO> getDefaultExternalBrowserInfo() {
		List<ExternalBrowserInfoDAO> listBrowser = new ArrayList<ExternalBrowserInfoDAO>();
		
		int intPort = 27017;
		try {
			intPort = (Integer.parseInt(strPort) + 1000);
		} catch(Exception e) {
			// ignore exception
		}
		
		ExternalBrowserInfoDAO extBrowserDAO = new ExternalBrowserInfoDAO();
		extBrowserDAO.setName("AdminBrowser");
		extBrowserDAO.setUrl(String.format("http://%s:%s", strIp, intPort));
		extBrowserDAO.setIs_used(PublicTadpoleDefine.YES_NO.YES.name());
		listBrowser.add(extBrowserDAO);
		
		return listBrowser;
	}

	/**
	 * @return the strIp
	 */
	public String getStrIp() {
		return strIp;
	}

	/**
	 * @param strIp the strIp to set
	 */
	public void setStrIp(String strIp) {
		this.strIp = strIp;
	}

	/**
	 * @return the strPort
	 */
	public String getStrPort() {
		return strPort;
	}

	/**
	 * @param strPort the strPort to set
	 */
	public void setStrPort(String strPort) {
		this.strPort = strPort;
	}
}
