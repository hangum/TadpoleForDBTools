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
package com.hangum.tadpole.commons.exception;

/**
 * Authority exception
 * 
 * @author hangum
 *
 */
public class TadpoleAuthorityException extends Exception {
	public TadpoleAuthorityException() {
		super();
	}

	public TadpoleAuthorityException(String message) {
		super(message);
	}

	public TadpoleAuthorityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TadpoleAuthorityException(Throwable cause) {
		super(cause);
	}
}
