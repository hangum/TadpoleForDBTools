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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;

/**
 * Modify DB Dialog
 * 
 * @author hangum
 *
 */
public class ModifyDBDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ModifyDBDialog.class);
	
	private UserDBDAO userDBDAO;
	/** group name */
	protected List<String> listGroupName;
//	/** 초기 선택한 그룹 */
//	private String selGroupName;
	
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;
	
	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ModifyDBDialog(Shell parentShell, UserDBDAO userDBDAO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDBDAO = userDBDAO;
		try {
			listGroupName = TadpoleSystem_UserDBQuery.getUserGroupName();
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(userDBDAO.getDbms_type() + " Database"); //$NON-NLS-1$
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		
		loginComposite = DBConnectionUtils.getDBConnection(userDBDAO.getDBDefine(), 
															compositeBody, 
															listGroupName, 
															userDBDAO.getGroup_name(), 
															userDBDAO,
															DATA_STATUS.MODIFY
				);

		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		if (!loginComposite.saveDBData()) return;
		this.retuserDb = loginComposite.getDBDTO();
		refreshManagerView();
		
		// 
		TadpoleSQLManager.removeInstance(userDBDAO);
		
		super.okPressed();
	}

	public UserDBDAO getDTO() {
		return retuserDb;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(DBLoginDialog.TEST_CONNECTION_ID == buttonId) {
			if(loginComposite.testConnection(true)) {
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().ModifyDBDialog_1);
			}
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, DBLoginDialog.TEST_CONNECTION_ID, Messages.get().ModifyDBDialog_2, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
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
