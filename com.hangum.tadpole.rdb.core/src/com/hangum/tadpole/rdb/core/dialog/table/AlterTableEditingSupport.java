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
package com.hangum.tadpole.rdb.core.dialog.table;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

/**
 * SampleDAtaEditingSupport
 * 
 * @author hangum
 */
public class AlterTableEditingSupport extends EditingSupport {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableDialog.class);

	private final TableViewer viewer;
	private int columnIndex;

	public AlterTableEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			return new ComboBoxCellEditor(viewer.getTable(), DataTypeDef.getAllTypeNames(dao.getDbdef() ));
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX || columnIndex == AlterTableConsts.NULLABLE_IDX) {
			//return new CheckboxCellEditor(viewer.getTable(), SWT.CHECK);// | SWT.READ_ONLY);
			return new CheckboxCellEditor(null, SWT.CHECK);// | SWT.READ_ONLY);
		} else {
			return new TextCellEditor(viewer.getTable());
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		switch (columnIndex){
		case AlterTableConsts.SEQ_NO_IDX:
		case AlterTableConsts.COLUMN_ID_IDX:
			return false;
		default:
			return true;
		}
	}

	@Override
	protected Object getValue(Object element) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		if (columnIndex == AlterTableConsts.SEQ_NO_IDX) {
			return String.valueOf(dao.getSeqNo());
		} else if (columnIndex == AlterTableConsts.COLUMN_ID_IDX) {
			return String.valueOf(dao.getColumnId());
		} else if (columnIndex == AlterTableConsts.COLUMN_NAME_IDX) {
			return dao.getColumnName();
		} else if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			return DataTypeDef.getIndexByType(dao.getDbdef(), (Integer)dao.getDataType());
		} else if (columnIndex == AlterTableConsts.DATA_SIZE_IDX) {
			return String.valueOf(dao.getDataSize());
		} else if (columnIndex == AlterTableConsts.DATA_PRECISION_IDX) {
			return String.valueOf(dao.getDataPrecision());
		} else if (columnIndex == AlterTableConsts.DATA_SCALE_IDX) {
			return String.valueOf(dao.getDataScale());
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX) {
			return dao.isPrimaryKey();
		} else if (columnIndex == AlterTableConsts.NULLABLE_IDX) {
			return dao.isNullable();
		} else if (columnIndex == AlterTableConsts.DEFAULT_VALUE_IDX) {
			return dao.getDefaultValue();
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;
		
		if (columnIndex == AlterTableConsts.SEQ_NO_IDX) {
			dao.setSeqNo(Integer.parseInt(value.toString()));
		} else if (columnIndex == AlterTableConsts.COLUMN_ID_IDX) {
			dao.setColumnId(Integer.parseInt(value.toString()));
		} else if (columnIndex == AlterTableConsts.COLUMN_NAME_IDX) {
			dao.setColumnName(value.toString());
		} else if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			dao.setDataType(DataTypeDef.getTypeByIndex(dao.getDbdef(), (Integer)value) );
		} else if (columnIndex == AlterTableConsts.DATA_SIZE_IDX) {
			dao.setDataSize(Integer.parseInt(value.toString()));
		} else if (columnIndex == AlterTableConsts.DATA_PRECISION_IDX) {
			dao.setDataPrecision(Integer.parseInt(value.toString()));
		} else if (columnIndex == AlterTableConsts.DATA_SCALE_IDX) {
			dao.setDataScale(Integer.parseInt(value.toString()));
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX) {
			dao.setPrimaryKey(Boolean.parseBoolean(value.toString()));
		} else if (columnIndex == AlterTableConsts.NULLABLE_IDX) {
			//dao.setNullable(Boolean.parseBoolean(value.toString()));
			dao.setNullable((Boolean) value );
		} else if (columnIndex == AlterTableConsts.DEFAULT_VALUE_IDX) {
			dao.setDefaultValue(value.toString());
		}

		viewer.update(element, null);
	}

}
