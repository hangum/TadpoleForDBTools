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

import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TableColumnObjectQuery;

/**
 * alter table editing support
 * 
 * @author hangum
 */
public class AlterTableEditingSupport extends EditingSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableDialog.class);

	private final TableViewer viewer;
	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private int columnIndex;

	public AlterTableEditingSupport(TableViewer viewer, int columnIndex, UserDBDAO userDB, TableDAO tableDAO) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
		this.userDB = userDB;
		this.tableDAO = tableDAO; 
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			return new ComboBoxCellEditor(viewer.getTable(), DataTypeDef.getAllTypeNames(dao.getDbdef()), SWT.READ_ONLY);
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX || columnIndex == AlterTableConsts.NULLABLE_IDX) {
			return new CheckboxCellEditor(null, SWT.CHECK);// | SWT.READ_ONLY);
		} else {
			return new TextCellEditor(viewer.getTable());
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		switch (columnIndex){
//		case AlterTableConsts.SEQ_NO_IDX:
//		case AlterTableConsts.COLUMN_ID_IDX:
//			return false;
		case AlterTableConsts.DATA_SIZE_IDX:
//			return dao.isUseSize();
//		case AlterTableConsts.DATA_PRECISION_IDX:
//		case AlterTableConsts.DATA_SCALE_IDX:
//			return dao.isUsePrecision();
		default:
			return true;
		}
	}

	@Override
	protected Object getValue(Object element) {
		AlterTableMetaDataDAO metaDao = (AlterTableMetaDataDAO) element;

//		if (columnIndex == AlterTableConsts.SEQ_NO_IDX) {
//			return String.valueOf(metaDao.getSeqNo());
//		} else if (columnIndex == AlterTableConsts.COLUMN_ID_IDX) {
//			return String.valueOf(dao.getColumnId());
//		} else 
		if (columnIndex == AlterTableConsts.COLUMN_NAME_IDX) {
			return metaDao.getColumnName();
		} else if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			return DataTypeDef.getIndexByType(metaDao.getDbdef(), Integer.parseInt(""+metaDao.getDataType()) );
		} else if (columnIndex == AlterTableConsts.DATA_SIZE_IDX) {
			return String.valueOf(metaDao.getDataSize());
//		} else if (columnIndex == AlterTableConsts.DATA_PRECISION_IDX) {
//			return String.valueOf(metaDao.getDataPrecision());
//		} else if (columnIndex == AlterTableConsts.DATA_SCALE_IDX) {
//			return String.valueOf(metaDao.getDataScale());
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX) {
			return metaDao.isPrimaryKey();
		} else if (columnIndex == AlterTableConsts.NULLABLE_IDX) {
			return metaDao.isNullable();
		} else if (columnIndex == AlterTableConsts.DEFAULT_VALUE_IDX) {
			return metaDao.getDefaultValue();
		} else if (columnIndex == AlterTableConsts.COMMENT_IDX) {
			return metaDao.getComment();
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		AlterTableMetaDataDAO metadataDao = (AlterTableMetaDataDAO) element;
		
//		if (columnIndex == AlterTableConsts.SEQ_NO_IDX) {
//			int changeValue = makeStringToInt(value.toString());
//			if(changeValue == dao.getSeqNo()) return;
//			dao.setSeqNo(changeValue);
//		} else if (columnIndex == AlterTableConsts.COLUMN_ID_IDX) {
//			dao.setColumnId(Integer.parseInt(value.toString()));
//		} else 
		if (columnIndex == AlterTableConsts.COLUMN_NAME_IDX) {
			String strNewColumnName = value.toString();
			if(metadataDao.getColumnName().equals(strNewColumnName)) return;
			if(!"".equals(strNewColumnName)) { 
				try {
					TableColumnObjectQuery.renameColumn(userDB, tableDAO, metadataDao, strNewColumnName);
					metadataDao.setColumnName(strNewColumnName);
				} catch (Exception e) {
					TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "Column name을 변경하는 중에" + e.getMessage());
					errDialog.open();
				}
				
			}
		} else if (columnIndex == AlterTableConsts.DATA_TYPE_IDX) {
			int dataType = DataTypeDef.getTypeByIndex(metadataDao.getDbdef(), (Integer)value);
			if(metadataDao.getDataType() == dataType) return;
			String strTypeName = DataTypeDef.getTypeName(userDB.getDBDefine(), dataType);
			
			try {
				AlterTableMetaDataDAO upMeataData = cloneAlterTable(metadataDao);
				upMeataData.setDataType(dataType);
				upMeataData.setDataTypeName(strTypeName);
				if(!DataTypeDef.isSecondArgument(userDB.getDBDefine(), dataType)) {
					upMeataData.setDataSize(0);
				}
				
				TableColumnObjectQuery.modifyColumnType(userDB, tableDAO, upMeataData);
				metadataDao = upMeataData;
				
			} catch (Exception e) {
				logger.error("clone object", e);
				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "Column name을 변경하는 중에" + e.getMessage());
				errDialog.open();
			}
		} else if (columnIndex == AlterTableConsts.DATA_SIZE_IDX) {
			int changeValue = makeStringToInt(value.toString());
			if(metadataDao.getDataSize() == changeValue) return;
			
			metadataDao.setDataSize(changeValue);
//		} else if (columnIndex == AlterTableConsts.DATA_PRECISION_IDX) {
//			int changeValue = makeStringToInt(value.toString());
//			if(dao.getDataPrecision() == changeValue) return;
//			
//			dao.setDataPrecision(changeValue);
//		} else if (columnIndex == AlterTableConsts.DATA_SCALE_IDX) {
//			int changeValue = makeStringToInt(value.toString());
//			if(dao.getDataScale() == changeValue) return;
//			
//			dao.setDataScale(changeValue);
		} else if (columnIndex == AlterTableConsts.PRIMARY_KEY_IDX) {
			boolean bool = Boolean.parseBoolean(value.toString());
			if(metadataDao.isPrimaryKey() == bool) return;
			
			metadataDao.setPrimaryKey(bool);
		} else if (columnIndex == AlterTableConsts.NULLABLE_IDX) {
			boolean bool = Boolean.parseBoolean(value.toString());
			if(metadataDao.isNullable() == bool) return;
			
			//dao.setNullable(Boolean.parseBoolean(value.toString()));
			metadataDao.setNullable(bool);
		} else if (columnIndex == AlterTableConsts.DEFAULT_VALUE_IDX) {
			if(value.toString().equals(metadataDao.getDefaultValue())) return;
			
			metadataDao.setDefaultValue(value.toString());
		} else if (columnIndex == AlterTableConsts.COMMENT_IDX) {
			if(metadataDao.getComment().equals(value.toString())) return;
			
			metadataDao.setComment(value.toString());
		}
		if(logger.isDebugEnabled()) logger.debug("Changed data message");
		
//		if(metadataDao.getDataStatus() != DATA_TYPE.INSERT) metadataDao.setDataStatus(DATA_TYPE.MODIFY);
		viewer.update(metadataDao, null);
	}
	
	/**
	 * make string to integer
	 * 
	 * @param value
	 * @return
	 */
	private int makeStringToInt(String value) {
		if(value.equals("")) value = "0";
		return Integer.parseInt(value);
	}
	
	private AlterTableMetaDataDAO cloneAlterTable(AlterTableMetaDataDAO oldObj) throws Exception{
		return (AlterTableMetaDataDAO)oldObj.clone();
	}
}
