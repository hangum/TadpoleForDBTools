package com.hangum.tadpole.engine.restful;

import com.hangum.tadpole.engine.define.TDBResultCodeDefine;

/**
 * Toadpole top exception class
 * 
 * @author hangum
 *
 */
public class TadpoleException extends Exception {
	private int errorCode = TDBResultCodeDefine.NORMAL_SUCC;
	
	public TadpoleException(int code, String msg) {
		super(msg);
		
		this.errorCode = code;
	}
	
	public TadpoleException(int code, String msg, Throwable t) {
		super(msg, t);
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public int getErrorCode() {
		return errorCode;
	}
}
