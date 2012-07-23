package com.hangum.tadpole.mongodb.core.composite.result;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;

/**
 * tree label provider
 * 
 * @author hangum
 *
 */
public class TreeMongoLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MongodbTreeViewDTO mongoTree = (MongodbTreeViewDTO)element;
		switch(columnIndex) {
		case 0: return mongoTree.getKey();
		case 1: return mongoTree.getValue();
		case 2: return mongoTree.getType();
		}
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}