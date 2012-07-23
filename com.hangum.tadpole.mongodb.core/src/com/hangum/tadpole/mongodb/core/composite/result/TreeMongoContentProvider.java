package com.hangum.tadpole.mongodb.core.composite.result;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;

/**
 * tree view content provider
 * 
 * @author hangum
 *
 */
public class TreeMongoContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<MongodbTreeViewDTO>)inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((MongodbTreeViewDTO)parentElement).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		MongodbTreeViewDTO mongoTree = (MongodbTreeViewDTO)element;
		if( mongoTree.getChildren().toArray().length > 0) return true;
		return false;
	}
	
}