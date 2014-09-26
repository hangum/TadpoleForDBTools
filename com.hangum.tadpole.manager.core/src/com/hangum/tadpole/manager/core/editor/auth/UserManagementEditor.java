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
package com.hangum.tadpole.manager.core.editor.auth;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;

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
		gl_composite.marginHeight = 0;
		gl_composite.verticalSpacing = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginWidth = 0;
		compositeMain.setLayout(gl_composite);

		CTabFolder tabFolder = new CTabFolder(compositeMain, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());

		// Users
		CTabItem tbtmItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmItem.setText("User List");

		Composite compositeUserList = new UserListComposite(tabFolder, SWT.NONE);
		tbtmItem.setControl(compositeUserList);
		compositeUserList.setLayout(new GridLayout(1, false));
		
		// DBs
		CTabItem tbtmDB = new CTabItem(tabFolder, SWT.NONE);
		tbtmDB.setText("DB List");
		
		Composite compositeDBList = new DBListComposite(tabFolder, SWT.NONE);
		tbtmDB.setControl(compositeDBList);
		compositeDBList.setLayout(new GridLayout(1, false));
		
		tabFolder.setSelection(0);
		
		// google analytic
		AnalyticCaller.track(UserManagementEditor.ID);
	}

	@Override
	public void setFocus() {
	}

}
