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
package com.hangum.tadpole.commons.libs.core.utils;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Browser composite
 * 
 * @author hangum
 *
 */
public class BrowserComposite extends Composite {
	private static final Logger logger = Logger.getLogger(BrowserComposite.class);
	
	private Text textURL;
	private Composite compositeBody;
	private Browser browserWebConsole;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param url
	 * @param isShowURL
	 */
	public BrowserComposite(Composite parent, int style, String url, boolean isShowURL) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite compositeTitle = new Composite(this, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(1, false));
		
		textURL = new Text(compositeTitle, SWT.BORDER);
		textURL.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) browserUrl();
			}
		});
		textURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textURL.setText(url);
		
		compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		browserWebConsole = new Browser(compositeBody, SWT.NONE);
		browserWebConsole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initWebConsole();
	}
	
	/**
	 * reload url
	 */
	private void browserUrl() {
		String strUrl = textURL.getText();
		if(!strUrl.startsWith("http")) { //$NON-NLS-1$
			strUrl = "http://" + strUrl; //$NON-NLS-1$
		}
		browserWebConsole.setUrl(strUrl);
		
		browserWebConsole.layout();		
		compositeBody.layout();
	}
	
	/**
	 * initialize web console 
	 */
	private void initWebConsole() {
		
		try {
			browserWebConsole.setUrl(textURL.getText());			
		} catch(Exception e) {
			logger.error("briwser init status", e); //$NON-NLS-1$
			
			MessageDialog.openError(null, "Error", "Browser Initialize error\n [url]" + textURL.getText() + "\n [error msg]"+ e.getMessage());
		}	
	}

	@Override
	protected void checkSubclass() {
	}

}
