package com.hangum.db.commons.sql;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.hangum.db.commons.sql.messages"; //$NON-NLS-1$
	public static String TadpoleSQLManager_0;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
