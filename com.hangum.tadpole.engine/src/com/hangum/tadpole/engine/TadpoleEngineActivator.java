/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.hangum.tadpole.commons.libs.core.logs.LogListener;

public class TadpoleEngineActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hangum.tadpole.engine";

	// The shared instance
	private static TadpoleEngineActivator plugin;
	private ILogListener listener;
	
	/**
	 * The constructor
	 */
	public TadpoleEngineActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// eclipse 로그도 log4j에 넣어주도록 수정 ... (해야할지 살짝 의문이고 삭제해야할지도....) -hangum, 11.09
		listener = new LogListener();
		Platform.addLogListener(listener);	
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		Platform.removeLogListener(listener);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TadpoleEngineActivator getDefault() {
		return plugin;
	}

}
