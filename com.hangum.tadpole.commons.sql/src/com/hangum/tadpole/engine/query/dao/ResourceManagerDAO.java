/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - RDBMS information for columns.
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.engine.query.dao.rdb.FieldNameAnnotationClass;

/**
 * 테이블의 모든 컬럼에 대한 정보를 조회
 * 
 * <pre>
 * 		조회 권한을 갖는 모든 테이블에 대한 컬럼 목록을 표시하여 컬럼의 통계정보 생성유무나 데이터 자료형의 불일치, 코멘드나 참조무결성 정의 여부들을 한 화면에서 조회할 수 있도록 한다.
 * 
 * </pre>
 * 
 * @author nilriri
 * 
 */
public class ResourceManagerDAO extends AbstractDAO {

	long resource_seq;
	String resource_types;
	long user_seq;
	String user_name = "";
	long db_seq;
	String display_name;
	
	long group_seq;
	String shared_type;
	String name;
	String description;
	String create_time;
	String delyn;
	
	String restapi_yesno = PublicTadpoleDefine.YES_NO.NO.name();
	String restapi_uri = "";
	String restapi_key = "";
	
	public ResourceManagerDAO() {
	}

	public ResourceManagerDAO(long resource_seq, String resource_types, long user_seq, String user_name, long db_seq, long group_seq, String name,
			String shared_type, String description, String create_time, String delyn, String restapi_yesno, String restapi_uri, String restapi_key) {
		this.resource_seq = resource_seq;
		this.resource_types = resource_types;
		this.user_seq = user_seq;
		this.user_name = user_name;
		this.db_seq = db_seq;
		this.group_seq = group_seq;
		this.name = name;
		this.shared_type = shared_type;
		this.description = description;
		this.create_time = create_time;
		this.delyn = delyn;

		this.restapi_yesno = restapi_yesno;
		this.restapi_uri = restapi_uri;
		this.restapi_key = restapi_key;
	}

	/**
	 * @return the resource_seq
	 */
	@FieldNameAnnotationClass(fieldKey = "resource_seq")
	public long getResource_seq() {
		return resource_seq;
	}

	/**
	 * @return the resource_types
	 */
	@FieldNameAnnotationClass(fieldKey = "resource_types")
	public String getResource_types() {
		return resource_types;
	}

	/**
	 * @return the user_seq
	 */
	@FieldNameAnnotationClass(fieldKey = "user_seq")
	public long getUser_seq() {
		return user_seq;
	}

	/**
	 * @return the user_name
	 */
	@FieldNameAnnotationClass(fieldKey = "user_name")
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @return the db_seq
	 */
	@FieldNameAnnotationClass(fieldKey = "db_seq")
	public long getDb_seq() {
		return db_seq;
	}

	/**
	 * @return the group_seq
	 */
	@FieldNameAnnotationClass(fieldKey = "group_seq")
	public long getGroup_seq() {
		return group_seq;
	}
	/**
	 * @return the shared_type
	 */
	@FieldNameAnnotationClass(fieldKey = "shared_type")
	public String getShared_type() {
		return shared_type;
	}

	/**
	 * @return the description
	 */
	@FieldNameAnnotationClass(fieldKey = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @return the create_time
	 */
	@FieldNameAnnotationClass(fieldKey = "create_time")
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @return the delyn
	 */
	@FieldNameAnnotationClass(fieldKey = "delyn")
	public String getDelyn() {
		return delyn;
	}

	/**
	 * @return the restapi_yesno
	 */
	@FieldNameAnnotationClass(fieldKey = "restapi_yesno")
	public String getRestapi_yesno() {
		return restapi_yesno;
	}

	/**
	 * @param restapi_yesno the restapi_yesno to set
	 */
	public void setRestapi_yesno(String restapi_yesno) {
		this.restapi_yesno = restapi_yesno;
	}

	/**
	 * @return the restapi_key
	 */
	@FieldNameAnnotationClass(fieldKey = "restapi_key")
	public String getRestapi_key() {
		return restapi_key;
	}

	/**
	 * @param restapi_key the restapi_key to set
	 */
	public void setRestapi_key(String restapi_key) {
		this.restapi_key = restapi_key;
	}

	/**
	 * @param resource_seq
	 *            the resource_seq to set
	 */
	public void setResource_seq(long resource_seq) {
		this.resource_seq = resource_seq;
	}

	/**
	 * @param resource_types
	 *            the resource_types to set
	 */
	public void setResource_types(String resource_types) {
		this.resource_types = resource_types;
	}

	/**
	 * @param user_seq
	 *            the user_seq to set
	 */
	public void setUser_seq(long user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @param user_name
	 *            the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @param db_seq
	 *            the db_seq to set
	 */
	public void setDb_seq(long db_seq) {
		this.db_seq = db_seq;
	}

	/**
	 * @param group_seq
	 *            the group_seq to set
	 */
	public void setGroup_seq(long group_seq) {
		this.group_seq = group_seq;
	}

	/**
	 * @param shared_type
	 *            the shared_type to set
	 */
	public void setShared_type(String shared_type) {
		this.shared_type = shared_type;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @param delyn
	 *            the delyn to set
	 */
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	@FieldNameAnnotationClass(fieldKey = "restapi_uri")
	public String getRestapi_uri() {
		return restapi_uri;
	}

	public void setRestapi_uri(String restapi_uri) {
		this.restapi_uri = restapi_uri;
	}

	@FieldNameAnnotationClass(fieldKey = "res_title")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

}
