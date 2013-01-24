package com.hangum.tadpole.mongodb.erd.core.figures.tables;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

import com.hangum.tadpole.mongodb.erd.core.figures.SubTableFigure;


public class ColumnLayoutFigure extends Figure {
	public ColumnLayoutFigure(Object obj){
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(4);
		
		setLayoutManager(layout);
		
		if(obj instanceof SubTableFigure) {
			setBorder(new SubTableCompartmentFigureBorder());	
		} else {
			setBorder(new TableCompartmentFigureBorder());
		}
	}
}

/**
 * table compartment border
 * 
 * @author hangum
 *
 */
class TableCompartmentFigureBorder extends AbstractBorder {
	public Insets getInsets(IFigure figure) {
		return new Insets(5, 5, 5, 5);
	}
	
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
	}
}

/**
 * Sub Table compartment border
 * 
 * @author hangum
 *
 */
class SubTableCompartmentFigureBorder extends AbstractBorder {
	public Insets getInsets(IFigure figure) {
		return new Insets(0, 0, 0, 0);
	}
	
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
	}
}