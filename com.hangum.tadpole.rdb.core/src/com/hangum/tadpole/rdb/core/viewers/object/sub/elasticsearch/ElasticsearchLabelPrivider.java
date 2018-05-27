/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.elasticsearch;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.elasticsearch.ElastIndexDAO;

/**
 * Elasticsearch 컬럼 정보
 * 
 * @author hangum
 *
 */
public class ElasticsearchLabelPrivider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
//		if(columnIndex == 0) return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/javascript.png"); //$NON-NLS-1$
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ElastIndexDAO tc = (ElastIndexDAO)element;
		
		switch(columnIndex) {
		case 0: return tc.getHealth();
		case 1: return tc.getStatus();
		case 2: return tc.getIndex();
		case 3: return tc.getUuid();
		case 4: return tc.getPri();
		case 5: return tc.getRep();
		case 6: return tc.getDocs_count();
		case 7: return tc.getDocs_deleted();
		case 8: return tc.getStore_size();
		case 9: return tc.getPri_store_size();
		
		}
		
		return "** not set column **";
	}

}
