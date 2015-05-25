/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.get;

import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.preference.define.PreferenceDefine;

/**
 * managed amazon preferencd
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 17.
 *
 */
public class GetAmazonPreference extends GetPreferenceGeneral {
	private static final Logger logger = Logger.getLogger(GetAmazonPreference.class);

	/**
	 * @param amazonAccessName
	 * @param amazonAccessValue
	 * @return
	 */
	public static String getAccessValue() {
		return CipherManager.getInstance().decryption(getValue(PreferenceDefine.AMAZON_ACCESS_NAME, PreferenceDefine.AMAZON_ACCESS_VALUE));
	}

	/**
	 * @param amazonSecretName
	 * @param amazonSecretValue
	 * @return
	 */
	public static String getSecretValue() {
		return CipherManager.getInstance().decryption(getValue(PreferenceDefine.AMAZON_SECRET_NAME, PreferenceDefine.AMAZON_SECRET_VALUE));
	}

}
