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

/**
 * An abstract class that implements an error handler dealing with business errors of Tadpole DB Hub.
 * This error handler is inspired by http://homo-ware.tistory.com/116.
 */

@SuppressWarnings("serial")
public abstract class AbstractTadpoleError extends Exception {
	private String code;    /* Error code. This must be unique. */
	private Level  level;   /* Error level. */
	private String message; /* Error message */ 

	private AbstractTadpoleError(String message) {
		super(message);
		this.message = message;
	}
	
	protected AbstractTadpoleError(String code, Level level, String message) {
		this(message);
		this.code  = code;
		this.level = level;
	}
	
	private AbstractTadpoleError(String message, Throwable error) {
		super(message, error);
		this.message = message;
	}
	
	protected AbstractTadpoleError(String code, Level level, String message, Throwable error) {
		this(message, error);
		this.code  = code;
		this.level = level;
	}
	
	protected AbstractTadpoleError(ErrorCodable errorCodable, String...args) {
		this(errorCodable.getCode(), errorCodable.getLevel(), errorCodable.getMessage(args));
	}
	
	public String getCode() {
		return code;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public String getMessage() {
		return message;
	}
}
