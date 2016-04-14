package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * text export dao
 * 
 * @author hangum
 *
 */
public class ExportSqlDAO extends AExportDAO {
	protected String statementType;
	protected List<String> listWhere = new ArrayList<>();
	protected int commit;
	
	
	public ExportSqlDAO() {
		super();
	}

	/**
	 * @return the separatorType
	 */
	public String getStatementType() {
		return statementType;
	}

	/**
	 * @param separatorType the separatorType to set
	 */
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}
	
	public List<String> getListWhere() {
		return listWhere;
	}

	public void setListWhere(List<String> listWhere) {
		this.listWhere.clear();
		this.listWhere = listWhere;
	}

	public int getCommit() {
		return commit;
	}

	public void setCommit(int commit) {
		this.commit = commit;
	}
	
}
