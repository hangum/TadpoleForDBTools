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
package com.hangum.tadpole.rdb.core.dialog.dml;

import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;

/**
 * DMLGenerae Stateme TableColumnDAO
 * 
 * @author nilriri
 *
 */
public class ExtendTableColumnDAO extends TableColumnDAO {
	private String tableAlias = "";
	private String columnAlias = "";
	private boolean isCheck = false;

	public ExtendTableColumnDAO(String name, String type, String index, String table) {
		super(name, type, index);
		this.tableAlias = table;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getColumnAlias() {
		return columnAlias == null || columnAlias.length() == 0 ? this.getSysName().toLowerCase() : columnAlias.toLowerCase();
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias.toLowerCase();
	}

	public String getTableAlias() {
		return tableAlias == null || tableAlias.length() == 0 ? "" : tableAlias.toLowerCase();
	}

	public String getColumnNamebyTableAlias() {
		return tableAlias == null || tableAlias.length() == 0 ? this.getSysName(): tableAlias + "." + this.getField() ;
	}
	
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias.trim().toLowerCase();
	}

}
