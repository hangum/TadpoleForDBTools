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
package com.hangum.tadpole.engine.sql.util.tables;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.sql.util.tables.DefaultViewerSorter.COLUMN_TYPE;

/**
 * eclipse swt table utils
 * @author hangum
 *
 */
public class TableUtil {
	
	/**
	 * make selected single column
	 * 
	 * @param tv
	 */
	public static void makeSelectSingleColumn(TableViewer tv) {
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tv,new FocusCellOwnerDrawHighlighter(tv));
	    ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tv) {
	        protected boolean isEditorActivationEvent(
	                ColumnViewerEditorActivationEvent event) {
	            return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
	                    || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
	                    || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
	                    || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
	        }
	    };
	    TableViewerEditor.create(tv, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
	            | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
	            | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
	}

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
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(table);
		table.setLayout(layoutColumnLayout);
		
		TableColumn[] columns = table.getColumns();		
		for (int i = 0; i < columns.length; i++) {			
			columns[i].pack();
			
			// 10의 숫자는 경험치이고 컬럼이 적었을 경우 좀더 편하게 보이는듯합니다.
			layoutColumnLayout.addColumnData(new ColumnPixelData(columns[i].getWidth() + 10));
		}

	}
}
