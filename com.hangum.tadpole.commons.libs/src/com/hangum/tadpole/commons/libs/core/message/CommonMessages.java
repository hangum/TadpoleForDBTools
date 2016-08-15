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
package com.hangum.tadpole.commons.libs.core.message;

import org.eclipse.rap.rwt.RWT;

public class CommonMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.message.messages"; //$NON-NLS-1$

	public String OK;
	public String Confirm;
	public String Cancel;
	
	public String Information;
	public String Warning;
	public String Error;
	public String Close;
	
	public String Yes;
	public String No;
	
	public String Start;
	public String Stop;
	
	public String Search;
	public String Filter;
	
	public String Add;
	public String Delete;
	public String Modify;
	public String Save;
	public String Run;
	
	public String Refresh;
	public String Clear;
	
	public String Title;
	public String Description;
	public String Email;
	public String Name;
	public String Date;
	
	/** 접근제어 시스템 연동 오류 */
	public String Check_DBAccessSystem;
	
	public static CommonMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, CommonMessages.class);
	}

	private CommonMessages() {
	}
}
