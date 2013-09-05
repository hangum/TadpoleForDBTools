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
package com.hangum.tadpole.rdb.core.editors.dbinfos;

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

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnsComposite;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.RDBInformationComposite;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TablesComposite;
import com.hangum.tadpole.util.TadpoleWidgetUtils;

/**
 * RDB DB Information editor
 * 
 * @author hangum
 *
 */
public class RDBDBInfosEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBDBInfosEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.rdb.dbinfos";
	
	private UserDBDAO userDB;
	private RDBInformationComposite compositeRDBInformation;
	private TablesComposite tableInformationComposite;
	private ColumnsComposite columnInformationComposite;

	public RDBDBInfosEditor() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		RDBDBInfoEditorInput moInput = (RDBDBInfoEditorInput)input;
		this.userDB = moInput.getUserDB();		
		setPartName(moInput.getName());
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginHeight = 2;
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		GridLayout gl_compositeRDBInformation = new GridLayout(1, false);
		gl_compositeRDBInformation.verticalSpacing = 2;
		gl_compositeRDBInformation.horizontalSpacing = 2;
		gl_compositeRDBInformation.marginHeight = 2;
		gl_compositeRDBInformation.marginWidth = 2;
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		//[information composite start]///////////////////////////////		
		CTabItem tbtmServerStatus = new CTabItem(tabFolder, SWT.NONE);
		tbtmServerStatus.setText("DB Information");
		
		compositeRDBInformation = new RDBInformationComposite(tabFolder, SWT.NONE, userDB);
		tbtmServerStatus.setControl(compositeRDBInformation);
		compositeRDBInformation.setLayout(gl_compositeRDBInformation);
		//[information composite end]/////////////////////////////////
		
		//[table information start]
		CTabItem tbtmCollectionSummary = new CTabItem(tabFolder, SWT.NONE);
		tbtmCollectionSummary.setText("Table Summary");
		
		tableInformationComposite = new TablesComposite(tabFolder, SWT.NONE, userDB);
		tbtmCollectionSummary.setControl(tableInformationComposite);
		
		GridLayout gl_compositeTableInformation = new GridLayout(1, false);
		gl_compositeTableInformation.verticalSpacing = 2;
		gl_compositeTableInformation.horizontalSpacing = 2;
		gl_compositeTableInformation.marginHeight = 2;
		gl_compositeTableInformation.marginWidth = 2;
		tableInformationComposite.setLayout(gl_compositeTableInformation);
		//[table information end]
		
		CTabItem tbtmColumnSummary = new CTabItem(tabFolder, SWT.NONE);
		tbtmColumnSummary.setText("Column Summary");
		columnInformationComposite = new ColumnsComposite(tabFolder, SWT.NONE, userDB);
		tbtmCollectionSummary.setControl(columnInformationComposite);
		
		GridLayout gl_compositeColumnInformation = new GridLayout(1, false);
		gl_compositeColumnInformation.verticalSpacing = 2;
		gl_compositeColumnInformation.horizontalSpacing = 2;
		gl_compositeColumnInformation.marginHeight = 2;
		gl_compositeColumnInformation.marginWidth = 2;
		columnInformationComposite.setLayout(gl_compositeColumnInformation);

		tabFolder.setSelection(0);
		
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
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}
	
	@Override
	public void setFocus() {
	}

}
