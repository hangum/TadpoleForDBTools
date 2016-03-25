/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.db.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.manager.core.Messages;

import org.eclipse.swt.widgets.Label;

/**
 * <pre>
 * Other info setting dialog
 * 	- db visible option
 *  - db lock option
 *
 * </pre>
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class DBOthresConfigDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(DBOthresConfigDialog.class);
	
	private UserDBDAO userDB;
	private Button btnVisible;
	private Button btnDbLock;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBOthresConfigDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().DBOthresConfigDialog_0);
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
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		Label lblSettingDatabaseOther = new Label(compositeHead, SWT.NONE);
		lblSettingDatabaseOther.setText(Messages.get().DBOthresConfigDialog_0);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		btnVisible = new Button(composite, SWT.CHECK);
		btnVisible.setText(Messages.get().DBOthresConfigDialog_2);
		
		btnDbLock = new Button(composite, SWT.CHECK);
		btnDbLock.setText(Messages.get().DBOthresConfigDialog_3);
		
		initUI();

		return container;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if(MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().DBOthresConfigDialog_5)) {
			try {
				userDB.setIs_visible(btnVisible.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
				userDB.setIs_lock(btnDbLock.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
				
				TadpoleSystem_UserDBQuery.updateDBOtherInformation(userDB);
			} catch (Exception e) {
				logger.error("update faile", e); //$NON-NLS-1$
			}
			
			super.okPressed();
		} else {
			return;
		}
	}
	
	/**
	 * ui initialize 
	 */
	private void initUI() {
		String isVisible = userDB.getIs_visible();
		String isLock 	= userDB.getIs_lock();
		
		btnVisible.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(isVisible));
		btnDbLock.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(isLock));
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(300, 200);
	}
	
	/**
	 * 나중에 결과 리턴용.
	 * @return
	 */
	public UserDBDAO getUserDBDAO() {
		return userDB;
	}

}
