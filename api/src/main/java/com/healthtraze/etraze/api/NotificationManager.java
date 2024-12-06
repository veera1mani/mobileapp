package com.healthtraze.etraze.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.security.model.SendMail;
import com.healthtraze.etraze.api.security.service.EmailService;

@Component
public class NotificationManager {
	
	private final EmailService emailService;
	private final EmailNotification emailnotification;
	
	@Autowired(required = true)
	public NotificationManager(EmailService emailService,EmailNotification emailnotification) {
		this.emailService = emailService;
		this.emailnotification = emailnotification;
	}	


	@Scheduled(fixedDelay = 5000, initialDelay = 5000)
	public void emailJob() {
		List<SendMail> list = emailService.findByStatus(Constants.PENDING);
		for (int i = 0; i < list.size(); i++) {
			emailnotification.doNotification(list.get(i));
		}
	}
}