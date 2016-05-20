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

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hangum.tadpole.application.Activator;
import com.hangum.tadpole.application.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.swtdesigner.ResourceManager;

/**
 * System Admin wizard user type
 * 
 * @author hangum
 *
 */
public class SystemAdminWizardUseTypePage extends WizardPage {
	private static final Logger logger = Logger.getLogger(SystemAdminWizardUseTypePage.class);
	
	private String[] USER_GROUP = {Messages.get().SystemAdminWizardUseType_3, Messages.get().SystemAdminWizardUseType_4};
	private String[] USER_INFO = {Messages.get().SystemAdminWizardUseType_6, Messages.get().SystemAdminWizardUseType_7};
	private String[] USER_IMAGE= {"resources/icons/user.png", "resources/icons/user_group.png"}; //$NON-NLS-1$ //$NON-NLS-1$
	
	private Label labelInfo;
	private Combo comboUserGroup;
	private Label labelImage;
	
	/**
	 * Create the wizard.
	 */
	public SystemAdminWizardUseTypePage() {
		super(Messages.get().SystemAdminWizardUseType_1);
		setTitle(Messages.get().SystemAdminWizardUseType_1);
		setDescription(Messages.get().SystemAdminWizardUseTypePage_0);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		container.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.get().SystemAdminWizardUseType_5);
		
		comboUserGroup = new Combo(container, SWT.READ_ONLY);
		comboUserGroup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectComoboGroup();
			}
		});
		comboUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i=0; i<USER_GROUP.length; i++) {
			comboUserGroup.add(USER_GROUP[i], i);
		}
		comboUserGroup.select(1);
		
		setControl(container);
		
		labelImage = new Label(container, SWT.NONE);
		labelImage.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, USER_IMAGE[0]));
		
		new Label(container, SWT.NONE);
		
		labelInfo = new Label(container, SWT.WRAP);
		labelInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		labelInfo.setText(getUseType());
		new Label(container, SWT.NONE);
		
		
		labelImage.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, USER_IMAGE[comboUserGroup.getSelectionIndex()]));
		labelInfo.setText(USER_INFO[comboUserGroup.getSelectionIndex()]);
		
		// 
		setPageComplete(true);
		
		AnalyticCaller.track("SystemAdminWizardUseType"); //$NON-NLS-1$
	}
	
	/**
	 * selected combo group
	 */
	private void selectComoboGroup() {
		labelImage.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, USER_IMAGE[comboUserGroup.getSelectionIndex()]));
		labelInfo.setText(USER_INFO[comboUserGroup.getSelectionIndex()]);
		
		getWizard().getContainer().updateButtons();	
	}
	
	@Override
	public boolean canFlipToNextPage() {
		if(comboUserGroup.getSelectionIndex() == 1) return true;
		else return false;
	}

	/**
	 * use type
	 * 
	 * @return
	 */
	public String getUseType() {
		if(comboUserGroup.getSelectionIndex() == 0) {
			return PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name();
		} else {
			return PublicTadpoleDefine.SYSTEM_USE_GROUP.GROUP.name();
		}
	}
}
