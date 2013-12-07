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
package com.hangum.tadpole.notes.core.dialogs;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.notes.core.define.NotesDefine;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_Notes;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;

/**
 * new note dialog
 * 
 * @author hangum
 *
 */
public class NewNoteDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(NewNoteDialog.class);
	
	private Combo comboTypes;
	private Combo comboUserName;
	private Text textContent;
	private Text textTitle;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewNoteDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Note"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayout(new GridLayout(3, false));
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTypes = new Label(compositeHead, SWT.NONE);
		lblTypes.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTypes.setText("Types");
		
		comboTypes = new Combo(compositeHead, SWT.NONE);
		comboTypes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(NotesDefine.TYPES type: NotesDefine.TYPES.values()) {
			comboTypes.add(type.toString());
		}
		comboTypes.select(1);
		
		comboUserName = new Combo(compositeHead, SWT.NONE);
		comboUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(2, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		Label lblTitle = new Label(compositeBody, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblContent = new Label(compositeBody, SWT.NONE);
		lblContent.setText("Content");
		
		textContent = new Text(compositeBody, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();

		return container;
	}
	
	@Override
	protected void okPressed() {
		String strSender = comboUserName.getText();
		if("".equals(strSender)) {
			MessageDialog.openConfirm(null, "Confirm", "사용자를 입력하여 주십시오.");
			comboUserName.setFocus();
			return;
		}
		
		String strTitle = textTitle.getText();
		if("".equals(strTitle)) {
			MessageDialog.openConfirm(null, "Confirm", "제목을 입력하여 주십시오.");
			textTitle.setFocus();
			return;
		}
		
		String strContent = textContent.getText();
		if("".equals(strContent)) {
			MessageDialog.openConfirm(null, "Confirm", "내용을 입력하여 주십시오.");
			textContent.setFocus();
			return;
		}
		
		// find receive seq
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.findUser(strSender);
			
			// User data save
			TadpoleSystem_Notes.saveNote(comboTypes.getText(), SessionManager.getSeq(), userDao.getSeq(), strTitle, strContent);
		} catch(Exception e) {
			logger.error("note save", e);
			MessageDialog.openError(null, "Confirm", e.getMessage());
			return;
		}
		
		super.okPressed();
	}
	
	/**
	 * initialize combo data
	 */
	private void initUI() {
		comboUserName.add("All");
		
		try {
			List<UserGroupAUserDAO> listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getGroupSeqs());
			for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
				comboUserName.add(userGroupAUserDAO.getEmail());
			}
			
		} catch(Exception e) {
			logger.error("init user list", e);
		}

		comboUserName.select(1);
		textTitle.setFocus();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Send", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
