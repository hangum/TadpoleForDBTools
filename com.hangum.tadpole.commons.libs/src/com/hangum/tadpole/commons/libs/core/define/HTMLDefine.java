/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.define;

/**
 * HTML Define
 * 
 * @author hangum
 *
 */
public class HTMLDefine {
	public static final String HTML_STYLE = 
			"<meta charset='UTF-8'>" +
			"<style type='text/css'>" +
			".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}" +
			".tg td{font-family:Arial, sans-serif;font-size:12px;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}" +
			".tg th{font-family:Arial, sans-serif;font-size:12px;font-weight:normal;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}" +
			".tg .tg-yw4l{vertical-align:top}" +
			"</style>";

	/** group */
	public static String TABLE = "<table class='tg'>%s%s</table>";
	public static String TR = "<tr>%s</tr>";
	public static String TH = "<th class='tg-yw4l'>%s</th>";
	public static String TD = "<td class='tg-yw4l'>%s</td>";

	public static String makeTABLE(String header, String body) {
		return String.format(TABLE, header, body);
	}
	
	public static String makeTR(String content) {
		return String.format(TR, content);
	}
	
	public static String makeTH(String content) {
		return String.format(TH, content);
	}
	
	public static String makeTD(String content) {
		return String.format(TD, content);
	}
}
