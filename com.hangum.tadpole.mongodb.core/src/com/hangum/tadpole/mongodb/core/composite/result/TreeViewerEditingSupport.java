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
package com.hangum.tadpole.mongodb.core.composite.result;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.core.utils.MongoDBJavaStrToJavaObj;

/**
 * data direct editor
 * 
 * @author hangum
 *
 */
public class TreeViewerEditingSupport extends EditingSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TreeViewerEditingSupport.class);

	private UserDBDAO userDB;
	private String collectionName;
	private TreeViewer viewer;

	public TreeViewerEditingSupport(UserDBDAO userDB, String collectionName, TreeViewer viewer) {
		super(viewer);
		this.userDB = userDB;
		this.collectionName = collectionName;
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTree());
	}

	@Override
	protected boolean canEdit(Object element) {
		MongodbTreeViewDTO dto = (MongodbTreeViewDTO)element;
		if( "Array".equals(dto.getType()) || //$NON-NLS-1$
			"Document".equals(dto.getType()) //$NON-NLS-1$
		){
			return false;
		}
		
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		MongodbTreeViewDTO dto = (MongodbTreeViewDTO)element;
		
		return dto.getValue();
	}

	@Override
	protected void setValue(Object element, Object value) {
		MongodbTreeViewDTO dto = (MongodbTreeViewDTO)element;
		
		// data 수정하자
		if( dto.getValue().equals(value.toString()) ) return;
		
		try {
			// 데이터가 올바른지 검사합니다.
			MongoDBJavaStrToJavaObj.convStrToObj(dto.getType(), value.toString());
			
			// fully key를 검색합니다.
			String fullyKey = findRealKey(dto, dto.getRealKey());
			if(logger.isDebugEnabled())	logger.debug("====>[update][real key]======> " + fullyKey + "\t [value]" + value.toString());
			
			MongoDBQuery.updateDocument(userDB, collectionName, dto.getDbObject(), fullyKey, value.toString());
			
		} catch(Exception e) {
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().TreeViewerEditingSupport_3 +  dto.getType() + Messages.get().TreeViewerEditingSupport_4);
			return;
		}
		
		dto.setValue(value.toString());
		viewer.refresh();
	}
	
	/** 
	 * real key path를 얻는다
	 * 
	 *  @param dto
	 *  @param key
	 */
	private String findRealKey(MongodbTreeViewDTO dto, String key) {
		String retVal = "";
		MongodbTreeViewDTO tmpDto = dto.getParent();
//		logger.debug("[key]" + key + "[retVal]"+ retVal);
		
		if( tmpDto != null) {
			if(tmpDto.getRealKey() != null) {
				retVal += findRealKey(tmpDto, tmpDto.getRealKey() + "." + key);
			} else {
				retVal = key;
			}
		} else {
			retVal = key;
		}
		
		return retVal;
	}

}
