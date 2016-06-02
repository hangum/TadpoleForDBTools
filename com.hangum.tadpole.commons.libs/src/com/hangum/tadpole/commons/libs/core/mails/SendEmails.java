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
package com.hangum.tadpole.commons.libs.core.mails;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.Messages;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.sendgrid.core.utils.SendgridUtils;

/**
 * SendEmail
 * 
 * @author hangum
 *
 */
public class SendEmails {
	private static final Logger logger = Logger.getLogger(SendEmails.class);
	SMTPDTO smtpDto;
	
	public SendEmails(SMTPDTO smtpDto) {
		this.smtpDto = smtpDto;
	}

	/**
	 * send email
	 * 
	 * @param emailDao
	 */
	public void sendMail(EmailDTO emailDao) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("Add new message");
		
		if("".equals(smtpDto.getSendgrid_api())) {
			try {			
				HtmlEmail email = new HtmlEmail();
				email.setCharset("euc-kr");
				email.setHostName(smtpDto.getHost());
				email.setSmtpPort(NumberUtils.toInt(smtpDto.getPort()));
				email.setAuthenticator(new DefaultAuthenticator(smtpDto.getEmail(), smtpDto.getPasswd()));
				email.setSSLOnConnect(true);
				
				email.setFrom(smtpDto.getEmail(), Messages.get().TadpoleHub);
				email.addTo(emailDao.getTo());
				email.setSubject(emailDao.getSubject());
				email.setHtmlMsg(emailDao.getContent());
				
				email.send();
				
			} catch(Exception e) {
				logger.error("send email", e);
				throw e;
			}
		} else {
			SendgridUtils.send(smtpDto.getSendgrid_api(), SystemDefine.ADMIN_EMAIL, emailDao.getTo(), emailDao.getSubject(), emailDao.getContent());
		}
	}
}
