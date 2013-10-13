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
package com.hangum.tadpole.commons.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.swtdesigner.SWTResourceManager;

/**
 * Tadpole widget utils
 * 
 * @author hangum
 *
 */
public class TadpoleWidgetUtils {
	
	/** tab content */
	public static final String TAB_CONETNT = "    ";
	
	/**
	 * CTabFolder background color
	 * @return
	 */
	public static Color[] getTabFolderBackgroundColor() {
		Color[] arrayColor = new Color[]{SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW),
				SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
				SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
				SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW)};
		return arrayColor;
	}
	
	/**
	 * CTabFolder background percents
	 * @return
	 */
	public static int[] getTabFolderPercents() {
		return new int[] {100, 100, 100};
	}

}
