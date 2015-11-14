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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.rdb.core.Messages;

/**
 * 테이블뷰의 테이블 수정관련 정의
 * 
 * @author hangum
 *
 */
public class TbUtils {

	/** table modify type */
//	public static enum TABLE_MOD_TYPE {NONE, EDITOR};
	public static String NONE_MSG 	= Messages.get().TbUtils_0;
	public static String EDITOR_MSG = Messages.get().TbUtils_1;
	
	/** column modify type */
	public static enum COLUMN_MOD_TYPE {NONE, INSERT, UPDATE, DELETE};
	/** 컬럼 헤더 */
	public static String getColumnText(COLUMN_MOD_TYPE type) {
		if(type == COLUMN_MOD_TYPE.INSERT) {
			return "<em style='color:rgb(0, 0, 255)'>INSERT</em>"; //$NON-NLS-1$
		} else if(type == COLUMN_MOD_TYPE.UPDATE) {
			return "<em style='color:rgb(0, 0, 255)'>UPDATE</em>"; //$NON-NLS-1$
		} else if(type == COLUMN_MOD_TYPE.DELETE) {
			return "<em style='color:rgb(255, 0, 0)'>DELETE</em>"; //$NON-NLS-1$
		}
		
		return type.toString();
	}
	/** is insert column */
	public static boolean isInsert(String value) {
		return value.indexOf("INSERT") != -1; //$NON-NLS-1$
	}
	/** is update column */
	public static boolean isUpdate(String value) {
		return value.indexOf("UPDATE") != -1; //$NON-NLS-1$
	}
	/** is delete column */
	public static boolean isDelete(String value) {
		return value.indexOf("DELETE") != -1; //$NON-NLS-1$
	}
	
	
	/** 데이터 수정 */
	public static String MODIFY_DATA_START = "<em style='color:rgb(255, 100, 0)'>"; //$NON-NLS-1$
	public static String MODIFY_DATA_END = "</em>"; //$NON-NLS-1$
	public static String MODIFY_DATA = MODIFY_DATA_START + "%s" + MODIFY_DATA_END; //$NON-NLS-1$
	/** 데이터 항목 수정 */
	public static String getModifyData(String value) {
		return String.format(MODIFY_DATA, StringEscapeUtils.escapeXml(value));
	}
	/** is modify data */
	public static boolean isModifyData(String value) {
		return value.indexOf("<em style='color:rgb") != -1; //$NON-NLS-1$
	}
	
	/** 원본 데이터를 가지고온다 */
	public static String getOriginalData(String value) {
		value = StringUtils.replace(value, TbUtils.MODIFY_DATA_START, ""); //$NON-NLS-1$
		value = StringUtils.replace(value, TbUtils.MODIFY_DATA_END, ""); //$NON-NLS-1$
		
		return StringEscapeUtils.unescapeXml(value);
	}

}
