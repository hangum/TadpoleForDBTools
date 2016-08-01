/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilrir - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleDBLinkDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * RDB Default label provider
 * 
 * @author hangum
 *
 */
public class DefaultLabelProvider extends LabelProvider implements ITableLabelProvider {

	private TableViewer tableViewer;
	private AbstractDAO dao = null;

	public DefaultLabelProvider(TableViewer tv) {
		this.tableViewer = tv;

		// cell merge compare value initialize.
		for (TableColumn column : tableViewer.getTable().getColumns()) {
			column.setData("preValue", "");
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		AbstractDAO dao = (AbstractDAO) element;

		if (columnIndex == 0) {
			if (dao instanceof OracleSynonymDAO){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/synonyms.png"); //$NON-NLS-1$
			}else if (dao instanceof OracleJobDAO){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/jobs.png"); //$NON-NLS-1$
			}else if (dao instanceof OracleDBLinkDAO){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/database_link.png"); //$NON-NLS-1$
			}else if (dao instanceof OracleSequenceDAO){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/sequence.png"); //$NON-NLS-1$
			}else if (PublicTadpoleDefine.isPK(dao.getvalue("pk"))) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/primary_key_column.png"); //$NON-NLS-1$
			} else if (PublicTadpoleDefine.isFK(dao.getvalue("pk"))) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/foreign_key_column.png"); //$NON-NLS-1$
			} else if (PublicTadpoleDefine.isMUL(dao.getvalue("pk"))) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/multi_key_column.png"); //$NON-NLS-1$
			}
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/column.png"); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String columnName = (String) tableViewer.getTable().getColumn(columnIndex).getData("column");
		boolean cellMerge = (Boolean) tableViewer.getTable().getColumn(columnIndex).getData("merge");
		String preValue = (String) tableViewer.getTable().getColumn(columnIndex).getData("preValue");

		dao = (AbstractDAO) element;

		if (cellMerge) {
			String tableName = dao.getvalue(columnName);
			
			if (!preValue.equals(tableName)) {
				tableViewer.getTable().getColumn(columnIndex).setData("preValue", tableName);
				return tableName;
			} else {
				// ubuntu에서 특수문자 깨져서..
				// return "   ➥ " + infoDao.getColumnValuebyName(columnName);
				// return "   " + dao.getColumnValuebyName(columnName);
				return "   " + dao.getvalue(columnName);
			}
		} else {
			return dao.getvalue(columnName);
		}
	}

}
