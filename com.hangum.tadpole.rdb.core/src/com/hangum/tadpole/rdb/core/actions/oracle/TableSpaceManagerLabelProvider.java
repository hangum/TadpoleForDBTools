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
package com.hangum.tadpole.rdb.core.actions.oracle;

import java.math.BigDecimal;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleTablespaceDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.swtdesigner.ResourceManager;

/**
 * Tablespace manager label provider
 * 
 * @author nilriri
 *
 */
public class TableSpaceManagerLabelProvider extends DefaultLabelProvider {

	public TableSpaceManagerLabelProvider(TableViewer tv) {
		super(tv);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			if (dao instanceof OracleTablespaceDAO ){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/tablespace.png");
			}else{
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/data_file.png");
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
			} else {
				return (String) obj;
			}

		} else {
			return "*** not support ***";
		}

	}

}
