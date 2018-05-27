/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * text export dao
 * 
 * @author hangum
 *
 */
public class ExportSqlDAO extends AbstractExportDAO {
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
