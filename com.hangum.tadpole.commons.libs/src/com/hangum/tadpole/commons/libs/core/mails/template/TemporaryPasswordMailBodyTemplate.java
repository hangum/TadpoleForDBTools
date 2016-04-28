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
 * new user mail template
 * 
 * @author hangum
 *
 */
public class TemporaryPasswordMailBodyTemplate extends MailBodyTemplate {

	/**
	 * 
	 * @param strEmail
	 * @param strConfirmKey
	 * @return
	 */
	public String getContent(String strEmail, String strConfirmKey) {
		StringBuffer strContent = new StringBuffer();
		
		// title
		strContent.append(makeHead(Messages.get().SendTemporaryPassword));
		
		// body
		strContent.append("<table class='tg'>");
		strContent.append(mailBodyTemplate(Messages.get().Email, strEmail));
		strContent.append(mailBodyTemplate(Messages.get().Confirmkey, strConfirmKey));
		strContent.append("</table>");
		// tail
		strContent.append(makeTail());
		return strContent.toString();
	}
}
