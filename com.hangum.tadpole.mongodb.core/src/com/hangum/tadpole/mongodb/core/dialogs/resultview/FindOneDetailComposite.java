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
package com.hangum.tadpole.mongodb.core.dialogs.resultview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.hangum.tadpole.mongodb.core.composite.result.TreeMongoContentProvider;
import com.hangum.tadpole.mongodb.core.composite.result.TreeMongoLabelProvider;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * collection detail composite
 * 
 * @author hangum
 *
 */
public class FindOneDetailComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindOneDetailComposite.class);
	
	private String collectionName;
	private DBObject dbResultObject;

	private List<MongodbTreeViewDTO> listTrees;
	private TreeViewer treeViewerMongo;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param collectionName
	 * @param dbResultObject 
	 */
	public FindOneDetailComposite(Composite parent, String collectionName, DBObject dbResultObject) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.collectionName = collectionName;
		this.dbResultObject = dbResultObject;
		
		treeViewerMongo = new TreeViewer(this, SWT.BORDER | SWT.VIRTUAL | SWT.FULL_SELECTION);		
		Tree tree = treeViewerMongo.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTreeColumn();
		
		treeViewerMongo.setContentProvider(new TreeMongoContentProvider() );
		treeViewerMongo.setLabelProvider(new TreeMongoLabelProvider());
		
		initData();
	}
	
	/**
	 * init composite
	 */
	private void initData() {
		listTrees = new ArrayList<MongodbTreeViewDTO>();
		try {
			MongodbTreeViewDTO treeDto = new MongodbTreeViewDTO(dbResultObject, "", "", "Document");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			parserTreeObject(dbResultObject, treeDto, dbResultObject);
			listTrees.add(treeDto);
			
			treeViewerMongo.setInput(listTrees);			
			treeViewerMongo.expandToLevel(2);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * parser tree obejct
	 * 
	 * @param rootDbObject
	 * @param treeDto
	 * @param dbObject
	 */
	private void parserTreeObject(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final DBObject dbObject) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		Map<Integer, String> tmpMapColumns = MongoDBTableColumn.getTabelColumnView(dbObject);
		for(int i=0; i<tmpMapColumns.size(); i++)	{
			MongodbTreeViewDTO tmpTreeDto = new MongodbTreeViewDTO();
			tmpTreeDto.setDbObject(rootDbObject);
			
			String keyName = tmpMapColumns.get(i);			
			Object keyVal = dbObject.get(keyName);
			
			tmpTreeDto.setRealKey(keyName);
			// is sub document
			if( keyVal instanceof BasicDBObject ) {
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " {..}"); //$NON-NLS-1$
				tmpTreeDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, tmpTreeDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList dbObjectList = (BasicDBList)keyVal;
				
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " [" + dbObjectList.size() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				tmpTreeDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, tmpTreeDto, dbObjectList);
			} else {
				tmpTreeDto.setKey(tmpMapColumns.get(i));
				tmpTreeDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) tmpTreeDto.setValue(""); //$NON-NLS-1$
				else tmpTreeDto.setValue(keyVal.toString());
			}
			
			// 컬럼의 데이터를 넣는다.
			listTrees.add(tmpTreeDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * object array
	 * 
	 * @param rootDbObject
	 * @param treeDto
	 * @param dbObject
	 * @throws Exception
	 */
	private void parseObjectArray(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final BasicDBList dbObjectList) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		for(int i=0; i<dbObjectList.size(); i++) {
			MongodbTreeViewDTO mongodbDto = new MongodbTreeViewDTO();
			
			mongodbDto.setRealKey("" + i ); //$NON-NLS-1$
			mongodbDto.setKey("(" + i + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			mongodbDto.setDbObject(rootDbObject);

			Object keyVal = dbObjectList.get(i);
			if( keyVal instanceof BasicDBObject ) {
				mongodbDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, mongodbDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList tmpDbObjectList = (BasicDBList)keyVal;
				
				mongodbDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, mongodbDto, tmpDbObjectList);
			} else {
				mongodbDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) mongodbDto.setValue(""); //$NON-NLS-1$
				else mongodbDto.setValue(keyVal.toString());
			}
			
			listTrees.add(mongodbDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * treeview create
	 */
	private void createTreeColumn() {
		String[] columnName = {"Key", "Value", "Type"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int[] columnSize = {140, 200, 140};
		
		try {
			for(int i=0; i<columnName.length; i++) {
				final TreeViewerColumn tableColumn = new TreeViewerColumn(treeViewerMongo, SWT.LEFT);
				tableColumn.getColumn().setText( columnName[i] );
				tableColumn.getColumn().setWidth( columnSize[i] );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Tree view Editor", e); //$NON-NLS-1$
		}		
	}

	@Override
	protected void checkSubclass() {	
	}

}
