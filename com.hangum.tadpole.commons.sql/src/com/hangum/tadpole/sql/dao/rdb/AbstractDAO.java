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
package com.hangum.tadpole.sql.dao.rdb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@interface FieldNameAnnotationClass {
	String fieldKey();
}

/**
 * 
 * 
 * @author nilriri
 * 
 */
public abstract class AbstractDAO {
	
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
						return (String) method.invoke(this, null);
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
