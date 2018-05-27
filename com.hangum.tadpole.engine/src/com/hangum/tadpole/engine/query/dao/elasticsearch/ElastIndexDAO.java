/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.elasticsearch;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;

/**
 * Elasticsearch index dao
 * 
 * Elasticsearch v6.2
 * 
 * @author hangum
 *
 */
public class ElastIndexDAO extends AbstractDAO {
	String health;
	String status;
	String index;
	String uuid;
	String pri;
	String rep;
	String docs_count;
	String docs_deleted;
	String store_size;
	String pri_store_size;
	
	public ElastIndexDAO() {
	}

	/**
	 * @return the health
	 */
	public String getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(String health) {
		this.health = health;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the pri
	 */
	public String getPri() {
		return pri;
	}

	/**
	 * @param pri the pri to set
	 */
	public void setPri(String pri) {
		this.pri = pri;
	}

	/**
	 * @return the rep
	 */
	public String getRep() {
		return rep;
	}

	/**
	 * @param rep the rep to set
	 */
	public void setRep(String rep) {
		this.rep = rep;
	}

	/**
	 * @return the docs_count
	 */
	public String getDocs_count() {
		return docs_count;
	}

	/**
	 * @param docs_count the docs_count to set
	 */
	public void setDocs_count(String docs_count) {
		this.docs_count = docs_count;
	}

	/**
	 * @return the docs_deleted
	 */
	public String getDocs_deleted() {
		return docs_deleted;
	}

	/**
	 * @param docs_deleted the docs_deleted to set
	 */
	public void setDocs_deleted(String docs_deleted) {
		this.docs_deleted = docs_deleted;
	}

	/**
	 * @return the store_size
	 */
	public String getStore_size() {
		return store_size;
	}

	/**
	 * @param store_size the store_size to set
	 */
	public void setStore_size(String store_size) {
		this.store_size = store_size;
	}

	/**
	 * @return the pri_store_size
	 */
	public String getPri_store_size() {
		return pri_store_size;
	}

	/**
	 * @param pri_store_size the pri_store_size to set
	 */
	public void setPri_store_size(String pri_store_size) {
		this.pri_store_size = pri_store_size;
	}

	@Override
	public String getFullName() {
		return index;
	}
	
}
