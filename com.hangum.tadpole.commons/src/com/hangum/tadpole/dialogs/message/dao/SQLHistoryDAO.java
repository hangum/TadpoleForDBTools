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
package com.hangum.tadpole.dialogs.message.dao;

import java.util.Date;

/**
 * query history
 * 
 * @author hangum
 *
 */
public class SQLHistoryDAO {
	/** 실행시간*/
	Date dateExecute;
	/** sql text */
	String strSQLText;
	
	public SQLHistoryDAO(Date dateExecute, String strSQLText) {
		this.dateExecute = dateExecute;
		this.strSQLText = strSQLText;		
	}

	public Date getDateExecute() {
		return dateExecute;
	}

	public void setDateExecute(Date dateExecute) {
		this.dateExecute = dateExecute;
	}

	public String getStrSQLText() {
		return strSQLText;
	}

	public void setStrSQLText(String strSQLText) {
		this.strSQLText = strSQLText;
	}
	
	
}
