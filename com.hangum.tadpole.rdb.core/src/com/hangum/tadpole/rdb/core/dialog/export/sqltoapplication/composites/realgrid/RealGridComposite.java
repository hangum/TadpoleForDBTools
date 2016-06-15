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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.realgrid;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.SQLToLanguageConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.AbstractSQLToComposite;

/**
 * real grid
 * 
 * @author hangum
 *
 */
public class RealGridComposite extends AbstractSQLToComposite {
	private static final Logger logger = Logger.getLogger(RealGridComposite.class);
	
	private Text textVariable;
	private CTabFolder tabFolderPreview;
	private Text textConvert;
	private Browser browserPreview;
	private SQLToLanguageConvert slt;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RealGridComposite(Composite tabFolderObject, UserDBDAO userDB, String strTitle, final String sql, EditorDefine.SQL_TO_APPLICATION type) {
		super(tabFolderObject, SWT.NONE, userDB, sql, type);
		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText(strTitle);
		tbtmTable.setData(type);
		
		Composite compositeBody = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeBody);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTitle = new Composite(compositeBody, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(4, false));
		
		Label lblVariable = new Label(compositeTitle, SWT.NONE);
		lblVariable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVariable.setText(Messages.get().Variable);
		
		slt = new SQLToLanguageConvert(userDB, type);
		textVariable = new Text(compositeTitle, SWT.BORDER);
		textVariable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textVariable.setText(slt.getDefaultVariable());
		
		Button btnConvertSQL = new Button(compositeTitle, SWT.NONE);
		btnConvertSQL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlToStr();
			}
		});
		btnConvertSQL.setText(String.format(Messages.get().SQLToStringDialog_btnNewButton_text, type));
		
		Button btnOriginalText = new Button(compositeTitle, SWT.NONE);
		btnOriginalText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textConvert.setText(sql);
			}
		});
		btnOriginalText.setText(Messages.get().SQLToStringDialog_4);
		
		SashForm sashForm = new SashForm(compositeBody, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolderPreview = new CTabFolder(sashForm, SWT.BORDER | SWT.BOTTOM);
		tabFolderPreview.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolderPreview.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		CTabItem tabItemConvert = new CTabItem(tabFolderPreview, SWT.NONE);
		tabItemConvert.setText("HTML");
		
		textConvert = new Text(tabFolderPreview, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		tabItemConvert.setControl(textConvert);
		
//		CTabItem tabItemHTML = new CTabItem(tabFolderPreview, SWT.NONE);
//		tabItemHTML.setText("Browser");
//		
//		browserPreview = new Browser(tabFolderPreview, SWT.NONE);
//		tabItemHTML.setControl(browserPreview);
		tabFolderPreview.setSelection(0);
		
		sashForm.setWeights(new int[] {1});
		sqlToStr();
	}

	/**
	 * sql to str
	 */
	private void sqlToStr() {
		StringBuffer sbStr = new StringBuffer();
		String[] sqls = parseSQL();
		
		if(StringUtils.isEmpty(textVariable.getText())){ 
			textVariable.setText(slt.getDefaultVariable());
		}
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("name", textVariable.getText());
		
		for(int i=0; i < sqls.length; i++) {
			if("".equals(StringUtils.trimToEmpty(sqls[i]))) continue; //$NON-NLS-1$
			
			if(i ==0) {
				sbStr.append( slt.sqlToString(sqls[i], options, null) );
			}else{
				options.put("name", textVariable.getText() + "_" + i);
				sbStr.append( slt.sqlToString(sqls[i], options, null) );
			}
			
			// 쿼리가 여러개일 경우 하나씩 한개를 준다.
			sbStr.append("\r\n"); //$NON-NLS-1$
		}
		
		textConvert.setText(sbStr.toString());
	}

}