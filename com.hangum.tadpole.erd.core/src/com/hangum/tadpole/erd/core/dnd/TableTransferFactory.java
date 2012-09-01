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
package com.hangum.tadpole.erd.core.dnd;

import org.eclipse.gef.requests.CreationFactory;

import com.hangum.tadpole.model.Table;

public class TableTransferFactory implements CreationFactory {
	private Table table;

	@Override
	public Object getNewObject() {
		return table;
	}

	@Override
	public Object getObjectType() {
		return Table.class;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}

}
