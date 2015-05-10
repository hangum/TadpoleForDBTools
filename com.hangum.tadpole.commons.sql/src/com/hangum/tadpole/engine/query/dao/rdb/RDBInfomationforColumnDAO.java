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
public class RDBInfomationforColumnDAO extends AbstractDAO {

	String table_name;
	String table_comment;
	String column_name;
	String nullable;
	String data_type;
	String data_default;
	String column_comment;
	String data_type_mod;
	String char_used;
	String histogram;
	String num_distinct;
	String num_nulls;
	String density;
	String last_analyzed;
	String pk;

	public RDBInfomationforColumnDAO() {
		this("", "", "", "", "", "", "", "");
	}
	/**
	 * 
	 * @param table_name
	 * @param table_comment
	 * @param column_name
	 * @param column_comment
	 * @param data_type
	 * @param notnull
	 * @param dflt_value
	 * @param pk
	 */
	public RDBInfomationforColumnDAO(String table_name, String table_comment, String column_name, String column_comment, String data_type, String notnull,
			String dflt_value, String pk) {
		this.table_name = table_name;
		this.table_comment = table_comment;
		this.column_name = column_name;
		this.column_comment = column_comment;
		this.data_type = data_type;
		this.nullable = notnull;
		this.data_default = dflt_value;
		this.pk = pk;
	}

	/**
	 * @return the table_name
	 */
	@FieldNameAnnotationClass(fieldKey = "table_name")
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @return the table_comment
	 */
	@FieldNameAnnotationClass(fieldKey = "table_comment")
	public String getTable_comment() {
		return table_comment;
	}

	/**
	 * @return the column_name
	 */
	@FieldNameAnnotationClass(fieldKey = "column_name")
	public String getColumn_name() {
		return column_name;
	}

	/**
	 * @return the nullable
	 */
	@FieldNameAnnotationClass(fieldKey = "nullable")
	public String getNullable() {
		return nullable;
	}

	/**
	 * @return the data_type
	 */
	@FieldNameAnnotationClass(fieldKey = "data_type")
	public String getData_type() {
		return data_type;
	}

	/**
	 * @return the data_default
	 */
	@FieldNameAnnotationClass(fieldKey = "data_default")
	public String getData_default() {
		return data_default;
	}

	/**
	 * @return the column_comment
	 */
	@FieldNameAnnotationClass(fieldKey = "column_comment")
	public String getColumn_comment() {
		return column_comment;
	}

	/**
	 * @return the data_type_mod
	 */
	@FieldNameAnnotationClass(fieldKey = "data_type_mod")
	public String getData_type_mod() {
		return data_type_mod;
	}

	/**
	 * @return the char_used
	 */
	@FieldNameAnnotationClass(fieldKey = "char_used")
	public String getChar_used() {
		return char_used;
	}

	/**
	 * @return the histogram
	 */
	@FieldNameAnnotationClass(fieldKey = "histogram")
	public String getHistogram() {
		return histogram;
	}

	/**
	 * @return the num_distinct
	 */
	@FieldNameAnnotationClass(fieldKey = "num_distinct")
	public String getNum_distinct() {
		return num_distinct;
	}

	/**
	 * @return the num_nulls
	 */
	@FieldNameAnnotationClass(fieldKey = "num_nulls")
	public String getNum_nulls() {
		return num_nulls;
	}

	/**
	 * @return the density
	 */
	@FieldNameAnnotationClass(fieldKey = "density")
	public String getDensity() {
		return density;
	}

	/**
	 * @return the last_analyzed
	 */
	@FieldNameAnnotationClass(fieldKey = "last_analyzed")
	public String getLast_analyzed() {
		return last_analyzed;
	}

	/**
	 * @return the pk
	 */
	@FieldNameAnnotationClass(fieldKey = "pk")
	public String getPk() {
		return pk;
	}

	/**
	 * @param table_name
	 *            the table_name to set
	 */
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	/**
	 * @param table_comment
	 *            the table_comment to set
	 */
	public void setTable_comment(String table_comment) {
		this.table_comment = table_comment;
	}

	/**
	 * @param column_name
	 *            the column_name to set
	 */
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	/**
	 * @param nullable
	 *            the nullable to set
	 */
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	/**
	 * @param data_type
	 *            the data_type to set
	 */
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	/**
	 * @param data_default
	 *            the data_default to set
	 */
	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	/**
	 * @param column_comment
	 *            the column_comment to set
	 */
	public void setColumn_comment(String column_comment) {
		this.column_comment = column_comment;
	}

	/**
	 * @param data_type_mod
	 *            the data_type_mod to set
	 */
	public void setData_type_mod(String data_type_mod) {
		this.data_type_mod = data_type_mod;
	}

	/**
	 * @param char_used
	 *            the char_used to set
	 */
	public void setChar_used(String char_used) {
		this.char_used = char_used;
	}

	/**
	 * @param histogram
	 *            the histogram to set
	 */
	public void setHistogram(String histogram) {
		this.histogram = histogram;
	}

	/**
	 * @param num_distinct
	 *            the num_distinct to set
	 */
	public void setNum_distinct(String num_distinct) {
		this.num_distinct = num_distinct;
	}

	/**
	 * @param num_nulls
	 *            the num_nulls to set
	 */
	public void setNum_nulls(String num_nulls) {
		this.num_nulls = num_nulls;
	}

	/**
	 * @param density
	 *            the density to set
	 */
	public void setDensity(String density) {
		this.density = density;
	}

	/**
	 * @param last_analyzed
	 *            the last_analyzed to set
	 */
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}

	/**
	 * @param pk
	 *            the pk to set
	 */
	public void setPk(String pk) {
		this.pk = pk;
	}

}
