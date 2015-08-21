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
package com.hangum.tadpole.rdb.core.editors.externalbrowser;

import java.util.List;

import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * External Browser editor
 * 
 * @author hangum
 *
 */
public class ExternalBrowserEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(ExternalBrowserEditor.class);
	public static String ID = "com.hangum.tadpole.rdb.core.editor.externalBrowser";
	
	// define input values
	private UserDBDAO userDB;
	private List<ExternalBrowserInfoDAO> listExternalBrowser;
	
	private CTabFolder tfMain;

	public ExternalBrowserEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		tfMain = new CTabFolder(parent, SWT.NONE);
		tfMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tfMain.setBorderVisible(false);
		tfMain.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
	
		for(ExternalBrowserInfoDAO dao : listExternalBrowser) {
			if(dao.getIs_used().equals(PublicTadpoleDefine.YES_NO.YES.name())) {
				createExtBrowser(dao);
			}
		}
		
		tfMain.setSelection(0);
	}
	
	/**
	 * set external browser
	 * @param dao
	 */
	private void createExtBrowser(ExternalBrowserInfoDAO dao) {
		CTabItem tbtmNewtab = new CTabItem(tfMain, SWT.NONE);
		tbtmNewtab.setText(dao.getName());
		tbtmNewtab.setToolTipText(dao.getComment());
		
		Composite compositeHead = new ExtBrowserWidget(tfMain, SWT.NONE, dao.getUrl());
		tbtmNewtab.setControl(compositeHead);
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
		
		ExternalBrowserInput ebi = (ExternalBrowserInput)input;
		setPartName(ebi.getName());
		userDB = ebi.getUserDB();
		listExternalBrowser = ebi.getListExternalBrowser();
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
	public void setFocus() {
	}
}
