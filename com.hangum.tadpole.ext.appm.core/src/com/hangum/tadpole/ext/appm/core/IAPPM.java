/*******************************************************************************
 * Copyright (c) 2012 - 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.ext.appm.core;

import java.util.Map;

/**
 * APPM(Application Password Management) interface
 * 
 * @author hangum
 *
 */
public interface IAPPM {
	/**
	 * get password
	 * 
	 * @param mapAppm
	 * @return
	 * @throws Exception
	 */
	public String getPassword(Map<String, String> mapAppm) throws Exception;
}
