package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.documents.DateFormat;
import com.healthtraze.etraze.api.masters.service.DateFormatService;


@RestController
public class DateFormatController implements BaseCrudController<DateFormat, String>{

	
	private final DateFormatService service;
	
	public DateFormatController(DateFormatService service) {
		this.service = service;
	}
	

	@GetMapping("date-formats")
	@Override
	public List<DateFormat> findAll() {
		return service.findAll();
	}

	@GetMapping("date-format/{id}")
	@Override
	public DateFormat findById(@PathVariable String id) {
		return service.findById(id);
	}

	@PostMapping("date-format")
	@Override
	public Result<DateFormat> create(@RequestBody DateFormat t) {
		return service.create(t);
	}

	@PutMapping("date-format")
	@Override
	public Result<DateFormat> update(@RequestBody DateFormat t) {
		return service.update(t);
	}

	@DeleteMapping("date-format/{id}")
	@Override
	public Result<DateFormat> delete(@PathVariable String id) {
		return service.delete(id);
	}

}
