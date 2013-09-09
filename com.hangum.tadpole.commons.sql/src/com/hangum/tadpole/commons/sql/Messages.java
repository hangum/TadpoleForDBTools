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
package com.hangum.tadpole.commons.sql;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.hangum.db.commons.sql.messages"; //$NON-NLS-1$
	public static String TadpoleSQLManager_0;
	public static String SQLHistoryCreateColumn_0;
	public static String SQLHistoryCreateColumn_1;
	public static String SQLHistoryCreateColumn_2;
	public static String SQLHistoryCreateColumn_3;
	public static String SQLHistoryCreateColumn_4;
	public static String SQLHistoryCreateColumn_5;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
