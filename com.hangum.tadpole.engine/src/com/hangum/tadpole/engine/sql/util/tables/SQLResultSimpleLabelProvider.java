package com.hangum.tadpole.engine.sql.util.tables;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;

/**
 * SQLResultLableProvider
 * 
 * @author hangum
 *
 */
public class SQLResultSimpleLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	protected ResultSetUtilDTO rsDAO;
	protected Map<Integer, Integer> _showColumnIndex;
	
	public SQLResultSimpleLabelProvider() {
	}
	
	public SQLResultSimpleLabelProvider(final ResultSetUtilDTO rsDAO, Map<Integer, Integer> _showColumnIndex) {
		this.rsDAO = rsDAO;
		this._showColumnIndex = _showColumnIndex;
	}


	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		
		return null;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		int realColumnIndex = _showColumnIndex.get(columnIndex);
		Object obj = rsResult.get(realColumnIndex);
		if(obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
}
