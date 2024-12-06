package com.healthtraze.etraze.api.security.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.UserSession;
import com.healthtraze.etraze.api.security.service.UserSessionService;

@RestController
public class UserSessionController implements BaseCrudController<UserSession, String> {

	private Logger logger = LogManager.getLogger(UserSessionController.class);

	
	private final UserSessionService service;
	
	
    @Autowired
	public UserSessionController(UserSessionService service) {
		this.service = service;
	}

	@GetMapping(value = "/usersessions")
	@Override
	public List<UserSession> findAll() {
		try {
			return service.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@GetMapping(value = "/usersession/{id}")
	@Override
	public UserSession findById(@PathVariable String id) {
		try {
			return service.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/usersession/user/{userId}")
	public List<UserSession> findByRoleId(@PathVariable String userId) {
		try {
			return service.getUserByUserId(userId);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@PostMapping(value = "/usersession")
	@Override
	public Result<UserSession> create(@RequestBody UserSession t) {
		try {
			return service.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/usersession")
	@Override
	public Result<UserSession> update(@RequestBody UserSession t) {
		
		return null;
	}

	@DeleteMapping(value = "/usersession/{roleId}")
	@Override
	public Result<UserSession> delete(@PathVariable String roleId) {
		return service.delete(roleId);
	}
}
