package com.hangum.tadpole.engine.restful;

import java.io.UnsupportedEncodingException;

@SuppressWarnings("serial")
/**
 * 
 * @author hangum
 *
 */
public class RESTFULUnsupportedEncodingException extends UnsupportedEncodingException {
	private String errorCode = "5000";
	
	public RESTFULUnsupportedEncodingException(String message) {
		super(message);
	}
	
	public RESTFULUnsupportedEncodingException(UnsupportedEncodingException e) {
		super(e.getMessage());
	}

	public String getErrorCode() {
		return errorCode;
	}
}
