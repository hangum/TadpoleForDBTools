/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.db;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.AbstractLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.CubridLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.MSSQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.MongoDBLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.MySQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.OracleLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.PostgresLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.SQLiteLoginComposite;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.system.permission.PermissionChecks;

/**
 * DB 정보를 보여 주는 다이얼로그
 * 
 * @author hangum
 *
 */
public class DBInformationDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DBInformationDialog.class);
	
	private Composite container;
	private Composite compositeBody;
	
	private UserDBDAO userDB;
	private AbstractLoginComposite loginComposite;
	/** group name */
	private List<String> groupName;
	private String selGroupName;	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBInformationDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Database Information"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		
		Composite compositeTail = new Composite(container, SWT.NONE);
		GridLayout gl_compositeTail = new GridLayout(1, false);
		gl_compositeTail.verticalSpacing = 3;
		gl_compositeTail.horizontalSpacing = 3;
		gl_compositeTail.marginHeight = 3;
		gl_compositeTail.marginWidth = 3;
		compositeTail.setLayout(gl_compositeTail);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Group grpOtherInformation = new Group(compositeTail, SWT.NONE);
		grpOtherInformation.setLayout(new GridLayout(2, false));
		grpOtherInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpOtherInformation.setText("User");
		
		Label lblGroupName = new Label(grpOtherInformation, SWT.NONE);
		lblGroupName.setText("Group: ");
		
		Label lblGroupValue = new Label(grpOtherInformation, SWT.NONE);
		lblGroupValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblGroupValue.setText(SessionManager.getGroupName());
		
		Label lblEmail = new Label(grpOtherInformation, SWT.NONE);
		lblEmail.setText("email: ");
		
		Label lblEmailValue = new Label(grpOtherInformation, SWT.NONE);
		lblEmailValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblEmailValue.setText(SessionManager.getEMAIL());
		
		Label lblName = new Label(grpOtherInformation, SWT.NONE);
		lblName.setText("Name: ");
		new Label(grpOtherInformation, SWT.NONE);
		
		if(PermissionChecks.isShow(SessionManager.getLoginType(), userDB)) {
			Label lblNameValue = new Label(grpOtherInformation, SWT.NONE);
			lblNameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lblNameValue.setText(SessionManager.getName());
			
			compositeBody = new Composite(container, SWT.NONE);
			compositeBody.setLayout(new GridLayout(1, false));
			compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
			initDBWidget();
		} else {
			Group grpDetail = new Group(container, SWT.NONE);
			grpDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			grpDetail.setText("Detail");
			grpDetail.setLayout(new GridLayout(1, false));
			
			Label lblNewLabel = new Label(grpDetail, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lblNewLabel.setText(Messages.MainEditor_21);
		}
		
		return container;
	}
	
	/**
	 * db widget
	 */
	private void initDBWidget() {
		// db groupData 
		try {
			groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getSeq());
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		selGroupName = userDB.getGroup_name();
		
		DBDefine dbDefine = DBDefine.getDBDefine(userDB.getTypes());
		if (dbDefine == DBDefine.MYSQL_DEFAULT) {
			loginComposite = new MySQLLoginComposite(DBDefine.MYSQL_DEFAULT, compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT) {
			loginComposite = new OracleLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.SQLite_DEFAULT) {
			loginComposite = new SQLiteLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.MSSQL_DEFAULT) {
			loginComposite = new MSSQLLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.CUBRID_DEFAULT) {
			loginComposite = new CubridLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.POSTGRE_DEFAULT) {
			loginComposite = new PostgresLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.MONGODB_DEFAULT) {
			loginComposite = new MongoDBLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName, userDB);
		}
		compositeBody.layout();
		container.layout();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
//		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(452, 530);
	}

}
