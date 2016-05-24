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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.preference.define.PreferenceDefine;

/**
 * api server preference
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 23.
 *
 */
public class GetSecurityCredentialPreference extends GetPreferenceGeneral {
	private static final Logger logger = Logger.getLogger(GetSecurityCredentialPreference.class);
	
	/**
	 * is api server use?
	 * 
	 * @return
	 */
	public static String getSecurityCredentialUse() {
		return getValue(PreferenceDefine.SECURITY_CREDENTIAL_USE, PublicTadpoleDefine.YES_NO.NO.name());
	}

	/**
	 * api server access key
	 * 
	 * @return
	 */
	public static String getAccessValue() {
		return getValue(PreferenceDefine.SECURITY_CREDENTIAL_ACCESS_KEY, Utils.getUniqueID());
	}

	/**
	 * api server secret key
	 * 
	 * @return
	 */
	public static String getSecretValue() {
		return getValue(PreferenceDefine.SECURITY_CREDENTIAL_SECRET_KEY, Utils.getUniqueID());
	}

}
