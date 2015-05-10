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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultLabelProvider;

/**
 * SQLResult의 TableEditorLabelProvider
 * 
 * @author hangum
 *
 */
public class TableEditorLabelProvider extends SQLResultLabelProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableEditorLabelProvider.class);
	private UserDBDAO userDB;
	
	public TableEditorLabelProvider(UserDBDAO userDB) {
		this.userDB = userDB;
	}
	
	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		
		// 오라클, PGSQL인 경우 1 번째 컬럼은 업데이트를 위해 ROWID, CID를 조회 하여서 보내주지 않도록 하였다. 
		int intShowColIndex = columnIndex;
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			if(columnIndex != 0) intShowColIndex++;
		}
		
		Object obj = rsResult.get(intShowColIndex);
		return obj == null ? "" : obj.toString();
	}
	
}
