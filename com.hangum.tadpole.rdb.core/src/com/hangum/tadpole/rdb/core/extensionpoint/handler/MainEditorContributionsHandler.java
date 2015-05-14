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

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IMainEditorExtension;

/**
 * Main Editor extension handler
 * 
 * @author hangum
 *
 */
public class MainEditorContributionsHandler {
	private static final Logger logger = Logger.getLogger(MainEditorContributionsHandler.class);
	private static final String MAIN_EDITOR_ID = "com.hangum.tadpole.rdb.core.extensionpoint.definition.main.editor";

	/**
	 * extension widget creation
	 * 
	 * @return
	 */
	public IMainEditorExtension[] evaluateCreateWidgetContribs(final UserDBDAO userDB) {
		final LinkedList listReturn = new LinkedList();
		
		final DBDefine selectDBDefine = userDB.getDBDefine();
		if(selectDBDefine == DBDefine.HIVE_DEFAULT 		|| 
				selectDBDefine == DBDefine.HIVE2_DEFAULT 	|| 
				selectDBDefine == DBDefine.TAJO_DEFAULT 	
		) {
			logger.info("Tadpole extension not support this db type ==> " +selectDBDefine.getDBToString());
		} else {
			IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(MAIN_EDITOR_ID);
			
			try {
				for (IConfigurationElement e : config) {
					final Object mainEditorExtension = e.createExecutableExtension("class");
					if (mainEditorExtension instanceof IMainEditorExtension) {
						ISafeRunnable runnable = new ISafeRunnable() {
	
							@Override
							public void handleException(Throwable exception) {
								logger.error("Exception create widget", exception);
							}
	
							@Override
							public void run() throws Exception {
								IMainEditorExtension compositeExt = (IMainEditorExtension) mainEditorExtension;
								compositeExt.initExtension(userDB);
								if(compositeExt.isEnableExtension()) listReturn.add(compositeExt);
							}
						};
						SafeRunner.run(runnable);
					}
				}
			} catch (CoreException ex) {
				logger.error("create main editor", ex);
			}
		}
		
		return (IMainEditorExtension[]) listReturn.toArray(new IMainEditorExtension[listReturn.size()]);
	}

//	/**
//	 * table column double click
//	 * 
//	 * @param selectIndex
//	 * @param mapColumns
//	 */
//	public void evaluateResultColumnDoublClick(final int selectIndex, final Map<Integer, Object> mapColumns) {
//		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(MainEditor_ID);
//		try {
//			for (IConfigurationElement e : config) {
//				final Object mainEditorExtension = e.createExecutableExtension("class");
//				if (mainEditorExtension instanceof IMainEditorExtension) {
//					ISafeRunnable runnable = new ISafeRunnable() {
//						@Override
//						public void handleException(Throwable exception) {
//							logger.error("Exception create widget", exception);
//						}
//
//						@Override
//						public void run() throws Exception {
//							((IMainEditorExtension) mainEditorExtension).resultSetDoubleClick(selectIndex, mapColumns);
//						}
//					};
//					SafeRunner.run(runnable);
//				}
//			}
//		} catch (CoreException ex) {
//			logger.error("table result double click exception", ex);
//		}
//	}
}
