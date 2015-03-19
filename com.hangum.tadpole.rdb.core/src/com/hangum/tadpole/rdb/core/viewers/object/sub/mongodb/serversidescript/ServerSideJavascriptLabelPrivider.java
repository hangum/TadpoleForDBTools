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
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.serversidescript;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * ServerSide Javascript 컬럼 정보
 * 
 * @author hangum
 *
 */
public class ServerSideJavascriptLabelPrivider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex == 0) return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/javascript.png"); //$NON-NLS-1$
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
