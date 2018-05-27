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
package com.hangum.tadpole.preference.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;

import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;

/**
 * System default preference page
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 17.
 *
 */
public abstract class TadpoleDefaulPreferencePage extends PreferencePage {

	/**
	 * 
	 */
	public TadpoleDefaulPreferencePage() {
	}

	/**
	 * @param title
	 */
	public TadpoleDefaulPreferencePage(String title) {
		super(title);
	}

	/**
	 * @param title
	 * @param image
	 */
	public TadpoleDefaulPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}
	
	/**
	 * encript user data
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	protected void updateEncriptInfo(String key, String value) throws Exception {
		TadpoleSystem_UserInfoData.updateEncriptValue(key, value);
	}
	
	protected void updateInfo(String key, String value) throws Exception {
		TadpoleSystem_UserInfoData.updateValue(key, value);
	}

}
