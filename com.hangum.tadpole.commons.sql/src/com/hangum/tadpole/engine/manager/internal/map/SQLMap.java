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
package com.hangum.tadpole.engine.manager.internal.map;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
		
		return SqlMapClientBuilder.buildSqlMapClient(new StringReader(config));
	}

	/**
	 * DB환경 정보 파일을 올바른 정보로 바꾸어준다.
	 * @param dbInfo 
	 * @return
	 * @throws Exception
	 */
	private static String getConfig(UserDBDAO dbInfo) throws Exception {
		String config = getFileToString(dbInfo.getDBDefine().getLocation());
		
		config = config.replace(URL, StringEscapeUtils.escapeXml(dbInfo.getUrl()));	
		config = config.replace(USERNAME, StringEscapeUtils.escapeXml(dbInfo.getUsers()));
		config = config.replace(PASSWORD, StringEscapeUtils.escapeXml(dbInfo.getPasswd())) ;
		
		return config;		
	}
	
	/**
	 * SQLMap XML to string
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
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
