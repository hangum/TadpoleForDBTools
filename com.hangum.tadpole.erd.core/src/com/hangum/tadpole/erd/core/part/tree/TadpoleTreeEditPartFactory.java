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
