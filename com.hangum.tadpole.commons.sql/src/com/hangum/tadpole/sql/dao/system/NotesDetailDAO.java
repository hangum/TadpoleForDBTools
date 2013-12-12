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
package com.hangum.tadpole.sql.dao.system;

/**
 * notes_detail dao
 * 
 * @author hangum
 *
 */
public class NotesDetailDAO {
	int seq;
	int note_seq;
	String data;
	
	public NotesDetailDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getNote_seq() {
		return note_seq;
	}

	public void setNote_seq(int note_seq) {
		this.note_seq = note_seq;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
