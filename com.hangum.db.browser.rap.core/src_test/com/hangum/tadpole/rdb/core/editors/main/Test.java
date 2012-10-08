package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.commons.lang.StringUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "This is a string.\nThis is a long string.\r";
        String str1 = str.replaceAll("(\r\n|\n|\r)", "<br />");
        System.out.println("["+str1+"]");
        
        
        String str2 = StringUtils.replace(str, "(\r\n|\n|\r)", "<br />");
        System.out.println("["+str2+"]");
	}

}
