/*******************************************************************************

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * Cookie utils
 * 
 * @author hangum
 *
 */
public class CookieUtils {
	private static final Logger logger = Logger.getLogger(CookieUtils.class);
	
	/**
	 * initialize cookie data
	 */
	public static boolean isUpdateChecker() {
		HttpServletRequest request = RWT.getRequest();
		Cookie[] cookies = request.getCookies();
		
		if(cookies == null) return true;
		for (Cookie cookie : cookies) {				
			if(PublicTadpoleDefine.TDB_COOKIE_UPDATE_CHECK.equals(cookie.getName())) {
				String val = cookie.getValue();
				
				return Boolean.getBoolean(val);
			}
		}
		
		return true;
	}
	
	/**
	 * save cookie
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveCookie(String key, String value) {
		try {
			HttpServletResponse response = RWT.getResponse();
			Cookie tdbCookie = new Cookie(key, value);
			tdbCookie.setMaxAge(60 * 60 * 24 * 365);
			tdbCookie.setPath(PublicTadpoleDefine._cookiePath);
			response.addCookie(tdbCookie);
		} catch(Exception e) {
			logger.error("regist user info", e);
		}
	}
	
	/**
	 * delete login cookie
	 */
	public static void deleteLoginCookie() {
		try {
			HttpServletResponse response = RWT.getResponse();
			HttpServletRequest request = RWT.getRequest();
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if(PublicTadpoleDefine.TDB_COOKIE_USER_SAVE_CKECK.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath(PublicTadpoleDefine._cookiePath);
					response.addCookie(cookie);
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_ID.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath(PublicTadpoleDefine._cookiePath);
					response.addCookie(cookie);
				} else  if(PublicTadpoleDefine.TDB_COOKIE_USER_PWD.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath(PublicTadpoleDefine._cookiePath);
					response.addCookie(cookie);
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath(PublicTadpoleDefine._cookiePath);
					response.addCookie(cookie);
				}
			}

		} catch(Exception e) {
			logger.error("regist user info", e);
		}
	}
}
