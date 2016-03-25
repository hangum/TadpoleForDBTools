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
package com.hangum.tadpole.db.metadata;

/**
 * {@link java.sql.DatabaseMetaData} 를 올챙이에 맡게 정의하였습니다.
 * @author hangum
 *
 */
public class TadpoleMetaData {
	/** db major version */
	private int dbMajorVersion = -1;
	/** db minor version */
	private int MinorVersion = -1;

	/**
	 * 이슈정리 : https://github.com/hangum/TadpoleForDBTools/issues/412
	 * 
	 *	mysql, maria, ms sql, sqlite
	 * 공백이 있을 경우 묶는다.(대소문자 상관없음)
	 *
	 *	pg sql, tajo
	 * 대문자 이거나 중간에 공백이 있으면 묶는다.
	 * 
	 *	oracle
	 * 소문자 이거나 중간에 공백이 있으면 묶는다.
	 *
	 *	hive  
	 * 처리하지 않음.
	 *
	 * @author hangum
	 *
	 */
	public static enum STORES_FIELD_TYPE {NONE, BLANK, UPPERCASE_BLANK, LOWCASE_BLANK};
	
	/**
	 * {@link java.sql.DatabaseMetaData#getIdentifierQuoteString()}
	 */
	private String identifierQuoteString = "";
	private STORES_FIELD_TYPE STORE_TYPE = STORES_FIELD_TYPE.NONE;
	

	/**
	 * {@link java.sql.DatabaseMetaData#getSQLKeywords()}
	 */
	private String keywords = "";

	/**
	 * 
	 * @param identifierQuoteString
	 * @param STORE_TYPE
	 */
	public TadpoleMetaData(String identifierQuoteString, STORES_FIELD_TYPE STORE_TYPE) {
		this.identifierQuoteString = identifierQuoteString;
		this.STORE_TYPE = STORE_TYPE;
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

	/**
	 * @return the sTORE_TYPE
	 */
	public final STORES_FIELD_TYPE getSTORE_TYPE() {
		return STORE_TYPE;
	}

	/**
	 * @param sTORE_TYPE the sTORE_TYPE to set
	 */
	public final void setSTORE_TYPE(STORES_FIELD_TYPE sTORE_TYPE) {
		STORE_TYPE = sTORE_TYPE;
	}

	/**
	 * @return the keywords
	 */
	public final String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public final void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the dbMajorVersion
	 */
	public int getDbMajorVersion() {
		return dbMajorVersion;
	}

	/**
	 * @param dbMajorVersion the dbMajorVersion to set
	 */
	public void setDbMajorVersion(int dbMajorVersion) {
		this.dbMajorVersion = dbMajorVersion;
	}

	/**
	 * @return the minorVersion
	 */
	public int getMinorVersion() {
		return MinorVersion;
	}

	/**
	 * @param minorVersion the minorVersion to set
	 */
	public void setMinorVersion(int minorVersion) {
		MinorVersion = minorVersion;
	}
	
}
