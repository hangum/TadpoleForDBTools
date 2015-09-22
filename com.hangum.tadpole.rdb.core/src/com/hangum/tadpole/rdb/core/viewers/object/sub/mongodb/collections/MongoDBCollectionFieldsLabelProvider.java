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
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.collections;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * Mongodb collection field label provide
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionFieldsLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		CollectionFieldDAO dao = (CollectionFieldDAO) element;
		
		if(columnIndex == 0) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(dao.getKey())) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/index_column.png"); //$NON-NLS-1$
			}
			
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/column.png"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		CollectionFieldDAO dao = (CollectionFieldDAO) element;
		
		switch(columnIndex) {
		case 0: return dao.getField();
		case 1: return dao.getType();
		case 2: return dao.getKey();
		}
		return null;
	}

}
