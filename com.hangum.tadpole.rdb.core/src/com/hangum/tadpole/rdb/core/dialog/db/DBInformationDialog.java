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

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.DBConnectionUtils;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;

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
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DBInformationDialog_0);
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
		grpOtherInformation.setText(Messages.DBInformationDialog_1);
		
//		Label lblGroupName = new Label(grpOtherInformation, SWT.NONE);
//		lblGroupName.setText(Messages.DBInformationDialog_2);
//		
//		Label lblGroupValue = new Label(grpOtherInformation, SWT.NONE);
//		lblGroupValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		lblGroupValue.setText(SessionManager.getGroupName());
		
		Label lblEmail = new Label(grpOtherInformation, SWT.NONE);
		lblEmail.setText(Messages.DBInformationDialog_3);
		
		Label lblEmailValue = new Label(grpOtherInformation, SWT.NONE);
		lblEmailValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblEmailValue.setText(SessionManager.getEMAIL());
		
		Label lblName = new Label(grpOtherInformation, SWT.NONE);
		lblName.setText(Messages.DBInformationDialog_4);
		
		if(PermissionChecker.isShow(SessionManager.getRepresentRole())) {//SessionManager.getRoleType(userDB.getGroup_seq()), userDB)) {
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
			grpDetail.setText(Messages.DBInformationDialog_5);
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
		try {
			groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getGroupSeqs());
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		selGroupName = userDB.getGroup_name();
		
		DBDefine dbDefine = DBDefine.getDBDefine(userDB);
		loginComposite = DBConnectionUtils.getDBConnection(dbDefine, compositeBody, groupName, selGroupName, userDB);
		compositeBody.layout();
		container.layout();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.DBInformationDialog_6, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		DBDefine dbDefine = DBDefine.getDBDefine(userDB);
		if (dbDefine == DBDefine.SQLite_DEFAULT) {
			return new Point(450, 460);
		} else if(dbDefine == DBDefine.HIVE_DEFAULT) {
			return new Point(450, 540);
		} else {
			return new Point(450, 590);
		}
	}

}
