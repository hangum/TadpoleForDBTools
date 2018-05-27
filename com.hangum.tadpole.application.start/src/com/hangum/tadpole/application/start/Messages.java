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
package com.hangum.tadpole.application.start;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.application.start.messages"; //$NON-NLS-1$

	public String NewVersionCheckAction_0;
	public String NewVersionCheckAction_2;
	
	public String ApplicationWorkbenchWindowAdvisor_2;

	public String LoginDialog_21;
	public String LoginDialog_22;
	public String LoginDialog_23;
	public String LoginDialog_26;
	public String LoginDialog_28;
	public String BugIssueAction_0;
	
	public String NewVersionViewDialog_0;
	public String NewVersionViewDialog_1;
	public String NewVersionViewDialog_2;
	public String NewVersionViewDialog_5;
	public String NewVersionViewDialog_6;
	public String NewVersionViewDialog_7;
	public String NewVersionViewDialog_8;
	public String NewVersionViewDialog_NewVersion;
	public String NewVersionViewDialog_DoesnotCheck;

	public String OpenUserManuel;
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
