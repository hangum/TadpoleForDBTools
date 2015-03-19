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
package com.hangum.tadpole.engine.query.dao.mysql;

public class ConstraintDAO {
	private String column_name;
	private String constraint_type;
	private String constraint_type_nm;
	private String constraint_name;
	private String search_condition;
	private String status;
	private String ref_table;

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getConstraint_type_nm() {
		return constraint_type_nm;
	}

	public void setConstraint_type_nm(String constraint_type_nm) {
		this.constraint_type_nm = constraint_type_nm;
	}

	public String getConstraint_type() {
		return constraint_type;
	}

	public void setConstraint_type(String constraint_type) {
		this.constraint_type = constraint_type;
	}

	public String getConstraint_name() {
		return constraint_name;
	}

	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}

	public String getSearch_condition() {
		return search_condition;
	}

	public void setSearch_condition(String search_condition) {
		this.search_condition = search_condition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRef_table() {
		return ref_table;
	}

	public void setRef_table(String ref_table) {
		this.ref_table = ref_table;
	}

}
