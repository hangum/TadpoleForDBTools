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

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.ITableDecorationExtension;

/**
 * Table decoration contribution handler
 * 
 * @author hangum
 *
 */
public class TableDecorationContributionHandler {
	private static final Logger logger = Logger.getLogger(TableDecorationContributionHandler.class);
	private static final String TABLE_DECORATION_ID = "com.hangum.tadpole.rdb.core.extensionpoint.definition.table.decoration";

	/**
	 * extension widget creation
	 * 
	 * @return
	 */
	public ITableDecorationExtension evaluateCreateWidgetContribs(final UserDBDAO userDB) {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(TABLE_DECORATION_ID);
		final LinkedList list = new LinkedList();
		try {
			for (IConfigurationElement e : config) {
				final Object mainEditorExtension = e.createExecutableExtension("class");
				if (mainEditorExtension instanceof ITableDecorationExtension) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception create widget", exception);
						}

						@Override
						public void run() throws Exception {
							ITableDecorationExtension compositeExt = (ITableDecorationExtension) mainEditorExtension;
							if(compositeExt.initExtension(userDB)) list.add(compositeExt);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("create main editor", ex);
		}
		
		if(list.isEmpty()) return null;
		else return (ITableDecorationExtension)list.get(0);
	}
}
