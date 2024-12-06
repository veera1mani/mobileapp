package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.documents.State;
import com.healthtraze.etraze.api.masters.service.StateService;

@RestController()
public class StateController implements BaseCrudController<State, String> {

	private Logger logger = LogManager.getLogger(StateController.class);

	private final StateService stateService;
	@Autowired
	public StateController(StateService stateService) {
		this.stateService=stateService;
	}
	
	@GetMapping(value = "/states")
	@Override
	public List<State> findAll() {
		try {
			return stateService.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@GetMapping(value = "/state/{id}")
	@Override
	public State findById(@PathVariable String id) {
		try {
			return stateService.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/state/country/{countryCode}")
	public List<State> findByCountryCode(@PathVariable String countryCode) {
		try {
			return stateService.findByCountryCode(countryCode);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@PostMapping(value = "/state")
	@Override
	
	public Result<State> create(@RequestBody State t) {
		try {
			return stateService.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/state")
	@Override
	
	public Result<State> update(@RequestBody State t) {
		try {

			return stateService.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@DeleteMapping(value = "/state/{id}")
	@Override
	public Result<State> delete(@PathVariable String id) {
		Result<State> result = new Result<>();
		try {
			stateService.delete(id);
			result.setCode(StringIteration.SUCCESS_CODE);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@GetMapping(value = "/allState")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		
		return stateService.findAllState(page,  value,sortBy,sortDir);
	}

	 @PutMapping("/de-activate-state")
	    public Result<State> activeAndDeActivateCity(@RequestParam String id) {
	        return stateService.activeAndDeActivateState(id);
	    }
	 
	 @PostMapping("/state/fileReader/{sheetIndex}")
		public List<State> fileReader(@RequestPart(name = "file" , required = true) 
		MultipartFile file , @PathVariable() String sheetIndex) throws  IOException  {
				 if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".xlsx")) {

				return Arrays.asList(new State());
			}
				return stateService.fileReader(file , sheetIndex);
		}
	
	
	
}
