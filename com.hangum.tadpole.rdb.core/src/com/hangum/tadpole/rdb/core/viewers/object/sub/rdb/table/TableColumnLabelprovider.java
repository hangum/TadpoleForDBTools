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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.ITableDecorationExtension;
import com.hangum.tadpole.rdb.core.viewers.object.ObjectExploreDefine;
import com.swtdesigner.ResourceManager;

/**
 * TABLE, VIEW의 컬럼 정보
 * 
 * @author hangum
 *
 */
public class TableColumnLabelprovider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(TableColumnLabelprovider.class);
	TableViewer tableListViewer;
	ITableDecorationExtension tableDecorationExtension;
	
	public TableColumnLabelprovider() {}
	
	public TableColumnLabelprovider(TableViewer tableListViewer, ITableDecorationExtension tableDecorationExtension) {
		this.tableListViewer = tableListViewer;
		this.tableDecorationExtension = tableDecorationExtension;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		TableColumnDAO tc = (TableColumnDAO) element;
		
		if(columnIndex == 0)  {
			Image imageBase = null;
			
			if(PublicTadpoleDefine.isPK(tc.getKey())) 		imageBase = ObjectExploreDefine.IMAGE_PRIMARY_KEY; 
			else if(PublicTadpoleDefine.isFK(tc.getKey())) 	imageBase = ObjectExploreDefine.IMAGE_FOREIGN_KEY; 
			else if(PublicTadpoleDefine.isMUL(tc.getKey())) imageBase = ObjectExploreDefine.IMAGE_MULTI_KEY;
			else 											imageBase = ObjectExploreDefine.IMAGE_COLUMN;
			
			// image decoration extension point 
			try {
				if(tableListViewer != null && tableDecorationExtension != null) {
					IStructuredSelection iss = (IStructuredSelection)tableListViewer.getSelection();
					TableDAO table = (TableDAO)iss.getFirstElement();
					
					Image imageExtension = tableDecorationExtension.getColumnImage(table.getName(), tc.getField());
					if(imageExtension != null) {
						return ResourceManager.decorateImage(imageBase, imageExtension, ResourceManager.BOTTOM_RIGHT);
					}
				}
			} catch(Exception e){
				logger.error("extension point exception", e);
			}
			
			return imageBase;
		}
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableColumnDAO tc = (TableColumnDAO) element;
			
		switch(columnIndex) {
		case 0: return tc.getField();
		case 1: return tc.getType();
		case 2: return tc.getKey();
		case 3: return tc.getComment();
		case 4: return tc.getNull();
		case 5: return tc.getDefault();
		case 6: return tc.getExtra();
		}
		return null;
	}

}
