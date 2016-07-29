/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - 데이터베이스 링크 정보
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.rdb;


import org.apache.commons.lang.StringUtils;

/**
 * 데이터베이스에 정의된 링크 정보를 조회한다.
 * 
 * @author nilriri
 * 
 */
public class OracleDBLinkDAO extends AbstractDAO {

	String db_link;  // 데이터베이스 링크 명
	String username; // 원격지 데이터 베이스에 연결하기 위한 유저 아이
	String host;     // 원격지 데이터 베이스를 정의하는 TNS명칭 tnsnames.ora에 정의되어 있음.
	String created;  // 데이터베이스링크를 생성한 날짜.
	
	//DB_LINK    ,USERNAME    ,HOST    ,CREATED    ,OWNER
    
	public OracleDBLinkDAO() {
		this("", "", "", "");
	}

	public OracleDBLinkDAO(String db_link, String username, String host, String created) {
		this.db_link = db_link;
		this.username = username;
		this.host = host;
		this.created = created;
	}

	@Override
	public String getFullName() {
		if(StringUtils.isEmpty(this.schema_name)) {
			return this.getSysName();
		}else{
			return String.format("%s.%s", this.getSchema_name(), this.getSysName());
		}
	}

	@FieldNameAnnotationClass(fieldKey = "db_link")
	public String getDb_link() {
		return db_link;
	}

	public void setDb_link(String db_link) {
		this.db_link = db_link;
	}

	@FieldNameAnnotationClass(fieldKey = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@FieldNameAnnotationClass(fieldKey = "host")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@FieldNameAnnotationClass(fieldKey = "created")
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
