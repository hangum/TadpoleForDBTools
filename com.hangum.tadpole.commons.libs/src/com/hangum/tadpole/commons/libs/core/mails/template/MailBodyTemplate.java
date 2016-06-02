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
package com.hangum.tadpole.commons.libs.core.mails.template;

import com.hangum.tadpole.commons.libs.core.Messages;

/**
 * default mail template
 * 
 * @author hangum
 * 
 */
public abstract class MailBodyTemplate {
//	"<style type='text/css'>" +
//			".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}" +
//			".tg td{font-family:Arial, sans-serif;font-size:12px;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}" +
//			".tg th{font-family:Arial, sans-serif;font-size:12px;font-weight:normal;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}" +
//			".tg .tg-yw4l{vertical-align:top}" +
//			"</style>";
//	
	/**
	 * make head
	 * 
	 * @param title
	 * @return
	 */
	protected String makeHead(String title) {
		StringBuffer strContent = new StringBuffer();
		
		// 환영메시지.
		strContent.append("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
			strContent.append("<tr>");
			strContent.append(String.format("<td width='100' valign='top'>%s</td>", title));
			strContent.append("</tr>");
			strContent.append("<br>");
		strContent.append("</table>");
		
		return strContent.toString();
	}
	
	/**
	 * mail body template
	 * @param title
	 * @param content
	 * @return
	 */
	protected String mailBodyTemplate(String title, String content) {
		StringBuffer strContent = new StringBuffer();
		strContent.append("<tr>");
		strContent.append(String.format("<th width='100' class='tg-yw4l'>%s</th>", title));
		strContent.append(String.format("<td width='260' class='tg-yw4l'>%s</td>", content));
		strContent.append("</tr>");
		
		return strContent.toString();
	}
	
	/**
	 * make tail
	 * 
	 * @return
	 */
	protected String makeTail() {
		StringBuffer strContent = new StringBuffer();

		strContent.append("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
			strContent.append(String.format("<tr><td>%s</td></tr>", Messages.get().Thanks));
			strContent.append("<br>");
			strContent.append(String.format("<tr><td width='100' valign='top'>%s%s</td></tr>", Messages.get().MailBodyTempAdmin, "hangum@tadpolehub.com"));
			strContent.append("<tr><td>" + Messages.get().HomePage +"https://tadpolehub.com</td></tr>");
		strContent.append("</table>");
		
		return strContent.toString();
	}
}
