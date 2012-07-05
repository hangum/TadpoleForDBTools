package com.hangum.tadpole.erd.core.part.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Table;

public class TadpoleTreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(model instanceof DB) {
			part = new DBTreeEditPart();
		} else if(model instanceof Table) {
			part = new TableTreeEditPart();
		}
		
		if(part != null) {
			part.setModel(model);
		}
		
		return part;
	}

}
