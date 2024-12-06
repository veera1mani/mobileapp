package com.healthtraze.etraze.api.security.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.service.NotificationTemplateService;



@RestController
public class NotificationTemplateController implements BaseCrudController<NotificationTemplate, String> {

	
	private final NotificationTemplateService notificationTemplateService;
	
	@Autowired
	public NotificationTemplateController(NotificationTemplateService notificationTemplateService) {
		this.notificationTemplateService = notificationTemplateService;
	}


	@GetMapping("/notificationTemplates")
	@Override
	public List<NotificationTemplate> findAll() {
		return notificationTemplateService.findAll();
	}

	@GetMapping("/notificationTemplate/{id}")
	@Override
	public NotificationTemplate findById(@PathVariable String id) {
		return notificationTemplateService.findById(id);
	}

	@PostMapping(value = "/notificationTemplate")
	@Override
	public Result<NotificationTemplate> create(@RequestBody NotificationTemplate t) {
		return notificationTemplateService.create(t);
	}

	@PutMapping(value = "/notificationTemplate")
	@Override
	public Result<NotificationTemplate> update(@RequestBody NotificationTemplate t) {
		return notificationTemplateService.update(t);
	}

	@DeleteMapping(value = "/notificationTemplate/{id}")
	@Override
	public Result<NotificationTemplate> delete(@PathVariable String id) {
		return notificationTemplateService.delete(id);
	}

	

}
