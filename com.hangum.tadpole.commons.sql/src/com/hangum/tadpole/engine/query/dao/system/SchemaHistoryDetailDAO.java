/*******************************************************************************
 * Copyright (c) 2014 hangum.
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
 * schema_history_detail table DAO
 * 
 * This table is schema_history child.
 * 
 * @author hangum
 *
 */
public class SchemaHistoryDetailDAO {
	int seq;
	int schema_seq;
	String source;

	public SchemaHistoryDetailDAO() {
	}

	/**
	 * @return the seq
	 */
	public final int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public final void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the schema_seq
	 */
	public final int getSchema_seq() {
		return schema_seq;
	}

	/**
	 * @param schema_seq the schema_seq to set
	 */
	public final void setSchema_seq(int schema_seq) {
		this.schema_seq = schema_seq;
	}

	/**
	 * @return the source
	 */
	public final String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public final void setSource(String source) {
		this.source = source;
	}

	
}
