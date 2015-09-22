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
package com.hangum.tadpole.commons.dialogs.message.dao;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * system message dao
 * 
 * @author hangum
 *
 */
public class TadpoleMessageDAO  {
	
	/** Start execute query time */
	private Date dateExecute;
	
	/** sql text */
	private String strMessage;
	
	/**
	 * If rise exception
	 */
	private Throwable throwable;

	public TadpoleMessageDAO(Date dateExecute, String strMessage, Throwable throwable) {
		this.dateExecute = dateExecute;
		setStrMessage( strMessage );
		this.throwable = throwable;
	}

	public Date getDateExecute() {
		return dateExecute;
	}

	public void setDateExecute(Date dateExecute) {
		this.dateExecute = dateExecute;
	}

	public String getStrMessage() {
		return strMessage;
	}

	public void setStrMessage(String strMessage) {
		this.strMessage = strMessage;
	}

	/**
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}	
}
