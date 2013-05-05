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
package com.hangum.tadpole.util.download;


import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServiceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

/**
 * file download utils
 * 
 * @author hangum
 *
 */
public class DownloadUtils {

	/** download url */
	private static String createDownloadUrl(String id) {
		ServiceManager manager = RWT.getServiceManager();
		String url = manager.getServiceHandlerUrl( id );
		
		return url;
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
