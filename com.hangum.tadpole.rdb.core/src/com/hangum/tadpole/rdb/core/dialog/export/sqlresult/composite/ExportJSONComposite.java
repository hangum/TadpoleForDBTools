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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportJsonDAO;

/**
 * sql result to text export
 * 
 * @author hangum
 */
public class ExportJSONComposite extends AbstractExportComposite {
	private static final Logger logger = Logger.getLogger(ExportJSONComposite.class);
	private Text textSchemeKey;
	private Text textRecordKey;
	private Button btnIncludeHeader;
	private Button btnFormat;
	private Button btnCompact;
    private Composite compositeScheme;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExportJSONComposite(Composite tabFolderObject, int style, String defaultTargetName) {
		super(tabFolderObject, style);

		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText("JSON");
		tbtmTable.setData("JSON");//$NON-NLS-1$

		Composite compositeText = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeText);
		GridLayout gl_compositeTables = new GridLayout(3, false);
		compositeText.setLayout(gl_compositeTables);
		compositeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblIncludeHead = new Label(compositeText, SWT.NONE);
		lblIncludeHead.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIncludeHead.setText(Messages.get().IncludeScheme);
		
		btnIncludeHeader = new Button(compositeText, SWT.CHECK);
		btnIncludeHeader.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeScheme.setEnabled(btnIncludeHeader.getSelection());
			}
		});
		btnIncludeHeader.setSelection(true);
		new Label(compositeText, SWT.NONE);
		
		Label lblJsonKey = new Label(compositeText, SWT.NONE);
		lblJsonKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJsonKey.setText("JSON Key");
		
		compositeScheme = new Composite(compositeText, SWT.NONE);
		compositeScheme.setLayout(new GridLayout(4, false));
		
		Label lblSchemeKey = new Label(compositeScheme, SWT.NONE);
		lblSchemeKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSchemeKey.setText(Messages.get().Schemas);
		
		textSchemeKey = new Text(compositeScheme, SWT.BORDER);
		textSchemeKey.setText("scheme");
		GridData gd_textScheme = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textScheme.minimumWidth = 100;
		textSchemeKey.setLayoutData(gd_textScheme);
		
		Label lblNewLabel_1 = new Label(compositeScheme, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(Messages.get().Record);
		
		textRecordKey = new Text(compositeScheme, SWT.BORDER);
		textRecordKey.setText("record");
		GridData gd_textRecordKey = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textRecordKey.minimumWidth = 100;
		textRecordKey.setLayoutData(gd_textRecordKey);
		new Label(compositeText, SWT.NONE);
		
		Label lblFormat = new Label(compositeText, SWT.NONE);
		lblFormat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFormat.setText(Messages.get().format);
		
		Composite composite = new Composite(compositeText, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		btnFormat = new Button(composite, SWT.RADIO);
		btnFormat.setSelection(true);
		btnFormat.setText(Messages.get().format);
		
		btnCompact = new Button(composite, SWT.RADIO);
		btnCompact.setText(Messages.get().Compact);
		new Label(compositeText, SWT.NONE);
		
		Label lblFileName = new Label(compositeText, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText(Messages.get().FileName);
		
		textTargetName = new Text(compositeText, SWT.BORDER);
		new Label(compositeText, SWT.NONE);
		
		Label lblEncoding = new Label(compositeText, SWT.NONE);
		lblEncoding.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEncoding.setText(Messages.get().encoding);
		
		comboEncoding = new Combo(compositeText, SWT.NONE);
		
		textTargetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTargetName.setText(defaultTargetName);
		comboEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboEncoding.setText("UTF-8");
	}

	@Override
	public ExportJsonDAO getLastData() {
		ExportJsonDAO dao = new ExportJsonDAO();
		
		dao.setIsncludeHeader(this.btnIncludeHeader.getSelection());
		dao.setComboEncoding(this.comboEncoding.getText());
		
		if (this.btnIncludeHeader.getSelection()){
			dao.setSchemeKey(this.textSchemeKey.getText());
			dao.setRecordKey(this.textRecordKey.getText());
		}
		
		dao.setTargetName(this.textTargetName.getText());
		dao.setFormat(this.btnFormat.getSelection());
		
		return dao;
	}

	@Override
	public boolean isValidate() {
		if(super.isValidate()) {
			
			if (this.btnIncludeHeader.getSelection()){
				if(StringUtils.isEmpty(this.textSchemeKey.getText()) ){
					MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ExportJSONCompositeSchemaMSG);
					this.textSchemeKey.setFocus();
					return false;
				}
				if(StringUtils.isEmpty(this.textRecordKey.getText()) ){
					MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ExportJSONCompositeRecordMSG);
					this.textRecordKey.setFocus();
					return false;
				}
			}		
			
		}else{
			return false;
		}
		return true;
	}
	
}
