/**
 * Copyright (c) 2017 Tadpole Hub.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * @author sun.han
 */
package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class TadpoleError extends AbstractTadpoleError {
	
	public TadpoleError(String code, Level level, String message) {
		super(code, level, message);
	}
	
	public TadpoleError(String code, Level level, String message, Throwable error) {
		super(code, level, message, error);
	}
	
	public TadpoleError(ErrorCodable errorCodable, String...args) {
		super(errorCodable, args);
	}
}
