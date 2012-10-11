/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jeong jaehong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dialogs.message.dao.SQLLibraryDAO;

public class SqlLibraryEditorSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580056065989895412L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SqlLibraryEditorSupport.class);

	private final TableViewer viewer;
	private int columnIndex;
	private UserDBDAO userDB;

	public SqlLibraryEditorSupport(TableViewer viewer, int columnIndex, UserDBDAO userDB) {
		super(viewer);
		this.viewer = viewer;
		this.columnIndex = columnIndex;
		this.userDB = userDB;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		logger.debug("-----------getCellEditor---------------");
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return (columnIndex > 0);
	}

	@Override
	protected Object getValue(Object element) {
		try {
			SQLLibraryDAO dao = (SQLLibraryDAO) element;

			switch (columnIndex) {
			case 0:
				return dao.getSeqNo() + "";
			case 1:
				return dao.getTitle();
			case 2:
				return dao.getSQLText();
			case 3:
				return dao.getDescription();
			}

			return null;
		} catch (Exception e) {
			logger.error("getValue error ", e);
			return "";
		}
	}

	@Override
	protected void setValue(Object element, Object value) {
		try {
			SQLLibraryDAO dao = (SQLLibraryDAO) element;

			switch (columnIndex) {
			case 0:
				dao.setSeqNo((Integer) value);
				break;
			case 1:
				dao.setTitle((String) value);
				break;
			case 2:
				dao.setSqltext((String) value);
				break;
			case 3:
				dao.setDescription((String) value);
				break;
			}

			viewer.update(element, null);
		} catch (Exception e) {
			logger.error("setValue error ", e);
		}
		viewer.update(element, null);
	}
}
