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
