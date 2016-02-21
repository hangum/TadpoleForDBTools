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

import java.lang.reflect.Method;

import com.hangum.tadpole.engine.query.dao.mysql.StructObjectDAO;


/**
 * 
 * 
 * @author nilriri
 * 
 */
public abstract class AbstractDAO extends StructObjectDAO {
	
	/**
	 * 컬럼명을 인수로 넘겨서 값을 조회한다.
	 * @param columnName
	 * @return
	 */
	public String getvalue(String columnName) {

		Class<? extends Object> clazz = this.getClass();
		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(FieldNameAnnotationClass.class)) {
				FieldNameAnnotationClass fieldAnnotation = method.getAnnotation(FieldNameAnnotationClass.class);
				try {
					if (columnName.toLowerCase().equals(fieldAnnotation.fieldKey().toLowerCase())) {
						Object result = method.invoke(this, null);
						if (result!=null){
							return String.valueOf(result);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 대소문자 구분없이 컬럼 값을 비교한다.
	 * @param target
	 * @param columnName
	 * @return
	 */
	public int compareToIgnoreCase(AbstractDAO target, String columnName) {

		String value1 = "";
		String value2 = "";
		if (columnName != null && !"".equals(columnName)) {

			value1 = (String) this.getvalue(columnName);
			value2 = (String) target.getvalue(columnName);
		}
		return value1.compareToIgnoreCase(value2);

	}

}
