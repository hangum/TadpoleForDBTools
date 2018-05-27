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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import com.swtdesigner.SWTResourceManager;

public class DBFigure extends Figure {
	private Label labelURL		= new Label();
	
	private Label labelDBType 	= new Label();
	private Label labelID 		= new Label();
	
	private XYLayout layout;
	
	public DBFigure() {
		layout = new XYLayout();
		setLayoutManager(layout);
		
		labelDBType.setForegroundColor(ColorConstants.black());
		labelDBType.setBorder(new LineBorder(1));
		labelDBType.setBackgroundColor(SWTResourceManager.getColor(100, 149, 237));
		labelDBType.setOpaque(true);
		
		add(labelDBType);
		setConstraint(labelDBType, new Rectangle(2, 2, -1, -1));
		
//		labelID.setForegroundColor(ColorConstants.lightBlue);
//		add(labelID);
//		setConstraint(labelID, new Rectangle(5, 17, -1, -1));
//		
//		labelURL.setForegroundColor(ColorConstants.blue);
//		add(labelURL);
//		setConstraint(labelURL, new Rectangle(5, 29, -1, -1));
		
		setForegroundColor(ColorConstants.black());
		setBorder(new LineBorder(1));
		
	}
	
	public void setLayout(Rectangle rect) {
		setBounds(rect);
	}

	public void setLabelDBType(String dbType) {
		this.labelDBType.setText(dbType);
	}
	
	public void setLabelID(String id) {
		this.labelID.setText(id);
	}
	
	public void setLabelURL(String labelURL) {
		this.labelURL.setText(labelURL);
	}
}
