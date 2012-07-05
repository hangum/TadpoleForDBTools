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
