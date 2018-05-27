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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Others connection group
 * 
 * @author hangum
 *
 */
public class HistoryHubConnectionGroup extends AbstractOthersConnection {
	
	/** read only connection */
	protected Button btnHistoryHubLocation;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public HistoryHubConnectionGroup(Composite parent, int style, DBDefine selectDB) {
		super(parent, style, selectDB);
		setText(Messages.get().SettingOtherInfo);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		btnHistoryHubLocation = new Button(this, SWT.CHECK);
		btnHistoryHubLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnHistoryHubLocation.setSelection(true);
		btnHistoryHubLocation.setText(Messages.get().TadpoleHistoryHubDataLocation);
		
		initUI();
	}
	
	public void setUserData(UserDBDAO oldUserDB) {
		btnHistoryHubLocation.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_history_data_location()));
	}

	/**
	 * initialize UI
	 */
	public void initUI() {
	}

	/**
	 * other db connection info
	 * 
	 * @return
	 */
	public OthersConnectionInfoDAO getOthersConnectionInfo() {
		otherConnectionDAO.setHistoryHubLocation(btnHistoryHubLocation.getSelection());
		otherConnectionDAO.setReadOnlyConnection(true);
		otherConnectionDAO.setAutoCommit(true);
		otherConnectionDAO.setShowTables(false);
		
		otherConnectionDAO.setProfiling(true);
		otherConnectionDAO.setDMLStatement(false);
		
		return otherConnectionDAO;
	}
	
	@Override
	public List<ExternalBrowserInfoDAO> getDefaultExternalBrowserInfo() {
		return new ArrayList<ExternalBrowserInfoDAO>();
	}
}
