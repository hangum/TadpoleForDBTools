package com.hangum.tadpole.rdb.erd.core.extensionpoint.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
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
