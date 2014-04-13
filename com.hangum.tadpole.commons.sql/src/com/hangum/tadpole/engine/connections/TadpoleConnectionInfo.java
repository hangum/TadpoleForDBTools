/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.connections;

/**
 * 올챙이에서 사용하려는 connection 정보를 추가로 가지고 있습니다. 
 * @author hangum
 *
 */
public class TadpoleConnectionInfo {

	/**
	 * {@link java.sql.DatabaseMetaData#getIdentifierQuoteString()}
	 */
	private String identifierQuoteString = "";
	
	public TadpoleConnectionInfo() {
	}

	/**
	 * @return the identifierQuoteString
	 */
	public final String getIdentifierQuoteString() {
		return identifierQuoteString;
	}

	/**
	 * @param identifierQuoteString the identifierQuoteString to set
	 */
	public final void setIdentifierQuoteString(String identifierQuoteString) {
		this.identifierQuoteString = identifierQuoteString;
	}
	
}
