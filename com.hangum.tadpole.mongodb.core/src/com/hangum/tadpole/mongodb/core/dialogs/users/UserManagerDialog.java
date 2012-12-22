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
package com.hangum.tadpole.mongodb.core.dialogs.users;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.dto.UserDTO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 사용자를 추가합니다.
 * 
 * @author hangum
 *
 */
public class UserManagerDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserManagerDialog.class);
	
	private static int DELETE_ID = 999;
	
	private UserDBDAO userDB;
	private Text textID;
	private Text textPassword;
	private Text textRePassword;
	private Button btnReadOnly;
	private Composite composite;
	
	private TableViewer tableViewerUser;
	private List<UserDTO> listUser = new ArrayList<UserDTO>();
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public UserManagerDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		gridLayout.numColumns = 2;
		
		Label lblId = new Label(container, SWT.NONE);
		lblId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblId.setText("ID"); //$NON-NLS-1$
		
		textID = new Text(container, SWT.BORDER);
		textID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password"); //$NON-NLS-1$
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePassword.setText("Re Password"); //$NON-NLS-1$
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnReadOnly = new Button(container, SWT.CHECK);
		btnReadOnly.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnReadOnly.setText("Read Only"); //$NON-NLS-1$
		
		composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Group grpUserList = new Group(composite, SWT.NONE);
		grpUserList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpUserList.setText("User List"); //$NON-NLS-1$
		grpUserList.setLayout(new GridLayout(1, false));
		
		tableViewerUser = new TableViewer(grpUserList, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewerUser.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerUser, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(162);
		tblclmnId.setText("ID"); //$NON-NLS-1$
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerUser, SWT.NONE);
		TableColumn tblclmnReadOnly = tableViewerColumn_1.getColumn();
		tblclmnReadOnly.setWidth(100);
		tblclmnReadOnly.setText("Read Only"); //$NON-NLS-1$
		
		tableViewerUser.setContentProvider(new ArrayContentProvider());
		tableViewerUser.setLabelProvider(new UserListLabelProvider());
		tableViewerUser.setInput(listUser);
		
		initTable();
		
		textID.setFocus();
		return container;
	}
	
	/**
	 * table data
	 */
	private void initTable() {
		listUser.clear();		
		DBCursor userCursor = null;
		
		try {
			userCursor = MongoDBQuery.getUser(userDB);
			for (DBObject dbObject : userCursor) {
				UserDTO user = new UserDTO();
				user.setId( dbObject.get("user").toString() ); //$NON-NLS-1$
				user.setReadOnly( dbObject.get("readOnly").toString() ); //$NON-NLS-1$
				
				listUser.add(user);
			}
			
			tableViewerUser.refresh();
			
		} catch (Exception e) {
			logger.error("mongodb user list", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Get User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			if(userCursor != null) userCursor.close();
		}
		
	}
	
	@Override
	protected void okPressed() {
		String id = textID.getText().trim();
		String passwd = textPassword.getText().trim();
		String passwd2 = textRePassword.getText().trim();
		boolean isReadOnly = btnReadOnly.getSelection();
		
		if("".equals(id)) { //$NON-NLS-1$
			MessageDialog.openError(null, "Error", Messages.UserManagerDialog_11); //$NON-NLS-1$
			textID.setFocus();
			return;
		} else if("".equals(passwd)) { //$NON-NLS-1$
			MessageDialog.openError(null, "Error", Messages.UserManagerDialog_14); //$NON-NLS-1$
			textPassword.setFocus();
			return;
		} else if("".equals(passwd2)) { //$NON-NLS-1$
			MessageDialog.openError(null, "Error", Messages.UserManagerDialog_17); //$NON-NLS-1$
			textRePassword.setFocus();
			return;
		} else if(!passwd.equals(passwd2)) {
			MessageDialog.openError(null, "Error", Messages.UserManagerDialog_19); //$NON-NLS-1$
			textPassword.setFocus();
			return;
		}
		
		try {
			MongoDBQuery.addUser(userDB, id, passwd2, isReadOnly);
		} catch (Exception e) {
			logger.error("mongodb add user", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Add User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$

			return;
		}
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, DELETE_ID, "Delete", false); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection is = (IStructuredSelection)tableViewerUser.getSelection();
				Object selElement = is.getFirstElement();
				
				if(selElement instanceof UserDTO) {
					if(MessageDialog.openConfirm(null, "Confirm", Messages.UserManagerDialog_22)) { //$NON-NLS-1$
						UserDTO user = (UserDTO)selElement;
						try {
							MongoDBQuery.deleteUser(userDB, user.getId());
							
							listUser.remove(user);
							tableViewerUser.refresh();
						} catch (Exception e1) {
							logger.error("mongodb delete user", e1); //$NON-NLS-1$
							
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, "Error", "Delete User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			}
		});
		
		createButton(parent, IDialogConstants.OK_ID, "Ok", true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(449, 379);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add User Dialog"); //$NON-NLS-1$
	}

}

class UserListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDTO dto = (UserDTO)element;
		
		switch(columnIndex) {
		case 0: return dto.getId();
		case 1: return dto.getReadOnly();		
		}
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}
