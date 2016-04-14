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
public class ExportHTMLComposite extends AExportComposite {
	private static final Logger logger = Logger.getLogger(ExportHTMLComposite.class);

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExportHTMLComposite(Composite tabFolderObject, int style, String defaultTargetName) {
		super(tabFolderObject, style);

		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText("HTML");
		tbtmTable.setData("HTML");//$NON-NLS-1$

		Composite compositeText = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeText);
		GridLayout gl_compositeTables = new GridLayout(2, false);
		compositeText.setLayout(gl_compositeTables);
		compositeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblFileName = new Label(compositeText, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText("File name");
		
		textTargetName = new Text(compositeText, SWT.BORDER);
		textTargetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTargetName.setText(defaultTargetName);
		
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

		dao.setComboEncoding(this.comboEncoding.getText());
		dao.setTargetName(this.textTargetName.getText());
		
		return dao;
	}

	@Override
	public boolean isValidate() {
		if(super.isValidate()) {
			return true;
		}
		return false;
	}
	
}
