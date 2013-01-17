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
package com.hangum.tadpole.erd.core.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.Table;

public class TableCreateCommand extends Command {
//	private static Dimension defaultDimension = new Dimension(230, 150);
	
	private Table newTable;
	private Rectangle constraints;
	private DB db;
	
	@Override
	public void execute() {
//		newTable.setName(newTable.getName());
		
		// dnd로 옮겨졌을때 먹는 모델
		// db에 table이 이미 존재할 경우 table의 모델을 dnd로 옮겨진 쪽으로 이동해줍니다.
		if(constraints != null) {
			newTable.setConstraints(constraints);
		}
//		newTable.setDb(db);
		
	}
	
	@Override
	public void undo() {
		newTable.setDb(null);
	}
	
	public void setLocation(Point location) {
		constraints = new Rectangle(location, new Dimension(-1, -1));
	}
	
	public void setParent(DB db) {
		this.db = db;
	}
	
	public void setTable(Table table) {
		this.newTable = table;
	}
}
