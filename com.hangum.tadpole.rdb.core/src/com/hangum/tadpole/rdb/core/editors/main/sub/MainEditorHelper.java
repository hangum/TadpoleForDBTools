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
package com.hangum.tadpole.rdb.core.editors.main.sub;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.commons.sql.util.tables.DefaultViewerSorter;
import com.hangum.tadpole.commons.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * Maineditor helper
 * 
 * @author hangum
 *
 */
public class MainEditorHelper {

	/**
	 * error message tableviewer
	 * 
	 * @param tableViewerMessage
	 * @param sorterMessage
	 * @param layoutColumnLayoutMsg
	 */
	public static void createTableMessageColumn(TableViewer tableViewerMessage, SQLHistorySorter sorterMessage, AutoResizeTableLayout layoutColumnLayoutMsg) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		TableColumn tblclmnDate = tableViewerColumn.getColumn();
		tblclmnDate.setWidth(140);
		tblclmnDate.setText(Messages.MainEditor_14);
		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnDate, 0));
		layoutColumnLayoutMsg.addColumnData(new ColumnPixelData(160));
				
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText("Message"); //$NON-NLS-1$
		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnSql, 1));
		layoutColumnLayoutMsg.addColumnData(new ColumnWeightData(500));
	}
	
	/**
	 * tablecolumn adapter
	 * @param column
	 * @param index
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
