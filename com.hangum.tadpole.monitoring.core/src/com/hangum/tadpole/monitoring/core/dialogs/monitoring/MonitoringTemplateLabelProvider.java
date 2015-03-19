package com.hangum.tadpole.monitoring.core.dialogs.monitoring;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;

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
		case 0: return dao.getTemplate_type();
		case 1: return dao.getMonitoring_type();
		case 2: return dao.getKpi_type();
		case 3: return dao.getTitle();
		case 4: return dao.getDescription();
		case 5: return dao.getQuery();
		case 6: return dao.getParam_1_column();
		case 7: return dao.getParam_1_init_value();
		case 8: return dao.getParam_2_column();
		case 9: return dao.getParam_2_init_value();
		case 10: return dao.getIndex_nm();
		case 11: return dao.getCondition_type();
		case 12: return dao.getCondition_value();
		}

		return null;
	}

}
