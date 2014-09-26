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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * system message dao
 * 
 * @author hangum
 *
 */
public class TadpoleMessageDAO  {
	
	/** Start execute query time */
	Date dateExecute;
	/** sql text */
	String strMessage;
	
	/** table 에 보여줄때 html markup형식이 되기 위해 라이피드 문자를 <br/>로 변경해서 저장합니다 */
	String strViewMessage;

	public TadpoleMessageDAO(Date dateExecute, String strMessage) {
		setDateExecute( dateExecute );
		setStrMessage( strMessage );
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
		setStrViewMessage( StringUtils.replace(strMessage, PublicTadpoleDefine.LINE_SEPARATOR, "<br />") );
	}

	/**
	 * @return the strViewMessage
	 */
	public String getStrViewMessage() {
		return strViewMessage;
	}

	/**
	 * @param strViewMessage the strViewMessage to set
	 */
	public void setStrViewMessage(String strViewMessage) {
		this.strViewMessage = strViewMessage;
	}
	
	
	
	
}
