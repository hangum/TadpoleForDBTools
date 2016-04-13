/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportTextDAO;
import org.eclipse.swt.widgets.Group;

/**
 * sql result to text export
 * 
 * @author hangum
 *
 */
public class ExportSQLComposite extends AExportComposite {
	private static final Logger logger = Logger.getLogger(ExportSQLComposite.class);
	private Combo comboEncoding;
	
	private Button btnInsert;
	private Button btnUpdate;
	private Button btnMerge;
	private Composite compositeTargetTable;
	private Text textTargetTable;
	private Label lblExSchematable;
	private Composite compositeCommit;
	private Text textCommit;
	private Label label;
	private Button btnBatchInsert;
	private Group grpWhere;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExportSQLComposite(Composite tabFolderObject, int style) {
		super(tabFolderObject, style);

		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText("SQL");
		tbtmTable.setData("SQL");

		Composite compositeText = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeText);
		GridLayout gl_compositeTables = new GridLayout(2, false);
		compositeText.setLayout(gl_compositeTables);
		compositeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblIncludeHead = new Label(compositeText, SWT.NONE);
		lblIncludeHead.setText("대상테이블");
		
		compositeTargetTable = new Composite(compositeText, SWT.NONE);
		compositeTargetTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeTargetTable.setLayout(new GridLayout(2, false));
		
		textTargetTable = new Text(compositeTargetTable, SWT.BORDER);
		textTargetTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblExSchematable = new Label(compositeTargetTable, SWT.NONE);
		lblExSchematable.setText("ex) schema.table");
		
		Label lblCommit = new Label(compositeText, SWT.NONE);
		lblCommit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCommit.setText("커밋");
		
		compositeCommit = new Composite(compositeText, SWT.NONE);
		compositeCommit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeCommit.setLayout(new GridLayout(2, false));
		
		textCommit = new Text(compositeCommit, SWT.BORDER | SWT.RIGHT);
		textCommit.setText("0");
		textCommit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		label = new Label(compositeCommit, SWT.NONE);
		label.setText("건 마다");
		
		Label lblSeparator = new Label(compositeText, SWT.NONE);
		lblSeparator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator.setText("DML Type");
		
		Composite compositeSeparator = new Composite(compositeText, SWT.NONE);
		compositeSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSeparator.setLayout(new GridLayout(4, false));
		
		btnBatchInsert = new Button(compositeSeparator, SWT.RADIO);
		btnBatchInsert.setText("Batch Insert");
		
		btnInsert = new Button(compositeSeparator, SWT.RADIO);
		btnInsert.setText("INSERT");
		btnInsert.setSelection(true);
		
		btnUpdate = new Button(compositeSeparator, SWT.RADIO);
		btnUpdate.setText("Update");
		
		btnMerge = new Button(compositeSeparator, SWT.RADIO);
		btnMerge.setText("Merge");
		
		Label lblEncoding = new Label(compositeText, SWT.NONE);
		lblEncoding.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEncoding.setText("encoding");
		
		comboEncoding = new Combo(compositeText, SWT.NONE);
		comboEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboEncoding.setText("UTF-8");
		new Label(compositeText, SWT.NONE);
		
		grpWhere = new Group(compositeText, SWT.NONE);
		grpWhere.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpWhere.setText("Where");
	}

	@Override
	public ExportTextDAO getLastData() {
		ExportTextDAO dao = new ExportTextDAO();
		
		
		return dao;
	}

	@Override
	public boolean isValidate() {
		if(super.isValidate()) {
		
			MessageDialog.openWarning(getShell(), Messages.get().Warning, "파일이름이 공백입니다.");
		}
		return false;
	}
	
}
