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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

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
	private SMTPDTO smtpDto;
	
	// send grid로 보내지지 않는 리스트.
	private String[] OLD_TYPE_DOMAIN = {"@daum.net", "@hanmail.net", "kakao"};
	
	public SendEmails(SMTPDTO smtpDto) {
		this.smtpDto = smtpDto;
	}

	/**
	 * send email
	 * 
	 * @param emailDao
	 */
	public void sendMail(EmailDTO emailDao) throws Exception {
		if(!smtpDto.isValid()) {
			throw new Exception("Valid smtp information." + emailDao);
		}
		if(logger.isDebugEnabled()) logger.debug("Add new message");

		// send grid 와 둘다 살려있다면.
		if(!"".equals(smtpDto.getSendgrid_api())) {
			for(String strDomain : OLD_TYPE_DOMAIN) {
				if(StringUtils.contains(emailDao.getTo(), strDomain)) {
					if(logger.isDebugEnabled()) logger.debug(String.format("=== sendind SMTP=>%s", emailDao.getTo()));
					sendSTMT(emailDao);
					return;
				}
			}
			
			// 메일을 보내지 못했다면 sendgrid 를 이용해서 보낸다.
			if(logger.isDebugEnabled()) logger.debug(String.format("=== sending SENDGRID=>%s", emailDao.getTo()));
			sendSendgrid(emailDao);
		} else {
			if(logger.isDebugEnabled()) logger.debug(String.format("=== sending SMTP=>%s", emailDao.getTo()));
			sendSTMT(emailDao);
		}
	}
	
	/**
	 * using sendgrid server
	 * @param emailDao
	 * @throws Exception
	 */
	private void sendSendgrid(EmailDTO emailDao) throws Exception {
		SendgridUtils.send(smtpDto.getSendgrid_api(), SystemDefine.ADMIN_EMAIL, emailDao.getTo(), emailDao.getSubject(), emailDao.getContent());
	}
	
	/**
	 * using smtp server send 
	 * @param emailDao
	 * @throws Exception
	 */
	private void sendSTMT(EmailDTO emailDao) throws Exception {
		try {			
			HtmlEmail email = new HtmlEmail();
			email.setCharset("euc-kr");
			email.setHostName(smtpDto.getHost());
			email.setSmtpPort(NumberUtils.toInt(smtpDto.getPort()));
			email.setAuthenticator(new DefaultAuthenticator(smtpDto.getEmail(), smtpDto.getPasswd()));
			email.setSSLOnConnect(true);
			
			email.setFrom(smtpDto.getEmail(), "Tadpole Hub");
			email.addTo(emailDao.getTo());
			email.setSubject(emailDao.getSubject());
			email.setHtmlMsg(emailDao.getContent());
			
			email.send();
			
		} catch(Exception e) {
			logger.error("send email", e);
			throw e;
		}
	}
}
