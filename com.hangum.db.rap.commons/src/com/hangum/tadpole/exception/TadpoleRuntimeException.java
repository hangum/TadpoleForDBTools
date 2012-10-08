/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.exception;

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
