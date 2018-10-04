package com.webtrekk.demo.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.webtrekk.demo.data.EMailData;

public interface IEMailService {
	
	void sendToEMailQueue(EMailData data);
	
	void sendEmail(EMailData data) throws MessagingException, IOException;

}
