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
package com.hangum.tadpole.rdb.core.editors.intro;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * 기본 intro
 * 
 * @author hangum
 *
 */
public class IntroEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(IntroEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.intor";
	private Text textURL;
	private Browser browser;

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
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(3, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Label lblUrl = new Label(compositeHead, SWT.NONE);
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUrl.setText("URL  ");
		
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
		textURL.setText(GetPreferenceGeneral.getDefaultHomePage());
		
		Button btnGo = new Button(compositeHead, SWT.NONE);
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setBrowserURL(textURL.getText());
			}
		});
		btnGo.setText("go");
		
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		browser = new Browser(composite, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		logger.debug("home page : " + GetPreferenceGeneral.getDefaultHomePage());
		setBrowserURL(GetPreferenceGeneral.getDefaultHomePage());
		
	}
	
	/**
	 * broswer set
	 * 
	 * https://sites.google.com/site/tadpolefordb/home
	 * @param url
	 */
	private void setBrowserURL(String url) {
		boolean boolStartHttp = StringUtils.startsWith(url, "http");
		if(boolStartHttp) browser.setUrl(url);
		else browser.setUrl("http://" + url);
	}

	@Override
	public void setFocus() {
	}
}
