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
package com.hangum.tadpole.commons.exception;

/**
 * tadpole system exception
 * 
 * @author hangum
 *
 */
public class TadpoleRuntimeException extends RuntimeException {

	public TadpoleRuntimeException() {
		super();
	}

	public TadpoleRuntimeException(String message) {
		super(message);
	}

	public TadpoleRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TadpoleRuntimeException(Throwable cause) {
		super(cause);
	}
}
