/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.help.core.views.sub.doc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Database document composite
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 18.
 *
 */
public class DBDocComposite extends Composite {
	
	private Text textURL;
	private Browser browserDoc;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DBDocComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		textURL = new Text(compositeHead, SWT.BORDER);
		textURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textURL.setText("https://tadpoledbhub.atlassian.net/wiki/pages/viewpage.action?pageId=20578325");
		
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeLocation();
			}
		});
		btnSearch.setText("Search");
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		browserDoc = new Browser(composite, SWT.NONE);
		browserDoc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		changeLocation();
	}
	
	private void changeLocation() {
		String strUrl = textURL.getText();
		if(!strUrl.startsWith("http")) {
			strUrl = "http://" + strUrl;
			textURL.setText(strUrl);
		}
		browserDoc.setUrl(strUrl);
	}

	@Override
	protected void checkSubclass() {
	}
}
