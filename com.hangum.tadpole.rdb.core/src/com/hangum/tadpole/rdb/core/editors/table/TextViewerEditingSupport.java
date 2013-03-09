/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.table;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.datetype.DataTypeValidate;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * text editor
 * 
 * @author hangum
 *
 */
public class TextViewerEditingSupport extends EditingSupport {
	private static final Logger logger = Logger.getLogger(TextViewerEditingSupport.class);
	private TableViewerEditPart editPart;
	private int columnIndex;
	private final TableViewer viewer;
	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
	
	public TextViewerEditingSupport(TableViewerEditPart editPart, int columnIndex, TableViewer viewer) {
		super(viewer);
		
		this.editPart 		= editPart;
		this.columnIndex 	= columnIndex;
		this.viewer 		= viewer;
		this.tableDataTypeList = editPart.getTableDataTypeList();
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		HashMap<Integer, String> data = (HashMap<Integer, String>)element;
		
		return TbUtils.getOriginalData(data.get(columnIndex)==null?"":data.get(columnIndex));
	}

	@Override
	protected void setValue(Object element, Object value) {
		HashMap<Integer, String> data = (HashMap<Integer, String>)element;
		HashMap<Integer, String> oldDataMap = (HashMap<Integer, String>)data.clone();
		
		String oldData = data.get(columnIndex)==null?"":data.get(columnIndex);
		if(oldData.equals(value.toString())) return;

		// 입력 값이 올바른지 검사합니다.
		String colType = tableDataTypeList.get(columnIndex-1);
		if(!DataTypeValidate.isValid(editPart.getUserDB(), colType, value.toString())) {
			MessageDialog.openError(null, Messages.TextViewerEditingSupport_0, Messages.TextViewerEditingSupport_1 + value + Messages.TextViewerEditingSupport_2 + colType + Messages.TextViewerEditingSupport_3); 
			return;
		} 
		// insert가 아닌 경우에는 
		if(!TbUtils.isInsert(data.get(0))) {
			data.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.UPDATE));
		}
		// 수정된 데이터 표시
		data.put(columnIndex, TbUtils.getModifyData(value.toString()));
		editPart.setModifyButtonControl();
		
		viewer.refresh();
	}

}
