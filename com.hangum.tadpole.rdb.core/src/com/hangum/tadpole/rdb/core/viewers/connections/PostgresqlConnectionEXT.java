/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.connections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.DBOtherDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO.DB_RESOURCE_TYPE;
import com.hangum.tadpole.engine.query.sql.pgsql.PGSQLSystem;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * postgresql connection ext
 * 
 * @author hangum
 *
 */
public class PostgresqlConnectionEXT {
	private static final Logger logger = Logger.getLogger(PostgresqlConnectionEXT.class);
	
	/**
	 *  extension 
	 */
	public static void connectionext(UserDBDAO userDB) {
		try {
			ResourcesDAO resourcesDAO = new ResourcesDAO(userDB);
			List<DBOtherDAO> listOtherObj = new ArrayList<DBOtherDAO>();
			
			for (Object object : PGSQLSystem.getExtension(userDB)) {
				DBOtherDAO dao = new DBOtherDAO();
				Map<String, String> map = (Map<String, String>)object;
	
				dao.setName(""+map.get("extname"));
				dao.setComment(""+map.get("comment"));
				dao.setUserObject(map);
				dao.setParent(resourcesDAO);
				
				listOtherObj.add(dao);
			}
			resourcesDAO.setType(DB_RESOURCE_TYPE.PG_EXTENSION);
			resourcesDAO.setName(Messages.get().Extensions);
			resourcesDAO.setListResource(listOtherObj);
			userDB.getListResource().add(resourcesDAO);
		} catch(Exception e) {
			logger.error("connection exception", e);
		}
	}
}
