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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * table의 컬럼의 0번째 값을 소스로 설정합니다. 
 * 
 * @author hangumNote
 */
public class DragListener implements DragSourceListener {
	private UserDBDAO userDB;
	private TableViewer viewer;
	
	public DragListener(UserDBDAO userDB, TableViewer viewer) {
		this.userDB = userDB;
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection iss = (IStructuredSelection)viewer.getSelection();
		if(!iss.isEmpty()) {
//			Table table = viewer.getTable();
//			if( table.getSelectionCount() == 0) return;
//			event.data = table.getSelection()[0].getText();
			TableDAO td = (TableDAO)iss.getFirstElement();
			event.data = userDB.getSeq() + PublicTadpoleDefine.DELIMITER + td.getName();
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

}
