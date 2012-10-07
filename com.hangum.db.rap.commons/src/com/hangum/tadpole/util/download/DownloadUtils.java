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
package com.hangum.tadpole.util.download;


import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

/**
 * data export util
 * 
 * @author hangum
 *
 */
public class DownloadUtils {

	/** download url */
	private static String createDownloadUrl(String id) {
		StringBuilder url = new StringBuilder();
		url.append(RWT.getRequest().getContextPath());
		url.append(RWT.getRequest().getServletPath());
		url.append("?");
		url.append(IServiceHandler.REQUEST_PARAM);
		url.append("=" + id);
		String encodedURL = RWT.getResponse().encodeURL(url.toString());
		return encodedURL;
	}

	/**
	 * download browser
	 * @param editor
	 * @param downloadUrl
	 */
	public static void provideDownload(Composite composite, String id) {
		String downloadUrl = createDownloadUrl(id);
		
		Browser downloadBrowser = new Browser(composite, SWT.NONE);
		downloadBrowser.setBounds(0, 0, 0, 0);
		downloadBrowser.setUrl(downloadUrl);
	}
	
}
