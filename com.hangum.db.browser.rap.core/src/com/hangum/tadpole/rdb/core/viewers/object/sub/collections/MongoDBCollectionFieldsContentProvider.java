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
package com.hangum.tadpole.rdb.core.viewers.object.sub.collections;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;

/**
 * mongodb collection field content provider
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionFieldsContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List listDao = (List)inputElement;
		return listDao.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		CollectionFieldDAO dao = (CollectionFieldDAO)parentElement;
		if(!dao.getChildren().isEmpty()) {
			return dao.getChildren().toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		CollectionFieldDAO dao = (CollectionFieldDAO)element;
		return dao.getChildren().isEmpty()?false:true;
	}
}
