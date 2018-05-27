/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.sessionlist;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.swtdesigner.ResourceManager;

/**
 * Tablespace manager label provider
 * 
 * @author nilriri
 *
 */
public class SessionLocksLabelProvider extends DefaultLabelProvider {

	public SessionLocksLabelProvider(TableViewer tv) {
		super(tv);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {			
			Map<String, String> map = (Map<String, String>) element;			
			if(map.containsKey("SQL_TEXT")){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/lock_block.png");
			}else{
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/dbms_lock.png");
			}
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String columnName = (String) tableViewer.getTable().getColumn(columnIndex).getData("column");

		if (element instanceof AbstractDAO) {
			
			return super.getColumnText(element, columnIndex);

		} else if (element instanceof Map) {
			// HashMap으로 리턴받은 경우 맵 키를 이용하여 컬럼에 출력할 자료를 추출한다.
			Map<String, String> map = (Map<String, String>) element;
			Object obj = map.get(columnName);
			if (obj instanceof java.math.BigDecimal) {
				return ((BigDecimal) obj).toString();
			} else if (obj instanceof java.math.BigInteger) {
				return ((BigInteger) obj).toString();
			} else if (obj instanceof java.sql.Timestamp) {
				return ((Timestamp) obj).toString();
			} else {
				return String.valueOf(obj);
			}

		} else {
			return "*** not support ***";
		}

	}

}
