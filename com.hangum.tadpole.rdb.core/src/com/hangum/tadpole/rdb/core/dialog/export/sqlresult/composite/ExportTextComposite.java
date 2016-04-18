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
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportTextDAO;

/**
 * sql result to text export
 * 
 * @author hangum
 *
 */
public class ExportTextComposite extends AbstractExportComposite {
	private static final Logger logger = Logger.getLogger(ExportTextComposite.class);

	private Button btnIncludeHeader;
	
	private Button btnTab;
	private Button btnComma;
	private Button btnEtc;
	private Text textSeparatorEtc;	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExportTextComposite(Composite tabFolderObject, int style, String defaultTargetName) {
		super(tabFolderObject, style);

		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText("Text");
		tbtmTable.setData("TEXT");//$NON-NLS-1$

		Composite compositeText = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeText);
		GridLayout gl_compositeTables = new GridLayout(2, false);
		compositeText.setLayout(gl_compositeTables);
		compositeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblIncludeHead = new Label(compositeText, SWT.NONE);
		lblIncludeHead.setText(Messages.get().IncludeHead);
		
		btnIncludeHeader = new Button(compositeText, SWT.CHECK);
		btnIncludeHeader.setSelection(true);
		
		Label lblFileName = new Label(compositeText, SWT.NONE);
		lblFileName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileName.setText(Messages.get().FileName);
		
		textTargetName = new Text(compositeText, SWT.BORDER);
		textTargetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTargetName.setText(defaultTargetName);
		
		Label lblSeparator = new Label(compositeText, SWT.NONE);
		lblSeparator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSeparator.setText(Messages.get().Separator);
		
		Composite compositeSeparator = new Composite(compositeText, SWT.NONE);
		compositeSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSeparator.setLayout(new GridLayout(4, false));
		
		btnTab = new Button(compositeSeparator, SWT.RADIO);
		btnTab.setText(Messages.get().Tab);
		
		btnComma = new Button(compositeSeparator, SWT.RADIO);
		btnComma.setText(Messages.get().Comma);
		btnComma.setSelection(true);
		
		btnEtc = new Button(compositeSeparator, SWT.RADIO);
		btnEtc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textSeparatorEtc.setEnabled(true);
				if (StringUtils.isEmpty(textSeparatorEtc.getText())) {
					textSeparatorEtc.setText("|");
				}
			}
		});
		btnEtc.setText(Messages.get().etc);
		
		textSeparatorEtc = new Text(compositeSeparator, SWT.BORDER);
		textSeparatorEtc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSeparatorEtc.setEnabled(false);
		
		Label lblEncoding = new Label(compositeText, SWT.NONE);
		lblEncoding.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEncoding.setText(Messages.get().encoding);
		
		comboEncoding = new Combo(compositeText, SWT.NONE);
		comboEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboEncoding.setText("UTF-8");
	}

	@Override
	public ExportTextDAO getLastData() {
		ExportTextDAO dao = new ExportTextDAO();
		
		dao.setIsncludeHeader(btnIncludeHeader.getSelection());
		dao.setComboEncoding(this.comboEncoding.getText());
		
		if (this.btnComma.getSelection()) {
			dao.setSeparatorType(',');
		}else if (this.btnTab.getSelection()) {
			dao.setSeparatorType('\t');
		}else if (this.btnEtc.getSelection()) {
			dao.setSeparatorType( this.textSeparatorEtc.getText().charAt(0) );
		}else{
			dao.setSeparatorType(',');
		}
		
		dao.setTargetName(this.textTargetName.getText()); 
		
		return dao;
	}

	@Override
	public boolean isValidate() {
		if(super.isValidate()) {
			
		   if (btnEtc.getSelection()) {
			   if (StringUtils.isEmpty( textSeparatorEtc.getText() )){
				   MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ExportTextCompositeEmptySeparator);  
				   textSeparatorEtc.setFocus();
				   return false;
			   }else if (textSeparatorEtc.getText().length() != 1 ) {
				   MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ExportTextCompositeEmptySeparatorOne); 
				   textSeparatorEtc.setFocus();
				   return false;
			   }
		   } 
		}else{
			return false;
		}
		return true;
	}
	
}
