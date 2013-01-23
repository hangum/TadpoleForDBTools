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
package com.hangum.tadpole.mongodb.erd.core.figures;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;

import com.hangum.tadpole.mongodb.erd.core.figures.others.ColumnLayoutFigure;

public class TableFigure extends Figure {
	private static final Logger logger = Logger.getLogger(TableFigure.class);
	protected Label tableName = new Label();
	
	/** column type */
	public static enum COLUMN_TYPE{KEY, NAME, TYPE, NULL}; 
	protected ColumnLayoutFigure columnFigure;// = new ColumnLayoutFigure();
	
	protected ColumnDetailFigure colKeyFigure;
	protected ColumnDetailFigure colNameFigure;
	protected ColumnDetailFigure colTypeFigure;
	
	protected ConnectionAnchor connectionAnchor;
	
	public TableFigure() {
		this.tableName = new Label();
		this.tableName.setBorder(new MarginBorder(2, 2, 0, 2));
		this.tableName.setForegroundColor(ColorConstants.blue);

		// key
		this.colKeyFigure = new ColumnDetailFigure();
		this.colKeyFigure.setForegroundColor(ColorConstants.red);
		// name 
		this.colNameFigure = new ColumnDetailFigure();
		this.colNameFigure.setForegroundColor(ColorConstants.black);
		// type
		this.colTypeFigure = new ColumnDetailFigure();
		this.colTypeFigure.setForegroundColor(ColorConstants.buttonDarker);

		this.columnFigure = new ColumnLayoutFigure();
		this.columnFigure.add(colKeyFigure);
		this.columnFigure.add(colNameFigure);
		this.columnFigure.add(colTypeFigure);
		
		setLayoutManager(new ToolbarLayout());
//		setBackgroundColor(new Color(Display.getDefault(), 255, 255, 206));
		setBorder(new LineBorder(ColorConstants.black, 1));
		setOpaque(true);
		
		add(this.tableName);
		add(this.columnFigure);
	}
	
	public void setTableName(String tableName){
		this.tableName.setText(tableName);
	}
	
	public Label getTableName() {
		return tableName;
	}
	
	public void add(IFigure figure, Object constraint, int index) {
		if(figure instanceof ColumnFigure){
			ColumnFigure tmpFigure = (ColumnFigure)figure;
			
			if(COLUMN_TYPE.KEY == tmpFigure.getColumnType()) {
				colKeyFigure.add(figure);
			} else if(COLUMN_TYPE.NAME == tmpFigure.getColumnType()) {
				colNameFigure.add(figure);
			} else if(COLUMN_TYPE.TYPE == tmpFigure.getColumnType()) {
				colTypeFigure.add(figure);
			}
		} else {
			super.add(figure, constraint, index);
		}
	}

	public void remove(IFigure figure) {		
		if(figure instanceof ColumnFigure){
			colKeyFigure.remove(figure);
			colNameFigure.remove(figure);
			colTypeFigure.remove(figure);
		} else {
			super.remove(figure);
		}
	}

	public void removeAllColumns(){
		logger.debug("[TableFigure figure remove all]");
		
		colKeyFigure.removeAll();
		colNameFigure.removeAll();
		colTypeFigure.removeAll();
		

//		List<Figure> listFigure = getChildren();
//		for (Figure figure : listFigure) {
//			if(figure instanceof SubTableFigure) {
//				SubTableFigure stFigure = (SubTableFigure)figure;
//				stFigure.removeAllColumns();	
//				
//				stFigure.removeAll();
//			}
//		}
	}
	
	public ConnectionAnchor getConnectionAnchor() {
		if(connectionAnchor == null) connectionAnchor = new ChopboxAnchor(this);
		return connectionAnchor;
	}
}
