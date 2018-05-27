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
package com.hangum.tadpole.commons.util;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;

/**
 * help utils
 * 
 * @author hangum
 *
 */
public class HelpUtils {

	public static void showHelp(String strUrl) {
		UrlLauncher launcher = RWT.getClient().getService( UrlLauncher.class );
		launcher.openURL(strUrl);
	}
}
