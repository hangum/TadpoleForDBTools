package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;

@SuppressWarnings("serial")
public abstract class AbstractTadpoleException extends Exception {
	private String code;    /* Error code. This must be unique. */
	private Level  level;   /* Error level. */
	private String message; /* Error message */ 

	private AbstractTadpoleException(String message) {
		super(message);
		this.message = message;
	}
	
	protected AbstractTadpoleException(String code, Level level, String message) {
		this(message);
		this.code  = code;
		this.level = level;
	}
	
	private AbstractTadpoleException(String message, Throwable error) {
		super(message, error);
		this.message = message;
	}
	
	protected AbstractTadpoleException(String code, Level level, String message, Throwable error) {
		this(message, error);
		this.code  = code;
		this.level = level;
	}
	
	protected AbstractTadpoleException(ErrorCodable errorCodable, String...args) {
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
