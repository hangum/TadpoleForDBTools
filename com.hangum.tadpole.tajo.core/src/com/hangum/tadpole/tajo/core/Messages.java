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
package com.hangum.tadpole.tajo.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.tajo.core.messages"; //$NON-NLS-1$

	public String SessionListEditor_1;
	public String SessionListEditor_13;
	public String SessionListEditor_15;
	public String SessionListEditor_2;
	public String SessionListEditor_21;
	public String SessionListEditor_22;
	public String SessionListEditor_23;
	public String SessionListEditor_24;
	public String SessionListEditor_25;
	public String SessionListEditor_26;
	public String SessionListEditor_27;
	public String SessionListEditor_28;
	public String SessionListEditor_3;
	public String SessionListEditor_4;
	public String SessionListEditor_6;
	public String SessionListEditor_7;
	public String SessionListEditor_8;
	public String SessionListEditorInput_0;
	public String SessionListEditorInput_2;
	
	public String MainEditor_19;
	public String MainEditor_21;
	

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
