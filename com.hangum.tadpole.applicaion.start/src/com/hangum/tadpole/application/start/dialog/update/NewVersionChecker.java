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
package com.hangum.tadpole.application.start.dialog.update;

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
	public static final String CHECK_URI = "https://github.com/hangum/TadpoleForDBTools/tree/master/com.hangum.tadpole.applicaion.start/src/com/hangum/tadpole/application/start/dialog/update/versionInfo.json";
	
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
		long diffTimeMills = (currentTimeMillis - lastCheckTimeMillis) * 1000 / 60 / 60;
		if(logger.isDebugEnabled()) logger.debug("diff mills " + diffTimeMills);
		
		if(diffTimeMills > 0 || newVersionObj == null) {
			return getVersionInfoData();
		}
		return false;
	}
		
	/**
	 * get version info data
	 * 
	 * @return
	 */
	private boolean getVersionInfoData() {
		InputStream is = null;
		try {
			is = new URL(CHECK_URI).openConnection().getInputStream();
			Gson gson = new Gson();
			newVersionObj = gson.fromJson(IOUtils.toString(is), NewVersionObject.class);
			
			String[] arryCurVer = StringUtils.split(SystemDefine.MAJOR_VERSION, ".");
			String[] arryNewVer = StringUtils.split(newVersionObj.getMajorVer(), ".");
			for (int i=0; i<arryCurVer.length; i++) {
				if(Integer.parseInt(arryNewVer[i]) > Integer.parseInt(arryCurVer[i])) {
					return true;
				}
			}
			
		} catch (Exception e) {
			logger.error("New version checker", e);
		} finally {
			if(is != null) {
				try { IOUtils.toString(is); } 
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
