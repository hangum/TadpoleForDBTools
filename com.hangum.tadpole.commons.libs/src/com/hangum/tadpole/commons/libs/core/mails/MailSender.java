//package com.hangum.tadpole.commons.libs.core.mails;
//
//import java.util.Date;
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
// 
//
//public class MailSender {
//	public static void send() {
//		Properties p = System.getProperties();
//		p.put("mail.smtp.starttls.enable", "false");
//		p.put("mail.smtp.host", "218.38.118.112");
//		p.put("mail.smtp.auth", "false");
//		p.put("mail.smtp.port", "25");
//
//		Authenticator auth = new MyAuthentication();
//
//		// session 생성 및 MimeMessage생성
//		Session session = Session.getDefaultInstance(p, auth);
//		MimeMessage msg = new MimeMessage(session);
//
//		try {
//			// 편지보낸시간
//			msg.setSentDate(new Date());
//
//			InternetAddress from = new InternetAddress();
//
//			from = new InternetAddress("smyoo@iloen.com");
//
//			// 이메일 발신자
//			msg.setFrom(from);
//
//			// 이메일 수신자
//			InternetAddress to = new InternetAddress("smyoo@iloen.com");
//			msg.setRecipient(Message.RecipientType.TO, to);
//
//			// 이메일 제목
//			msg.setSubject("메일 전송 테스트", "UTF-8");
//
//			// 이메일 내용
//			msg.setText("내용", "UTF-8");
//
//			// 이메일 헤더
//			msg.setHeader("content-Type", "text/html");
//
//			// 메일보내기
//			javax.mail.Transport.send(msg);
//
//			System.out.println("success");
//
//		} catch (AddressException addr_e) {
//			addr_e.printStackTrace();
//		} catch (MessagingException msg_e) {
//			msg_e.printStackTrace();
//		}
//	}
//
//}
