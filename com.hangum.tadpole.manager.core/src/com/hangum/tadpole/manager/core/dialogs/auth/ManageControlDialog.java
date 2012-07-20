package com.hangum.tadpole.manager.core.dialogs.auth;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.hangum.db.define.Define;
import com.hangum.db.rap.commons.session.SessionManager;

/**
 * user관리 화면
 * 
 * @author hangum
 *
 */
public class ManageControlDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ManageControlDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("User Manager");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if(Define.USER_TYPE.MANAGER.toString().equals( SessionManager.getLoginType() )) {
			TabItem tbtmManager = new TabItem(tabFolder, SWT.NONE);
			tbtmManager.setText("Manager");

			Composite composite = new AdminComposite(tabFolder, SWT.NONE);
			tbtmManager.setControl(composite);
			composite.setLayout(new GridLayout(1, false));

		} else {
			TabItem tbtmAdmin = new TabItem(tabFolder, SWT.NONE);
			tbtmAdmin.setText("Admin");
			
			Composite composite = new AdminComposite(tabFolder, SWT.NONE);
			tbtmAdmin.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
		}

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
//		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(876, 566);
	}

}
