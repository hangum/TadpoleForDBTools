package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;

public class ColumnHeaderCreator {

	public ColumnHeaderCreator() {
		// TODO Auto-generated constructor stub
	}
	
	public static void createColumnHeader(TableViewer tableViewer, ObjectComparator tableComparator, TableViewColumnDefine[] colDef) {
		boolean sortable = false;

		for (int i = 0; i < colDef.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, colDef[i].align);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setMoveable(true);
			tableColumn.setText(colDef[i].caption);
			tableColumn.setData("column", colDef[i].column);
			tableColumn.setData("merge", colDef[i].merge);
			tableColumn.setData("preValue", colDef[i].preValue);
			tableColumn.setWidth(colDef[i].width);

			if (colDef[i].sortable) {
				tableColumn.addSelectionListener(getSelectionAdapter(tableViewer, tableComparator, tableColumn, i));
				sortable = true;
			}

			if ("comments".equals(colDef[i].column)) {
				tableViewerColumn.setLabelProvider(getTooltipProvider(tableViewer));
			}

			if (colDef[i].editor != null) {
				tableViewerColumn.setEditingSupport(colDef[i].editor);
			}
		}
		if (sortable) {
			tableViewer.setSorter(tableComparator);
		}
	}

	/**
	 * 실제 컬럼의 헤더를 생성합니다.
	 * 
	 * @param name
	 * @param size
	 */
	public static void createColumnHeader(TableViewer tableViewer, TableViewColumnDefine[] colDef) {
		DefaultComparator tableComparator = new DefaultComparator();
		boolean sortable = false;

		for (int i = 0; i < colDef.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, colDef[i].align);

			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setMoveable(true);
			tableColumn.setText(colDef[i].caption);
			tableColumn.setData("column", colDef[i].column);
			tableColumn.setData("merge", colDef[i].merge);
			tableColumn.setData("preValue", colDef[i].preValue);
			tableColumn.setWidth(colDef[i].width);

			if (colDef[i].sortable) {
				tableColumn.addSelectionListener(getSelectionAdapter(tableViewer, tableComparator, tableColumn, i));
				sortable = true;
			}

			if ("comments".equals(colDef[i].column)) {
				tableViewerColumn.setLabelProvider(getTooltipProvider(tableViewer));
			}

			if (colDef[i].editor != null) {
				tableViewerColumn.setEditingSupport(colDef[i].editor);
			}
		}
		if (sortable) {
			tableViewer.setSorter(tableComparator);
		}
	}

	protected static CellLabelProvider getTooltipProvider(final TableViewer viewer) {
		ColumnViewerToolTipSupport.enableFor(viewer);
		CellLabelProvider provider = new CellLabelProvider() {
			public String getToolTipText(Object element) {
				AbstractDAO table = (AbstractDAO) element;
				return table.getvalue("comments");
			}

			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			public int getToolTipDisplayDelayTime(Object object) {
				return 5000;
			}

			public int getToolTipTimeDisplayed(Object object) {
				return 1000;
			}

			public void update(ViewerCell cell) {
				AbstractDAO table = (AbstractDAO) cell.getElement();
				cell.setText(table.getvalue("comments"));
			}
		};
		return provider;
	}

	protected static SelectionAdapter getSelectionAdapter(final TableViewer viewer, final ObjectComparator comparator, final TableColumn column, final int index) {
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};

		return adapter;
	}

}
