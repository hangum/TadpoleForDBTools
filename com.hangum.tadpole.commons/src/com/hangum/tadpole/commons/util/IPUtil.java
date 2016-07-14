/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilters;

/**
 * IP filter utils
 * 
 * @author hangum
 *
 */
public class IPUtil {
	private static final Logger logger = Logger.getLogger(IPUtil.class);
	
	/**
	 * ip sort
	 * 
	 * @param ipList
	 * @return
	 */
	public static List<String> ipSort(List<String> ipList) {

		Collections.sort(ipList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String[] ips1 = o1.split("\\.");
				String updatedIp1 = String.format("%3s.%3s.%3s.%3s", ips1[0], ips1[1], ips1[2], ips1[3]);
				String[] ips2 = o2.split("\\.");
				String updatedIp2 = String.format("%3s.%3s.%3s.%3s", ips2[0], ips2[1], ips2[2], ips2[3]);
				return updatedIp1.compareTo(updatedIp2);
			}
		});

		return ipList;
	}
	
	
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
