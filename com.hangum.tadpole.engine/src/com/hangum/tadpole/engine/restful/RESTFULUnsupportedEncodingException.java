package com.hangum.tadpole.engine.restful;

import com.hangum.tadpole.engine.define.TDBResultCodeDefine;

@SuppressWarnings("serial")
/**
 * 
 * @author hangum
 *
 */
public class RESTFULUnsupportedEncodingException extends TadpoleException {
	
	public RESTFULUnsupportedEncodingException(String message) {
		super(TDBResultCodeDefine.INTERNAL_SERVER_ERROR, message);
	}
	
}
