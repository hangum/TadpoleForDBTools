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
package com.hangum.db.util.tables;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * SQLResult의  Contentprovider 
 * //			ILazyContentProvider  로 구현을 수정해야합니다.
 * 
 * @author hangum
 *
 */ // ILazyContentProvider{ //,
public class SQLResultContentProvider implements  IStructuredContentProvider {

	List<HashMap<Integer, Object>> sourceDataList;
	
	public SQLResultContentProvider(List<HashMap<Integer, Object>> sourceDataList) {
		this.sourceDataList = sourceDataList;
	}

	public Object[] getElements(Object inputElement) {
		return sourceDataList.toArray();//queryViewer.getSourceDataList().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.sourceDataList = (List<HashMap<Integer, Object>>) newInput;
	}
	
	public void updateElement(int index) {
	}

}
