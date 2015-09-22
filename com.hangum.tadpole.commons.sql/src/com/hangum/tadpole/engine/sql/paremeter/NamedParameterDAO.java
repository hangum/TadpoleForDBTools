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
package com.hangum.tadpole.engine.sql.paremeter;

import java.util.ArrayList;
import java.util.List;

public class NamedParameterDAO {
	String strSQL = "";
	List<Object> listParam = new ArrayList<Object>();
	
	public NamedParameterDAO() {
	}

	public String getStrSQL() {
		return strSQL;
	}

	public void setStrSQL(String strSQL) {
		this.strSQL = strSQL;
	}

	public List<Object> getListParam() {
		return listParam;
	}

	public void setListParam(List<Object> listParam) {
		this.listParam = listParam;
	}
	
}
