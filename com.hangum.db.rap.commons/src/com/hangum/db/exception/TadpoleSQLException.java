package com.hangum.db.exception;

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
