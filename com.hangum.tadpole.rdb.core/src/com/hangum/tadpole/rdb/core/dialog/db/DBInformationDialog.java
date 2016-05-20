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

import java.util.ArrayList;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.DBConnectionUtils;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.session.manager.SessionManager;

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
	private List<String> listGroupName = new ArrayList<String>();
	private String selGroupName;	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBInformationDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().DatabaseInformation);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		grpOtherInformation.setText(Messages.get().User);
		
//		Label lblGroupName = new Label(grpOtherInformation, SWT.NONE);
//		lblGroupName.setText(Messages.get().DBInformationDialog_2);
//		
//		Label lblGroupValue = new Label(grpOtherInformation, SWT.NONE);
//		lblGroupValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		lblGroupValue.setText(SessionManager.getGroupName());
		
		Label lblEmail = new Label(grpOtherInformation, SWT.NONE);
		lblEmail.setText(Messages.get().Email);
		
		Label lblEmailValue = new Label(grpOtherInformation, SWT.NONE);
		lblEmailValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblEmailValue.setText(SessionManager.getEMAIL());
		
		Label lblName = new Label(grpOtherInformation, SWT.NONE);
		lblName.setText(Messages.get().Name);
		
		Label lblNameValue = new Label(grpOtherInformation, SWT.NONE);
		lblNameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNameValue.setText(SessionManager.getName());
		
		if(PermissionChecker.isShow(userDB.getRole_id())) {
			compositeBody = new Composite(container, SWT.NONE);
			compositeBody.setLayout(new GridLayout(1, false));
			compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
			initDBWidget();
		} else {
			Group grpDetail = new Group(container, SWT.NONE);
			grpDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			grpDetail.setText(Messages.get().Detail);
			grpDetail.setLayout(new GridLayout(1, false));
			
			Label lblNewLabel = new Label(grpDetail, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lblNewLabel.setText(Messages.get().MainEditor_21);
		}
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * db widget
	 */
	private void initDBWidget() {
		try {
			listGroupName = TadpoleSystem_UserDBQuery.getUserGroupName();
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		selGroupName = userDB.getGroup_name();
		
		loginComposite = DBConnectionUtils.getDBConnection(userDB.getDBDefine(), compositeBody, listGroupName, selGroupName, userDB);
		compositeBody.layout();
		container.layout();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		DBDefine dbDefine = userDB.getDBDefine();
		if (dbDefine == DBDefine.SQLite_DEFAULT) {
			return new Point(570, 500);
		} else if(dbDefine == DBDefine.HIVE_DEFAULT) {
			return new Point(560, 510);
		} else {
			return new Point(560, 550);
		}
	}

}
