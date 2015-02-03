package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.sql.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;

/**
 * monitoring templdate labelprovider 
 * 
 * @author hangum
 *
 */
public class MonitoringTemplateLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TeadpoleMonitoringTemplateDAO dao = (TeadpoleMonitoringTemplateDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getMonitoring_type();
		case 1: return dao.getTitle();
		case 2: return dao.getDescription();
		case 3: return dao.getQuery();
		case 4: return dao.getParam_1_column();
		case 5: return dao.getParam_1_init_value();
		case 6: return dao.getParam_2_column();
		case 7: return dao.getParam_2_init_value();
		case 8: return dao.getIndex_nm();
		case 9: return dao.getCondition_type();
		case 10: return dao.getCondition_value();
		}

		return null;
	}

}
