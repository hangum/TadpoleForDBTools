package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.AxisjHeaderDAO;
import com.hangum.tadpole.rdb.core.ext.Activator;
import com.swtdesigner.ResourceManager;

public class AxisJLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Image CHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/checked.gif"); //$NON-NLS-1$
	private static final Image UNCHECKED = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/unchecked.gif"); //$NON-NLS-1$

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		switch (columnIndex) {
		case 6:
			if (dao.isColHeadTool())
				return CHECKED;
			else
				return UNCHECKED;
		}

		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		switch (columnIndex) {
		case AxisjConsts.NO_IDX:
			return Integer.toString(dao.getSeqNo());
		case AxisjConsts.KEY_IDX:
			return dao.getKey();
		case AxisjConsts.LABEL_IDX:
			return dao.getLabel();
		case AxisjConsts.WIDTH_IDX:
			return Integer.toString(dao.getWidth());
		case AxisjConsts.ALIGN_IDX:
			return AxisjConsts.aligns[dao.getAlign()];
		case AxisjConsts.SORT_IDX:
			return AxisjConsts.sorts[dao.getSort()];
		case AxisjConsts.HEADTOOL_IDX:
			return "";//dao.isColHeadTool() ? "true" : "false"; 
		case AxisjConsts.FORMATTER_IDX:
			return dao.getFormatter();
		case AxisjConsts.TOOLTIP_IDX:
			return dao.getTooltip();
		case AxisjConsts.DISABLE_IDX:
			return dao.getDisabled();
		case AxisjConsts.CHECKED_IDX:
			return dao.getChecked();
		default:
			return null;
		}
	}

}