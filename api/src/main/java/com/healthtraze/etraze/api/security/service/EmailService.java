package com.healthtraze.etraze.api.security.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.security.model.SMSData;
import com.healthtraze.etraze.api.security.model.SendMail;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.repository.EmailRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service("emailService")
public class EmailService {

	private Logger logger = LogManager.getLogger(EmailService.class);

	private final EmailRepository emailRepository;

	private final UserRepository userRepository;

	private final JavaMailSender emailSender;

	EmailTemplateService emailTemplateService;

	@Autowired
	public EmailService(EmailRepository emailRepository, UserRepository userRepository, JavaMailSender emailSender,
			EmailTemplateService emailTemplateService) {
		this.emailRepository = emailRepository;
		this.userRepository = userRepository;
		this.emailSender = emailSender;
		this.emailTemplateService = emailTemplateService;
	}

	public Page<SendMail> findAll(int page,String sortBy,String sortDir,String search) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Pageable pageable = createPageableEmail(page, sortBy, sortDir);
				return emailRepository.findByTenantId(u.getTenantId(),search,pageable);

			}

		} catch (Exception e) {

			logger.error("", e);
		}
		return null;
	}

	public SendMail update(SendMail mail) {
		return emailRepository.save(mail);
	}
	
	private Pageable createPageableEmail(int page,String sortBy, String sortDir) {
		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = StringIteration.CREATED_ON;
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir =StringIteration.DESC;
		}
		return PageRequest.of(page,Constants.PAGE_SIZE_10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	}

	public SendMail findById(String id) {

		Optional<SendMail> ct = emailRepository.findById(id);
		if (ct.isPresent()) {
			return ct.get();
		}
		return null;

	}

	public List<SendMail> findByStatus(String status) {
		return emailRepository.findByStatus(status);
	}

	public String sendEmail(SendMail emailData) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(emailData.getFrom());
			message.setSubject(emailData.getMessage());
			message.setText(emailData.getMessage());

			emailSender.send(message);
		} catch (MailSendException e) {

			logger.error(StringIteration.SENDEMAILERROR, e);
			return StringIteration.ERROR_CODE1;
		}
		return StringIteration.SUCCESS_CODE;

	}

	public List<SMSData> pendingSMSList() {
		return new ArrayList<>();

	}

	public void constructResendVerificationTokenEmail(final String token, final UserAuth user, final String language) {

		Optional<User> u = userRepository.findById(user.getUserId());

		SendMail mail = new SendMail();
		mail.setFrom(ConfigUtil.getFromEmail());
		mail.setSubject("Welldercare - New User Activation");

		String[] s = { u.get().getEmail() };
		mail.setTo(s);
		mail.setToName(u.get().getFirstName());
		mail.setStatus(Constants.PENDING);
		mail.setId(CommonUtil.getID());

		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		VelocityEngine ve = new VelocityEngine();
		ve.init(p);

		VelocityContext context = new VelocityContext();
		context.put(Constants.USER, u);
		context.put(Constants.TOKEN, token);
		context.put(Constants.LANGUAGE, language);
		context.put(Constants.ORIGIN, ConfigUtil.getAppLink());
		context.put(Constants.NAME, u.get().getFirstName() + StringIteration.SPACE + u.get().getLastName());

		Template temp = ve.getTemplate("templates/email_html.vm");

		StringWriter writer = new StringWriter();
		temp.merge(context, writer);
		mail.setMessage(writer.toString());

		emailRepository.save(mail);

		logger.info(Constants.DONE);

	}

	/*
	 * common method for send mail
	 * 
	 */
	public void sendEmails(String[] emailId, String message, String subject,List<String> name) {
		SendMail mail = new SendMail();
		mail.setFrom(ConfigUtil.getFromEmail());
		mail.setStatus(Constants.PENDING);
		mail.setId(CommonUtil.getID());
		mail.setSubject(subject);
		mail.setMessage(message);
		mail.setTo(emailId);
		mail.setDeliverDate(new Date());
		Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
		if (us.isPresent()) {
			mail.setTenantId(us.get().getTenantId());
		}
		CommonUtil.setCreatedOn(mail);
		for(String to : name) {
			mail.setToName(to);
			emailRepository.save(mail);
		}
	}
	
	public void sendEmails(String[] emailId, String message, String subject,String name,User us) {
		SendMail mail = new SendMail();
		mail.setFrom(ConfigUtil.getFromEmail());
		mail.setStatus(Constants.PENDING);
		mail.setId(CommonUtil.getID());
		mail.setSubject(subject);
		mail.setMessage(message);
		mail.setTo(emailId);
		mail.setDeliverDate(new Date());
		mail.setTenantId(us.getTenantId());
		CommonUtil.setCreatedOn(mail);
		mail.setToName(name);
		emailRepository.save(mail);
		
	}

}
