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
package com.hangum.tadpole.system;

import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;

import junit.framework.TestCase;

/**
 * {@link com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand 시스템쿼리}
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommonsTest extends TestCase {

	/**
	 * {@link com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand#executSQL(com.hangum.db.dao.system.UserDBDAO, String) 쿼리실행(select 제외)}executeSQL
	 */
	public void testExecutSQL() {
		try {
			
			ExecuteDDLCommand.executSQL(TadpoleSystemInitializer.getUserDB(), "CREATE TABLE   sample_table_a   (  id INTEGER NOT NULL,   name char(60) default NULL,  PRIMARY KEY (id)  );");
			
			ExecuteDDLCommand.executSQL(TadpoleSystemInitializer.getUserDB(), "INSERT INTO sample_table_a  (id, name)  VALUES  ( 1, '11' ); ");
			
			ExecuteDDLCommand.executSQL(TadpoleSystemInitializer.getUserDB(), "drop table sample_table_a; ");
			
		} catch (Exception e) {
			fail("execute sql " + e.getMessage());
			
			e.printStackTrace();
		}
		
	}

}
