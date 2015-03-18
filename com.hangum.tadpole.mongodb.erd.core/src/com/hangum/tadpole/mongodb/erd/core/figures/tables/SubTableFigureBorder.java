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
package com.hangum.tadpole.mongodb.erd.core.figures.tables;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.rap.swt.SWT;

public class SubTableFigureBorder extends AbstractBorder {

	public static final int FOLD = 10;
	public Insets getInsets(IFigure figure) {
		return new Insets(0, 4, 0, 4); // top,left,bottom,right
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle r = figure.getBounds().getCopy();
		r.crop(insets);
		graphics.setLineWidth(1);
		// solid long edges around border
		graphics.drawLine(r.x + FOLD, r.y, r.x + r.width - 1, r.y);
		graphics.drawLine(r.x, r.y + FOLD, r.x, r.y + r.height - 1);
		graphics.drawLine(r.x + r.width - 1, r.y, r.x + r.width - 1, r.y + r.height - 1);
		graphics.drawLine(r.x, r.y + r.height - 1, r.x + r.width - 1, r.y + r.height - 1); // solid short edges
		
		graphics.drawLine(r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y + FOLD);
		// gray small triangle
		graphics.setBackgroundColor(ColorConstants.lightGray());
		graphics.fillPolygon(new int[] { r.x, r.y + FOLD, r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD });
		// dotted short diagonal line
		graphics.setLineStyle(SWT.LINE_DOT);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y);
	}
}
