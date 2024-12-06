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
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.repository.NotificationTemplateRepository;
@Component
public class NotificationTemplateService implements BaseService<NotificationTemplate, String> {
	private Logger logger = LogManager.getLogger(NotificationTemplateService.class);

	
	private NotificationTemplateRepository repo;
	
	@Autowired(required = true)
	public NotificationTemplateService(NotificationTemplateRepository repo) {
		this.repo=repo;
	}
	
	@Override
	public List<NotificationTemplate> findAll() {
		try {
			return repo.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@Override
	public NotificationTemplate findById(String id) {
		try {
			Optional<NotificationTemplate> option = repo.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<NotificationTemplate> create(NotificationTemplate t) {
		Result<NotificationTemplate> result = new Result<>();
		try {
			CommonUtil.setCreatedOn(t);
			NotificationTemplate s = repo.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage("NotificationTemplate created Successfully ");
			result.setData(s);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<NotificationTemplate> update(NotificationTemplate t) {
		Result<NotificationTemplate> result = new Result<>();
		try {
			
		Optional<NotificationTemplate>	option = repo.findById(t.getNotificationTemplateId());
		
		if(!option.isPresent()) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("TemplateId not found");
			result.setData(t);
			return result;
		}
		
		NotificationTemplate notificationtemp = option.get();
		
		
		CommonUtil.setModifiedOn(notificationtemp);
		notificationtemp.setSubject(t.getSubject());
		notificationtemp.setNotificationTemplate(t.getNotificationTemplate());
		NotificationTemplate s = repo.save(notificationtemp);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage("updated successfully ");
			result.setData(s);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<NotificationTemplate> delete(String id) {
		Result<NotificationTemplate> result = new Result<>();
		try {
			Optional<NotificationTemplate> tr = repo.findById(id);
			if (tr.isPresent()) {
				repo.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id +"deleted successfully");
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	
}
