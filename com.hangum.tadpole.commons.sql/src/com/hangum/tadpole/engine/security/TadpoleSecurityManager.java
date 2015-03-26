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

/**
 * Tadpole Security manager
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 24.
 *
 */
public class TadpoleSecurityManager {

	private static TadpoleSecurityManager instance = new TadpoleSecurityManager(); 
	
	/**
	 * security manager
	 */
	private TadpoleSecurityManager() {}
	
	public static TadpoleSecurityManager getInstance() {
		return instance;
	}
	

}
