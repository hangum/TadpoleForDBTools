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
package com.hangum.tadpole.sql.dao.rdb;

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

public class RDBInfomationforColumnDAO {

	private String table_name;
	private String table_comment;
	private String column_name;
	private String nullable;
	private String data_type;
	private String data_default;
	private String column_comment;
	private String data_type_mod;
	private String char_used;
	private String histogram;
	private String num_distinct;
	private String num_nulls;
	private String density;
	private String last_analyzed;
	private String pk;

	/**
	 * 
	 */
	public RDBInfomationforColumnDAO() {
		this("", "", "", "", "", "", "", "");
	}

	public RDBInfomationforColumnDAO(String table_name, String table_comment, String column_name, String column_comment, String data_type, String notnull, String dflt_value, String pk) {
		this.table_name = table_name;
		this.table_comment = table_comment;
		this.column_name = column_name;
		this.column_comment = column_comment;
		this.data_type = data_type;
		this.nullable = notnull;
		this.data_default = dflt_value;
		this.setPk(pk);
	}

	public int compareToIgnoreCase(RDBInfomationforColumnDAO target, String column) {

		String value1 = "";
		String value2 = "";
		if (column != null && !"".equals(column)) {

			value1 = (String) this.getColumnValuebyName(column);
			value2 = (String) target.getColumnValuebyName(column);
		}
		return value1.compareToIgnoreCase(value2);

	}

	public String getColumnValuebyName(String column) {
		String result = "";
		if ("table_name".equals(column.toLowerCase())) {
			result = this.getTable_name();
		} else if ("table_comment".equals(column.toLowerCase())) {
			result = this.getTable_comment();
		} else if ("column_name".equals(column.toLowerCase())) {
			result = this.getColumn_name();
		} else if ("nullable".equals(column.toLowerCase())) {
			result = this.getNullable();
		} else if ("data_type".equals(column.toLowerCase())) {
			result = this.getData_type();
		} else if ("data_default".equals(column.toLowerCase())) {
			result = this.getData_default();
		} else if ("column_comment".equals(column.toLowerCase())) {
			result = this.getColumn_comment();
		} else if ("data_type_mod".equals(column.toLowerCase())) {
			result = this.getData_type_mod();
		} else if ("char_used".equals(column.toLowerCase())) {
			result = this.getChar_used();
		} else if ("histogram".equals(column.toLowerCase())) {
			result = this.getHistogram();
		} else if ("num_distinct".equals(column.toLowerCase())) {
			result = this.getNum_distinct();
		} else if ("num_nulls".equals(column.toLowerCase())) {
			result = this.getNum_nulls();
		} else if ("density".equals(column.toLowerCase())) {
			result = this.getDensity();
		} else if ("last_analyzed".equals(column.toLowerCase())) {
			result = this.getLast_analyzed();
		} else if ("pk".equals(column.toLowerCase())) {
			result = this.pk.toLowerCase();
		} else {
			result = "";
		}
		return result == null ? "" : result;
	}

	/**
	 * @return the table_name
	 */
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @param table_name
	 *            the table_name to set
	 */
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	/**
	 * @return the table_comment
	 */
	public String getTable_comment() {
		return table_comment;
	}

	/**
	 * @param table_comment
	 *            the table_comment to set
	 */
	public void setTable_comment(String table_comment) {
		this.table_comment = table_comment;
	}

	/**
	 * @return the column_name
	 */
	public String getColumn_name() {
		return column_name;
	}

	/**
	 * @param column_name
	 *            the column_name to set
	 */
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	/**
	 * @return the nullable
	 */
	public String getNullable() {
		return nullable;
	}

	/**
	 * @param nullable
	 *            the nullable to set
	 */
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return the data_type
	 */
	public String getData_type() {
		return data_type;
	}

	/**
	 * @param data_type
	 *            the data_type to set
	 */
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	/**
	 * @return the data_default
	 */
	public String getData_default() {
		return data_default;
	}

	/**
	 * @param data_default
	 *            the data_default to set
	 */
	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	/**
	 * @return the column_comment
	 */
	public String getColumn_comment() {
		return column_comment;
	}

	/**
	 * @param column_comment
	 *            the column_comment to set
	 */
	public void setColumn_comment(String column_comment) {
		this.column_comment = column_comment;
	}

	/**
	 * @return the data_type_mod
	 */
	public String getData_type_mod() {
		return data_type_mod;
	}

	/**
	 * @param data_type_mod
	 *            the data_type_mod to set
	 */
	public void setData_type_mod(String data_type_mod) {
		this.data_type_mod = data_type_mod;
	}

	/**
	 * @return the char_used
	 */
	public String getChar_used() {
		return char_used;
	}

	/**
	 * @param char_used
	 *            the char_used to set
	 */
	public void setChar_used(String char_used) {
		this.char_used = char_used;
	}

	/**
	 * @return the histogram
	 */
	public String getHistogram() {
		return histogram;
	}

	/**
	 * @param histogram
	 *            the histogram to set
	 */
	public void setHistogram(String histogram) {
		this.histogram = histogram;
	}

	/**
	 * @return the num_distinct
	 */
	public String getNum_distinct() {
		return num_distinct;
	}

	/**
	 * @param num_distinct
	 *            the num_distinct to set
	 */
	public void setNum_distinct(String num_distinct) {
		this.num_distinct = num_distinct;
	}

	/**
	 * @return the num_nulls
	 */
	public String getNum_nulls() {
		return num_nulls;
	}

	/**
	 * @param num_nulls
	 *            the num_nulls to set
	 */
	public void setNum_nulls(String num_nulls) {
		this.num_nulls = num_nulls;
	}

	/**
	 * @return the density
	 */
	public String getDensity() {
		return density;
	}

	/**
	 * @param density
	 *            the density to set
	 */
	public void setDensity(String density) {
		this.density = density;
	}

	/**
	 * @return the last_analyzed
	 */
	public String getLast_analyzed() {
		return last_analyzed;
	}

	/**
	 * @param last_analyzed
	 *            the last_analyzed to set
	 */
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

}
