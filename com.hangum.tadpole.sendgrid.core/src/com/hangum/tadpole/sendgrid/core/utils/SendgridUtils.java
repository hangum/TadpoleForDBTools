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
package com.hangum.tadpole.sendgrid.core.utils;

import com.sendgrid.SendGrid;

/**
 * Sendgrid 
 * 
 * @author hangum
 *
 */
public class SendgridUtils {

	/**
	 * send mail
	 * 
	 * @param sendgridApi
	 * @param from
	 * @param subject
	 * @param content
	 * @param to
	 * @throws Exception
	 */
	public static void send(String sendgridApi, String from, String to, String subject, String content) throws Exception {
		try {
			SendGrid sendgrid = new SendGrid(sendgridApi);
			SendGrid.Email email = new SendGrid.Email();
			email.setFrom(from);
			email.addTo(to);
			email.setSubject(subject);
			email.setHtml(content);
			SendGrid.Response response = sendgrid.send(email);
			
			if(!response.getStatus()) {
				throw new Exception("[code]" + response.getCode() + "[msg]" + response.getMessage());
			}
		} catch(Exception e) {
			throw e;
		}
	}

}
