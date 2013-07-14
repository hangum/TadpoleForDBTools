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
package com.hangum.tadpole.rdb.core.editors.objects.table.scripts.types;


/**
 * RDB variable type
 * 
 * @author hangum
 *
 */
public class InOutParameter {
	/** input order */
	int order;
	/** rdb type */
	String rdbType;
	/** java type*/
	String javaType;
	/** variable name */
	String name;
	
	public InOutParameter(int order, String rdbType, String name) {
		this.order = order;
		this.rdbType = rdbType;
		this.name = name;
	}
	

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}



	/**
	 * @param order the order to set
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
	 * @param rdbType the rdbType to set
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
	 * @param name the name to set
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
	 * @param javaType the javaType to set
	 */
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

}
