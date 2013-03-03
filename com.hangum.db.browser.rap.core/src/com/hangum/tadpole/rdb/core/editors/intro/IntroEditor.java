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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * 기본 introduction
 * 
 * - 홈페이지에 PLAN정보기술 홈페이지와 올챙이 홈을 랜덤하게 표시되도록 수정합니다. (http://www.pitmongo.co.kr/)
 * - 도네이션 회사들은 어떻게 하지?
 * 
 * @author hangum
 *
 */
public class IntroEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(IntroEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.intor"; //$NON-NLS-1$
	private Text textURL;
	private Browser browser;
	
	/** default main home */
	public static String[] ARRAY_DEFAULT_MAIN_HOME_PAGE = {PreferenceDefine.DEFAULT_HOME_PAGE_VALUE};//, "http://www.pitmongo.co.kr/"};
	
	/** default dona home */
	public static String[] ARRAY_DONATION_HOME_PAGE = {"http://www.cubrid.org/", "www.osci.kr"}; //$NON-NLS-1$ //$NON-NLS-2$

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
		
		browser = new Browser(compositeBody, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpDonor = new Group(parent, SWT.BORDER);
		grpDonor.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpDonor.setText(com.hangum.tadpole.rdb.core.Messages.IntroEditor_0);
		grpDonor.setLayout(new GridLayout(2, false));
		
		CLabel lblWwwcubridorg = new CLabel(grpDonor, SWT.BORDER);
		lblWwwcubridorg.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblWwwcubridorg.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/donor/CUBRID.png")); //$NON-NLS-1$

		CLabel lblOpenSourceCunsulting = new CLabel(grpDonor, SWT.BORDER);
		lblOpenSourceCunsulting.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/donor/OpenSourceConsulting.png")); //$NON-NLS-1$
		
		CLabel label_1 = new CLabel(grpDonor, SWT.NONE);
		label_1.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		label_1.setText(Messages.IntroEditor_label_text);
		
		CLabel label = new CLabel(grpDonor, SWT.NONE);
		label.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		label.setText(Messages.IntroEditor_label_1_text);
		
		setBrowserURL();		
	}
	
	/**
	 * broswer set
	 */
	private void setBrowserURL() {
		int selectRandom = (int)(Math.random() * ARRAY_DEFAULT_MAIN_HOME_PAGE.length);
		logger.info("[select home]" + ARRAY_DEFAULT_MAIN_HOME_PAGE[selectRandom]); //$NON-NLS-1$
		browser.setUrl(ARRAY_DEFAULT_MAIN_HOME_PAGE[selectRandom]);
		
		textURL.setText(ARRAY_DEFAULT_MAIN_HOME_PAGE[selectRandom]);

	}
	
	/**
	 * set the user browser
	 * 
	 * @param url
	 */
	private void setBrowserURL(String url) {
		boolean boolStartHttp = StringUtils.startsWith(url, "http"); //$NON-NLS-1$
		if(boolStartHttp) browser.setUrl(url);
		else browser.setUrl("http://" + url); //$NON-NLS-1$
	}

	@Override
	public void setFocus() {
	}
	
}
