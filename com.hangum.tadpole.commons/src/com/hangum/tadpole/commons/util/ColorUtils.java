/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import org.eclipse.swt.graphics.RGB;

/**
 * Color utils
 * 
 * reference : http://www.programcreek.com/java-api-examples/index.php?example_code_path=codecover-org.codecover.eclipse.annotation.hotpath-LineExecutionImageProvider.java
 * 
 * 
 * @author hangum
 *
 */
public class ColorUtils {

	/**
	 * RGB to hexadecimal
	 * 
	 * @param rgb
	 * @return
	 */
	public static String rgbToHexa(RGB rgb) {
		String redHexadecimal = Integer.toHexString(rgb.red).toUpperCase();
		redHexadecimal = redHexadecimal.length() == 1 ? "0" + redHexadecimal
				: redHexadecimal;
		String greenHexadecimal = Integer.toHexString(rgb.green).toUpperCase();
		greenHexadecimal = greenHexadecimal.length() == 1 ? "0"
				+ greenHexadecimal : greenHexadecimal;
		String blueHexadecimal = Integer.toHexString(rgb.blue).toUpperCase();
		blueHexadecimal = blueHexadecimal.length() == 1 ? "0" + blueHexadecimal
				: blueHexadecimal;

		return "#" + redHexadecimal + greenHexadecimal + blueHexadecimal;
	}

	/**
	 * hexadecimal to RGB
	 * 
	 * @param hex
	 * @return
	 * @throws NumberFormatException
	 */
	public static RGB hexaToRGB(String hex) throws NumberFormatException {
		int r = Integer.parseInt(hex.substring(1, 3), 16);
		int g = Integer.parseInt(hex.substring(3, 5), 16);
		int b = Integer.parseInt(hex.substring(5, 7), 16);

		return new RGB(r, g, b);
	}

	private static String rgbToHex(RGB color) {
		return unsignedByteToString(color.red)
				+ unsignedByteToString(color.green)
				+ unsignedByteToString(color.blue);
	}
	private static String unsignedByteToString(int i) {
        return "" + Character.forDigit(i / 16, 16) //$NON-NLS-1$
                  + Character.forDigit(i % 16, 16);
    }
}
