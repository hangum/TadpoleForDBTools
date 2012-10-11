/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.util.secret;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * <pre>
 *	내용을 암호화 합니다. 
 * 	http://www.jasypt.org
 * </pre>
 * 
 * @author hangum
 *
 */
public class EncryptiDecryptUtil {
	final static String ENCRYPT_PASSWORD = "heechan.tadpole.owner.son";
	
	/**
	 * 텍스트를 암호화 합니다.
	 * 
	 * @param text 암호화된 값 
	 * @return
	 */
	public static String encryption(String text) {
		
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(ENCRYPT_PASSWORD);
		if("".equals(text) || null == text) return "";
		
		return textEncryptor.encrypt(text);
	}
	
	/**
	 * text를 복호화 합니다.
	 * 
	 * @param text 복호화 된 text
	 * @return
	 */
	public static String decryption(String text) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(ENCRYPT_PASSWORD);
		if("".equals(text) || null == text) return "";
		
		return textEncryptor.decrypt(text);
	}
	
}
