package com.hangum.tadpole.monitoring.core.editors.monitoring;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;

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
		MonitoringIndexDAO dao = (MonitoringIndexDAO)element;
		
		JsonObject jsonObj = dao.getResultJson();
		if(jsonObj != null) {
			JsonElement jsonValue = jsonObj.get(dao.getIndex_nm().toLowerCase());
			String strIndexValue = jsonValue != null?jsonValue.getAsString():"";
			
			switch(columnIndex) {
				case 0: return dao.getUserDB().getDisplay_name();
				case 1: return dao.getTitle();
				case 2: return strIndexValue;
				case 3: return dao.getIndex_value();
				case 4: return jsonObj.toString();
			}
			
		}
		
		return null;
	}


}
