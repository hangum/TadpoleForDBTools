package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;
import com.hangum.tadpole.commons.libs.core.errors.TadpoleErrorMessages;

public enum TadpoleErrors implements ErrorCodable {
	/* Warning messages relating user actions */
	TDP_001("TDP-001", Level.WARNING, TadpoleErrorMessages.get().EnterNumbersOnly),
	TDP_002("TDP-002", Level.WARNING, TadpoleErrorMessages.get().ItemIsEmpty),
	TDP_003("TDP-003", Level.WARNING, TadpoleErrorMessages.get().PasswordRule), 
	
	/* License */
	TDP_100("TDP-100", Level.SEVERE, TadpoleErrorMessages.get().InvalidLicense),
	
	/* Account */
	TDP_200("TDP-300", Level.SEVERE, TadpoleErrorMessages.get().UserNotFound)
	;
	
	private String code;
	private Level level;
	private String message;
	
	@Override 
	public String getCode() {
		return this.code;
	}
	
	@Override 
	public String getMessage(String...args) {
		return ErrorUtil.parseMessage(this.message, args);
	}
	
	@Override
	public Level getLevel() {
		return this.level;
	}
	
	TadpoleErrors(String code, Level level, String message) {
		this.code    = code;
		this.level   = level;
		this.message = message;
	}
}
