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
package com.hangum.tadpole.engine.query.dao.mysql;

public class InformationSchemaDAO extends StructObjectDAO {
	String TABLE_CATALOG;			
	String TABLE_SCHEMA;			
	String TABLE_NAME;			
	String NON_UNIQUE;	
	String INDEX_SCHEMA;			
	String INDEX_NAME;			
	String SEQ_IN_INDEX;	
	String COLUMN_NAME;			
	String COLLATION;			
	String CARDINALITY;			
	String SUB_PART;			
	String PACKED;			
	String NULLABLE;			
	String INDEX_TYPE;			
	String COMMENT;
	
	public InformationSchemaDAO() {
	}

	public String getTABLE_CATALOG() {
		return TABLE_CATALOG;
	}

	public void setTABLE_CATALOG(String tABLE_CATALOG) {
		TABLE_CATALOG = tABLE_CATALOG;
	}

	public String getTABLE_SCHEMA() {
		return TABLE_SCHEMA;
	}

	public void setTABLE_SCHEMA(String tABLE_SCHEMA) {
		TABLE_SCHEMA = tABLE_SCHEMA;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}

	public String getNON_UNIQUE() {
		return NON_UNIQUE;
	}

	public void setNON_UNIQUE(String nON_UNIQUE) {
		NON_UNIQUE = nON_UNIQUE;
	}

	public String getINDEX_SCHEMA() {
		return INDEX_SCHEMA;
	}

	public void setINDEX_SCHEMA(String iNDEX_SCHEMA) {
		INDEX_SCHEMA = iNDEX_SCHEMA;
	}

	public String getINDEX_NAME() {
		return INDEX_NAME;
	}

	public void setINDEX_NAME(String iNDEX_NAME) {
		INDEX_NAME = iNDEX_NAME;
	}

	public String getSEQ_IN_INDEX() {
		return SEQ_IN_INDEX;
	}

	public void setSEQ_IN_INDEX(String sEQ_IN_INDEX) {
		SEQ_IN_INDEX = sEQ_IN_INDEX;
	}

	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}

	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}

	public String getCOLLATION() {
		return COLLATION;
	}

	public void setCOLLATION(String cOLLATION) {
		COLLATION = cOLLATION;
	}

	public String getCARDINALITY() {
		return CARDINALITY;
	}

	public void setCARDINALITY(String cARDINALITY) {
		CARDINALITY = cARDINALITY;
	}

	public String getSUB_PART() {
		return SUB_PART;
	}

	public void setSUB_PART(String sUB_PART) {
		SUB_PART = sUB_PART;
	}

	public String getPACKED() {
		return PACKED;
	}

	public void setPACKED(String pACKED) {
		PACKED = pACKED;
	}

	public String getNULLABLE() {
		return NULLABLE;
	}

	public void setNULLABLE(String nULLABLE) {
		NULLABLE = nULLABLE;
	}

	public String getINDEX_TYPE() {
		return INDEX_TYPE;
	}

	public void setINDEX_TYPE(String iNDEX_TYPE) {
		INDEX_TYPE = iNDEX_TYPE;
	}

	public String getCOMMENT() {
		return COMMENT;
	}

	public void setCOMMENT(String cOMMENT) {
		COMMENT = cOMMENT;
	}
	
	
}
