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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;

import com.hangum.tadpole.mongodb.erd.core.figures.tables.ColumnLayoutFigure;
import com.hangum.tadpole.mongodb.erd.core.figures.tables.SubTableFigureBorder;
import com.swtdesigner.SWTResourceManager;

/**
 * SubDocument figure
 * 
 * @author hangum
 *
 */
public class SubTableFigure extends TableFigure {
	private static final Logger logger = Logger.getLogger(SubTableFigure.class);
	
	public SubTableFigure() {
		this.tableName = new Label();
		this.tableName.setBorder(new MarginBorder(2, 2, 2, 2));
		this.tableName.setForegroundColor(ColorConstants.lightBlue);

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

		this.setLayoutManager(new ToolbarLayout());
		this.setBackgroundColor(SWTResourceManager.getColor(0, 206, 206));
		this.setBorder(new SubTableFigureBorder());
		this.setOpaque(false);
		
		this.add(this.tableName);
		this.add(this.columnFigure);
	}
	
//	public void removeAllColumns(){
//		colKeyFigure.removeAll();
//		colNameFigure.removeAll();
//		colTypeFigure.removeAll();
//		
//		List<Figure> listFigure = getChildren();
//		for (Figure figure : listFigure) {
////			logger.debug(getClass().getName() + ":" + figure.toString());
//			
//			if(figure instanceof SubTableFigure) {
//				SubTableFigure stFigure = (SubTableFigure)figure;
//				stFigure.removeAllColumns();	
//				
//				stFigure.removeAll();			
//			}
//		}
//	}
	
}
