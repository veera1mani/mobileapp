package com.healthtraze.etraze.api.masters.controller;

import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.ListValue;
import com.healthtraze.etraze.api.masters.service.ListValueService;
import java.util.Collections;

@RestController()
@CrossOrigin
public class ListVauleController implements BaseCrudController<ListValue, String> {

	private Logger logger = LogManager.getLogger(ListVauleController.class);

	private final ListValueService service;

	public ListVauleController(ListValueService service) {
		this.service = service;
	}




	@GetMapping(value = "/listvalues")
	@Override
	public List<ListValue> findAll() {
		try {
			return service.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}
	
	
	

	@GetMapping(value = "/listvalue/{id}")
	@Override
	public ListValue findById(@PathVariable String id) {
		try {
			return service.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/listvalue")
	@Override
	public Result<ListValue> create(@RequestBody ListValue t) {
		try {

			return service.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/listvalue")
	@Override
	public Result<ListValue> update(@RequestBody ListValue t) {
		try {

			return service.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@DeleteMapping(value = "/listvalue/{id}")
	@Override
	public Result<ListValue> delete(@PathVariable String id) {
		Result<ListValue> result = new Result<>();
		try {
			service.delete(id);
			result.setCode(StringIteration.SUCCESS_CODE);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;

	}


	
	@GetMapping(value = "/alllist")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		
		return service.findAllValue(page,  value,sortBy,sortDir);
	}

	

}
