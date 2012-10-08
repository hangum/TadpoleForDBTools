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
package com.hangum.tadpole.util.tables;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * SQLResult의 LabelProvider
 * 
 * @author hangum
 *
 */
public class SQLResultLabelProvider extends LabelProvider implements ITableLabelProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLResultLabelProvider.class);

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, String> rsResult = (HashMap<Integer, String>)element;
		
		return rsResult.get(columnIndex);
	}
	
	/**
	 * table의 Column을 생성한다.
	 */
	public static void createTableColumn(final TableViewer tableViewer, final HashMap<Integer, String> mapColumns, final SQLResultSorter tableSorter) {
		// 기존 column을 삭제한다.
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		try {
			
			for(int i=0; i<mapColumns.size(); i++) {
				final int index = i;
				final TableViewerColumn tableColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
				tableColumn.getColumn().setText( mapColumns.get(i) );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(true);
				
				tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						tableSorter.setColumn(index);
						int dir = tableViewer.getTable().getSortDirection();
						if (tableViewer.getTable().getSortColumn() == tableColumn.getColumn()) {
							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
						} else {

							dir = SWT.DOWN;
						}
						tableViewer.getTable().setSortDirection(dir);
						tableViewer.getTable().setSortColumn(tableColumn.getColumn());
						tableViewer.refresh();
					}
				});
				
//				tableColumn.setLabelProvider(new StyledCellLabelProvider() {
//					@Override
//					public void update(ViewerCell cell) {
//						String search = searchText.getText();
//						Person person = (Person) cell.getElement();
//						String cellText = person.getFirstName();
//						cell.setText(cellText);
//						if (search != null && search.length() > 0) {
//							int intRangesCorrectSize[] = SearchUtil
//									.getSearchTermOccurrences(search, cellText);
//							List<StyleRange> styleRange = new ArrayList<StyleRange>();
//							for (int i = 0; i < intRangesCorrectSize.length / 2; i++) {
//								int start = intRangesCorrectSize[i];
//								int length = intRangesCorrectSize[++i];
//								StyleRange myStyledRange = new StyleRange(start,
//										length, null, colorYellow);
//
//								styleRange.add(myStyledRange);
//							}
//							cell.setStyleRanges(styleRange
//									.toArray(new StyleRange[styleRange.size()]));
//						} else {
//							cell.setStyleRanges(null);
//						}
//
//						super.update(cell);
//
//					}
//				});
				
				
//				tableColumn.setEditingSupport(new TextEditingSupport(i, tableViewer));
				
			}	// end for
			
			
		} catch(Exception e) { 
			logger.error("SQLResult TableViewer", e);
		}
		
	}
}
