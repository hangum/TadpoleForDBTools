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
package com.hangum.tadpole.rdb.core.editors.main.utils;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * SQLDefine
 * 
 * @author hangum
 *
 */
public class UserPreference {

	/**
	 * query delimiter
	 */
	public static final String QUERY_DELIMITER = ";";
	public static String EXPORT_DEMILITER = GetPreferenceGeneral.getExportDelimit().equalsIgnoreCase("tab")?"	":GetPreferenceGeneral.getExportDelimit() + " "; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * 사용자 email
	 */
	private final String userEMail 		= SessionManager.getEMAIL();
	
	/**
	 * 사용자 seq
	 */
	private final int userSeq 			= SessionManager.getSeq();
	
	/** 
	 * 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. 
	 */
	private final int queryResultCount 	= GetPreferenceGeneral.getQueryResultCount();
	
	/** 
	 * 쿼리 결과를 page당 처리 하는 카운트
	 */
	private final int queryPageCount 	= GetPreferenceGeneral.getPageCount();

	/** 
	 * oracle plan table 이름 
	 */
	private final String planTableName 	= GetPreferenceGeneral.getPlanTableName();

	/** 
	 * 결과 컬럼이 숫자이면 ,를 찍을 것인지 
	 */
	private final boolean isResultComma = GetPreferenceGeneral.getISRDBNumberIsComma();

	/**
	 * @return the userEMail
	 */
	public final String getUserEMail() {
		return userEMail;
	}

	/**
	 * @return the userSeq
	 */
	public final int getUserSeq() {
		return userSeq;
	}

	/**
	 * @return the queryResultCount
	 */
	public final int getQueryResultCount() {
		return queryResultCount;
	}

	/**
	 * @return the queryPageCount
	 */
	public final int getQueryPageCount() {
		return queryPageCount;
	}

	/**
	 * @return the planTableName
	 */
	public final String getPlanTableName() {
		return planTableName;
	}

	/**
	 * @return the isResultComma
	 */
	public final boolean isResultComma() {
		return isResultComma;
	}
}
