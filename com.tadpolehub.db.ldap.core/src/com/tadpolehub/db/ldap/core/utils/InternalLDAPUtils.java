package com.tadpolehub.db.ldap.core.utils;

import org.apache.log4j.Logger;
import org.ldaptive.BindConnectionInitializer;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.Credential;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.auth.AuthenticationRequest;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.Authenticator;
import org.ldaptive.auth.BindAuthenticationHandler;
import org.ldaptive.auth.SearchDnResolver;

/**
 * LDAP 관련 유틸리티
 * 
 * 
 * @author hangum
 *
 */
public class InternalLDAPUtils {
	private static final Logger logger = Logger.getLogger(InternalLDAPUtils.class);
	
	/**
	 * ldap login
	 * 
	 * @param url
	 * @param baseDN
	 * @param userId
	 * @param userPwd
	 * @throws Exception
	 */
	public static void isLogin(String url, String baseDN, String userId, String userPwd) throws Exception {
		try {
			ConnectionConfig connConfig = new ConnectionConfig(url);
			connConfig.setUseStartTLS(false);
			
			SearchDnResolver dnResolver = new SearchDnResolver(new DefaultConnectionFactory(connConfig));
			dnResolver.setBaseDn(baseDN);
			dnResolver.setUserFilter(userId);
			BindAuthenticationHandler authHandler = new BindAuthenticationHandler(new DefaultConnectionFactory(connConfig));
			Authenticator auth = new Authenticator(dnResolver, authHandler);
			AuthenticationResponse response = auth.authenticate(new AuthenticationRequest(userId, new Credential(userPwd)));
			if (response.getResult()) { // authentication succeeded
				if(logger.isDebugEnabled()) logger.debug(String.format("\t == 일반유저 %s 연결 성공====", userId));

			} else { // authentication failed
				String msg = response.getMessage(); // read the failure message
				if(logger.isDebugEnabled()) logger.debug(String.format("\t == 일반유저 %s 연결 실패====", userId));
				throw new Exception("Login fail : " + msg);
			}
		} catch (Exception e) {
			logger.error("ldap bind login fail" + e.getMessage());
			
			throw e;
		}
	}
	
	/**
	 * bind ldap login 
	 * 
	 * @param url
	 * @param bindDN
	 * @param bindPwd
	 * @param baseDN
	 * @param userId
	 * @param userPwd
	 * @throws Exception
	 */
	public static void isBindLogin(String url, String bindDN, String bindPwd, String baseDN, String userId, String userPwd) throws Exception {
		try {
			ConnectionConfig connConfig = new ConnectionConfig(url);
			connConfig.setConnectionInitializer(new BindConnectionInitializer(bindDN, new Credential(bindPwd)));

			// use a search dn resolver
			SearchDnResolver dnResolver = new SearchDnResolver(new DefaultConnectionFactory(connConfig));
			dnResolver.setBaseDn(baseDN);
			dnResolver.setUserFilter(userId);

			// perform a bind for password validation
			BindAuthenticationHandler authHandler = new BindAuthenticationHandler(new DefaultConnectionFactory(connConfig));

			Authenticator auth = new Authenticator(dnResolver, authHandler);
			AuthenticationResponse response = auth.authenticate(new AuthenticationRequest(userId, new Credential(userPwd), new String[] {}));
			if (response.getResult()) { // authentication succeeded
//				LdapEntry entry = response.getLdapEntry(); // read mail and sn attributes
				if(logger.isDebugEnabled()) logger.debug(String.format("\t == 일반유저 %s 연결 성공====", userId));

			} else { // authentication failed
				String msg = response.getMessage(); // read the failure message
//				ResponseControl[] ctls = response.getControls(); // read any response controls
				if(logger.isDebugEnabled()) logger.debug(String.format("\t == 일반유저 %s 연결 실패====", userId));
				throw new Exception("Login fail : " + msg);
			}
		} catch (Exception e) {
			logger.error("ldap bind login fail" + e.getMessage());
			
			throw e;
		}
	}
}
