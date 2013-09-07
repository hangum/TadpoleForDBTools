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
package com.hangum.tadpole.cipher.core.manager;

import com.hangum.tadpole.cipher.core.utils.DefaultEncryptDecrypt;
import com.hangum.tadpole.cipher.core.utils.EncryptiDecryptUtil;

/**
 * Tadpole Cipher Manager
 * 
 * @author hangum
 *
 */
public class CipherManager {
	private static CipherManager instance;
	private static DefaultEncryptDecrypt encryptDecryptUtil;

	private CipherManager() {}
	
	/**
	 * cipher manage
	 * @return
	 */
	public static CipherManager getInstance() {
		if(instance == null) {
			instance = new CipherManager();
			
			encryptDecryptUtil = new EncryptiDecryptUtil();
		}
		
		return instance;
	}
	
	/**
	 * {@link EncryptiDecryptUtil}
	 * 
	 * @param text
	 * @return
	 */
	public String encryption(String text) {
		return encryptDecryptUtil.encryption(text);
	}
	
	/**
	 * {@link EncryptiDecryptUtil}
	 * 
	 * @param text
	 * @return
	 */
	public String decryption(String text) {
		return encryptDecryptUtil.decryption(text);
	}

}
