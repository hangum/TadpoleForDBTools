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
package com.hangum.tadpole.erd.core.figures;

import org.eclipse.draw2d.Label;

import com.hangum.tadpole.erd.core.figures.TableFigure.COLUMN_TYPE;

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
