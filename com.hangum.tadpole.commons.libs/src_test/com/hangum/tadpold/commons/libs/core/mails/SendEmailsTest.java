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
		emailDao.setContent("Testcase content");
		
		SMTPDTO smtpDto = new SMTPDTO();
		smtpDto.setHost("smtp.googlemail.com");
		smtpDto.setPort("465");
		smtpDto.setEmail("hangum@gmail.com");
		smtpDto.setPasswd("question");
		
		SendEmails sendMail = new SendEmails(smtpDto);
		try {
			sendMail.sendMail(emailDao);
		} catch (Exception e) {
			fail("" + e.getMessage());
		}
		
	}

}
