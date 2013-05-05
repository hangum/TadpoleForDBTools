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
package com.hangum.tadpole.commons.sql.map;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.util.secret.EncryptiDecryptUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * SQLMapMap을 사용하기위해 디비 정보와 인스턴스를 생성한다.
 * 
 * @author hangum
 */
public class SQLMap {
	private final static String URL 		= "${JDBC.ConnectionURL}";
	private final static String USERNAME 	= "${JDBC.Username}";
	private final static String PASSWORD 	= "${JDBC.Password}";
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLMap.class);

	private SQLMap() {}
	public static SqlMapClient getInstance(UserDBDAO dbInfo) throws Exception {
		String config = getConfig(dbInfo);
		
		return SqlMapClientBuilder.buildSqlMapClient( new StringReader( config ) );
	}

	/**
	 * DB환경 정보 파일을 올바른 정보로 바꾸어준다.
	 * @param dbInfo 
	 * @return
	 * @throws Exception
	 */
	private static String getConfig(UserDBDAO dbInfo) throws Exception {
		String config = getFileToString(DBDefine.getDBDefine(dbInfo.getTypes()).getLocation());
		
		// url chnage
		config = config.replace(URL, StringEscapeUtils.escapeXml( dbInfo.getUrl() ));			
		// id change
		config = config.replace(USERNAME, StringEscapeUtils.escapeXml( dbInfo.getUsers() ));
		
		// pass change
		String passwdDecrypt = "";
		try {
			passwdDecrypt = EncryptiDecryptUtil.decryption(dbInfo.getPasswd());
		} catch(Exception e) {
			passwdDecrypt = dbInfo.getPasswd();
		}
		config = config.replace(PASSWORD, StringEscapeUtils.escapeXml( passwdDecrypt )) ;
		
		return config;		
	}
	
	private static String getFileToString(String url) throws Exception{
		ClassLoader loader = SQLMap.class.getClassLoader();
		InputStream is = loader == null ? ClassLoader.getSystemResourceAsStream(url) : loader.getResourceAsStream(url);
		
		int size = is.available();
		byte[] dataByte = new byte[size];
		is.read(dataByte, 0, size);
		is.close();
		
		return new String(dataByte);
	}
	
}
