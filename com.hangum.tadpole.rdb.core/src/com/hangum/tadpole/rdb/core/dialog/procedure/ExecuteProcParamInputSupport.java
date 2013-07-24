///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.dialog.procedure;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jface.viewers.CellEditor;
//import org.eclipse.jface.viewers.EditingSupport;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TextCellEditor;
//
//import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
//
///**
// * SampleDAtaEditingSupport
// * 
// * @author hangum
// */
//public class ExecuteProcParamInputSupport extends EditingSupport {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 6917152389392816571L;
//
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(ExecuteProcedureDialog.class);
//
//	private final int EDITABLE_COLUMN_INDEX = 5;
//	
//	private final TableViewer viewer;
//	private int columnIndex;
//
//	public ExecuteProcParamInputSupport(TableViewer viewer, int columnIndex) {
//		super(viewer);
//
//		this.viewer = viewer;
//		this.columnIndex = columnIndex;
//	}
//
//	@Override
//	protected CellEditor getCellEditor(Object element) {
//		InOutParameterDAO dao = (InOutParameterDAO) element;
//
//		if (columnIndex == EDITABLE_COLUMN_INDEX) {
//
//			return new TextCellEditor(viewer.getTable());
//
//		} else {
//
//			return null;
//
//		}
//	}
//
//	@Override
//	protected boolean canEdit(Object element) {
//		InOutParameterDAO dao = (InOutParameterDAO) element;
//
//		if (columnIndex == EDITABLE_COLUMN_INDEX) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	protected Object getValue(Object element) {
//		InOutParameterDAO dao = (InOutParameterDAO) element;
//
//		if (columnIndex == EDITABLE_COLUMN_INDEX) {
//			return dao.getValue();
//		}
//
//		return null;
//	}
//
//	@Override
//	protected void setValue(Object element, Object value) {
//		InOutParameterDAO dao = (InOutParameterDAO) element;
//		if (columnIndex == EDITABLE_COLUMN_INDEX) {
//			dao.setValue(value.toString());
//		}
//
//		viewer.update(element, null);
//	}
//
//}
