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

public class TadpoleSQLException extends Exception {
	public TadpoleSQLException() {
		super();
	}

	public TadpoleSQLException(String message) {
		super(message);
	}

	public TadpoleSQLException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TadpoleSQLException(Throwable cause) {
		super(cause);
	}
}
