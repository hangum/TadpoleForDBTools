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
package com.hangum.tadpole.mongodb.erd.core.figures;

import org.eclipse.draw2d.Label;

import com.hangum.tadpole.mongodb.erd.core.figures.TableFigure.COLUMN_TYPE;

public class ColumnFigure extends Label {
	private COLUMN_TYPE columnType;
	
	public ColumnFigure(COLUMN_TYPE columnType){
		this.columnType = columnType;
	}
	
//	protected void paintFigure(Graphics graphics) {
//		super.paintFigure(graphics);
//	}

	public COLUMN_TYPE getColumnType() {
		return columnType;
	}
}
