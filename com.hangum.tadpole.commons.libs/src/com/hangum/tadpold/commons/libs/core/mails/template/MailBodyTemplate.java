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
package com.hangum.tadpold.commons.libs.core.mails.template;


/**
 * default mail template
 * 
 * @author hangum
 * 
 */
public abstract class MailBodyTemplate {
	// <html>The apache logo - <img src=\"cid:" + cid + "\"></html>

	protected String makeHead(String title) {
		StringBuffer strContent = new StringBuffer();
		
		// 환영메시지.
		strContent.append("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
			strContent.append("<tr>");
			strContent.append("<td width='100' valign='top'>");
			strContent.append(title);//"User has been added.\n Please check.");
			strContent.append("</td>");
			strContent.append("</tr>");
			strContent.append("<br>");
		strContent.append("</table>");
		
		return strContent.toString();
	}
	
	protected String makeTail() {
		StringBuffer strContent = new StringBuffer();

		strContent.append("<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
			strContent.append("<br>");
			strContent.append("<tr>");
			strContent.append("<td width='100' valign='top'>");
			strContent.append("* Tadpole DB Hub (http://hangum.github.io/TadpoleForDBTools/)");
			strContent.append("</td>");
			strContent.append("</tr>");
		strContent.append("</table>");
		
		return strContent.toString();
	}
}
