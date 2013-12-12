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

import com.hangum.tadpole.notes.core.Messages;
import com.hangum.tadpole.notes.core.define.NotesDefine;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_Notes;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * new note dialog
 * 
 * @author hangum
 *
 */
public class NewNoteDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(NewNoteDialog.class);
	
//	private Combo comboTypes;
	private Combo comboUserName;
	private Text textContent;
	private Text textTitle;
	
	private NotesDAO notesDAO;
	private String strReceiver;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public NewNoteDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewNoteDialog(Shell parentShell, NotesDAO noteDAO, String strReceiver) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.notesDAO  = noteDAO;
		this.strReceiver = strReceiver;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.NewNoteDialog_1); 
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
		lblTypes.setText("User");//Messages.NewNoteDialog_2); 
		
//		comboTypes = new Combo(compositeHead, SWT.READ_ONLY);
//		comboTypes.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(NotesDefine.TYPES.GROUP.toString().equals(comboTypes.getText())) {
//					comboUserName.setEnabled(false);
//				} else {
//					comboUserName.setEnabled(true);
//				}
//			}
//		});
//		comboTypes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		for(NotesDefine.TYPES type: NotesDefine.TYPES.values()) {
//			comboTypes.add(type.toString());
//		}
//		comboTypes.select(1);
		
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
		lblTitle.setText(Messages.NewNoteDialog_3); 
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblContent = new Label(compositeBody, SWT.NONE);
		lblContent.setText(Messages.NewNoteDialog_4); 
		
		textContent = new Text(compositeBody, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();

		return container;
	}
	
	@Override
	protected void okPressed() {
		// 사용자 검사.
		String strSender = comboUserName.getText();
		if("".equals(strSender)) { //$NON-NLS-1$
			MessageDialog.openConfirm(null, Messages.NewNoteDialog_0, Messages.NewNoteDialog_5); 
			comboUserName.setFocus();
			return;
		}
		UserDAO userDao = null;
		try {
			userDao = TadpoleSystem_UserQuery.findUser(strSender);
		} catch(Exception e) {
			MessageDialog.openError(null, Messages.NewNoteDialog_0, e.getMessage()); 
			return;
		}
		
		// 타이틀 검사.
		String strTitle = textTitle.getText();
		if("".equals(strTitle)) { 
			MessageDialog.openConfirm(null, Messages.NewNoteDialog_0, Messages.NewNoteDialog_8); 
			textTitle.setFocus();
			return;
		}
		
		// 내용 검사.
		String strContent = textContent.getText();
		if("".equals(strContent)) { 
			MessageDialog.openConfirm(null, Messages.NewNoteDialog_0, Messages.NewNoteDialog_11); 
			textContent.setFocus();
			return;
		}
		
		// find receive seq
		try {
			// User data save
			TadpoleSystem_Notes.saveNote(NotesDefine.TYPES.PERSON.toString(), SessionManager.getSeq(), userDao.getSeq(), strTitle, strContent);
		} catch(Exception e) {
			logger.error("note save", e); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.NewNoteDialog_0, e.getMessage()); 
			return;
		}
		
		super.okPressed();
	}
	
	/**
	 * initialize combo data
	 */
	private void initUI() {
		// combo 초기화.
		try {
			List<UserGroupAUserDAO> listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getGroupSeqs());
			for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
				comboUserName.add(userGroupAUserDAO.getEmail());
			}
			comboUserName.select(0);			
		} catch(Exception e) {
			logger.error("init user list", e); //$NON-NLS-1$
		}
		
		// 대답할 때면. 
		if(notesDAO != null) {
//			comboTypes.setText(notesDAO.getTypes());
			comboUserName.setText(strReceiver);
			
			textTitle.setText("RE] " + notesDAO.getTitle()); //$NON-NLS-1$
			textContent.setText("\n\n----------------------------------------\n" + notesDAO.getContents()); //$NON-NLS-1$
			
			textContent.setFocus();
		} else {
			textTitle.setFocus();
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.NewNoteDialog_6, true); 
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.NewNoteDialog_7, false); 
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 500);
	}

}
