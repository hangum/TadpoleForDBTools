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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * table column auto layout
 * 
 * @author hangum
 *
 */
public class AutoResizeTableLayout extends TableLayout implements
		ControlListener {

	private final Table table;
	private List<ColumnLayoutData> columns = new ArrayList<ColumnLayoutData>();
	private boolean autosizing = false;

	public AutoResizeTableLayout(Table table) {
		this.table = table;
		table.addControlListener(this);
	}

	public void addColumnData(ColumnLayoutData data) {
		columns.add(data);
		super.addColumnData(data);
	}

	public void controlMoved(ControlEvent e) {
	}

	public void controlResized(ControlEvent e) {
		if (autosizing) {
			return;
		}
		autosizing = true;
		try {
			autoSizeColumns();
		} finally {
			autosizing = false;
		}
	}

	private void autoSizeColumns() {
		int width = table.getClientArea().width;
		if (width <= 1) {
			return;
		}

		TableColumn[] tableColumns = table.getColumns();
		int size = Math.min(columns.size(), tableColumns.length);
		int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		GC gc = new GC(table);
		// First do some calculations
		for (int i = 0; i < size; i++) {
			ColumnLayoutData col = columns.get(i);
			if (col instanceof ColumnPixelData) {
				int fixPixels = ((ColumnPixelData) col).width;
				if (fixPixels == 0) {
					Point measurement = gc.textExtent(table.getColumn(i)
							.getText());
					int maxWidth = measurement.x;
					for (TableItem tableItem : table.getItems()) {
						measurement = gc.textExtent(tableItem.getText(i));
						maxWidth = Math.max(maxWidth, measurement.x);
					}
					widths[i] = maxWidth + 15;
					fixedWidth += maxWidth + 15;
				} else {
					widths[i] = fixPixels;
					fixedWidth += fixPixels;
				}
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw = (ColumnWeightData) col;
				numberOfWeightColumns++;
				int weight = cw.weight;
				totalWeight += weight;
			} else {
				throw new IllegalStateException("Unknown column layout data");
			}
		}
		gc.dispose();

		// Do we have columns that have a weight?
		if (numberOfWeightColumns > 0) {
			// Now, distribute the rest
			// to the columns with weight.
			int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; i++) {
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw = (ColumnWeightData) col;
					int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest
							/ totalWeight;
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}

			// Distribute any remaining pixels
			// to columns with weight.
			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; i++) {
				if (i == size) {
					i = 0;
				}
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		for (int i = 0; i < size; i++) {
			if (tableColumns[i].getWidth() != widths[i]) {
				tableColumns[i].setWidth(widths[i]);
			}
		}
	}
}