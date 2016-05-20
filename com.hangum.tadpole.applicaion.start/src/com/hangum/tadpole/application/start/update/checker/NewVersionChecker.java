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
package com.hangum.tadpole.application.start.update.checker;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;

/**
 * Update checker
 * 
 * @author hangum
 *
 */
public class NewVersionChecker {
	private static final Logger logger = Logger.getLogger(NewVersionChecker.class);
	public static final String CHECK_URI = "https://raw.githubusercontent.com/hangum/TadpoleForDBTools/gh-pages/versionInfo.json";
	
	private long lastCheckTimeMillis = 0l;
	private NewVersionObject newVersionObj;
	
	public static NewVersionChecker instance = new NewVersionChecker();
	private NewVersionChecker() {}
	
	public static synchronized NewVersionChecker getInstance() {
		return instance;
	}

	/**
	 * new version checker
	 * @return
	 */
	public boolean check() {
		if(lastCheckTimeMillis == 0l) lastCheckTimeMillis = System.currentTimeMillis();
		
		// 60분에 한번씩 check 하여 값을 돌려준다.
		long currentTimeMillis = System.currentTimeMillis();
		long diffTimeMills = (currentTimeMillis - lastCheckTimeMillis) / 1000l;
		
		if(logger.isDebugEnabled()) {
			logger.debug("currentTimeMillis mills " + currentTimeMillis);
			logger.debug("lastCheckTimeMillis mills " + lastCheckTimeMillis);
			logger.debug("diff mills " + diffTimeMills);
		}
		
		// 24시간에 한번씩 검사한다.
		if(diffTimeMills > (60 * 60 * 24) || newVersionObj == null) {
			lastCheckTimeMillis = currentTimeMillis;
			return getVersionInfoData();
		}
		return false;
	}
		
	/**
	 * get version info data
	 * 
	 * @return
	 */
	public boolean getVersionInfoData() {
		InputStream is = null;
		try {
			is = new URL(CHECK_URI).openConnection().getInputStream();
			String strJson = IOUtils.toString(is);
			if(logger.isDebugEnabled()) {
				logger.debug("==[start]===================================");
				logger.debug(strJson);
				logger.debug("==[end]===================================");
			}
			Gson gson = new Gson();
			newVersionObj = gson.fromJson(strJson, NewVersionObject.class);
			if(newVersionObj == null) return false;
			
			String[] arryCurVer = StringUtils.split(SystemDefine.MAJOR_VERSION, ".");
			String[] arryNewVer = StringUtils.split(newVersionObj.getMajorVer(), ".");
			for (int i=0; i<arryCurVer.length; i++) {
				if(Integer.parseInt(arryNewVer[i]) > Integer.parseInt(arryCurVer[i])) {
					return true;
				}
			}
			
		} catch (Exception e) {
			logger.error(String.format("New version checkerer %s.", e.getMessage()));
		} finally {
			if(is != null) {
				try {
//					IOUtils.toString(is);
					IOUtils.closeQuietly(is); 
				} 
				catch(Exception e) {
					//igoner excetpion
				}
			}
		}

		return false;
	}
	
	public NewVersionObject getNewVersionObj() {
		return newVersionObj;
	}
	
}
