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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.engine.sql.util.DataTypeValidate;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * text editor
 * 
 * @author hangum
 *
 */
public class TextViewerEditingSupport extends EditingSupport {
	private static final Logger logger = Logger.getLogger(TextViewerEditingSupport.class);
	private TableDirectEditorComposite editPart;
	private int columnIndex;
	private final TableViewer viewer;
	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
	
	public TextViewerEditingSupport(TableDirectEditorComposite editPart, int columnIndex, TableViewer viewer) {
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
		String strData = TbUtils.getOriginalData(data.get(columnIndex)==null?"":data.get(columnIndex));

		return strData;
	}

	@Override
	protected void setValue(Object element, Object value) {
		HashMap<Integer, String> data = (HashMap<Integer, String>)element;
		HashMap<Integer, String> oldDataMap = (HashMap<Integer, String>)data.clone();
		
		String oldData = data.get(columnIndex)==null?"":data.get(columnIndex);
		if(logger.isDebugEnabled()) {
			logger.debug("original data :" + oldData + ":" + value.toString() + ":" + StringEscapeUtils.escapeXml(oldData));
		}
		String compareValue = StringEscapeUtils.escapeXml(value.toString());
		if(oldData.equals(compareValue)) return;

		// 입력 값이 올바른지 검사합니다.
		String colType = tableDataTypeList.get(columnIndex-1);
		if(!DataTypeValidate.isValid(editPart.getUserDB(), colType, value.toString())) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().InputValue + " '" + value + "'. " + Messages.get().TextViewerEditingSupport_2 + " is " + colType + ".");// + " " + Messages.get().TextViewerEditingSupport_3); 
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
