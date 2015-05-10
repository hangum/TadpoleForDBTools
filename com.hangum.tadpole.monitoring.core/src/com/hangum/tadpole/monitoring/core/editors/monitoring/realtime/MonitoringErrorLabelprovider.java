package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringDashboardDAO;

/**
 * monitoring label provider
 * 
 * @author hangum
 *
 */
public class MonitoringErrorLabelprovider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MonitoringDashboardDAO dao = (MonitoringDashboardDAO)element;

		switch(columnIndex) {
			case 0: return dao.getStart_time().toString();
			
			case 1: return dao.getDisplay_name();
			case 2: return dao.getKpi_type();
			case 3: return dao.getTitle();
			case 4: return dao.getDescription();
			case 5: return ""+dao.getWarring_cnt();
			case 6: return ""+dao.getCritical_cnt();
		}
	
		return null;
	}


}
