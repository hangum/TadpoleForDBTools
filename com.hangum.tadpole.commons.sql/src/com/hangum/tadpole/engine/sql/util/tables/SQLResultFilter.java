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
package com.hangum.tadpole.engine.sql.util.tables;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * result의 필터를 수행한다.
 * 
 * 필터는 SQLResult의 첫번째 컬럼을 필터링한다.
 * 
 * @author hangum
 *
 */
public class SQLResultFilter extends ViewerFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLResultFilter.class);
	
	// 테이블의 헤더와 인덱스 정보를 가지고 있는다.
	HashMap<String, Integer> tableToHeaderInfo = new HashMap<String, Integer>();
	String filter = ""; //$NON-NLS-1$

	public void setFilter(String val) {
		this.filter = StringUtils.trimToEmpty(val);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		HashMap<Integer, Object> model = (HashMap<Integer, Object>)element;
		
		if(filter.equals("*") || filter.equals("")) return true; //$NON-NLS-1$ //$NON-NLS-2$
		
		if(filter.indexOf("=") == -1) {	// 모든 컬럼에서 검색합니다. //$NON-NLS-1$
			for(int i=0; i<model.size(); i++) {
				String tmp = model.get(i) == null?"":model.get(i).toString();
				String key = ( tmp ).toLowerCase();
				
				if(!"".equals(key)) { //$NON-NLS-1$
					if(key.matches(".*" + filter.toLowerCase() + ".*")) return true; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			
		} else {	// 특정 컬럼에서 or 검색합니다.
			String[] baseArray = filter.split(",");		 //$NON-NLS-1$
			for (String baseTmp : baseArray) {
				try {
					String[] searchFillText = baseTmp.split("="); //$NON-NLS-1$
					String columnName = (""+searchFillText[0]).toLowerCase(); //$NON-NLS-1$
					String searchText = searchFillText.length == 2?(StringUtils.trimToEmpty(searchFillText[1])):""; //$NON-NLS-1$ //$NON-NLS-2$
					if("".equals(searchText)) return false; //$NON-NLS-1$
					
					int index = tableToHeaderInfo.get( columnName );
					String key = ( model.get(index) == null?"":model.get(index).toString() ).toLowerCase();
	
					if(!"".equals(key)) { //$NON-NLS-1$
						if(key.matches(".*" + searchText.toLowerCase() + ".*")) return true; //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				} catch(Exception e) {
					return false;
				}
			}
		}
		
		return false;
	}

	/**
	 * 테이블의 컬럼 정보를 사용하기 위해 가공
	 * 
	 * @param sqlResultTable
	 */
	public void setTable(Table sqlResultTable) {
		tableToHeaderInfo.clear();
		
		TableColumn[] tc = sqlResultTable.getColumns();
		for (int i=0; i<tc.length; i++) {
			TableColumn tableColumn = tc[i];
			String cName = tableColumn.getText();
			
			tableToHeaderInfo.put(cName.toLowerCase(), i);
		}
		
	}

}
