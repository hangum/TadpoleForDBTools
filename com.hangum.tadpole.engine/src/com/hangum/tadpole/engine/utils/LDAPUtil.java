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
package com.hangum.tadpole.engine.utils;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.Messages;
import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.tadpolehub.db.ldap.core.utils.InternalLDAPUtils;

/**
 * LDAP utils
 * 
 * @author hangum
 *
 */
public class LDAPUtil {
	private static final Logger logger = Logger.getLogger(LDAPUtil.class);
	
	public static LDAPUtil instance = null;
//	private static String _ldapHost;		 // LDAP 호스트
//	private static String _principal;	 // LDAP 쿼리
	
	private LDAPUtil() {}
	
	public static LDAPUtil getInstance() {
		if(instance == null) {
			instance = new LDAPUtil();
		}
		
		return instance;
	}
	
	public static void isValidation() throws TadpoleAuthorityException {
		
	}
	
	/**
	 * ldap login 합니다. 
	 * 
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	public static void isValidation(String userId, String userPwd) throws TadpoleAuthorityException {
		String url 		= GetAdminPreference.getLDAPURL();
		String bindDn	= GetAdminPreference.getLDAPBindDN(); 
		String bindPwd	= GetAdminPreference.getLDAPBindDNPwd();
		String userDn	= GetAdminPreference.getLDAPUser();
		String baseDN	= GetAdminPreference.getLDAPBaseDN();
		
		try {
			if(bindDn.equals("")) {
				InternalLDAPUtils.isLogin(url, baseDN, userId, userPwd);				
			} else {
				InternalLDAPUtils.isBindLogin(url, bindDn, bindPwd, baseDN, String.format(userDn, userId), userPwd);
			}
		} catch(Exception e) {
			logger.error("LDAP Login fail" + e.getMessage());
			throw new TadpoleAuthorityException(Messages.get().PleaseCheckIDpassword + "\n" + e.getMessage());
		}
	}

//	/**
//	 * LDAP Login
//	 * 
//	 * @param strEmail
//	 * @param strPass
//	 * @deprecated
//	 */
//	public static void ldapLogin(String strEmail, String strPass) throws TadpoleAuthorityException {
//		Hashtable<String, String> properties = new Hashtable<String, String>();
//		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		properties.put(Context.PROVIDER_URL, _ldapHost);
//		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
//		properties.put(Context.SECURITY_PRINCIPAL, String.format(_principal, strEmail));
//		properties.put(Context.SECURITY_CREDENTIALS, strPass);
//		properties.put("com.sun.jndi.ldap.connect.timeout", "5000");
//		
//		DirContext con = null;
//		try {
//			con = new InitialDirContext(properties);
//		} catch (Exception e) {
//			logger.error("LDAP Login fail" + e.getMessage());
//			throw new TadpoleAuthorityException(Messages.get().PleaseCheckIDpassword);
//		} finally {
//			 if(con != null) try { con.close(); } catch(Exception e) {}
//		}
//	}
}
