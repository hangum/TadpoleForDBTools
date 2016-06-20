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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AltibaseLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.CubridLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.HiveLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MSSQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MariaDBLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MongoDBLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.MySQLLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.OracleLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.PostgresLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.SQLiteLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.TajoLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.TiberoLoginComposite;

/**
 * DB Connection utils
 * 
 * @author hangum
 *
 */
public class DBConnectionUtils {
	/**
	 * db connection composite
	 * 
	 * @param dbDefine
	 * @param compositeBody
	 * @param listGroupName
	 * @param selGroupName
	 * @param userDB
	 * @return
	 */
	public static AbstractLoginComposite getDBConnection(DBDefine dbDefine, Composite compositeBody, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		return getDBConnection(dbDefine, compositeBody, listGroupName, selGroupName, userDB, PublicTadpoleDefine.DATA_STATUS.NEW);
	}

	/**
	 * db connection composite
	 * 
	 * @param dbDefine
	 * @param compositeBody
	 * @param listGroupName
	 * @param selGroupName
	 * @param userDB
	 * @param dataStatus
	 * 
	 * @return
	 */
	public static AbstractLoginComposite getDBConnection(DBDefine dbDefine, Composite compositeBody, List<String> listGroupName, String selGroupName, UserDBDAO userDB, 
			PublicTadpoleDefine.DATA_STATUS dataStatus) {
		AbstractLoginComposite loginComposite = null;
		
		if (dbDefine == DBDefine.MYSQL_DEFAULT) {
			loginComposite = new MySQLLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.MARIADB_DEFAULT) {	
			loginComposite = new MariaDBLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT) {
			loginComposite = new OracleLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.SQLite_DEFAULT) {
			loginComposite = new SQLiteLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.MSSQL_DEFAULT) {
			loginComposite = new MSSQLLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if (dbDefine == DBDefine.CUBRID_DEFAULT) {
			loginComposite = new CubridLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.POSTGRE_DEFAULT) {
			loginComposite = new PostgresLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.MONGODB_DEFAULT) {
			loginComposite = new MongoDBLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
//		} else if(dbDefine == DBDefine.AMAZONRDS_DEFAULT) {
//			loginComposite = new AWSRDSLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.HIVE_DEFAULT || dbDefine == DBDefine.HIVE2_DEFAULT) {
			loginComposite = new HiveLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.TAJO_DEFAULT) {
			loginComposite = new TajoLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.TIBERO_DEFAULT) {
			loginComposite = new TiberoLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		} else if(dbDefine == DBDefine.ALTIBASE_DEFAULT) {
			loginComposite = new AltibaseLoginComposite(compositeBody, SWT.NONE, listGroupName, selGroupName, userDB);
		}
		
		loginComposite.setDataActionStatus(dataStatus);
		
		return loginComposite;
	}
}
