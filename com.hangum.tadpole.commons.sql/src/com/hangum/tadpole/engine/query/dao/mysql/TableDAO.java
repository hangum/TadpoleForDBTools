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

import java.util.ArrayList;
import java.util.List;

/**
 * table 정보 
 * 
 * @author hangum
 *
 */
public class TableDAO extends StructObjectDAO {
	/** 
	 * 시스템에서 쿼리에 사용할 이름을 정의 .
	 * 보여줄때는 {@link TableDAO#name}을 사용하고, 쿼리를 사용할때는 . 
	 * 
	 * 자세한 사항은 https://github.com/hangum/TadpoleForDBTools/issues/412 를 참고합니다.
	 */
	String sysName = "";
	
	String name;
	String comment="";
	
	/* postgresql, MSSQL Server schema support */
	String table_name = "";
	
	/** hive */
	String tab_name = "";

	/** mongoDB */
	long rows = 0l;
	long size = 0l;
	
	/** table columns */
	List<TableColumnDAO> listColumn = new ArrayList<TableColumnDAO>();
	
	public TableDAO() {
	}
	
	public TableDAO(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the table_name
	 */
	public String getTable_name() {
		return table_name;
	}

	/**
	 * @param table_name the table_name to set
	 */
	public void setTable_name(String table_name) {
		this.table_name = table_name;
		setName(table_name);
	}

	public String getComment() {
		return comment == null ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getTab_name() {
		return tab_name;
	}
	
	public void setTab_name(String tab_name) {
		this.tab_name = tab_name;
		setName(tab_name);
	}

	public long getRows() {
		return rows;
	}

	public void setRows(long rows) {
		this.rows = rows;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the sysName
	 */
	public final String getSysName() {
		return sysName;
	}

	/**
	 * @param sysName the sysName to set
	 */
	public final void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TableDAO) {
			TableDAO userDB = (TableDAO)obj;
			return userDB.getName() == getName();
		}
		
		return super.equals(obj);
	}

	/**
	 * @return the listColumn
	 */
	public List<TableColumnDAO> getListColumn() {
		return listColumn;
	}

	/**
	 * @param listColumn the listColumn to set
	 */
	public void setListColumn(List<TableColumnDAO> listColumn) {
		this.listColumn = listColumn;
	}
}
