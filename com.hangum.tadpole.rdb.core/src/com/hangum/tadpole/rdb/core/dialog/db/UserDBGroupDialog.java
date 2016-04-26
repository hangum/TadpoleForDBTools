/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * 사용자 디비 선택 다이얼로그
 * 
 * @author hangum
 *
 */
public class UserDBGroupDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(UserDBGroupDialog.class);
	
	private List<UserDBDAO> listUserGroup = new ArrayList<>();
	private UserDBDAO oriUserDB;
	private org.eclipse.swt.widgets.List listDBGroup;
	private UserDBDAO userDB;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param oriUserDB 
	 */
	public UserDBGroupDialog(Shell parentShell, UserDBDAO oriUserDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		// get group list ----------------------------------
		try {
			listUserGroup = TadpoleSystem_UserDBQuery.getUserGroupDB(oriUserDB.getGroup_name());
		} catch(Exception e) {
			logger.error("get group info", e);
		}
		// ---------------------------------------------------
		this.oriUserDB = oriUserDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().GroupDBSelected);
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
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite composite = new Composite(container, SWT.H_SCROLL | SWT.V_SCROLL);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 1;
		gl_composite.horizontalSpacing = 1;
		gl_composite.marginHeight = 1;
		gl_composite.marginWidth = 1;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		listDBGroup = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listDBGroup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
		listDBGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		for (UserDBDAO userDBDAO : listUserGroup) {
			if(oriUserDB.getSeq() != userDBDAO.getSeq()) {
				String strDisplayName = String.format(userDBDAO.getDisplay_name());
				listDBGroup.add(strDisplayName);
				listDBGroup.setData(strDisplayName, userDBDAO);
			}
		}
		listDBGroup.select(0);
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		userDB = (UserDBDAO)listDBGroup.getData(listDBGroup.getItem(listDBGroup.getSelectionIndex()));

		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(280, 300);
	}
	
	/**
	 * selected user db
	 * @return
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}

}
