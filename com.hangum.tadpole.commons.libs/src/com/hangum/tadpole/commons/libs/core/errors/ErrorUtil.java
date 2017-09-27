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

public class ErrorUtil {
	public static String parseMessage(String message, String...args) {
		if(message==null ||message.trim().length() <= 0 ) 
			return message;
		
		if(args==null || args.length <= 0) 
			return message;
		
		String[] splitMessages = message.split("%");
		if(splitMessages==null || splitMessages.length <= 1) 
			return message;
		
		for(int i=0; i<args.length; i++) {
			String replaceChar = "%" + (i+1);
			message = message.replaceFirst(replaceChar,  args[i]);
		}
		
		return message;
	}
}
