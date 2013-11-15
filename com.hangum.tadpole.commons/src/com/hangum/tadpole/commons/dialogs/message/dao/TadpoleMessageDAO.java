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

/**
 * system message dao
 * 
 * @author hangum
 * 
 */
public class TadpoleMessageDAO {

	/** Start execute query time */
	Date dateExecute;
	/** sql text */
	String strMessage;

	public TadpoleMessageDAO(Date dateExecute, String strMessage) {
		this.dateExecute = dateExecute;
		this.strMessage = strMessage;
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

}
