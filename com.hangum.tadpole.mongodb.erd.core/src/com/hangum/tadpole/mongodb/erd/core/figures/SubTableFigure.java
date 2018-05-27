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

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
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
//		this.tableName = new Label();
//		this.tableName.setBorder(new MarginBorder(0, 0, 0, 0));
//		this.tableName.setForegroundColor(ColorConstants.lightBlue);

		// key
		this.colKeyFigure = new ColumnDetailFigure();
		this.colKeyFigure.setForegroundColor(ColorConstants.red());
		// name 
		this.colNameFigure = new ColumnDetailFigure();
		this.colNameFigure.setForegroundColor(ColorConstants.black());
		// type
		this.colTypeFigure = new ColumnDetailFigure();
		this.colTypeFigure.setForegroundColor(ColorConstants.buttonDarker());

		this.columnFigure = new ColumnLayoutFigure(this);
		this.columnFigure.add(colKeyFigure);
		this.columnFigure.add(colNameFigure);
		this.columnFigure.add(colTypeFigure);

		this.setLayoutManager(new ToolbarLayout());
		this.setBackgroundColor(SWTResourceManager.getColor(255, 255, 206));
		this.setBorder(new SubTableFigureBorder());
		this.setOpaque(true);
		
		this.add(this.tableName);
		this.add(this.columnFigure);
	}
	
	public void removeAllColumns(){
		colKeyFigure.removeAll();
		colNameFigure.removeAll();
		colTypeFigure.removeAll();

		tableName.removeAll();
		columnFigure.removeAll();
	}
	
}
