/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.initialize.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hangum.tadpole.application.Messages;

/**
 * System Admin wizard user type
 * 
 * @author hangum
 *
 */
public class SystemAdminWizardUseType extends WizardPage {
	private String[] USER_GROUP = {Messages.SystemAdminWizardUseType_3, Messages.SystemAdminWizardUseType_4};//"개인", "그룹"};
	private Combo comboUserGroup;
	
	/**
	 * Create the wizard.
	 */
	public SystemAdminWizardUseType() {
		super(Messages.SystemAdminWizardUseType_1);
		setTitle(Messages.SystemAdminWizardUseType_1);
		setDescription(Messages.SystemAdminWizardUseType_2);//"Select User Group");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.SystemAdminWizardUseType_5);
		
		comboUserGroup = new Combo(container, SWT.READ_ONLY);
		comboUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i=0; i<USER_GROUP.length; i++) {
			comboUserGroup.add(USER_GROUP[i], i);
		}
		comboUserGroup.select(0);
	}
}
