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
package com.hangum.tadpole.preference.ui;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.CookieUtils;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * general preference
 * 
 * @author hangum
 *
 */
public class GeneralPreferencePage extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(GeneralPreferencePage.class);
	
	private Label lblLanguage;
	private Combo comboLanguage;
	
	private Text textSessionTime;
	private Text textExportDelimit;
	private Text textHomePage;
	private Button btnCheckButtonHomepage;

	public GeneralPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		lblLanguage = new Label(container, SWT.NONE);
		lblLanguage.setText(Messages.get().LoginDialog_lblLanguage_text);
		
		comboLanguage = new Combo(container, SWT.READ_ONLY);
		comboLanguage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeUILocale(comboLanguage.getText());
			}
		});
		comboLanguage.add(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
		comboLanguage.add(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN));
		comboLanguage.setData(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), Locale.ENGLISH);
		comboLanguage.setData(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN), Locale.KOREAN);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.get().DefaultPreferencePage_2);
		
		textSessionTime = new Text(container, SWT.BORDER);
		textSessionTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExportDilimit = new Label(container, SWT.NONE);
		lblExportDilimit.setText(Messages.get().GeneralPreferencePage_lblExportDilimit_text);
		
		textExportDelimit = new Text(container, SWT.BORDER);
		textExportDelimit.setText(Messages.get().GeneralPreferencePage_text_text);
		textExportDelimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblHomePage = new Label(container, SWT.NONE);
		lblHomePage.setText(Messages.get().GeneralPreferencePage_lblHomePage_text);
		
		textHomePage = new Text(container, SWT.BORDER);
		textHomePage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnCheckButtonHomepage = new Button(container, SWT.CHECK);
		btnCheckButtonHomepage.setText(Messages.get().GeneralPreferencePage_btnCheckButton_text);
		btnCheckButtonHomepage.setSelection(true);
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * change ui locale
	 * 
	 * @param strComoboStr
	 */
	private void changeUILocale(String strComoboStr) {
		Locale localeSelect = (Locale)comboLanguage.getData(strComoboStr);
		RWT.getUISession().setLocale(localeSelect);
	}
	
	@Override
	public boolean performOk() {
		String strLocale 		= comboLanguage.getText();
		String txtSessionTime 	= textSessionTime.getText();
		String txtExportDelimit = textExportDelimit.getText();
		String txtHomePage 		= textHomePage.getText();
		String txtHomePageUse 	= ""+btnCheckButtonHomepage.getSelection();
		
		// change locale
		Locale locale = (Locale)comboLanguage.getData(strLocale);
		CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE, locale.toLanguageTag());
		RWT.getUISession().setLocale(locale);
		
		if(!NumberUtils.isNumber(txtSessionTime)) {
			textSessionTime.setFocus();
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().DefaultPreferencePage_2 + Messages.get().GeneralPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {			
			updateInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, txtSessionTime);
			updateInfo(PreferenceDefine.EXPORT_DILIMITER, 			txtExportDelimit);
			updateInfo(PreferenceDefine.DEFAULT_HOME_PAGE, 			txtHomePage);
			updateInfo(PreferenceDefine.DEFAULT_HOME_PAGE_USE, 		txtHomePageUse);
		} catch(Exception e) {
			logger.error("GeneralPreference saveing", e);
			
			MessageDialog.openError(getShell(), Messages.get().Confirm, Messages.get().GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return super.performOk();
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
	
	/**
	 * initialize locale
	 */
	private void initLocale() {
		
		// 개인 사용자는 기본 언어가 없을 수 있으므로..
		HttpServletRequest request = RWT.getRequest();
		Cookie[] cookies = request.getCookies();

		boolean isExist = false;
		if(cookies != null) {
			for (Cookie cookie : cookies) {				
				if(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE.equals(cookie.getName())) {
					
					Locale locale = Locale.forLanguageTag(cookie.getValue());
					comboLanguage.setText(locale.getDisplayLanguage(locale));

					changeUILocale(comboLanguage.getText());
					isExist = true;
					
					break;
				}
			}
		}
		
		// 세션에 기본 로케일이 지정되어 있지 않으면.
		if(!isExist) comboLanguage.setText(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
		
	}
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		initLocale();
		
		textSessionTime.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE));//"" + GetPreferenceGeneral.getSessionTimeout() ); //$NON-NLS-1$
		textExportDelimit.setText(GetPreferenceGeneral.getValue(PreferenceDefine.EXPORT_DILIMITER, PreferenceDefine.EXPORT_DILIMITER_VALUE));// "" + GetPreferenceGeneral.getExportDelimit() ); //$NON-NLS-1$
		textHomePage.setText(GetPreferenceGeneral.getValue(PreferenceDefine.DEFAULT_HOME_PAGE, PreferenceDefine.DEFAULT_HOME_PAGE_VALUE)); //$NON-NLS-1$
		
		String use = GetPreferenceGeneral.getValue(PreferenceDefine.DEFAULT_HOME_PAGE_USE, PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE);//GetPreferenceGeneral.getDefaultHomePageUse();
		if(Boolean.parseBoolean(use)) {
			btnCheckButtonHomepage.setSelection(true);
		} else {
			btnCheckButtonHomepage.setSelection(false);
		}
	}
}
