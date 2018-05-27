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
package com.hangum.tadpole.mongodb.erd.core.part.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.Table;

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
