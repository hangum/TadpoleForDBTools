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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * table information editor
 * 
 * 
 * @author hangum
 *
 */
public class TableInformationEditor extends EditorPart {
	
	public static final String ID = "com.hangum.tadpole.rdb.core.editors.table.edit";
	
	private TableDAO tableDao;
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
		setPartName(Messages.get().TableInformationEditor_1 + " [" + qei.getName() + "]");
		
		tableDao = qei.getTableDAO();
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
		
		GridLayout gl_compositeTableData = new GridLayout(1, false);
		gl_compositeTableData.verticalSpacing = 1;
		gl_compositeTableData.horizontalSpacing = 1;
		gl_compositeTableData.marginHeight = 1;
		gl_compositeTableData.marginWidth = 1;
		
		compositeTableData = new TableDirectEditorComposite(parent, SWT.NONE, userDB, tableDao, columnList, primaryKEYListString);
		compositeTableData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTableData.setLayout(gl_compositeTableData);
	}

	@Override
	public void setFocus() {
	}

}
