/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * select perspective
 * 
 * @author hangum
 *
 */
public class PerspectivePreferencePage extends PreferencePage implements IWorkbenchPreferencePage{

	private static final Logger logger = Logger.getLogger(PerspectivePreferencePage.class);
	
	private final String ADMIN = "admin";
	private final String MANAGER = "manager";
	private final String DEFAULT = "default";
	
	private Button btnDefault;
	private Button btnManager;
	private Button btnAdmin;
	
	private String selection;
	public PerspectivePreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Group grpSelectPerspective = new Group(container, SWT.NONE);
		grpSelectPerspective.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSelectPerspective.setText("Select perspective");
		grpSelectPerspective.setLayout(new GridLayout(1, false));
		
		btnDefault = new Button(grpSelectPerspective, SWT.RADIO);
		btnDefault.setText("Developer(default)");
		
		btnManager = new Button(grpSelectPerspective, SWT.RADIO);
		btnManager.setText("Manager");
//		if(PublicTadpoleDefine.USER_TYPE.USER.toString().equals(SessionManager.getRepresentRole())) {
			btnManager.setEnabled(false);
//		}
		
		btnAdmin = new Button(grpSelectPerspective, SWT.RADIO);
		btnAdmin.setText("Administrator");
		if(SessionManager.isAdmin()) {
			btnAdmin.setEnabled(false);
		}
		
		setSelectedButton();
		
		return container;
	}
	
	private void setSelectedButton() {
		selection = SessionManager.getPerspective();
		if (selection.equals(ADMIN)) { 
			btnAdmin.setSelection(true);
		} else if (selection.equals(MANAGER)) { 
			btnManager.setSelection(true);
		} else { 
			btnDefault.setSelection(true);
		}
	}

	@Override
	public boolean performOk() {
		changePerspective();
		return super.performOk();
	}
	
	@Override
	protected void performApply() {
		changePerspective();
	}
	
	private void changePerspective() {
		String perspective;
		if (btnAdmin.getSelection()) {
			perspective = ADMIN;
		} else if (btnManager.getSelection()) {
			perspective = MANAGER;
		} else {
			perspective = DEFAULT;
		}
		if (!selection.equals(perspective)) {
			SessionManager.setPerspective(perspective);
			selection = perspective;
			logger.info("Change Perspective to " + selection);
		}
	}
}
