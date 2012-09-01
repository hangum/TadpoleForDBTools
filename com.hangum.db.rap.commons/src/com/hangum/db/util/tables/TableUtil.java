/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.util.tables;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.db.util.tables.DefaultViewerSorter.COLUMN_TYPE;

public class TableUtil {

	public static void makeTableColumns(Table table, String[] headers) {
		TableColumn column;
		for (int i = 0; i < headers.length; i++) {
			column = new TableColumn(table, SWT.NONE);
			column.setText(headers[i]);
		}
	}
	
	public static void alignmentForNumericColumn(Table table, DefaultViewerSorter sorter) {
		TableColumn[] columns = table.getColumns();
		COLUMN_TYPE type;
		for (int i = 0; i < columns.length; i++) {
			type = sorter.getColumnType(i);
			if ( type != DefaultViewerSorter.COLUMN_TYPE.String )
				columns[i].setAlignment(SWT.RIGHT);
			}
	}
	
	public static void makeTableColumnViewer(TableViewer tableViewer, String[] header) {
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		for (int i = 0; i < header.length; i++) {
			TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
			column.getColumn().setText(header[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			column.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return element.toString();
				}
			});
		}
	}
	
	public static void packTable(Table table) {
		TableColumn[] columns = table.getColumns();
		
		for (int i = 0; i < columns.length; i++) {
			columns[i].pack();
			
			// column이 2개 이하일 경우 자신의 size만큼 표시해준다
			if(columns.length >= 3) {
				if(columns[i].getWidth() < 50) columns[i].setWidth(50);
				else if(columns[i].getWidth() >=  300) columns[i].setWidth(300);
			}
		}
	}
}
