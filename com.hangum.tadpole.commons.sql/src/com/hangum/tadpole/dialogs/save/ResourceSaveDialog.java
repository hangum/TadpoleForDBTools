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
package com.hangum.tadpole.dialogs.save;

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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBResource;

/**
 * Resource save dialog
 * 
 * @author hangum
 *
 */
public class ResourceSaveDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ResourceSaveDialog.class);
	
	private UserDBDAO userDB;
	private PublicTadpoleDefine.RESOURCE_TYPE resourceType;
	
	private Text textName;
	private Text textDescription;
	private Combo comboSharedType;
	
	/** return UserDBResourceDAO */
	private UserDBResourceDAO retResourceDao = new UserDBResourceDAO();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param resourceType
	 */
	public ResourceSaveDialog(Shell parentShell, UserDBDAO userDB, PublicTadpoleDefine.RESOURCE_TYPE resourceType) {
		super(parentShell);
		
		this.userDB = userDB;
		this.resourceType = resourceType;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Resource Save Dialog"); //$NON-NLS-1$
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
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name");
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSharedType = new Label(container, SWT.NONE);
		lblSharedType.setText("Shared Type");
		
		comboSharedType = new Combo(container, SWT.READ_ONLY);
		comboSharedType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.SHARED_TYPE type : PublicTadpoleDefine.SHARED_TYPE.values()) {
			comboSharedType.add(type.toString());
		}
		comboSharedType.select(0);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description");
		
		textDescription = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return container;
	}
	
	@Override
	protected void okPressed() {
		String errMsg = isValid();
		if(null != errMsg) {
			MessageDialog.openError(null, "Confirm", errMsg);
			textName.setFocus();
			return;
		}
		
		retResourceDao.setDb_seq(userDB.getSeq());
		retResourceDao.setResource_types(resourceType.toString());
		retResourceDao.setGroup_seq(SessionManager.getGroupSeq());
		retResourceDao.setUser_seq(SessionManager.getSeq());
		
		retResourceDao.setName(textName.getText().trim());
		retResourceDao.setShared_type(comboSharedType.getText());
		retResourceDao.setDescription(textDescription.getText());
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Save", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	/**
	 * data validation
	 * 
	 * @param name
	 * @return
	 */
	public String isValid() {
		int len = textName.getText().length();
		if(len < 5) return "The name must enter at least 5 characters.";
		
		try {
			if(!TadpoleSystem_UserDBResource.userDBResourceDuplication(resourceType, userDB.getUser_seq(), userDB.getSeq(), textName.getText())) {
				return "The name is duplication.";
			}
		} catch (Exception e) {
			logger.error("SQL Editor File validator", e);
		}
				
		return null;
	}
	
	/**
	 * return ResourceDAO
	 * 
	 * @return
	 */
	public UserDBResourceDAO getRetResourceDao() {
		return retResourceDao;
	}
}
