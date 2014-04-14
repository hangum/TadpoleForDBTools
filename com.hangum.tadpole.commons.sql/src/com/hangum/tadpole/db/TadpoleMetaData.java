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
package com.hangum.tadpole.db;

/**
 * {@link java.sql.DatabaseMetaData} 를 올챙이에 맡게 정의하였습니다.
 * @author hangum
 *
 */
public class TadpoleMetaData {

	/**
	 * 이슈정리 : https://github.com/hangum/TadpoleForDBTools/issues/412
	 * 
	 *	ms sql, sqlite
	 * 공백이 있을 경우 묶는다.(대소문자 상관없음)
	 *
	 *	pg sql, tajo
	 * 대문자 이거나 중간에 공백이 있으면 묶는다.
	 * 
	 *	oracle
	 * 소문자 이거나 중간에 공백이 있으면 묶는다.
	 *
	 *	mysql, maria, hive  
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
}
