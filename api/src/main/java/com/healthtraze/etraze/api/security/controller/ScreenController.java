package com.healthtraze.etraze.api.security.controller;

import java.util.ArrayList;
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
import com.healthtraze.etraze.api.masters.dto.UserRoleServiceDTO;
import com.healthtraze.etraze.api.security.model.Screen;
import com.healthtraze.etraze.api.security.service.ScreenService;

@RestController()
public class ScreenController implements BaseCrudController<Screen, String> {

	private Logger logger = LogManager.getLogger(ScreenController.class);

	private final ScreenService screenService;

	@Autowired(required = true)
	public ScreenController(ScreenService screenService) {
		this.screenService = screenService;
	}
	
	@GetMapping(value = "/screens")
	@Override
	public List<Screen> findAll() {
		try {
			return screenService.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	
	@GetMapping(value = "/user-screens/{id}")
	public List<UserRoleServiceDTO> findScreenForUser(@PathVariable String id) {
		try {
			return screenService.findScreenForUser(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	

	@GetMapping(value = "/screen/{id}")
	@Override
	public Screen findById(@PathVariable String id) {
		try {
			return screenService.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/screen")
	@Override
	public Result<Screen> create(@RequestBody Screen t) {
		try {
			return screenService.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/screen")
	@Override
	public Result<Screen> update(@RequestBody Screen t) {
		try {
			return screenService.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}


	
	@DeleteMapping(value = "/screen/{id}")
	@Override
	public Result<Screen> delete(@PathVariable String id) {
		Result<Screen> result = new Result<>();
		try {
			screenService.delete(id);
			result.setCode(StringIteration.SUCCESS_CODE);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}
}
