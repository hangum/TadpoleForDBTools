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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * abstract exporter
 * 
 * @author hangum
 *
 */
public abstract class AbstractTDBExporter {
	protected static int DATA_COUNT = 3000;

	public static String makeFileName(String strFileName, String strExt) {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + strFileName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		return strTmpDir + strFileName + "." + strExt;
	}
	
//	public abstract void beforeMakeFile();
//	
//	public abstract String makeHeader();
//	public abstract String makeContent();
}
