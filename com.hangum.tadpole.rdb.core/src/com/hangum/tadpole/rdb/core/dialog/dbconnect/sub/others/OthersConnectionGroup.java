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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog.ExtensionBrowserURLDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;

/**
 * Others connection group
 * 
 * @author hangum
 *
 */
public abstract class OthersConnectionGroup extends AbstractOthersConnection {
	
	protected OthersConnectionInfoDAO otherConnectionDAO = new OthersConnectionInfoDAO();
	
	/** read only connection */
	protected Button btnReadOnlyConnection;
	/** auto commit */
	protected Button btnAutoCommit;
	/** tunneling */
	protected Button btnExternalBrowser;
	protected Button btnProfiler;
	protected Button btnExecuteQuestionDml;
	protected Button btnShowTables;
//	protected Button btnIsVisible;
//	protected Button btnSendMonitoring;
//	protected Button btnIsMonitoring;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionGroup(Composite parent, int style, DBDefine selectDB) {
		super(parent, style, selectDB);
		setText(Messages.get().SettingOtherInfo);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnReadOnlyConnection = new Button(this, SWT.CHECK);
		btnReadOnlyConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnReadOnlyConnection.setText(Messages.get().ReadOnly);
		
		btnAutoCommit = new Button(this, SWT.CHECK);
		btnAutoCommit.setSelection(true);
		btnAutoCommit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAutoCommit.setText(Messages.get().OthersConnectionRDBGroup_2);
		
//		Button btnTableFilters = new Button(this, SWT.NONE);
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
//		btnTableFilters.setText(Messages.get().OthersConnectionRDBGroup_3);
		
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
		
		btnProfiler = new Button(this, SWT.CHECK);
		btnProfiler.setText(Messages.get().OthersConnectionRDBGroup_5);
		btnProfiler.setSelection(true);
		
		btnShowTables = new Button(this, SWT.CHECK);
		btnShowTables.setSelection(true);
		btnShowTables.setText(Messages.get().ShowTables);
		
		btnExecuteQuestionDml = new Button(this, SWT.CHECK);
		btnExecuteQuestionDml.setText(Messages.get().OthersConnectionRDBGroup_6);
		
//		btnIsVisible = new Button(this, SWT.CHECK);
//		btnIsVisible.setSelection(true);
//		btnIsVisible.setText(Messages.get().OthersConnectionRDBGroup_btnIsVisible_text);
		
//		btnSendMonitoring = new Button(this, SWT.CHECK);
//		btnSendMonitoring.setEnabled(false);
//		btnSendMonitoring.setSelection(false);
//		btnSendMonitoring.setText(Messages.get().OthersConnectionRDBGroup_btnSendMonitoring_text);
		
//		btnIsMonitoring = new Button(this, SWT.CHECK);
//		btnIsMonitoring.setSelection(true);
//		btnIsMonitoring.setText(Messages.get().OthersConnectionGroup_btnIsMonitoring_text);
//		new Label(this, SWT.NONE);
//		new Label(this, SWT.NONE);
		
		initUI();
	}

	/**
	 * initialize UI
	 */
	public abstract void initUI();
	
	/**
	 * 기존에 데이터를 가지고 있었을 경우에 값을 설정 합니다.
	 * @param oldUserDB
	 */
	public void setUserData(UserDBDAO oldUserDB) {
		setBtnReadOnlyConnection(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_readOnlyConnect())?true:false);
		setBtnAutoCommit(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_autocommit())?true:false);
		setBtnShowTables(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_showtables())?true:false);
		
		setBtnProfiler(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_profile())?true:false);
		
		setBtnExecuteQuestionDml(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getQuestion_dml())?true:false);
		
//		setIsVisible(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_visible())?true:false);
//		setSendMonitoring(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_summary_report())?true:false);
//		setIsMonitoring(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_monitoring())?true:false);
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
		
//		otherConnectionDAO.setVisible(getIsVisible());
//		otherConnectionDAO.setSummaryReport(getSendMonitoring());
//		otherConnectionDAO.setMonitoring(getIsMonitoring());
		
		return otherConnectionDAO;
	}
	
	/**
	 * @return the btnReadOnlyConnection
	 */
	public boolean getBtnReadOnlyConnection() {
		return btnReadOnlyConnection.getSelection();
	}
	public void setBtnReadOnlyConnection(boolean isSelect) {
		btnReadOnlyConnection.setSelection(isSelect);
	}

	/**
	 * @return the btnAutoCommit
	 */
	public boolean getBtnAutoCommit() {
		return btnAutoCommit.getSelection();
	}
	public void  setBtnAutoCommit(boolean isSelect) {
		btnAutoCommit.setSelection(isSelect);
	}

	/**
	 * @return the btnTunneling
	 */
	public boolean getBtnTunneling() {
		return btnExternalBrowser.getSelection();
	}
	public void  setBtnTunneling(boolean isSelect) {
		btnExternalBrowser.setSelection(isSelect);
	}

	public boolean getBtnExecuteQuestionDml() {
		return btnExecuteQuestionDml.getSelection();
	}
	public void setBtnExecuteQuestionDml(boolean isSelect) {
		btnExecuteQuestionDml.setSelection(isSelect);
	}
	
	public boolean getBtnProfiler() {
		return btnProfiler.getSelection();
	}
	public void setBtnProfiler(boolean isSelect) {
		btnProfiler.setSelection(isSelect);
	}
	
//	public boolean getIsVisible() {
//		return btnIsVisible.getSelection();
//	}
//	public void setIsVisible(boolean isSelect) {
//		btnIsVisible.setSelection(isSelect);
//	}
	
//	public boolean getSendMonitoring() {
//		return btnSendMonitoring.getSelection();
//	}
//	public void setSendMonitoring(boolean isSelect) {
//		btnIsMonitoring.setSelection(isSelect);
//	}
//	
//	public boolean getIsMonitoring() {
//		return btnSendMonitoring.getSelection();
//	}
//	public void setIsMonitoring(boolean isSelect) {
//		btnIsMonitoring.setSelection(isSelect);
//	}

	/**
	 * @param btnShowTables
	 */
	public void setBtnShowTables(boolean btnShowTables) {
		this.btnShowTables.setSelection(btnShowTables);
	}
	public boolean getBtnShowTables() {
		return btnShowTables.getSelection();
	}

	@Override
	public List<ExternalBrowserInfoDAO> getDefaultExternalBrowserInfo() {
		return new ArrayList<ExternalBrowserInfoDAO>();
	}
}
