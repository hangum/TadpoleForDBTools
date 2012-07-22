package com.hangum.tadpole.preference.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.dao.system.UserDAO;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.Messages;

/**
 * 사용자 정보 수정
 * 
 * @author hangum
 *
 */
public class UserInfoPerference extends PreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(UserInfoPerference.class);
	
	private Text textGroupName;
	private Text textEmail;
	private Text textPassword;
	private Text textRePassword;
	private Text textName;

	/**
	 * Create the preference page.
	 */
	public UserInfoPerference() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroupName.setText("Group Name"); //$NON-NLS-1$
		
		textGroupName = new Text(container, SWT.BORDER);
		textGroupName.setEnabled(false);
		textGroupName.setEditable(false);
		textGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textGroupName.setText(SessionManager.getName());
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("email"); //$NON-NLS-1$
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setEnabled(false);
		textEmail.setEditable(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.setText(SessionManager.getEMAIL());
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("password"); //$NON-NLS-1$
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setText(SessionManager.getPassword());
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePassword.setText("re password"); //$NON-NLS-1$
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textRePassword.setText(SessionManager.getPassword());
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name"); //$NON-NLS-1$
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.setText(SessionManager.getName());
		
		return container;
	}

	@Override
	public boolean performOk() {
		String pass = textPassword.getText().trim();
		String rePass = textRePassword.getText().trim();
		
		if(!pass.equals(rePass)) {
			MessageDialog.openError(getShell(), Messages.UserInfoPerference_0, Messages.UserInfoPerference_6);
			return false;
		}
		
		UserDAO user = new UserDAO();
		user.setSeq(SessionManager.getSeq());
		user.setPasswd(pass);		
		try {
			TadpoleSystem_UserQuery.updateUserPassword(user);
			
			MessageDialog.openConfirm(getShell(), Messages.UserInfoPerference_0, Messages.UserInfoPerference_8);
		} catch (Exception e) {
			logger.error("password change", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", e.getMessage());			 //$NON-NLS-1$
			
			return false;
		}
		
		return super.performOk();
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
