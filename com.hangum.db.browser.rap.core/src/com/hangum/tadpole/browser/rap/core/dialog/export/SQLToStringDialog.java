/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.browser.rap.core.dialog.export;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.browser.rap.core.Messages;
import com.hangum.tadpole.browser.rap.core.dialog.export.application.SQLToJavaConvert;
import com.hangum.tadpole.define.Define;

/**
 * sql to application string 
 * 
 * @author hangum
 *
 */
public class SQLToStringDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SQLToStringDialog.class);
	
	private Combo comboLanguageType;
	private String languageType = ""; //$NON-NLS-1$
	private String sql = ""; //$NON-NLS-1$
	
	Text textConvert;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param languageType 디폴트 변화 언어
	 * @param sql sql
	 */
	public SQLToStringDialog(Shell parentShell, String languageType, String sql) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		this.languageType = languageType;
		this.sql = sql;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.SQLToStringDialog_2);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTitle = new Composite(compositeBody, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(3, false));
		
		Label lblType = new Label(compositeTitle, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText(Messages.SQLToStringDialog_3);
		
		comboLanguageType = new Combo(compositeTitle, SWT.READ_ONLY);
		comboLanguageType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlToStr();
			}
		});
		comboLanguageType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(Define.SQL_TO_APPLICATION app : Define.SQL_TO_APPLICATION.values()) {
			comboLanguageType.add(app.toString());
			comboLanguageType.setData(app.toString(), app);
		}
		comboLanguageType.setText(this.languageType);
		
		Button btnOriginalText = new Button(compositeTitle, SWT.NONE);
		btnOriginalText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textConvert.setText(sql);
			}
		});
		btnOriginalText.setText(Messages.SQLToStringDialog_4);
		
		textConvert = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		textConvert.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				sql = textConvert.getText();
			}
		});
		textConvert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textConvert.setText(sql);
		
		sqlToStr();

		return container;
	}
	
	private void sqlToStr() {
		StringBuffer sbStr = new StringBuffer();
		String[] sqls = parseSQL();
		
		SQLToLanguageConvert slt = new SQLToLanguageConvert( (Define.SQL_TO_APPLICATION)comboLanguageType.getData(comboLanguageType.getText()) );
		for(int i=0; i < sqls.length; i++) {
			if("".equals(StringUtils.trimToEmpty(sqls[i]))) continue;
			
			if(i ==0) sbStr.append( slt.sqlToString(SQLToJavaConvert.name, sqls[i]) );
			else sbStr.append( slt.sqlToString(SQLToJavaConvert.name + i, sqls[i]) );
		}
		
		textConvert.setText(sbStr.toString());
	}
	
	private String[] parseSQL() {
		String[] arry = sql.split(Define.SQL_DILIMITER); //$NON-NLS-1$
		 if( arry.length == 1) {
			 String ars[] = { sql };
			 return ars;
		 }
		 return arry;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(562, 388);
	}

}
