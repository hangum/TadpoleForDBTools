package com.hangum.tadpole.rdb.core.dialog.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

public class AlterTableLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Image CHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/checked.gif"); //$NON-NLS-1$
	private static final Image UNCHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/unchecked.gif"); //$NON-NLS-1$

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;
		
		switch (columnIndex) {
		case AlterTableConsts.PRIMARY_KEY_IDX:
			if (dao.isPrimaryKey())
				return CHECKED;
			else
				return UNCHECKED;
		case AlterTableConsts.NULLABLE_IDX:
			if (dao.isNullable())
				return CHECKED;
			else
				return UNCHECKED;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		AlterTableMetaDataDAO dao = (AlterTableMetaDataDAO) element;

		switch (columnIndex) {
		case AlterTableConsts.SEQ_NO_IDX:
			return String.valueOf(dao.getSeqNo());
		case AlterTableConsts.COLUMN_NAME_IDX:
			return dao.getColumnName();
		case AlterTableConsts.COLUMN_ID_IDX:
			return String.valueOf(dao.getColumnId());
		case AlterTableConsts.DATA_TYPE_IDX:
			return DataTypeDef.getTypeName(dao.getDbdef(), dao.getDataType());
		case AlterTableConsts.DATA_SIZE_IDX:
			return String.valueOf(dao.getDataSize());
		case AlterTableConsts.DATA_PRECISION_IDX:
			return String.valueOf(dao.getDataPrecision());
		case AlterTableConsts.DATA_SCALE_IDX:
			return String.valueOf(dao.getDataScale());
		default:
			return null;
		}
	}
}
