/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.utils;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * make jdbc connection string tuils
 * 
 * @author hangum
 *
 */
public class MakeJDBCConnectionStringUtil {

	/**
	 * make connection url
	 * 
	 * @param userDB
	 * @return
	 */
	public static String makeConnectionUrl(final UserDBDAO userDB) {
		String dbUrl = "";
		
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT) {
			String selectLocale = StringUtils.trimToEmpty(userDB.getLocale());
			if(selectLocale.equals("") || DBLocaleUtils.NONE_TXT.equals(selectLocale)) {
				dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB);
				if(!"".equals(userDB.getUrl_user_parameter())) {
					dbUrl += "?" + userDB.getUrl_user_parameter();
				}
	
			} else {			
				dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB) + "?useUnicode=false&characterEncoding=" + selectLocale;
				if(!"".equals(userDB.getUrl_user_parameter())) {
					dbUrl += "&" + userDB.getUrl_user_parameter();
				}
			}
		} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
			if(userDB.getExt1().equals("SID")) {
				dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB);
			} else if(userDB.getExt1().equals("Service Name")) {
				dbUrl = getPueURL("jdbc:oracle:thin:@//%s:%s/%s", userDB);
			}
			
			if(!"".equals(userDB.getUrl_user_parameter())) {
				dbUrl += "?" + userDB.getUrl_user_parameter();
			}
		} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
			dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB);
			if(!"".equals(userDB.getUrl_user_parameter())) {
				dbUrl += "/?" + userDB.getUrl_user_parameter();
			}
		} else if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
			String selectLocale = StringUtils.trimToEmpty(userDB.getLocale());
			
			if(selectLocale.equals("") || DBLocaleUtils.NONE_TXT.equals(selectLocale)) {
				dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB);
				
				if(!"".equals(userDB.getUrl_user_parameter())) {
					dbUrl += "?" + userDB.getUrl_user_parameter();
				}
			} else {
				dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB)  + "?charset=" + selectLocale;
				
				if(!"".equals(userDB.getUrl_user_parameter())) {
					dbUrl += "&" + userDB.getUrl_user_parameter();
				}
			}
		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			dbUrl = getPueURL(userDB.getDBDefine().getDB_URL_INFO(), userDB);
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getExt1())) {
				dbUrl += "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"; //$NON-NLS-1$
				
				if(!"".equals(userDB.getUrl_user_parameter())) { //$NON-NLS-1$
					dbUrl += "&" + userDB.getUrl_user_parameter(); //$NON-NLS-1$
				}
			} else {
				if(!"".equals(userDB.getUrl_user_parameter())) { //$NON-NLS-1$
					dbUrl += "?" + userDB.getUrl_user_parameter(); //$NON-NLS-1$
				}
			}
			
		}
		
		return dbUrl;
	}
	
	private static String getPueURL(String strTempURL, final UserDBDAO userDB) {
		return String.format(
				strTempURL, 
				StringUtils.trimToEmpty(userDB.getHost()), 
				StringUtils.trimToEmpty(userDB.getPort()), 
				StringUtils.trimToEmpty(userDB.getDb())
			);
	}
}
