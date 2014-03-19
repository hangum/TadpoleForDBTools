/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * SQLDefine
 * 
 * @author hangum
 *
 */
public class SQLDefine {

	/**
	 * query delimiter
	 */
	public static final String QUERY_DELIMITER = ";";
	public static String EXPORT_DEMILITER = GetPreferenceGeneral.getExportDelimit().equalsIgnoreCase("tab")?"	":GetPreferenceGeneral.getExportDelimit() + " "; //$NON-NLS-1$ //$NON-NLS-2$

}
