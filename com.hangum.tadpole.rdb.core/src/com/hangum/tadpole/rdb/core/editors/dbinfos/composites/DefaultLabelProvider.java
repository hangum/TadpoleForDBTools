package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

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
//			for (DB_KEY key : DB_KEY.values()) {
				if (PublicTadpoleDefine.isPK(dao.getvalue("pk"))) {
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/primary_key_column.png"); //$NON-NLS-1$
				} else if (PublicTadpoleDefine.isFK(dao.getvalue("pk"))) {
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/foreign_key_column.png"); //$NON-NLS-1$
				} else if (PublicTadpoleDefine.isMUL(dao.getvalue("pk"))) {
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/multi_key_column.png"); //$NON-NLS-1$
				}
//			}
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
