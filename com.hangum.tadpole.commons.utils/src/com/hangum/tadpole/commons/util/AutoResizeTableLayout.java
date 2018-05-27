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
package com.hangum.tadpole.commons.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * table column auto layout
 * 
 * Original implementation by Dan Rubel & Eric Clayberg Authors: "Eclipse: Building Commercial Quality Plug-ins" Addison-Wesley, June 2004, ISBN: 0321228472
 * http://www.qualityeclipse.com/. Used with permission
 * 
 * @author hangum
 * 
 */
public class AutoResizeTableLayout extends TableLayout implements ControlListener {
	private static final long serialVersionUID = -5496184969984103717L;
	private final Table table;
	private List<ColumnLayoutData> columns = new ArrayList<>();
//	private boolean autosizing = false;

	public AutoResizeTableLayout(Table table) {
		super();
		this.table = table;
		table.addControlListener(this);
	}

	public void controlMoved(ControlEvent e) {
		// empty
	}

	public void controlResized(ControlEvent e) {
//		if (autosizing)
//			return;
//
//		autosizing = true;
//		try {
			autoSizeColumns();
//		} finally {
//			autosizing = false;
//		}
	}

	private void autoSizeColumns() {
		int width = table.getClientArea().width;

		// XXX: Layout is being called with an invalid value
		// the first time it is being called on Linux.
		// This method resets the layout to null,
		// so we run it only when the value is OK.
		if (width <= 1)
			return;

		TableColumn[] tableColumns = table.getColumns();
		int size = Math.min(columns.size(), tableColumns.length);
		int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		// First calculate space occupied by fixed columns.
		for (int i = 0; i < size; ++i) {
			ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
			if (col instanceof ColumnPixelData) {
				int pixels = ((ColumnPixelData) col).width;
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw = (ColumnWeightData) col;
				++numberOfWeightColumns;
				int weight = cw.weight;
				totalWeight += weight;
			} else {
				throw new IllegalStateException("Unknown column layout data");
			}
		}

		// Do we have coluns that have a weight?
		if (numberOfWeightColumns > 0) {
			// Now, distribute the rest
			// to the columns with weight.

			int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; ++i) {
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw = (ColumnWeightData) col;
					int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest
							/ totalWeight;
					if (pixels < cw.minimumWidth)
						pixels = cw.minimumWidth;
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}

			// Distribute any remaining pixels
			// to columns with weight.

			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; ++i) {
				if (i == size)
					i = 0;
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		for (int i = 0; i < size; ++i) {
			if (tableColumns[i].getWidth() != widths[i]) {
				tableColumns[i].setWidth(widths[i]);
			}
		}
	}

	public void addColumnData(ColumnLayoutData data) {
		columns.add(data);
		super.addColumnData(data);
	}

}
