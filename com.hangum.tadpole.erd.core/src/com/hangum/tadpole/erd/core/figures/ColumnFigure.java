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
