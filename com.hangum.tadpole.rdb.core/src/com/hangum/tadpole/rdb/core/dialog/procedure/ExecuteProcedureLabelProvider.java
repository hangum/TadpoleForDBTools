package com.hangum.tadpole.rdb.core.dialog.procedure;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.dao.rdb.InOutParameterDAO;

public class ExecuteProcedureLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		InOutParameterDAO dao = (InOutParameterDAO) element;

		switch (columnIndex) {
		case 0:
			return Integer.toString(dao.getOrder());
		case 1:
			return dao.getName();
		case 2:
			return dao.getType();
		case 3:
			return dao.getRdbType();
		case 4:
			return dao.getLength();
		case 5:
			return dao.getValue();
		default:
			return null;
		}
	}

}
