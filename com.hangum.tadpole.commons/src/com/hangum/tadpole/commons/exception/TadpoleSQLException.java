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

import java.sql.SQLException;

public class TadpoleSQLException extends SQLException {
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
