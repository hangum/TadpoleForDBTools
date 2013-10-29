package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.sql.dao.rdb.AbstractDAO;

public class ColumnInformLabelProvider extends LabelProvider implements ITableLabelProvider {

	private TableViewer tableViewer;
	private AbstractDAO dao = null;

	public ColumnInformLabelProvider(TableViewer tv) {
		this.tableViewer = tv;

		// cell merge compare value initialize.
		for (TableColumn column : tableViewer.getTable().getColumns()) {
			column.setData("preValue", "");
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String columnName = (String) tableViewer.getTable().getColumn(columnIndex).getData("column");
		boolean cellMerge = (Boolean) tableViewer.getTable().getColumn(columnIndex).getData("merge");
		String preValue = (String) tableViewer.getTable().getColumn(columnIndex).getData("preValue");

		// RDBInfomationforColumnDAO dao = (RDBInfomationforColumnDAO) element;
		dao = (AbstractDAO) element;

		if (cellMerge) {
			String tableName = dao.getColumnValuebyName(columnName);
			if (!preValue.equals(tableName)) {
				tableViewer.getTable().getColumn(columnIndex).setData("preValue", tableName);
				return tableName;
			} else {
				// ubuntu에서 특수문자 깨져서..
				// return "   ➥ " + infoDao.getColumnValuebyName(columnName);
				return "   " + dao.getColumnValuebyName(columnName);
			}
		} else {
			return dao.getColumnValuebyName(columnName);
		}

		// return infoDao.getColumnValuebyName(columnName);
	}

}
