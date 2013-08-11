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
package com.hangum.tadpole.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Tadpole Default Display
 * This class make Default font
 *
 * @author hangum
 *
 */
public class TadpoleDisplay extends Display {
	private Font defaultFont;
	private static Font defaultFont1;
	private static Font defaultBoldFont;
	
	public TadpoleDisplay() {
		super();
	}
	
	@Override
	public Font getSystemFont() {
		if (defaultFont == null) {
			defaultFont = getFont();
		}

		return defaultFont;
	}
	
	public static Font getDefaultFont1() {
		if (defaultFont1 == null) {
			defaultFont1 = getFont();
		}

		return defaultFont1;
	}
	
	public static Font getDefaultBoldFont() {
		if (defaultBoldFont == null) {
			defaultBoldFont = getBoldFont();
		}

		return defaultBoldFont;
	}
	
	public Font getDefaultFont() {
		if (defaultFont1 == null) {
			defaultFont1 = getFont();
		}

		return defaultFont1;
	}
	
	public static Font getBoldSystemFont() {
		if (defaultBoldFont == null) {
			defaultBoldFont = getBoldFont();
		}

		return defaultBoldFont;
	}
	
	private static Font getFont() {
		return new Font(getCurrent(), "Verbose", 10, SWT.NONE);
	}
	
	private static Font getBoldFont() {
		return new Font(getCurrent(), "Verbose", 10, SWT.BOLD);
	}
	
	protected void release () {
		super.release();
		
		if (defaultFont != null) {
			defaultFont.dispose();
			defaultFont = null;
		}
		if( defaultFont1 != null) {
			defaultFont1.dispose();
			defaultFont1 = null;
		}
		if (defaultBoldFont != null) {
			defaultBoldFont.dispose();
			defaultBoldFont = null;
		}
	}
}
