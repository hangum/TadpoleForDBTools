/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpold.commons.libs.core.mails;

import static org.junit.Assert.fail;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;

import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;

/**
 * Test class for {@link com.hangum.tadpole.commons.libs.core.mails.SendEmails}.
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 21.
 *
 */
public class SendEmailsTest {
	
	/**
	 * Test method for {@link com.hangum.tadpole.commons.libs.core.mails.SendEmails#sendMail(com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO)}.
	 */
	@Test
	public void testSendMail() {
		EmailDTO emailDao = new EmailDTO();
		emailDao.setTo("hangum@gmail.com");
		emailDao.setSubject("Testcase title");
		emailDao.setContent("Testcase --- content");
		
		SMTPDTO smtpDto = new SMTPDTO();
		smtpDto.setHost("smtp.googlemail.com");
		smtpDto.setPort("25");
		smtpDto.setEmail("hangum@gmail.com");
		smtpDto.setPasswd("atkgbvxcpqdbhtfv");
		
//		SendEmails sendMail = new SendEmails(smtpDto);
		try {
			sendSTMT(smtpDto, emailDao);
			System.out.println("보냈습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			fail("" + e.getMessage());
		}
	}
	
	/**
	 * using smtp server send 
	 * @param emailDao
	 * @throws Exception
	 */
	private static void sendSTMT(SMTPDTO smtpDto, EmailDTO emailDao) throws Exception {
		try {			
			HtmlEmail email = new HtmlEmail();
			email.setCharset("euc-kr");
			email.setHostName(smtpDto.getHost());
			email.setSmtpPort(NumberUtils.toInt(smtpDto.getPort()));
			if(!"".equals(smtpDto.getEmail()) || !"".equals(smtpDto.getPasswd())) {
				email.setAuthenticator(new DefaultAuthenticator(smtpDto.getEmail(), smtpDto.getPasswd()));
				email.setSSLOnConnect(true);
			}
			
			email.setFrom(smtpDto.getEmail(), "Tadpole DB Hub");
			email.addTo(emailDao.getTo());
			email.setSubject(emailDao.getSubject());
			email.setHtmlMsg(emailDao.getContent());
			
			email.send();
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
