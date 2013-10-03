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
package com.hangum.tadpole.sql.util.tables;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.Messages;


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
		TableViewerColumn tvcDate = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnDate = tvcDate.getColumn();
		tblclmnDate.setWidth(150);
		tblclmnDate.setText(Messages.SQLHistoryCreateColumn_0);
		tblclmnDate.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDate, 0));
		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
		
		// sql
		TableViewerColumn tvcSQL = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnSql = tvcSQL.getColumn();
		tblclmnSql.setWidth(300);
		tblclmnSql.setText(Messages.SQLHistoryCreateColumn_1);
		tblclmnSql.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnSql, 1));
		layoutColumnLayout.addColumnData(new ColumnWeightData(300));
		
		// duration
		TableViewerColumn tvcDuration = new TableViewerColumn(tv, SWT.RIGHT);
		TableColumn tblclmnDuration = tvcDuration.getColumn();
		tblclmnDuration.setWidth(85);
		tblclmnDuration.setText(Messages.SQLHistoryCreateColumn_2);
		tblclmnDuration.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDuration, 2));
		layoutColumnLayout.addColumnData(new ColumnWeightData(85));
		
		// rows
		TableViewerColumn tvcRows = new TableViewerColumn(tv, SWT.RIGHT);
		TableColumn tblclmnRows = tvcRows.getColumn();
		tblclmnRows.setWidth(60);
		tblclmnRows.setText(Messages.SQLHistoryCreateColumn_3);
		tblclmnRows.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnRows, 3));
		layoutColumnLayout.addColumnData(new ColumnWeightData(60));
		
		// result
		TableViewerColumn tvcResult = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnResult = tvcResult.getColumn();
		tblclmnResult.setWidth(90);
		tblclmnResult.setText(Messages.SQLHistoryCreateColumn_4);
		tblclmnResult.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnResult, 4));
		layoutColumnLayout.addColumnData(new ColumnWeightData(90));
		
		TableViewerColumn tvcMessage = new TableViewerColumn(tv, SWT.NONE);
		TableColumn tblclmnMessage = tvcMessage.getColumn();
		tblclmnMessage.setWidth(250);
		tblclmnMessage.setText(Messages.SQLHistoryCreateColumn_5);
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
