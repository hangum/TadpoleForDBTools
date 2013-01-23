package com.hangum.tadpole.mongodb.erd.core.figures.others;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;


public class ColumnLayoutFigure extends Figure {
	public ColumnLayoutFigure(){
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(4);
		setLayoutManager(layout);
		setBorder(new CompartmentFigureBorder());
	}
}