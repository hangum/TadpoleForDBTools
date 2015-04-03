/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.security;

import org.apache.log4j.Logger;

/**
 * audit manager
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 2.
 *
 */
public class AuditManager {
	private static final Logger logger = Logger.getLogger(AuditManager.class);
	private static AuditManager instance = null;

	/**
	 * 
	 */
	private AuditManager() {}
	
	public static AuditManager getInstance() {
		if(instance == null) {
			instance = new AuditManager();
		}
		
		return instance;
	}
	
	

}
