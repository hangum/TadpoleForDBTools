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
package com.hangum.tadpole.importexport.core.utils.mongodb;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * ? to mongodb database import
 * 
 * @author hangum
 *
 */
public abstract class DBImport {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DBImport.class);
	
	protected UserDBDAO targetUserDB;
	protected UserDBDAO sourceUserDB;
	
	public DBImport(UserDBDAO sourceUserDB, UserDBDAO targetUserDB) {
		this.sourceUserDB = sourceUserDB;
		this.targetUserDB = targetUserDB;
	}

	public abstract Job workTableImport();

	/**
	 * @return the targetUserDB
	 */
	public UserDBDAO getTargetUserDB() {
		return targetUserDB;
	}

	/**
	 * @param targetUserDB the targetUserDB to set
	 */
	public void setTargetUserDB(UserDBDAO targetUserDB) {
		this.targetUserDB = targetUserDB;
	}

	/**
	 * @return the sourceUserDB
	 */
	public UserDBDAO getSourceUserDB() {
		return sourceUserDB;
	}

	/**
	 * @param sourceUserDB the sourceUserDB to set
	 */
	public void setSourceUserDB(UserDBDAO sourceUserDB) {
		this.sourceUserDB = sourceUserDB;
	}
	
	
}
