/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.cipher.core.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Encryption, Decryption utils
 * 
 * @see http://www.jasypt.org
 * @author hangum
 *
 */
public class EncryptiDecryptUtil implements DefaultEncryptDecrypt {
	/** default encrypt password */
	final static String DEFAULT_ENCRYPT_PASSWORD = "heechan.tadpole.owner.son";
	
	/**
	 * encryption
	 * 
	 * @param text  
	 * @return
	 */
	@Override
	public String encryption(String text) {
		if("".equals(text) || null == text) return "";
		
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(DEFAULT_ENCRYPT_PASSWORD);
		
		return textEncryptor.encrypt(text);
	}
	
	/**
	 * description
	 * 
	 * @param text 
	 * @return
	 */
	@Override
	public String decryption(String text) {
		if("".equals(text) || null == text) return "";
		
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(DEFAULT_ENCRYPT_PASSWORD);
		
		return textEncryptor.decrypt(text);
	}
	
}
