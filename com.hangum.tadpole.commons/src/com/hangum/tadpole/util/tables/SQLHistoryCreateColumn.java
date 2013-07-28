package com.hangum.tadpole.util.tables;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

/**
 * SQL history create column
 * 
 * @author hangum
 *
 */
public class SQLHistoryCreateColumn {

	/**
	 * history column
	 * 
	 * @param tv
	 * @param sorterHistory
	 * @param layoutColumnLayout
	 */
	public static void createTableHistoryColumn(TableViewer tv, SQLHistorySorter sorterHistory, AutoResizeTableLayout layoutColumnLayout) {
		// time
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnDate = tableViewerColumn.getColumn();
		tblclmnDate.setWidth(150);
		tblclmnDate.setText("Date");
		tblclmnDate.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDate, 0));
		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
		
		// sql
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(300);
		tblclmnSql.setText("SQL");
		tblclmnSql.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnSql, 1));
		layoutColumnLayout.addColumnData(new ColumnWeightData(300));
		
		// duration
		TableViewerColumn tableViewerColumnDuration = new TableViewerColumn(tv, SWT.RIGHT);
		TableColumn tblclmnDuration = tableViewerColumnDuration.getColumn();
		tblclmnDuration.setWidth(85);
		tblclmnDuration.setText("Duration");
		tblclmnDuration.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDuration, 2));
		layoutColumnLayout.addColumnData(new ColumnWeightData(85));
		
		// rows
		TableViewerColumn tableViewerColumnRows = new TableViewerColumn(tv, SWT.RIGHT);
		TableColumn tblclmnRows = tableViewerColumnRows.getColumn();
		tblclmnRows.setWidth(60);
		tblclmnRows.setText("Rows");
		tblclmnRows.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnRows, 3));
		layoutColumnLayout.addColumnData(new ColumnWeightData(60));
		
		// result
		TableViewerColumn tableViewerColumnResult = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnResult = tableViewerColumnResult.getColumn();
		tblclmnResult.setWidth(90);
		tblclmnResult.setText("Result");
		tblclmnResult.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnResult, 4));
		layoutColumnLayout.addColumnData(new ColumnWeightData(90));
		
		TableViewerColumn tableViewerColumnMessage = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnMessage = tableViewerColumnMessage.getColumn();
		tblclmnMessage.setWidth(250);
		tblclmnMessage.setText("Message");
		tblclmnMessage.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnMessage, 4));
		layoutColumnLayout.addColumnData(new ColumnWeightData(250));
	}
	
	/**
	 * tablecolumn adapter
	 * @param viewer
	 * @param comparator
	 * @param column
	 * @param index sort index
	 * @return
	 */
	private static SelectionAdapter getSelectionAdapter(final TableViewer viewer, final DefaultViewerSorter comparator, final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

}
