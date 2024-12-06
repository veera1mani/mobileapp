package com.healthtraze.etraze.api.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.repository.EmailTemplateRepository;

@Component
public class EmailTemplateService implements BaseService<EmailTemplate, String> {

	private Logger logger = LogManager.getLogger(EmailTemplateService.class);

	private final EmailTemplateRepository repository;
	
	@Autowired(required = true)
	public EmailTemplateService(EmailTemplateRepository repository) {
		this.repository=repository;
	}

	@Override
	public List<EmailTemplate> findAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@Override
	public EmailTemplate findById(String id) {
		try {
			Optional<EmailTemplate> option = repository.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<EmailTemplate> create(EmailTemplate t) {
		Result<EmailTemplate> result = new Result<>();
		try {
			CommonUtil.setCreatedOn(t);
			EmailTemplate s = repository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYCREATED);
			result.setData(s);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<EmailTemplate> update(EmailTemplate t) {
		Result<EmailTemplate> result = new Result<>();
		try {
			
		Optional<EmailTemplate>	option = repository.findById(t.getMailTemplateId());
		
		if(!option.isPresent()) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.MAILTEMPLATEIDNOTFOUND);
			result.setData(t);
			return result;
		}
		
		EmailTemplate emailTemplate = option.get();
		
		
		CommonUtil.setModifiedOn(emailTemplate);
		emailTemplate.setSubject(t.getSubject());
		emailTemplate.setMailTemplate(t.getMailTemplate());
			EmailTemplate s = repository.save(emailTemplate);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			result.setData(s);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<EmailTemplate> delete(String id) {
		Result<EmailTemplate> result = new Result<>();
		try {
			Optional<EmailTemplate> tr = repository.findById(id);
			if (tr.isPresent()) {
				repository.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id +StringIteration.DELETEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	

}
