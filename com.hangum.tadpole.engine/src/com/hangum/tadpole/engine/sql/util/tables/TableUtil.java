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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.util.AutoResizeTableLayout;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.tables.DefaultViewerSorter.COLUMN_TYPE;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * eclipse swt table utils
 * @author hangum
 *
 */
public class TableUtil {
	private static final Logger logger = Logger.getLogger(TableUtil.class);
	
	/**
	 * create column of table
	 * 
	 * @param tableViewer
	 * @param rsDAO
	 */
	public static void createTableColumn(final TableViewer tableViewer, final ResultSetUtilDTO rsDAO) {
		createTableColumn(tableViewer, rsDAO, null);
	}

	/**
	 * table의 Column을 생성한다.
	 * 
	 * @param tableViewer
	 * @param rsDAO
	 * @param tableSorter
	 */
	public static void createTableColumn(final TableViewer tableViewer,
										final ResultSetUtilDTO rsDAO,
										final SQLResultSorter tableSorter
									) {
		// 기존 column을 삭제한다.
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		if(rsDAO.getColumnName() == null) return;
			
		try {			
			for(int i=0; i<rsDAO.getColumnName().size(); i++) {
				final int index = i;
				final int columnAlign = RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i))?SWT.RIGHT:SWT.LEFT;
				String strColumnName = rsDAO.getColumnLabelName().get(i);
		
				/** 표시 되면 안되는 컬럼을 제거 합니다 */
				if(StringUtils.startsWithIgnoreCase(strColumnName, PublicTadpoleDefine.SPECIAL_USER_DEFINE_HIDE_COLUMN)) continue;
				
				final TableViewerColumn tv = new TableViewerColumn(tableViewer, columnAlign);
				final TableColumn tc = tv.getColumn();
				
				tc.setText(strColumnName);
				tc.setResizable(true);
				tc.setMoveable(true);
				
				tc.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(tableSorter != null) tableSorter.setColumn(index);
						int dir = tableViewer.getTable().getSortDirection();
						if (tableViewer.getTable().getSortColumn() == tc) {
							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
						} else {
							dir = SWT.DOWN;
						}
						tableViewer.getTable().setSortDirection(dir);
						tableViewer.getTable().setSortColumn(tc);
						tableViewer.refresh();
					}
				});
				
			}	// end for
			
		} catch(Exception e) { 
			logger.error("SQLResult TableViewer", e);
		}		
	}
	
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
	
	/**
	 * 
	 * @param tableViewer
	 * @param header
	 */
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
	
	/**
	 * 
	 * @param table
	 */
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
