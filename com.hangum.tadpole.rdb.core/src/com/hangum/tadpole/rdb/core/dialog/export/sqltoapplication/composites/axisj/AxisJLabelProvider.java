/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.util.GlobalImageUtils;

/**
 * axisj label provider
 * 
 * @author nilriri
 *
 */
public class AxisJLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		AxisjHeaderDAO dao = (AxisjHeaderDAO) element;

		switch (columnIndex) {
		case AxisjConsts.HEADTOOL_IDX:
			if (dao.isColHeadTool())
				return GlobalImageUtils.getCheck();
			else
				return GlobalImageUtils.getUnCheck();
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