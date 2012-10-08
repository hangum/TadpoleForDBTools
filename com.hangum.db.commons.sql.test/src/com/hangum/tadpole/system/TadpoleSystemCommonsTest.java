/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system;

import com.hangum.tadpole.system.TadpoleSystemCommons;
import com.hangum.tadpole.system.TadpoleSystemConnector;

import junit.framework.TestCase;

/**
 * {@link com.hangum.tadpole.system.TadpoleSystemCommons 시스템쿼리}
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommonsTest extends TestCase {

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystemCommons#executSQL(com.hangum.db.dao.system.UserDBDAO, String) 쿼리실행(select 제외)}executeSQL
	 */
	public void testExecutSQL() {
		try {
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "CREATE TABLE   sample_table_a   (  id INTEGER NOT NULL,   name char(60) default NULL,  PRIMARY KEY (id)  );");
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "INSERT INTO sample_table_a  (id, name)  VALUES  ( 1, '11' ); ");
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "drop table sample_table_a; ");
			
		} catch (Exception e) {
			fail("execute sql " + e.getMessage());
			
			e.printStackTrace();
		}
		
	}

}
