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
package com.hangum.tadpole.mongodb.erd.core.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.Relation;
import com.hangum.tadpole.mongodb.model.Table;

/**
 * editpart factory
 * 
 * @author hangum
 *
 */
public class TadpoleEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(model instanceof DB) {
			part = new DBEditPart();
		} else if(model instanceof Table) {
			part = new TableEditPart();
		} else if(model instanceof Relation) {
			part = new RelationEditPart();
		} else {
			throw new IllegalArgumentException("Model class " + model.getClass() + " not supported yet.");
		}
		
		if(part != null) part.setModel(model);
		
		return part;
	}

}
