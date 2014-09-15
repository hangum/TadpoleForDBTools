/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.externalbrowser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;

/**
 * Extension browser
 * 
 * @author hangum
 *
 */
public class ExtBrowserWidget extends Composite {
	/** url history */
	private List<String> listHistory = new ArrayList<String>();
	private int intUrlPos = -1;
	
	private Browser browser;
	private Text textUrl;

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param defaultURL
	 */
	public ExtBrowserWidget(Composite parent, int style, String defaultURL) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(6, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Button btnBack = new Button(compositeHead, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(intUrlPos != 0) {
					String url = listHistory.get(intUrlPos-1);
					
					setURL(url, intUrlPos-1);
				}
			}
		});
		btnBack.setText("Back");
		
		Button btnForward = new Button(compositeHead, SWT.NONE);
		btnForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(intUrlPos != -1) {
					if(intUrlPos == listHistory.size()) return;
					
					String url = listHistory.get(intUrlPos+1);
					if(url != null) {
						setURL(url, intUrlPos+1);
					}
				}
			}
		});
		btnForward.setText("Forward"); 
		
		Button btnGo = new Button(compositeHead, SWT.NONE);
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				goUrl();
			}
		});
		btnGo.setText("Go");
		
		textUrl = new Text(compositeHead, SWT.BORDER);
		textUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				goUrl();
			}
		});
		textUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUrl.setText(defaultURL);
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		browser = new Browser(compositeBody, SWT.BORDER);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent event) {
				listHistory.add(browser.getUrl());
			}
			
			@Override
			public void changed(ProgressEvent event) {
			}
		});
		
		goUrl();
		
		browser.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * 
	 */
	private void goUrl() {
		String strUrl = textUrl.getText();
		if(!strUrl.startsWith("http")) {
			strUrl = "http://" + strUrl;
		}
		
		intUrlPos++;
		browser.setUrl(strUrl);
	}
	
	/**
	 * 
	 * @param strUrl
	 * @param intPos
	 */
	private void setURL(String strUrl, int intPos) {
		browser.setUrl(strUrl);
		textUrl.setText(strUrl);
		intUrlPos = intPos;
	}
	
}
