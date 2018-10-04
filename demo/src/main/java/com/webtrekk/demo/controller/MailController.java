package com.webtrekk.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.webtrekk.demo.data.EMailData;
import com.webtrekk.demo.service.IEMailService;



@RestController(value = "/")
public class MailController {
	
	@Autowired
    @Qualifier("eMailService")
    private IEMailService eMailService;



	@RequestMapping(value = "emails", method = RequestMethod.POST)
	public void sendMail(@RequestBody EMailData data) {

		eMailService.sendToEMailQueue(data);

	}


}
