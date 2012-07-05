package com.hangum.db.exception;

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
