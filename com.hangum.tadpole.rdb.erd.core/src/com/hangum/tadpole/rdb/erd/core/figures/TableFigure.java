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
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;

import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
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
		tableName = new Label();
		tableName.setBorder(new MarginBorder(2, 2, 0, 2));
		tableName.setForegroundColor(ColorConstants.blue());
		tableName.setFont(TadpoleApplicationContextManager.getERDTitleFont());

		columnFigure = new ColumnLayoutFigure();
		// key
		colKeyFigure = new ColumnDetailFigure();
		colKeyFigure.setForegroundColor(ColorConstants.red());
		
		// name
		colNameFigure = new ColumnDetailFigure();
		colNameFigure.setForegroundColor(ColorConstants.black());
		
		// comment
		colCommentFigure = new ColumnDetailFigure();
		colCommentFigure.setForegroundColor(ColorConstants.black());
		
		// type
		colTypeFigure = new ColumnDetailFigure();
		colTypeFigure.setForegroundColor(ColorConstants.buttonDarker());
		// null 
		colNullFigure = new ColumnDetailFigure();
		colNullFigure.setForegroundColor(ColorConstants.black());

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBackgroundColor(SWTResourceManager.getColor(255, 255, 206));
		setBorder(new LineBorder(ColorConstants.black(), 1));
		setOpaque(true);

		add(tableName);
		add(columnFigure);

		columnFigure.add(colKeyFigure);
		columnFigure.add(colNameFigure);
		columnFigure.add(colCommentFigure);
		columnFigure.add(colTypeFigure);
		columnFigure.add(colNullFigure);
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
	
}
