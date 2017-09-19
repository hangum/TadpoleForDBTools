package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class TadpoleError extends AbstractTadpoleException {
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
