/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import org.apache.log4j.Logger;
import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilters;

/**
 * IP filter utils
 * 
 * @author hangum
 *
 */
public class IPFilterUtil {
	private static final Logger logger = Logger.getLogger(IPFilterUtil.class);
	
	
	/**
	 * ip filter
	 * 
	 * usage : IPFilter.ifFilterString("1.2.*.*", "1.2.1.1")
	 * 
	 * @param strAllowIP
	 * @param strCheckIP
	 * @return
	 */
	public static boolean ifFilterString(String strAllowIP, String strCheckIP) {
		try {
			Config config = new Config();
			config.setAllowFirst(true);
			config.setDefaultAllow(false);
			config.allow(strAllowIP);
			
			return IpFilters.create(config).accept(strCheckIP);
		} catch(Exception e) {
			logger.error("check user ip", e);
		}
		return true;
	}
}
