package com.hangum.tadpole.sql.format.core;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.hangum.tadpole.commons.libs.core.logs.LogListener;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private ILogListener listener;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// log level설정
//		LogConfiguration.getInstance();
		
		// eclipse 로그도 log4j에 넣어주도록 수정 ... (해야할지 살짝 의문이고 삭제해야할지도....) -hangum, 11.09
		listener = new LogListener();
		Platform.addLogListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		Platform.removeLogListener(listener);
	}

}
