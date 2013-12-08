package com.hangum.tadpole.notes.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.notes.core.messages"; //$NON-NLS-1$
	public static String NewNoteDialog_11;
	public static String NewNoteDialog_5;
	public static String NewNoteDialog_8;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
