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
package com.hangum.tadpole.rdb.core.extensionpoint.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Table, column image decoration
 * 
 * @author hangum
 *
 */
public interface ITableDecorationExtension {

	/** table Column list */
	Map<String, List<String>> mapColumnDescList = new HashMap<>();
	
	/**
	 * 익스텐션을 초기화하고 동작가능한지 초기화 합니다.
	 */
	public boolean initExtension(UserDBDAO userDB);
	
	/**
	 * @param tableName
	 */
	public Image getTableImage(String tableName);
	
	/**
	 * 
	 * @param tableName
	 * @param columnName
	 */
	public Image getColumnImage(String tableName, String columnName);
}
