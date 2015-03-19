package com.hangum.tadpole.monitoring.core.editors.monitoring.manage;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;

/**
 * monitoring label provider
 * 
 * @author hangum
 *
 */
public class MonitoringResultLabelprovider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MonitoringResultDAO dao = (MonitoringResultDAO)element;
			
		switch(columnIndex) {
			case 0: return dao.getMonitoringIndexDAO().getTitle();
			case 1: return dao.getResult();
			case 2: return dao.getIndex_value();
			case 3: return dao.getUser_description();
			case 4: return dao.getSystem_description();
			case 5: return dao.getQuery_result();
			case 6: return dao.getCreate_time().toString();
		}
	
		return null;
	}


}
