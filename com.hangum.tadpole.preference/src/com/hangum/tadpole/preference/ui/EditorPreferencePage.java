/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.ui;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * ACE editor preference page
 * 
 * @author hangum
 *
 */
public class EditorPreferencePage extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(EditorPreferencePage.class);
	
	private Button btnAutoSave;
	
	private Combo comboFontSize;
	private Button btnShowGutter;
	private Button btnIsWrap;
	private Text textWrapLimit;
	private Combo comboTheme;
	private Button btnMybatisSupport;

	/**
	 * Create the preference page.
	 */
	public EditorPreferencePage() {
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblTheme = new Label(container, SWT.NONE);
		lblTheme.setText(Messages.get().EditorPreferencePage_lblTheme_text);
		
		comboTheme = new Combo(container, SWT.READ_ONLY);
		comboTheme.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFontSize = new Label(container, SWT.NONE);
		lblFontSize.setText(Messages.get().EditorPreferencePage_0);
		
		comboFontSize = new Combo(container, SWT.READ_ONLY);
		comboFontSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboFontSize.add("9"); //$NON-NLS-1$
		comboFontSize.add("10"); //$NON-NLS-1$
		comboFontSize.add("11"); //$NON-NLS-1$
		comboFontSize.add("12"); //$NON-NLS-1$
		comboFontSize.add("13"); //$NON-NLS-1$
		comboFontSize.add("14"); //$NON-NLS-1$
		comboFontSize.add("15"); //$NON-NLS-1$
		comboFontSize.add("20"); //$NON-NLS-1$
		comboFontSize.add("30"); //$NON-NLS-1$
		comboFontSize.select(3);
		
		comboFontSize.setVisibleItemCount(9);
		
		btnIsWrap = new Button(container, SWT.CHECK);
		btnIsWrap.setText(Messages.get().EditorPreferencePage_1);
		
		textWrapLimit = new Text(container, SWT.BORDER);
		textWrapLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnShowGutter = new Button(container, SWT.CHECK);
		btnShowGutter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnShowGutter.setText(Messages.get().EditorPreferencePage_2);
		
		btnMybatisSupport = new Button(container, SWT.CHECK);
		btnMybatisSupport.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnMybatisSupport.setText(Messages.get().isSupportMyBatisDollos);
		
		btnAutoSave = new Button(container, SWT.CHECK);
		btnAutoSave.setText(Messages.get().EditorPreferencePage_3);
		new Label(container, SWT.NONE);
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtAutoSave	= ""+btnAutoSave.getSelection();
		String txtTheme 	= comboTheme.getText();
		String txtFontSize 	= comboFontSize.getText();
		String txtIsWrap 	= ""+btnIsWrap.getSelection(); //$NON-NLS-1$
		String txtWrapLimit = textWrapLimit.getText();
		String txtIsGutter 	= ""+btnShowGutter.getSelection(); //$NON-NLS-1$
		String txtMyBatisDollar = ""+btnMybatisSupport.getSelection();
	
		if(!NumberUtils.isNumber(txtWrapLimit)) {
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().SQLFormatterPreferencePage_8);
			btnIsWrap.setFocus();
			return false;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_AUTOSAVE, txtAutoSave);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_THEME, txtTheme);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_FONT_SIZE, txtFontSize);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_IS_WARP, txtIsWrap);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_WRAP_LIMIT, txtWrapLimit);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_SHOW_GUTTER, txtIsGutter);
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.EDITOR_MYBatisDollart, txtMyBatisDollar);
			
			PlatformUI.getPreferenceStore().setValue(PreferenceDefine.EDITOR_CHANGE_EVENT, "EDITOR_CHANGE_EVENT" + System.currentTimeMillis());
	
		} catch(Exception e) {
			logger.error("Editor preference saveing", e); //$NON-NLS-1$
			
			MessageDialog.openError(getShell(), Messages.get().Confirm, Messages.get().RDBPreferencePage_5 + e.getMessage());
			return false;
		}
		
		return super.performOk();
	}

	/**
	 * initialize default value
	 */
	private void initDefaultValue() {
		btnAutoSave.setSelection(GetPreferenceGeneral.getEditorAutoSave());
		
		// initiaize themes
		for(String strTheme : PublicTadpoleDefine.getMapTheme().keySet()) {
			comboTheme.add(strTheme);	
		}
		comboTheme.setVisibleItemCount(PublicTadpoleDefine.getMapTheme().size());
		
		comboTheme.setText(GetPreferenceGeneral.getEditorTheme());
		comboFontSize.setText(GetPreferenceGeneral.getEditorFontSize());

		btnIsWrap.setSelection(GetPreferenceGeneral.getEditorIsWarp());
		textWrapLimit.setText(GetPreferenceGeneral.getEditorWarpLimitValue());
		btnShowGutter.setSelection(GetPreferenceGeneral.getEditorShowGutter());
		btnMybatisSupport.setSelection(GetPreferenceGeneral.getIsMyBatisDollor());
	}

	@Override
	public boolean performCancel() {
		initDefaultValue();
		
		return super.performCancel();
	}
	
	@Override
	protected void performApply() {

		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		initDefaultValue();

		super.performDefaults();
	}
	
}
