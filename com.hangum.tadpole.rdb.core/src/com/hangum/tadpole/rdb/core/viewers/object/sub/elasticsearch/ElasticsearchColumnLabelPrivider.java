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

import com.hangum.tadpole.engine.query.dao.elasticsearch.ElasticColumnDAO;

/**
 * Elasticsearch 컬럼 정보
 * 
 * @author hangum
 *
 */
public class ElasticsearchColumnLabelPrivider extends LabelProvider implements ITableLabelProvider {

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
		ElasticColumnDAO tc = (ElasticColumnDAO)element;
		
		switch(columnIndex) {
		case 0: return tc.getName();
		case 1: return tc.getType();
		case 2: return tc.getField();
		}
		
		return "** not set column **";
	}

}
