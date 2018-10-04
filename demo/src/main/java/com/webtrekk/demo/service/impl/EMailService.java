package com.webtrekk.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Component;

import com.sun.mail.smtp.SMTPTransport;
import com.webtrekk.demo.data.EMailData;
import com.webtrekk.demo.producer.ProducerCreator;
import com.webtrekk.demo.service.IEMailService;

@Component("eMailService")
public class EMailService implements IEMailService {

	@Autowired
	private Environment env;

	@Override
	public void sendToEMailQueue(EMailData data) {
		ProducerCreator.runProducer(data);

	}

	@Override
	public void sendEmail(EMailData data) throws MessagingException, IOException {

		sendMail1(data);

		
	}

	private void sendMail1(EMailData data)
			throws MessagingException, IOException {
		//		String email = env.getProperty("email.user");
				String password = env.getProperty("email.password");
		
				Properties props = System.getProperties();
				props.put("mail.smtps.host", "smtp.gmail.com");
				props.put("mail.smtps.auth", "true");
				Session session = Session.getInstance(props, null);
				
				
				Message message = new MimeMessage(session);
				Multipart multipart = new MimeMultipart();
				MimeBodyPart attachPart = new MimeBodyPart();

				
				String attachFile = data.getAttachment();
				 
				DataSource source = new FileDataSource(attachFile);
				attachPart.setDataHandler(new DataHandler(source));
				attachPart.setFileName(new File(attachFile).getName());
				attachPart.attachFile(new File(attachFile));
				multipart.addBodyPart(attachPart);
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(data.getBody());
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(attachPart);
				message.setContent(multipart);
		
				
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(data.getTo(), false));
				message.setSubject(data.getSubject());
//				message.setText(data.getBody());
				message.setHeader("X-Mailer", data.getHeader());
				message.setSentDate(new Date());

				 
				// sets the multipart as message's content
				
				SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
				t.connect("smtp.gmail.com", data.getFrom(), password);
				t.sendMessage(message, message.getAllRecipients());
				System.out.println("Response: " + t.getLastServerResponse());
				t.close();
	}

}
