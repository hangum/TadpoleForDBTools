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
package com.hangum.tadpole.mongodb.core.dialogs.collection.index;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * table comment editor
 * 
 * @author hangum
 *
 */
public class FieldIndexEditorSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6292003867430114514L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FieldIndexEditorSupport.class);

	private final TreeViewer viewer;
	private UserDBDAO userDB;
	public static final String[] arryIndexKey 	= new String[] {"", "Ascending", "Desending", "Geospatial"};
	public static final Object[] arryIndexValue = new Object[] {0, 		1, 			-1, 		"2d"};

	/**
	 * 
	 * @param viewer
	 * @param explorer
	 */
	public FieldIndexEditorSupport(TreeViewer viewer, UserDBDAO userDB) {
		super(viewer);
		
		this.viewer = viewer;
		this.userDB = userDB;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new ComboBoxCellEditor(viewer.getTree(), arryIndexKey, SWT.READ_ONLY);		
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		CollectionFieldDAO dao = (CollectionFieldDAO) element;
		for (int i=0; i<arryIndexKey.length; i++) {
			if(arryIndexKey[i].equals(dao.getNewIndex())) return i;
		}
		
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		try {
			CollectionFieldDAO dao = (CollectionFieldDAO) element;
			
			String tmpVal = arryIndexKey[Integer.valueOf(""+value)];			
			dao.setNewIndex(tmpVal);
		} catch (Exception e) {
			logger.error("setValue error ", e);
		}
		viewer.update(element, null);
	}

}
