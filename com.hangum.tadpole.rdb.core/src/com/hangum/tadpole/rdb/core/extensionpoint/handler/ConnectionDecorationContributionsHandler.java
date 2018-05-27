/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.extensionpoint.handler;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IConnectionDecoration;

/**
 * Connection decoration extension handler
 * 
 * @author hangum
 *
 */
public class ConnectionDecorationContributionsHandler {
	private static final Logger logger = Logger.getLogger(ConnectionDecorationContributionsHandler.class);
	private static final String CONNECTION_DECORATION_EXTENSION_ID = "com.hangum.tadpole.rdb.core.extensionpoint.definition.connection.decoration";

	/**
	 * Get extension DB Label image
	 * 
	 * @return
	 */
	public Image getImage(final UserDBDAO userDB) {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(CONNECTION_DECORATION_EXTENSION_ID);
		final LinkedList list = new LinkedList();
		try {
			for (IConfigurationElement e : config) {
				final Object cdExtension = e.createExecutableExtension("class");
				if (cdExtension instanceof IConnectionDecoration) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception connection decoration extension", exception);
						}

						@Override
						public void run() throws Exception {
							IConnectionDecoration compositeExt = (IConnectionDecoration) cdExtension;
							Image image = compositeExt.getImage(userDB);
							if(image != null) list.add(image);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("create connection decoration", ex);
		}
		if(!list.isEmpty()) return (Image)list.get(0);
		return null;
	}
}
