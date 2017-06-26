package com.company.services.mailer;

import org.springframework.stereotype.Service;

@Service
public class MailerService {

	public MailerService(){}
	
	public void sendMail(String message) {
		System.out.println(message);
	}
}