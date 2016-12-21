/*******************************************************************************
 * Copyright (c) 2016 hangum.
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

/**
 * 데이터베이스 공유 메일 템플릿
 * 
 * @author hangum
 *
 */
public class SharedDBMailTemplate extends MailBodyTemplate {
	/**
	 * make mail content
	 * 
	 * @param strName
	 * @param strEmail
	 * @param strConfirmKey
	 * @return
	 */
	public String getContent(String strHead, String strbody) {
		StringBuffer strContent = new StringBuffer();
		strContent.append(HTMLDefine.HTML_STYLE);
		strContent.append(makeHead(strHead));
		
		strContent.append("<table class='tg'>");
			strContent.append(mailBodyTemplate(Messages.get().operationResult, strbody));
		strContent.append("</table>");
		
		// tail
		strContent.append(makeTail());
		return strContent.toString();
	}

}
