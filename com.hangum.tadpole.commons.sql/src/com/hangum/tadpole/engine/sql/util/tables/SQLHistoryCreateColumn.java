///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.engine.sql.util.tables;
//
//import org.eclipse.jface.viewers.ColumnPixelData;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.widgets.TableColumn;
//
//import com.hangum.tadpole.engine.Messages;
//
//
///**
// * SQL history create column
// * 
// * @author hangum
// *
// */
//public class SQLHistoryCreateColumn {
//
//	/**
//	 * history column
//	 * 
//	 * @param tv
//	 * @param sorterHistory
//	 * @param layoutColumnLayout
//	 */
//	public static void createTableHistoryColumn(TableViewer tv, SQLHistorySorter sorterHistory, AutoResizeTableLayout layoutColumnLayout, boolean isQueryHistoryTrack) {
//		// time
//		TableViewerColumn tvcDate = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnDate = tvcDate.getColumn();
//		tblclmnDate.setWidth(150);
//		tblclmnDate.setText(Messages.get().SQLHistoryCreateColumn_0);
//		tblclmnDate.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDate, 0));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
//		// sql
//		TableViewerColumn tvcSQL = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnSql = tvcSQL.getColumn();
//		tblclmnSql.setWidth(300);
//		tblclmnSql.setText(Messages.get().SQLHistoryCreateColumn_1);
//		tblclmnSql.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnSql, 1));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(300));
//		
//		// duration
//		TableViewerColumn tvcDuration = new TableViewerColumn(tv, SWT.RIGHT);
//		TableColumn tblclmnDuration = tvcDuration.getColumn();
//		tblclmnDuration.setWidth(60);
//		tblclmnDuration.setText(Messages.get().SQLHistoryCreateColumn_2);
//		tblclmnDuration.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDuration, 2));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(50));
//		
//		// rows
//		TableViewerColumn tvcRows = new TableViewerColumn(tv, SWT.RIGHT);
//		TableColumn tblclmnRows = tvcRows.getColumn();
//		tblclmnRows.setWidth(60);
//		tblclmnRows.setText(Messages.get().SQLHistoryCreateColumn_3);
//		tblclmnRows.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnRows, 3));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(50));
//		
//		// result
//		TableViewerColumn tvcResult = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnResult = tvcResult.getColumn();
//		tblclmnResult.setWidth(90);
//		tblclmnResult.setText(Messages.get().SQLHistoryCreateColumn_4);
//		tblclmnResult.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnResult, 4));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(40));
//		
//		TableViewerColumn tvcMessage = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnMessage = tvcMessage.getColumn();
//		tblclmnMessage.setWidth(250);
//		tblclmnMessage.setText(Messages.get().SQLHistoryCreateColumn_5);
//		tblclmnMessage.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnMessage, 5));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(80));
//		
//		if (!isQueryHistoryTrack) {
//			return;
//		}
//	
//		// User name 
//		TableViewerColumn tvcUser = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnUser = tvcUser.getColumn();
//		tblclmnUser.setWidth(200);
//		tblclmnUser.setText(Messages.get().SQLHistoryCreateColumn_6);
//		tblclmnUser.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnUser, 6));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
//		// Database
//		TableViewerColumn tvcDatabase = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnDatabase = tvcDatabase.getColumn();
//		tblclmnDatabase.setWidth(250);
//		tblclmnDatabase.setText(Messages.get().SQLHistoryCreateColumn_7);
//		tblclmnDatabase.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnDatabase, 7));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//		
//		// ip
//		TableViewerColumn tvcIp = new TableViewerColumn(tv, SWT.NONE);
//		TableColumn tblclmnIp = tvcIp.getColumn();
//		tblclmnIp.setWidth(250);
//		tblclmnIp.setText(Messages.get().SQLHistoryCreateColumn_8);
//		tblclmnIp.addSelectionListener(getSelectionAdapter(tv, sorterHistory, tblclmnIp, 8));
//		layoutColumnLayout.addColumnData(new ColumnPixelData(150));
//	}
//	
//	/**
//	 * tablecolumn adapter
//	 * @param viewer
//	 * @param comparator
//	 * @param column
//	 * @param index sort index
//	 * @return
//	 */
//	private static SelectionAdapter getSelectionAdapter(final TableViewer viewer, final DefaultViewerSorter comparator, final TableColumn column, final int index) {
//		SelectionAdapter selectionAdapter = new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				comparator.setColumn(index);
//				
//				viewer.getTable().setSortDirection(comparator.getDirection());
//				viewer.getTable().setSortColumn(column);
//				viewer.refresh();
//			}
//		};
//		return selectionAdapter;
//	}
//
//}
