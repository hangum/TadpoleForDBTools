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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
	
	private static int DELETE_ID = IDialogConstants.CLIENT_ID + 1;
	private static int APPEND_USER_ID = IDialogConstants.CLIENT_ID + 2;
	
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
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
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
		lblId.setText(Messages.get().UserManagerDialog_0);
		
		textID = new Text(container, SWT.BORDER);
		textID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.get().UserManagerDialog_1);
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.get().UserManagerDialog_2);
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnReadOnly = new Button(container, SWT.CHECK);
		btnReadOnly.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnReadOnly.setText(Messages.get().UserManagerDialog_3);
		
		composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Group grpUserList = new Group(composite, SWT.NONE);
		grpUserList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpUserList.setText(Messages.get().UserManagerDialog_5);
		grpUserList.setLayout(new GridLayout(1, false));
		
		tableViewerUser = new TableViewer(grpUserList, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewerUser.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerUser, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(162);
		tblclmnId.setText(Messages.get().UserManagerDialog_0);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerUser, SWT.NONE);
		TableColumn tblclmnReadOnly = tableViewerColumn_1.getColumn();
		tblclmnReadOnly.setWidth(100);
		tblclmnReadOnly.setText(Messages.get().UserManagerDialog_3);
		
		tableViewerUser.setContentProvider(ArrayContentProvider.getInstance());
		tableViewerUser.setLabelProvider(new UserListLabelProvider());
		tableViewerUser.setInput(listUser);
		
		initTable();
		
		textID.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
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
			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Get User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			if(userCursor != null) userCursor.close();
		}
		
	}
	
	protected void buttonPressed(int buttonId) {
		if(APPEND_USER_ID == buttonId) {
			String id = textID.getText().trim();
			String passwd = textPassword.getText().trim();
			String passwd2 = textRePassword.getText().trim();
			boolean isReadOnly = btnReadOnly.getSelection();
			
			if("".equals(id)) { //$NON-NLS-1$
				MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().UserManagerDialog_11); //$NON-NLS-1$
				textID.setFocus();
				return;
			} else if("".equals(passwd)) { //$NON-NLS-1$
				MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().UserManagerDialog_14); //$NON-NLS-1$
				textPassword.setFocus();
				return;
			} else if("".equals(passwd2)) { //$NON-NLS-1$
				MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().UserManagerDialog_17); //$NON-NLS-1$
				textRePassword.setFocus();
				return;
			} else if(!passwd.equals(passwd2)) {
				MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().UserManagerDialog_19); //$NON-NLS-1$
				textPassword.setFocus();
				return;
			}
			
			try {
				MongoDBQuery.addUser(userDB, id, passwd2, isReadOnly);
				
				initTable();
			} catch (Exception e) {
				logger.error("mongodb add user", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Add User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$

				return;
			}
		} else if(buttonId == DELETE_ID) {
			IStructuredSelection is = (IStructuredSelection)tableViewerUser.getSelection();
			Object selElement = is.getFirstElement();
			
			if(selElement instanceof UserDTO) {
				if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, Messages.get().UserManagerDialog_22)) { //$NON-NLS-1$
					UserDTO user = (UserDTO)selElement;
					try {
						MongoDBQuery.deleteUser(userDB, user.getId());
						
						listUser.remove(user);
						tableViewerUser.refresh();
					} catch (Exception e1) {
						logger.error("mongodb delete user", e1); //$NON-NLS-1$
						
						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Delete User Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			} else {
				MessageDialog.openError(null,CommonMessages.get().Error, Messages.get().DeleteMsg);
			}
		} else if(buttonId == IDialogConstants.CANCEL_ID) {
			super.cancelPressed();
		}
		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, DELETE_ID, CommonMessages.get().Delete, false);
		createButton(parent, APPEND_USER_ID, "Add User", true);
//		createButton(parent, IDialogConstants.OK_ID, Messages.get().UserManagerDialog_6, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  CommonMessages.get().Cancel, false);
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
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
