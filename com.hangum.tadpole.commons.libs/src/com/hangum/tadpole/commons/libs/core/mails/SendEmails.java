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

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
	private SMTPDTO smtpDto = new SMTPDTO();
	
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
//		if(!smtpDto.isValid()) {
//			throw new Exception("Invalid smtp information." + emailDao);
//		}
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
//		try {			
//			HtmlEmail email = new HtmlEmail();
//			email.setCharset("euc-kr");
//			email.setHostName(smtpDto.getHost());
//			email.setSmtpPort(NumberUtils.toInt(smtpDto.getPort()));
//			if(!"".equals(smtpDto.getEmail()) || !"".equals(smtpDto.getPasswd())) {
//				email.setAuthenticator(new DefaultAuthenticator(smtpDto.getEmail(), smtpDto.getPasswd()));
//				email.setSSLOnConnect(true);
//			}
//			
//			email.setFrom(smtpDto.getEmail(), "Tadpole DB Hub");
//			email.addTo(emailDao.getTo());
//			email.setSubject(emailDao.getSubject());
//			email.setHtmlMsg(emailDao.getContent());
//			
//			email.send();
//			
//		} catch(Exception e) {
//			logger.error("send email", e);
//			throw e;
//		}
//	}
//	
//	private void sendMail(EmailDTO emailDao) throws Exception {
		Properties p = System.getProperties();
		p.put("mail.smtp.starttls.enable", "false");
		p.put("mail.smtp.host", smtpDto.getHost());
		p.put("mail.smtp.auth", "false");
		p.put("mail.smtp.port", smtpDto.getPort());
		
		Authenticator auth = new MyAuthentication(smtpDto.getEmail(), smtpDto.getPasswd());

		// session 생성 및 MimeMessage생성
		Session session = Session.getDefaultInstance(p, auth);
		MimeMessage msg = new MimeMessage(session);

		try {
			// 편지보낸시간
			msg.setSentDate(new Date());
			InternetAddress from = new InternetAddress();
			from = new InternetAddress(smtpDto.getEmail());
			// 이메일 발신자
			msg.setFrom(from);

			// 이메일 수신자
			InternetAddress to = new InternetAddress(emailDao.getTo());
			msg.setRecipient(Message.RecipientType.TO, to);

			// 이메일 제목
			msg.setSubject(emailDao.getSubject(), "UTF-8");

			// 이메일 내용
			msg.setText(emailDao.getContent(), "UTF-8");

			// 이메일 헤더
			msg.setHeader("content-Type", "text/html");

			// 메일보내기
			javax.mail.Transport.send(msg);

		} catch (AddressException addr_e) {
			logger.error("send eail", addr_e);
			throw addr_e;
		} catch (MessagingException msg_e) {
			logger.error("send eail", msg_e);
			throw msg_e;
		}
	}
}

class MyAuthentication extends Authenticator {
	PasswordAuthentication pa;

	public MyAuthentication(String id, String pw) {
		pa = new PasswordAuthentication(id, pw);
	}

	// 시스템에서 사용하는 인증정보
	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}