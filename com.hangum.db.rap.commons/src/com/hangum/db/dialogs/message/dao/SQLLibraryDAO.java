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
package com.hangum.db.dialogs.message.dao;


/**
 * query history
 * 
 * @author hangum
 * 
 */
public class SQLLibraryDAO {
	// column
	private long seq = -1;
	private String types = "USER";
	private long user_seq = -1;
	private String db_type = "";
	private String title = "";
	private String SQLText = "";
	private String description = "";
	private String delyn = "NO";

	// additional info
	private boolean isModify = false;
	private long seqNo;

	public SQLLibraryDAO() {
	}

	public SQLLibraryDAO(int index) {
		this.seqNo = index;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		setModify(this.seq != seq);
		this.seq = seq;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		setModify(!this.types.equals(types));
		this.types = types;
	}

	public long getUser_seq() {
		return user_seq;
	}

	public void setUser_seq(long user_seq) {
		setModify(this.user_seq != user_seq);
		this.user_seq = user_seq;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		setModify(!this.title.equals(title));
		this.title = title;
	}

	public String getDb_type() {
		return db_type;
	}

	public void setDb_type(String db_type) {
		setModify(!this.db_type.equals(db_type));
		this.db_type = db_type;
	}

	public String getSQLText() {
		return SQLText;
	}

	public void setSqltext(String sqltext) {
		setModify(!this.SQLText.equals(sqltext));
		this.SQLText = sqltext;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		setModify(!this.description.equals(description));
		this.description = description;
	}

	public String getDelyn() {
		return delyn;
	}

	public void setDelyn(String delyn) {
		setModify(!this.delyn.equals(delyn));
		this.delyn = delyn;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}

}
