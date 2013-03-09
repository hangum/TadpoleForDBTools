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
package com.hangum.tadpole.rdb.core;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.hangum.tadpole.log.LogConfiguration;
import com.hangum.tadpole.log.LogListener;
import com.hangum.tadpole.start.TadpoleSystem;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	private static final Logger logger = Logger.getLogger(Activator.class);
	// The plug-in ID
	public static final String PLUGIN_ID = "com.hangum.tadpole.rdb.core"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private ILogListener listener;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// log level설정
		LogConfiguration.getInstance();
		
		// eclipse 로그 설정
		listener = new LogListener();
		Platform.addLogListener(listener);
		
		// tadpole system 이 시작시 해야하는 부분 설정. 
		TadpoleSystem.startInit();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static Activator getDefault() {
		return plugin;
	}

}
