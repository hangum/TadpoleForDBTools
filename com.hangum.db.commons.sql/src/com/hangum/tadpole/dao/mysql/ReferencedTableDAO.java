/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.dao.mysql;

/**
 * 테이블의 참조 테이블 정보를 얻습니다.
 * 현재는 테이블 관계에서 참조 테이블 목록을 표현해 주는 목적으로 사용합니다.
 * (erd 관계 테이블 목록)
 * 
 * @author hangumNote
 *
 */
public class ReferencedTableDAO {
	String constraint_name;
	
	String table_name = "";
	String column_name = "";
	String referenced_table_name = "";
	String referenced_column_name = "";

	public ReferencedTableDAO() {
	}

	public String getTable_name() {
		return table_name;
	}
	
	public String getConstraint_name() {
		return constraint_name;
	}

	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getReferenced_table_name() {
		return referenced_table_name;
	}

	public void setReferenced_table_name(String referenced_table_name) {
		this.referenced_table_name = referenced_table_name;
	}

	public String getReferenced_column_name() {
		return referenced_column_name;
	}

	public void setReferenced_column_name(String referenced_column_name) {
		this.referenced_column_name = referenced_column_name;
	}

	@Override
	public String toString() {
		return "ReferencedTableDAO [constraint_name=" + constraint_name
				+ ", table_name=" + table_name + ", column_name=" + column_name
				+ ", referenced_table_name=" + referenced_table_name
				+ ", referenced_column_name=" + referenced_column_name + "]";
	}
	
	
}
