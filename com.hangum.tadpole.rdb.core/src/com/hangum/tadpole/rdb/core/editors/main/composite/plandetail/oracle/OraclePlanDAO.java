package com.hangum.tadpole.rdb.core.editors.main.composite.plandetail.oracle;

import java.util.ArrayList;
import java.util.List;

public class OraclePlanDAO {
	String level;
	String query;
	String cost;
	String rows;
	String bytes;
	int pos;
	
	List<OraclePlanDAO> listChildren = new ArrayList<>();

	public OraclePlanDAO() {
	}

	
	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public int getPos() {
		return pos;
	}


	public void setPos(int pos) {
		this.pos = pos;
	}


	public List<OraclePlanDAO> getListChildren() {
		return listChildren;
	}

	public void setListChildren(List<OraclePlanDAO> listChildren) {
		this.listChildren = listChildren;
	}

}
