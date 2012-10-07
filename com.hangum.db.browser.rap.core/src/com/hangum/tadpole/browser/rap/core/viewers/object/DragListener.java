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
package com.hangum.tadpole.browser.rap.core.viewers.object;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;

/**
 * table의 컬럼의 0번째 값을 소스로 설정합니다. 
 * 
 * @author hangumNote
 *
 */
public class DragListener implements DragSourceListener {
	TableViewer viewer;
	
	public DragListener(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		Table table = viewer.getTable();
		if( table.getSelectionCount() == 0) return;
		
//		StringBuffer sbTableName = new StringBuffer();
//		IStructuredSelection is = (IStructuredSelection)viewer.getSelection();
//		for(Object obj : is.toArray()) {
//			sbTableName.append(obj.toString()).append(":");
//		}
		
		event.data = table.getSelection()[0].getText();
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

}
