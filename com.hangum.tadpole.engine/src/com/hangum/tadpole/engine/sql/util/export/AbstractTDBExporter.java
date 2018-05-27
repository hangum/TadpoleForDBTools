/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export;

import java.io.File;

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * abstract exporter
 * 
 * @author hangum
 *
 */
public abstract class AbstractTDBExporter {
	protected static int DATA_COUNT = 3000;
	
	/**
	 * make tmp dir
	 * @param strFileName
	 * @return
	 */
	public static String makeDirName(String strFileName) {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + strFileName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		new File(strTmpDir).mkdirs();
		
		return strTmpDir;
	}

	/**
	 * make tmp file
	 * @param strFileName
	 * @param strExt
	 * @return
	 */
	public static String makeFileName(String strFileName, String strExt) {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + strFileName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		return strTmpDir + strFileName + "." + strExt;
	}
	
//	public abstract void beforeMakeFile();
//	
//	public abstract String makeHeader();
//	public abstract String makeContent();
}
