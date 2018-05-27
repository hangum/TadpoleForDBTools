/*******************************************************************************
 * Copyright (c) 2017 hangum.
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
import com.hangum.tadpole.commons.libs.core.define.HTMLDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;

/**
 * new user mail template
 * 
 * @author hangum
 *
 */
public class TempEmailMailBodyTemplate extends MailBodyTemplate {

	/**
	 * make mail content
	 * 
	 * @param strName
	 * @param strEmail
	 * @param strConfirmKey
	 * @return
	 */
	public String getContent(String strName, String strEmail, String strConfirmKey) {
		StringBuffer strContent = new StringBuffer();
		strContent.append(HTMLDefine.HTML_STYLE);
		strContent.append(makeHead(Messages.get().TemporaryPasswordTitle));
		
		strContent.append("<table class='tg'>");
			strContent.append(mailBodyTemplate(Messages.get().UserName, strName));
			strContent.append(mailBodyTemplate(CommonMessages.get().Email, strEmail));
			strContent.append(mailBodyTemplate(Messages.get().TemporaryPassword, strConfirmKey));
		strContent.append("</table>");
		
		// tail
		strContent.append(makeTail());
		return strContent.toString();
	}
}
