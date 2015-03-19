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
package com.hangum.tadpole.engine.query.dao.system;

/**
 * UserDBDAO to Decripty DAO
 * 
 * @author hangum
 *
 */
public class UserDBOriginalDAO extends UserDBDAO {

	/**
	 * decryption url
	 * 
	 * @return
	 */
	@Override
	public String getUrl() {
		return url;
	}
	
	/**
	 * decryption db
	 * @return
	 */
	@Override
	public String getDb() {
		return db;
	}
	
	/**
	 * decryption host
	 * 
	 * @return
	 */
	@Override
	public String getHost() {
		return host;
	}
	
	/**
	 * decryption port
	 * 
	 * @return
	 */
	@Override
	public String getPort() {
		return port;
	}
	
	/**
	 * decryption user
	 * 
	 * @return
	 */
	@Override
	public String getUsers() {
		return users;
	}
	
	/**
	 * decryption passwd
	 * 
	 * @return
	 */
	@Override
	public String getPasswd() {
		return passwd;
	}
}
