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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.ext.aws.rds.AWSRDSUserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AWSRDSLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.CubridLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.HiveLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MSSQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MariaDBLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MongoDBLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MySQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.OracleLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.PostgresLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.SQLiteLoginComposite;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;

/**
 * Single DB ADD Dialog
 * 
 * @author hangum
 *
 */
public class SingleAddDBDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SingleAddDBDialog.class);
	
	private AWSRDSUserDBDAO amazonRDSDto;
	/** group name */
	protected List<String> listGroupName;
	/** 초기 선택한 그룹 */
	private String selGroupName;
	
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;
	
	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SingleAddDBDialog(Shell parentShell, AWSRDSUserDBDAO amazonRDSDto, List<String> listGroupName, String selGroupName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.amazonRDSDto = amazonRDSDto;
		this.listGroupName = listGroupName;
		this.selGroupName = selGroupName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(amazonRDSDto.getDbms_types() + " add Database"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		
		compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		DBDefine dbDefine = DBDefine.getDBDefine(amazonRDSDto.getDbms_types());
		UserDBDAO userDB = (UserDBDAO)amazonRDSDto;
		if (dbDefine == DBDefine.MYSQL_DEFAULT) {
			loginComposite = new MySQLLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.MARIADB_DEFAULT) {	
			loginComposite = new MariaDBLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT) {
			loginComposite = new OracleLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.SQLite_DEFAULT) {
			loginComposite = new SQLiteLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.MSSQL_DEFAULT) {
			loginComposite = new MSSQLLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.CUBRID_DEFAULT) {
			loginComposite = new CubridLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.POSTGRE_DEFAULT) {
			loginComposite = new PostgresLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.MONGODB_DEFAULT) {
			loginComposite = new MongoDBLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.AMAZONRDS_DEFAULT) {
			loginComposite = new AWSRDSLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.HIVE_DEFAULT) {
			loginComposite = new HiveLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		}

		return container;
	}
	
	@Override
	protected void okPressed() {
		if (!loginComposite.connection()) return;
		this.retuserDb = loginComposite.getDBDTO();
		refreshManagerView();
		
		super.okPressed();
	}

	public UserDBDAO getDTO() {
		return retuserDb;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(DBLoginDialog.TEST_CONNECTION_ID == buttonId) {
			if(loginComposite.testConnection()) {
				MessageDialog.openInformation(null, "Confirm", "Connection Successful.");
			}
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, DBLoginDialog.TEST_CONNECTION_ID, "Test Connection", false);
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancle", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 500);
	}
	
	/**
	 * refresh manager view
	 */
	protected void refreshManagerView() {
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);			
		
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				managerView.init();
			}
		});	// end display
	}
}
