package com.hangum.tadpole.rdb.core.dialog.dml;

import com.hangum.tadpole.dao.mysql.TableColumnDAO;

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
		return columnAlias == null || columnAlias.length() == 0 ? this.getField().toLowerCase() : columnAlias.toLowerCase();
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias.toLowerCase();
	}

	public String getTableAlias() {
		return tableAlias == null || tableAlias.length() == 0 ? "" : tableAlias.toLowerCase();
	}

	public String getColumnNamebyTableAlias() {
		return tableAlias == null || tableAlias.length() == 0 ? this.getField(): tableAlias + "." + this.getField() ;
	}
	
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias.trim().toLowerCase();
	}

}
