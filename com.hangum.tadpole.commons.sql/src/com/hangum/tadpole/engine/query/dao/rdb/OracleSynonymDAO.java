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
package com.hangum.tadpole.engine.query.dao.rdb;

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
public class OracleSynonymDAO extends AbstractDAO {

	String synonym_name;
	String table_owner;
	String table_name;
	String db_link;
	String comments;
	String object_type;

	public OracleSynonymDAO() {
		this("", "", "", "", "");
	}

	/**
	 * 
	 * @param synonym_name
	 * @param table_owner
	 * @param table_name
	 * @param db_link
	 * @param comments
	 */
	public OracleSynonymDAO(String synonym_name, String table_owner, String table_name, String db_link, String comments) {
		this.synonym_name = synonym_name;
		this.table_owner = table_owner;
		this.table_name = table_name;
		this.db_link = db_link;
		this.comments = comments;
	}

	/**
	 * @return
	 */
	public String getName() {
		return table_name;
	}
	
	/**
	 * @return the table_name
	 */
	@FieldNameAnnotationClass(fieldKey = "table_name")
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @return the synonym_name
	 */
	@FieldNameAnnotationClass(fieldKey = "synonym_name")
	public String getSynonym_name() {
		return synonym_name;
	}

	/**
	 * @return the table_owner
	 */
	@FieldNameAnnotationClass(fieldKey = "table_owner")
	public String getTable_owner() {
		return table_owner;
	}

	/**
	 * @return the db_link
	 */
	@FieldNameAnnotationClass(fieldKey = "db_link")
	public String getDb_link() {
		return db_link;
	}

	/**
	 * @return the comments
	 */
	@FieldNameAnnotationClass(fieldKey = "comments")
	public String getComments() {
		return comments;
	}

	/**
	 * @return the object_type
	 */
	@FieldNameAnnotationClass(fieldKey = "object_type")
	public String getObject_type() {
		return object_type;
	}

	/**
	 * @param synonym_name
	 *            the synonym_name to set
	 */
	public void setSynonym_name(String synonym_name) {
		this.synonym_name = synonym_name;
	}

	/**
	 * @param table_owner
	 *            the table_owner to set
	 */
	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}

	/**
	 * @param table_name
	 *            the table_name to set
	 */
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	/**
	 * @param db_link
	 *            the db_link to set
	 */
	public void setDb_link(String db_link) {
		this.db_link = db_link;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param object_type
	 *            the object_type to set
	 */
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

}
