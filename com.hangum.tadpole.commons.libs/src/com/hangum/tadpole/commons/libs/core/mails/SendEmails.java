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

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

import com.hangum.tadpole.commons.libs.core.Messages;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.sendgrid.core.utils.SendgridUtils;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.SystemDefine;

/**
 * SendEmail
 * 
 * @author hangum
 *
 */
public class SendEmails {
	private static final Logger logger = Logger.getLogger(SendEmails.class);
	private static SMTPDTO _smtpInfoDto = new SMTPDTO();
	public static SendEmails instance;
	
	// send grid로 보내지지 않는 리스트.
	private String[] OLD_TYPE_DOMAIN = {"@daum.net", "@hanmail.net", "kakao"};
	
	private SendEmails() {};
	
	public static SendEmails getInstance() {
		if(instance == null) {
			instance = new SendEmails();
		}
		
		return instance;
	}
	
	/**
	 * send email
	 * 
	 * @param emailDao
	 */
	public void sendMail(EmailDTO emailDao) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("Add new message");
		
		ApplicationContext context = RWT.getApplicationContext();
		_smtpInfoDto = (SMTPDTO)context.getAttribute("smtpinfo");
		
		if(!StringUtils.contains(emailDao.getTo(), "@")) {
			if(StringUtils.contains(_smtpInfoDto.getDomain(), "@")) {
				emailDao.setTo(emailDao.getTo() + _smtpInfoDto.getDomain());
			} else {
				emailDao.setTo(emailDao.getTo() + "@" + _smtpInfoDto.getDomain());
			}
		}
		
		String strLoginMehtod = _smtpInfoDto.getLoginMethodType();
		if(PublicTadpoleDefine.MAIL_TYPE.SEND_GRID.name().equals(strLoginMehtod)) {
			for(String strDomain : OLD_TYPE_DOMAIN) {
				if(StringUtils.contains(emailDao.getTo(), strDomain)) {
					if(logger.isDebugEnabled()) logger.debug(String.format("=== sendind SMTP=>%s", emailDao.getTo()));
					sendSTMT(emailDao, _smtpInfoDto);
					return;
				}
			}
			
			// 메일을 보내지 못했다면 sendgrid 를 이용해서 보낸다.
			if(logger.isDebugEnabled()) logger.debug(String.format("=== sending SENDGRID=>%s", emailDao.getTo()));
			sendSendgrid(emailDao, _smtpInfoDto);
		} else if(PublicTadpoleDefine.MAIL_TYPE.SMTP.name().equals(strLoginMehtod)) {
			if(logger.isDebugEnabled()) logger.debug(String.format("=== sending SMTP=>%s", emailDao.getTo()));
			sendSTMT(emailDao, _smtpInfoDto);
		}
	}
	
	/**
	 * test email
	 * 
	 * @param _testSmtpInfoDto
	 * @param strTo
	 * @throws Exception
	 */
	public void testMail(SMTPDTO _testSmtpInfoDto, String strTo) throws Exception {
		String strLoginMehtod = _testSmtpInfoDto.getLoginMethodType();
		EmailDTO emailDao = new EmailDTO();
		emailDao.setSubject(Messages.get().MailSubject);
		emailDao.setContent(Messages.get().MailBody);
		emailDao.setTo(strTo);
		
		if(!StringUtils.contains(emailDao.getTo(), "@")) {
			if(StringUtils.contains(_smtpInfoDto.getDomain(), "@")) {
				emailDao.setTo(emailDao.getTo() + _smtpInfoDto.getDomain());
			} else {
				emailDao.setTo(emailDao.getTo() + "@" + _smtpInfoDto.getDomain());
			}
		}
		
		if(PublicTadpoleDefine.MAIL_TYPE.SEND_GRID.name().equals(strLoginMehtod)) {
			sendSendgrid(emailDao, _testSmtpInfoDto);
		} else if(PublicTadpoleDefine.MAIL_TYPE.SMTP.name().equals(strLoginMehtod)) {
			sendSTMT(emailDao, _testSmtpInfoDto);
		}
	}
	
	/**
	 * using sendgrid server
	 * @param emailDao
	 * @param _smtpInfoDto
	 * @throws Exception
	 */
	private void sendSendgrid(EmailDTO emailDao, SMTPDTO _smtpInfoDto) throws Exception {
		SendgridUtils.send(_smtpInfoDto.getSendgrid_api(), SystemDefine.ADMIN_EMAIL, emailDao.getTo(), emailDao.getSubject(), emailDao.getContent());
	}
	
	/**
	 * using smtp server send 
	 * @param emailDao
	 * @param _smtpInfoDto
	 * @throws Exception
	 */
	private void sendSTMT(EmailDTO emailDao, SMTPDTO _smtpInfoDto) throws Exception {
		Properties prop = System.getProperties();
		prop.put("mail.smtp.starttls.enable", "YES".equals(_smtpInfoDto.getStarttls_enable())?"true":"false");
		prop.put("mail.smtp.host", _smtpInfoDto.getHost());
		prop.put("mail.smtp.auth", "YES".equals(_smtpInfoDto.getIsAuth())?"true":"false");
		prop.put("mail.smtp.port", _smtpInfoDto.getPort());
		
		Authenticator auth = new MailAuthentication(_smtpInfoDto.getEmail(), _smtpInfoDto.getPasswd());
		MimeMessage msg = new MimeMessage(Session.getDefaultInstance(prop, auth));

		try {
			msg.setFrom(new InternetAddress(_smtpInfoDto.getEmail()));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailDao.getTo()));

			msg.setSubject(emailDao.getSubject(), "UTF-8");
			msg.setText(emailDao.getContent(), "UTF-8");
			msg.setHeader("content-Type", "text/html");
			Transport.send(msg);

		} catch (AddressException addr_e) {
			logger.error("send eail", addr_e);
			throw addr_e;
		} catch (MessagingException msg_e) {
			logger.error("send eail", msg_e);
			throw msg_e;
		}
	}
	
}

/**
 * my authentication
 * 
 * @author hangum
 *
 */
class MailAuthentication extends Authenticator {
	PasswordAuthentication pa;

	public MailAuthentication(String id, String pw) {
		pa = new PasswordAuthentication(id, pw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}