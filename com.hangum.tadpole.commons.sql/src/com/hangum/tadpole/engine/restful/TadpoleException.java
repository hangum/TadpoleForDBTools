package com.hangum.tadpole.engine.restful;

/**
 * Toadpole top exception class
 * 
 * @author hangum
 *
 */
public class TadpoleException extends Exception {
	private String errorCode = "";
	
	public TadpoleException(String msg) {
		super(msg);
	}
	
	public TadpoleException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
}
