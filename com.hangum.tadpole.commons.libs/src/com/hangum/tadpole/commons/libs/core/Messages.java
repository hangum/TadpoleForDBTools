/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.messages"; //$NON-NLS-1$
	
	public String CheckNumberString;
	public String CheckTextString;

	public String UserName;

	public String Confirmkey;

	public String NewUserMailTitle;

	public String HomePage;

	public String Thanks;

	public String SendTemporaryPassword;

	public String operationResult;

	public String MailSubject;

	public String MailBody;
	
	public String TemporaryPasswordTitle;
	
	public String TemporaryPassword;
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
