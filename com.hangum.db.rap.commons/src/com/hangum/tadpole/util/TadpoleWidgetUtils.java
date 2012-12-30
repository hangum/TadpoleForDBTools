/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.swtdesigner.SWTResourceManager;

/**
 * Tadpole wiget utils
 * 
 * @author hangum
 *
 */
public class TadpoleWidgetUtils {
	
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
