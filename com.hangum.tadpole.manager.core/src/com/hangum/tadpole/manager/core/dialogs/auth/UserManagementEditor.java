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
package com.hangum.tadpole.manager.core.dialogs.auth;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * 사용자 관리 에디터
 * 
 * @author hangum
 *
 */
public class UserManagementEditor extends EditorPart {
	public static final String ID = "com.hangum.tadpole.manager.core.editor.usermanagement";

	public UserManagementEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		UserManagementEditorInput qei = (UserManagementEditorInput)input;
		setPartName(qei.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 3;
		gl_parent.horizontalSpacing = 3;
		gl_parent.marginHeight = 3;
		gl_parent.marginWidth = 3;
		parent.setLayout(gl_parent);
		
		Composite compositeMain = new Composite(parent, SWT.NONE);
		compositeMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginHeight = 3;
		gl_composite.verticalSpacing = 3;
		gl_composite.horizontalSpacing = 3;
		gl_composite.marginWidth = 3;
		compositeMain.setLayout(gl_composite);

		TabFolder tabFolder = new TabFolder(compositeMain, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmItem = null;
		if(Define.USER_TYPE.MANAGER.toString().equals( SessionManager.getLoginType() )) {
			tbtmItem = new TabItem(tabFolder, SWT.NONE);
			tbtmItem.setText("Manager");
		} else {
			tbtmItem = new TabItem(tabFolder, SWT.NONE);
			tbtmItem.setText("Admin");			
		}

		Composite composite = new AdminComposite(tabFolder, SWT.NONE);
		tbtmItem.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
	}

	@Override
	public void setFocus() {
		
	}

}
