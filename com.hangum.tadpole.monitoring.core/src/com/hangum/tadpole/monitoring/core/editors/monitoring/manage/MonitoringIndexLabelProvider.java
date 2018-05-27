package com.hangum.tadpole.monitoring.core.editors.monitoring.manage;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;

/**
 * monitoring index label provider
 * 
 * @author hangum
 *
 */
public class MonitoringIndexLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MonitoringIndexDAO dao = (MonitoringIndexDAO)element;
		
		switch(columnIndex) {
			case 0: return dao.getTitle();
			case 1: return dao.getKpi_type();
			case 2: return dao.getRead_method();
			case 3: return dao.getMonitoring_type();
			case 4: return dao.getIndex_nm();
			case 5: return dao.getCondition_type();
			case 6: return dao.getCondition_value();
			case 7: return dao.getAfter_type();
		}
		
		return "*** not set column ***";
	}
	
	
}
