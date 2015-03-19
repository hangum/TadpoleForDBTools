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
package com.hangum.tadpole.engine.query.dao.rdb;

/**
 * RDB variable type
 * 
 * @author hangum
 * 
 */
public class InOutParameterDAO {
	/** input order */
	int order;

	/** rdb type */
	String rdbType;

	/** java type */
	String javaType;

	/** variable name */
	String name;

	/** parameter value length */
	String length;
	
	/** parameter value */
	String value = "";
	
	/** parameter in, out, inout type */
	String type;

	public InOutParameterDAO() {
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the rdbType
	 */
	public String getRdbType() {
		return rdbType;
	}

	/**
	 * @param rdbType
	 *            the rdbType to set
	 */
	public void setRdbType(String rdbType) {
		this.rdbType = rdbType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the javaType
	 */
	public String getJavaType() {
		return javaType;
	}

	/**
	 * @param javaType
	 *            the javaType to set
	 */
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getLength() {
		return this.length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getValue() {
		return this.value == null ? "" : this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
