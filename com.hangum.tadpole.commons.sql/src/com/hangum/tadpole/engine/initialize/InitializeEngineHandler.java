/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.initialize;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

import com.hangum.tadpole.engine.license.LicenseExtensionHandler;

/**
 * initialize engine id
 * @author hangum
 *
 */
public class InitializeEngineHandler {
	private static final Logger logger = Logger.getLogger(LicenseExtensionHandler.class);
	private static final String INITIALIZE_ENGINE_ID = "com.hangum.tadpole.engine.initialize.engine";

	/**
	 * extension initialize id
	 * 
	 * @return
	 */
	public InitializeEngine[] initializeEngine() {
		final LinkedList listReturn = new LinkedList();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(INITIALIZE_ENGINE_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object initializeExtension = e.createExecutableExtension("class");
				if (initializeExtension instanceof InitializeEngine) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception initialize engine", exception);
						}

						@Override
						public void run() throws Exception {
							InitializeEngine compositeExt = (InitializeEngine) initializeExtension;
							compositeExt.initialize();
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("Create License extension exception", ex);
		}
		
		return (InitializeEngine[]) listReturn.toArray(new InitializeEngine[listReturn.size()]);
	}
}
