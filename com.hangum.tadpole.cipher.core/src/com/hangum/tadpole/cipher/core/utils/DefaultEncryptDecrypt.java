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

/**
 * Tadpole default, encrypt interface
 * 
 * @author hangum
 * 
 */
public interface DefaultEncryptDecrypt {
	/**
	 * encryption
	 * 
	 * @param text
	 * @return
	 */
	public String encryption(String text);

	/**
	 * decryption
	 * 
	 * @param text
	 * @return
	 */
	public String decryption(String text);
}
