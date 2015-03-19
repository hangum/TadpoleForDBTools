/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.sqlite;

public class SQLiteForeignKeyListDAO {

	/**
	 * PRAGMA foreign_key_list('taget_tablename');
	 */

	long id;
	int seq;
	String table; // reference table name
	String from;  // target column
	String to;    // reference column
	String on_update; // update action
	String on_delete; // delete action
	String match;

	public SQLiteForeignKeyListDAO() {
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @return the on_update
	 */
	public String getOn_update() {
		return on_update;
	}

	/**
	 * @return the on_delete
	 */
	public String getOn_delete() {
		return on_delete;
	}

	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param seq
	 *            the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @param on_update
	 *            the on_update to set
	 */
	public void setOn_update(String on_update) {
		this.on_update = on_update;
	}

	/**
	 * @param on_delete
	 *            the on_delete to set
	 */
	public void setOn_delete(String on_delete) {
		this.on_delete = on_delete;
	}

	/**
	 * @param match
	 *            the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}
}
