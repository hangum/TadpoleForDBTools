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

/**
 * sql result to text export
 * 
 * @author hangum
 *
 */
public class ExportTextComposite extends AExportComposite {
	private static final Logger logger = Logger.getLogger(ExportTextComposite.class);

	private Button btnIncludeHeader;
	private Text textFileName;
	private Combo comboEncoding;
	
	private Button btnTab;
	private Button btnComma;
	private Button btnEtc;
	private Text textSeparatorEtc;	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExportTextComposite(Composite tabFolderObject, int style) {
		super(tabFolderObject, style);

		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText("Text");
		tbtmTable.setData("Text");

		Composite compositeText = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeText);
		GridLayout gl_compositeTables = new GridLayout(2, false);
		compositeText.setLayout(gl_compositeTables);
		compositeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblIncludeHead = new Label(compositeText, SWT.NONE);
		lblIncludeHead.setText("Include Head");
		
		btnIncludeHeader = new Button(compositeText, SWT.CHECK);
		
		Label lblFileName = new Label(compositeText, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText("File name");
		
		textFileName = new Text(compositeText, SWT.BORDER);
		textFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSeparator = new Label(compositeText, SWT.NONE);
		lblSeparator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator.setText("Separator");
		
		Composite compositeSeparator = new Composite(compositeText, SWT.NONE);
		compositeSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSeparator.setLayout(new GridLayout(4, false));
		
		btnTab = new Button(compositeSeparator, SWT.RADIO);
		btnTab.setText("Tab");
		
		btnComma = new Button(compositeSeparator, SWT.RADIO);
		btnComma.setText("Comma");
		btnComma.setSelection(true);
		
		btnEtc = new Button(compositeSeparator, SWT.RADIO);
		btnEtc.setText("etc");
		
		textSeparatorEtc = new Text(compositeSeparator, SWT.BORDER);
		textSeparatorEtc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSeparatorEtc.setEnabled(false);
		
		Label lblEncoding = new Label(compositeText, SWT.NONE);
		lblEncoding.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEncoding.setText("encoding");
		
		comboEncoding = new Combo(compositeText, SWT.NONE);
		comboEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboEncoding.setText("UTF-8");
	}

	@Override
	public ExportTextDAO getLastData() {
		ExportTextDAO dao = new ExportTextDAO();
		dao.setIsncludeHeader(btnIncludeHeader.getSelection());
		
		
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
