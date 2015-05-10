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
package com.hangum.tadpole.engine.query.dao;

/**
 * 올챙이에서 사용하는 디비 정보를 정의합니다.
 * 
 * @author hangum
 *
 */
public class DBInfoDAO {
	String productversion;
	String productlevel;
	String edition;
	
	public DBInfoDAO() {
	}

	public String getProductversion() {
		return productversion;
	}
	
	public void setProductversion(String productversion) {
		this.productversion = productversion;
	}

	public String getProductlevel() {
		return productlevel;
	}

	public void setProductlevel(String productlevel) {
		this.productlevel = productlevel;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	@Override
	public String toString() {
		return "DBInfoDAO [productversion=" + productversion
				+ ", productlevel=" + productlevel + ", edition=" + edition
				+ "]";
	}
	
	
}
