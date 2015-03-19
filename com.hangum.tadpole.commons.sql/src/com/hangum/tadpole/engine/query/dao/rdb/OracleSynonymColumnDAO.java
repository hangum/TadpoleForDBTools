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
 * 오라클 시노님의 세부 컬럼 정보.
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @author nilriri
 * 
 */
public class OracleSynonymColumnDAO extends AbstractDAO {

	int column_id;
	String table_owner;
	String table_name;
	String column_name;
	String data_type;
	String nullable;
	String key;
	String default_value;
	String extra;
	String comments;

	public OracleSynonymColumnDAO() {
		this(0, "", "", "", "", "", "", "", "", "", "");
	}

	public OracleSynonymColumnDAO(int column_id, String synonym_name, String table_owner, String table_name, String column_name, String comments, String data_type,
			String nullable, String key, String default_value, String extra) {
		this.column_id = column_id;
		this.table_owner = table_owner;
		this.table_name = table_name;
		this.column_name = column_name;
		this.data_type = data_type;
		this.nullable = nullable;
		this.key = key;
		this.default_value = default_value;
		this.extra = extra;
		this.comments = comments;
	}

	/**
	 * @return the column_id
	 */
	@FieldNameAnnotationClass(fieldKey = "column_id")
	public int getColumn_id() {
		return column_id;
	}
	
	/**
	 * @return the table_owner
	 */
	@FieldNameAnnotationClass(fieldKey = "table_owner")
	public String getTable_owner() {
		return table_owner;
	}

	/**
	 * @return the table_name
	 */
	@FieldNameAnnotationClass(fieldKey = "table_name")
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @return the column_name
	 */
	@FieldNameAnnotationClass(fieldKey = "column_name")
	public String getColumn_name() {
		return column_name;
	}

	/**
	 * @return the data_type
	 */
	@FieldNameAnnotationClass(fieldKey = "data_type")
	public String getData_type() {
		return data_type;
	}

	/**
	 * @return the nullable
	 */
	@FieldNameAnnotationClass(fieldKey = "nullable")
	public String getNullable() {
		return nullable;
	}

	/**
	 * @return the key
	 */
	@FieldNameAnnotationClass(fieldKey = "key")
	public String getKey() {
		return key;
	}

	/**
	 * @return the default_value
	 */
	@FieldNameAnnotationClass(fieldKey = "default_value")
	public String getDefault_value() {
		return default_value;
	}

	/**
	 * @return the extra
	 */
	@FieldNameAnnotationClass(fieldKey = "extra")
	public String getExtra() {
		return extra;
	}

	/**
	 * @return the comments
	 */
	@FieldNameAnnotationClass(fieldKey = "comments")
	public String getComments() {
		return comments;
	}

	/**
	 * @param column_id
	 *            the column_id to set
	 */
	public void setTable_owner(int column_id) {
		this.column_id = column_id;
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
	 * @param column_name
	 *            the column_name to set
	 */
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	/**
	 * @param data_type
	 *            the data_type to set
	 */
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	/**
	 * @param nullable
	 *            the nullable to set
	 */
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param default_value
	 *            the default_value to set
	 */
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

}
