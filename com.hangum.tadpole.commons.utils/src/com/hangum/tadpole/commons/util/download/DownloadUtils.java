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
package com.hangum.tadpole.commons.util.download;


import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.eclipse.rap.rwt.service.ServiceManager;
import org.eclipse.swt.widgets.Composite;

/**
 * file download utils
 * 
 * @author hangum
 *
 */
public class DownloadUtils {
	private static final Logger logger = Logger.getLogger(DownloadUtils.class);

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
		if(logger.isDebugEnabled()) logger.debug("#### download external file==[downloadUrl]" + downloadUrl);
				
		UrlLauncher launcher = RWT.getClient().getService( UrlLauncher.class );
		launcher.openURL(downloadUrl);
	}
	
}
