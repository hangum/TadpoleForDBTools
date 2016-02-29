/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system.userdb;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;

/**
 * Define Tadpole DB Hub db.
 * 
 * @author hangum
 *
 */
public class TDBDBDAO extends ManagerListViewDAO {
	/** This variable is schema assist */
	protected String schemaListSeparator = "";
	/** This variable is content assist */
	protected String tableListSeparator = null;
	/** This variable is content assit */
	protected String viewListSeparator = null;
	protected String functionLisstSeparator = null;
	
	/** table list */
	protected List<TableDAO> listTable = new ArrayList<TableDAO>();
	
	public TDBDBDAO() {
	}
	
	/**
	 * @return the schemaListSeparator
	 */
	public String getSchemaListSeparator() {
		return schemaListSeparator;
	}

	/**
	 * @param schemaListSeparator the schemaListSeparator to set
	 */
	public void setSchemaListSeparator(String schemaListSeparator) {
		this.schemaListSeparator = schemaListSeparator;
	}

	/**
	 * @return the viewListSeparator
	 */
	public String getViewListSeparator() {
		return viewListSeparator;
	}

	/**
	 * @param viewListSeparator the viewListSeparator to set
	 */
	public void setViewListSeparator(String viewListSeparator) {
		this.viewListSeparator = viewListSeparator;
	}

	/**
	 * @return the tableListSeparator
	 */
	public String getTableListSeparator() {
		return tableListSeparator;
	}

	/**
	 * @param tableListSeparator the tableListSeparator to set
	 */
	public void setTableListSeparator(String tableListSeparator) {
		this.tableListSeparator = tableListSeparator;
	}

	/**
	 * @return the listTable
	 */
	public List<TableDAO> getListTable() {
		return listTable;
	}

	/**
	 * @param listTable the listTable to set
	 */
	public void setListTable(List<TableDAO> listTable) {
		this.listTable = listTable;
	}

	/**
	 * @return the functionLisstSeparator
	 */
	public String getFunctionLisstSeparator() {
		return functionLisstSeparator;
	}

	/**
	 * @param functionLisstSeparator the functionLisstSeparator to set
	 */
	public void setFunctionLisstSeparator(String functionLisstSeparator) {
		this.functionLisstSeparator = functionLisstSeparator;
	}
	
}
