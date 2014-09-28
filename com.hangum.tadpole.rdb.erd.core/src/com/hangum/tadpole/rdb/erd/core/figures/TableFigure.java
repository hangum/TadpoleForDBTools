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
package com.hangum.tadpole.rdb.erd.core.figures;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

import com.swtdesigner.SWTResourceManager;

/**
 * define table figure
 * 
 * @author hangum
 *
 */
public class TableFigure extends Figure {
	private static final Logger logger = Logger.getLogger(TableFigure.class);
	private Label tableName = new Label();
	
	/** column type */
	public static enum COLUMN_TYPE{KEY, NAME, COMMENT, TYPE, NULL}; 
	private ColumnLayoutFigure columnFigure;// = new ColumnLayoutFigure();
	
	private ColumnDetailFigure colKeyFigure;
	private ColumnDetailFigure colNameFigure;
	private ColumnDetailFigure colCommentFigure;
	private ColumnDetailFigure colTypeFigure;
	private ColumnDetailFigure colNullFigure;
	
	private ConnectionAnchor connectionAnchor;
	
	public TableFigure() {
		this.tableName = new Label();
		this.tableName.setBorder(new MarginBorder(2, 2, 0, 2));
		this.tableName.setForegroundColor(ColorConstants.blue);

		this.columnFigure = new ColumnLayoutFigure();
		// key
		this.colKeyFigure = new ColumnDetailFigure();
		this.colKeyFigure.setForegroundColor(ColorConstants.red);
		// name
		this.colNameFigure = new ColumnDetailFigure();
		this.colNameFigure.setForegroundColor(ColorConstants.black);
		
		// comment
		this.colCommentFigure = new ColumnDetailFigure();
		this.colCommentFigure.setForegroundColor(ColorConstants.black);
		
		// type
		this.colTypeFigure = new ColumnDetailFigure();
		this.colTypeFigure.setForegroundColor(ColorConstants.buttonDarker);
		// null 
		this.colNullFigure = new ColumnDetailFigure();
		this.colNullFigure.setForegroundColor(ColorConstants.black);

		ToolbarLayout layout = new ToolbarLayout();
		this.setLayoutManager(layout);
		this.setBackgroundColor(SWTResourceManager.getColor(255, 255, 206));
		this.setBorder(new LineBorder(ColorConstants.black, 1));
		this.setOpaque(true);

		this.add(this.tableName);
		this.add(this.columnFigure);

		this.columnFigure.add(colKeyFigure);
		this.columnFigure.add(colNameFigure);
		this.columnFigure.add(colCommentFigure);
		this.columnFigure.add(colTypeFigure);
		this.columnFigure.add(colNullFigure);
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
			} else if(COLUMN_TYPE.COMMENT == tmpFigure.getColumnType()) {
				colCommentFigure.add(figure);
			} else if(COLUMN_TYPE.TYPE == tmpFigure.getColumnType()) {
				colTypeFigure.add(figure);
			} else if(COLUMN_TYPE.NULL == tmpFigure.getColumnType()) {
				colNullFigure.add(figure);
			}
		} else {
			super.add(figure, constraint, index);
		}
	}

	public void remove(IFigure figure) {
		if(figure instanceof  ColumnFigure){
			colKeyFigure.remove(figure);
			colNameFigure.remove(figure);
			colCommentFigure.remove(figure);
			colTypeFigure.remove(figure);
			colNullFigure.remove(figure);
		} else {
			super.remove(figure);
		}
	}

	public void removeAllColumns(){
		colKeyFigure.removeAll();
		colNameFigure.removeAll();
		colCommentFigure.removeAll();
		colTypeFigure.removeAll();
		colNullFigure.removeAll();
	}
	
	public ConnectionAnchor getConnectionAnchor() {
		if(connectionAnchor == null) connectionAnchor = new ChopboxAnchor(this);
		return connectionAnchor;
	}
	
	private class ColumnLayoutFigure extends Figure {
		public ColumnLayoutFigure(){
			ToolbarLayout layout = new ToolbarLayout(true);
			layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
			layout.setStretchMinorAxis(true);
			layout.setSpacing(2);
			setLayoutManager(layout);
			setBorder(new CompartmentFigureBorder());
		}
	}

	public class CompartmentFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(5, 5, 5, 5);
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
}
