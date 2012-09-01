/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.hangum.db.log.LogConfiguration;
import com.hangum.db.log.LogListener;

public class CommonsSQLActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hangum.db.commons.sql";

	// The shared instance
	private static CommonsSQLActivator plugin;
	private ILogListener listener;
	
	/**
	 * The constructor
	 */
	public CommonsSQLActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		LogConfiguration.getInstance();//.setLevel(Level.DEBUG.toString());
		
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
	public static CommonsSQLActivator getDefault() {
		return plugin;
	}

}
