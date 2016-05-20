package com.hangum.tadpole.tajo.core;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.hangum.tadpole.commons.libs.core.logs.LogConfiguration;
import com.hangum.tadpole.commons.libs.core.logs.LogListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hangum.tadpole.tajo.core"; //$NON-NLS-1$

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
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
