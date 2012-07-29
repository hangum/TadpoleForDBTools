package com.hangum.tadpole.mongodb.core.dialogs.users;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

import org.eclipse.swt.widgets.Button;

/**
 * 사용자를 추가합니다.
 * 
 * @author hangum
 *
 */
public class AddUserDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AddUserDialog.class);
	
	private UserDBDAO userDB;
	private Text textID;
	private Text textPassword;
	private Text textRePassword;
	private Button btnReadOnly;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddUserDialog(Shell parentShell, UserDBDAO userDB) {
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
		gridLayout.numColumns = 2;
		
		Label lblId = new Label(container, SWT.NONE);
		lblId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblId.setText("ID");
		
		textID = new Text(container, SWT.BORDER);
		textID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePassword.setText("Re Password");
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnReadOnly = new Button(container, SWT.CHECK);
		btnReadOnly.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnReadOnly.setText("Read Only");
		
		textID.setFocus();

		return container;
	}
	
	@Override
	protected void okPressed() {
		String id = textID.getText().trim();
		String passwd = textPassword.getText().trim();
		String passwd2 = textRePassword.getText().trim();
		boolean isReadOnly = btnReadOnly.getSelection();
		
		if("".equals(id)) {
			MessageDialog.openError(null, "Error", "ID가 공백입니다.");
			textID.setFocus();
			return;
		} else if("".equals(passwd)) {
			MessageDialog.openError(null, "Error", "Password가 공백입니다.");
			textPassword.setFocus();
			return;
		} else if("".equals(passwd2)) {
			MessageDialog.openError(null, "Error", "Re Password가 공백입니다.");
			textRePassword.setFocus();
			return;
		} else if(!passwd.equals(passwd2)) {
			MessageDialog.openError(null, "Error", "두 Password가 동일하지 않습니다.");
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
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(365, 217);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add User Dialog"); //$NON-NLS-1$
	}

}
