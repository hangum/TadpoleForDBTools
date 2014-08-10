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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * table information editor
 * 
 * 
 * @author hangum
 *
 */
public class TableInformationEditor extends EditorPart {
	
	public static final String ID = "com.hangum.tadpole.rdb.core.editors.table.edit";
	
	private String initTableNameStr;
	private UserDBDAO userDB;
	private List<TableColumnDAO> columnList;
	/** pk key의 이름을 가지고 있습니다 */
	private Map<String, Boolean> primaryKEYListString = new HashMap<String, Boolean>();
	
	///////////
	private TableDirectEditorComposite compositeTableData;
//	private DDLSourceComposite compositeDdl;

	public TableInformationEditor() {
		super();
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
		
		DBTableEditorInput qei = (DBTableEditorInput)input;
		userDB = qei.getUserDB();
		setPartName("[" + qei.getName() + "] Table Editor");		 //$NON-NLS-1$
		
		initTableNameStr = qei.getName();
		columnList = qei.getShowTableColumns();
		for (TableColumnDAO columnDAO : columnList) {
			if(PublicTadpoleDefine.isPK(columnDAO.getKey())) {
				primaryKEYListString.put(columnDAO.getField(), true);
			} else {
				primaryKEYListString.put(columnDAO.getField(), false);
			}
		}
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
		gl_parent.marginHeight = 1;
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		//======================================================================
		GridLayout gl_compositeTableData = new GridLayout(1, false);
		gl_compositeTableData.verticalSpacing = 1;
		gl_compositeTableData.horizontalSpacing = 1;
		gl_compositeTableData.marginHeight = 1;
		gl_compositeTableData.marginWidth = 1;
		//======================================================================

		//[table data composite start]///////////////////////////////		
		CTabItem tbtmTableData = new CTabItem(tabFolder, SWT.NONE);
		tbtmTableData.setText("Table Data");
		
		compositeTableData = new TableDirectEditorComposite(tabFolder, SWT.NONE, userDB, initTableNameStr, columnList, primaryKEYListString);
		tbtmTableData.setControl(compositeTableData);
		compositeTableData.setLayout(gl_compositeTableData);
		//[table data composite end]/////////////////////////////////
		
//		//[DDL composite start]/////////////////////////////////
//		CTabItem tbtmSource = new CTabItem(tabFolder, SWT.NONE);
//		tbtmSource.setText("DDL");
//		
//		compositeDdl = new DDLSourceComposite(tabFolder, SWT.NONE);
//		compositeDdl.setDdlSource("Select * from test");
//		tbtmSource.setControl(compositeDdl);
//		//[DDL composite end]/////////////////////////////////
		
		tabFolder.setSelection(0);
		
	}

	@Override
	public void setFocus() {
	}

}
