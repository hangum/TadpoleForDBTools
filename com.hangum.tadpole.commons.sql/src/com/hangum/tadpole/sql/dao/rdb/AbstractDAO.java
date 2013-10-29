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
package com.hangum.tadpole.sql.dao.rdb;

import java.util.List;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.sql.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.mysql.TriggerDAO;
import com.hangum.tadpole.sql.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * 
 * 
 * @author nilriri
 * 
 */
public abstract class AbstractDAO {

	/**
	 * 대소 문자를 구분없이 컬럼값을 비교.
	 * 
	 * @param target
	 * @param column
	 * @return
	 */
	public abstract int compareToIgnoreCase(AbstractDAO target, String column);

	/**
	 * 컬럼명을 이용하여 컬러명과 동일한 멤버 변수값을 리턴한다.
	 * 
	 * @param column
	 * @return
	 */
	public abstract String getColumnValuebyName(String column);

}
