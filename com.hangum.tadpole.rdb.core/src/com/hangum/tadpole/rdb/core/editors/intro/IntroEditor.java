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
package com.hangum.tadpole.rdb.core.editors.intro;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * 기본 introduction
 * 
 * @author hangum
 *
 */
public class IntroEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(IntroEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.intor"; //$NON-NLS-1$
	private Text textURL;
	private Browser browser;
	private Button btnCheckButton;
	
	public IntroEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		setSite(site);
		setInput(input);
		
		IntroEditorInput iei = (IntroEditorInput)input;
		setPartName(iei.getName());
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
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblUrl = new Label(compositeHead, SWT.NONE);
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUrl.setText("URL  "); //$NON-NLS-1$
		
		textURL = new Text(compositeHead, SWT.BORDER);
		textURL.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					setBrowserURL(textURL.getText());
				}
			}
		});
		textURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new Composite(parent, SWT.BORDER);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		browser = new Browser(compositeBody, SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		btnCheckButton = new Button(compositeBody, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateHomePage();
			}
		});
		btnCheckButton.setText(Messages.get().GeneralPreferencePage_btnCheckButton_text);
		btnCheckButton.setSelection(true);

		setBrowserURL();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * update home page
	 */
	private void updateHomePage() {
		try {
			String txtHomePageUse 	= ""+btnCheckButton.getSelection();
			TadpoleSystem_UserInfoData.updateValue(PreferenceDefine.DEFAULT_HOME_PAGE_USE, txtHomePageUse);
		} catch(Exception e) {
			logger.error("update home page", e);
		}
	}
	
	/**
	 * broswer set
	 */
	private void setBrowserURL() {
		textURL.setText(GetPreferenceGeneral.getDefaultHomePage());
		setBrowserURL(GetPreferenceGeneral.getDefaultHomePage());
	}
	
	/**
	 * set the user browser
	 * 
	 * @param url
	 */
	private void setBrowserURL(String url) {
		if(logger.isDebugEnabled()) logger.debug("Default home url is " + url);
		
		boolean boolStartHttp = StringUtils.startsWith(url, "http"); //$NON-NLS-1$
		if(boolStartHttp) browser.setUrl(url);
		else {
			textURL.setText("http://" + url);
			browser.setUrl("http://" + url); //$NON-NLS-1$
		}
	}

	@Override
	public void setFocus() {
	}
	
}
