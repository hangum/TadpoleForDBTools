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
package com.hangum.tadpole.sql.util.tables;

import java.util.List;
import java.util.Map;

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

	List<Map<Integer, Object>> sourceDataList;
	
	public SQLResultContentProvider(List<Map<Integer, Object>> sourceDataList) {
		this.sourceDataList = sourceDataList;
	}

	public Object[] getElements(Object inputElement) {
		return sourceDataList.toArray();//queryViewer.getSourceDataList().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.sourceDataList = (List<Map<Integer, Object>>) newInput;
	}
	
	public void updateElement(int index) {
	}

}
