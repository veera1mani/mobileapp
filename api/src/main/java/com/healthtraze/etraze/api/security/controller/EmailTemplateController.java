package com.healthtraze.etraze.api.security.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.SendMail;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;

@RestController
public class EmailTemplateController implements BaseCrudController<EmailTemplate, String> {

	
	private final EmailTemplateService emailTemplateService;
	
	private final EmailService emailService;
	
	@Autowired
	public EmailTemplateController(EmailTemplateService emailTemplateService, EmailService emailService) {
		super();
		this.emailTemplateService = emailTemplateService;
		this.emailService = emailService;
	}

	@GetMapping("/emailTemplates")
	@Override
	public List<EmailTemplate> findAll() {
		return emailTemplateService.findAll();
	}

	@GetMapping("/emailTemplate/{id}")
	@Override
	public EmailTemplate findById(@PathVariable String id) {
		return emailTemplateService.findById(id);
	}

	@PostMapping(value = "/emailTemplate")
	@Override
	public Result<EmailTemplate> create(@RequestBody EmailTemplate t) {
		return emailTemplateService.create(t);
	}

	@PutMapping(value = "/emailTemplate")
	@Override
	public Result<EmailTemplate> update(@RequestBody EmailTemplate t) {
		return emailTemplateService.update(t);
	}

	@DeleteMapping(value = "/emailTemplate/{id}")
	@Override
	public Result<EmailTemplate> delete(@PathVariable String id) {
		return emailTemplateService.delete(id);
	}
	
	 @GetMapping(value = "/mails")
	    public Page<SendMail> findAllMail(@RequestParam int page,@RequestParam (required = false) String search,
	             @RequestParam(required = false) String sortBy ,@RequestParam(required = false) String sortDir) {
		 return emailService.findAll(page,sortBy, sortDir,search);
	 }

	

}
