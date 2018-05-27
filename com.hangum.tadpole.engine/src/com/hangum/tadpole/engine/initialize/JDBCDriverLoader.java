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

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.TadpoleEngineActivator;

/**
 * Jar Class loader
 * 
 * @author hangum
 *
 */
public class JDBCDriverLoader {
	private static final Logger logger = Logger.getLogger(JDBCDriverLoader.class);
	
	/**
	 * initialize jdbc driver
	 * 
	 */
	public static void initializeJDBCDriver() {
		final String strJDBCDir = ApplicationArgumentUtils.JDBC_RESOURCE_DIR;
		File jdbcLocationDir = new File(strJDBCDir);

		if(!jdbcLocationDir.exists()) {
			try {
				jdbcLocationDir.mkdirs();
				
				File fileEngine = FileLocator.getBundleFile(TadpoleEngineActivator.getDefault().getBundle());
				String filePath = fileEngine.getAbsolutePath() + "/libs/driver";
				if(logger.isInfoEnabled()) logger.info("##### TDB JDBC URI: " + filePath);
				
				FileUtils.copyDirectory(new File(filePath), new File(strJDBCDir));
				
				// driver loading
				JDBCDriverLoader.addJARDir(strJDBCDir);
			} catch(Exception e) {
				logger.error("Initialize JDBC driver file", e);
			}
		}
		
	}
	
	/**
	 * add jar loader of file
	 * 
	 * @param strFile
	 * @throws Exception
	 */
	public static void addJarFile(String strFile) throws Exception {
		File file = new File(strFile);
		if(file.isFile()) {
			addJARLoader(new Object[]{file.toURI().toURL()});
		}
	}
	
	/**
	 * add JAR Loader of dir
	 * 
	 * @param strDir
	 * @throws Exception
	 */
	public static void addJARDir(String strDir) throws Exception {
//		if(logger.isDebugEnabled()) logger.debug("--> JAR path : " + strDir);
			
		File fileDir = new File(strDir);
		if(fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if(files != null) {
				for (File file : files) {
					if(file.isDirectory()) {
						addJARDir(file.getAbsolutePath());
					} else {
						if(StringUtils.endsWithIgnoreCase(file.getName(), ".jar")) {
							addJARLoader(new Object[]{file.toURI().toURL()});
						}
					}
				}
			}
			
		} else {
			if(StringUtils.endsWithIgnoreCase(fileDir.getName(), ".jar")) {
				addJARLoader(new Object[]{fileDir.toURI().toURL()});
			}
		}
	}

	/**
	 * Add jar loader
	 * 
	 * @param jarArray
	 * @throws Exception
	 */
	private static void addJARLoader(Object[] jarArray) throws Exception {
		URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(systemClassLoader, jarArray);
		} catch (Throwable t) {
			logger.error("jar loader", t);
		}
	}
}
