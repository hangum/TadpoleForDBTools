/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     sun.han - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.message;

import org.eclipse.rap.rwt.RWT;

/**
 * This class contains informative messages to let users have intuitive views on what is going on Tadpole DB Hub. 
 * These types of messages can be in this class.
 *
 *  - Loading data... 
 *  - Operation in progress ...
 *  - Operation successfully completed.
 *  - Downloading ... 
 * 
 * @author sun.han
 */
public class InfoMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.message.InfoMessages"; //$NON-NLS-1$
  
	public String LoadingData;
	public String PasswdOldNewIsSame;

	public static InfoMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, InfoMessages.class);
	}

	private InfoMessages() {
	}
}