package com.hangum.db.browser.rap.core.actions.adapter.rdb;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

/**
 * action filter 
 * 
 * @author hangum
 *
 */
public class DBTypeActionFilterAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IActionFilter.class) {
			return new DBTypeActionFilterAdapter();
		}
		
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {IActionFilter.class};
	}

}
