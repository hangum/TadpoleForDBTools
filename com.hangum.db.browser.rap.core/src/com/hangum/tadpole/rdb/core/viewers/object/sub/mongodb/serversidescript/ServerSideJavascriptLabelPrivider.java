/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.serversidescript;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.dao.mongodb.MongoDBServerSideJavaScriptDAO;

/**
 * ServerSide Javascript 컬럼 정보
 * 
 * @author hangumNote
 *
 */
public class ServerSideJavascriptLabelPrivider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		MongoDBServerSideJavaScriptDAO tc = (MongoDBServerSideJavaScriptDAO)element;
		
		switch(columnIndex) {
		case 0: return tc.getName();
		case 1: return tc.getContent();
		}
		
		return "** not set column **";
	}

}
