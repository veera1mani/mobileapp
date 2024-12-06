package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.Deadlines;
import com.healthtraze.etraze.api.masters.service.DeadlinesService;
@RestController
public class DeadlinesController implements BaseCrudController<Deadlines,Long> {
	
	
	private DeadlinesService deadlinesService;
		
	@Autowired
	public DeadlinesController(DeadlinesService deadlinesService ) {
		this.deadlinesService=deadlinesService;
	}
	
	@Override
	@GetMapping(value = "/deadlines")
	public List<Deadlines> findAll() {
		return deadlinesService.findAll();
	}

	@Override
	public Deadlines findById(Long id) {
	
		return null;
	}
	
	@GetMapping(value = "/deadlines-tenant/{tid}/{uid}")
	public Result<Deadlines> findByTenantId(@PathVariable String tid,@PathVariable String uid) {
		
		return deadlinesService.findByTenantId(tid,uid);
		
	}
	
	@GetMapping(value = "/deadlines-all-tenant/{tid}/{uid}")
	public List<Deadlines> findByAllTenantId(@PathVariable String tid,@PathVariable String uid) {
		
		return deadlinesService.findByAllTenantId(tid,uid);
		
	}

	@Override
	@PostMapping(value = "/deadlines")
	public Result<Deadlines> create(@RequestBody Deadlines t) {
		return deadlinesService.create(t);
	}

	@Override
	@PutMapping(value = "/deadlines")
	public Result<Deadlines> update(@RequestBody Deadlines t) {
		return deadlinesService.update(t);
	}

	@Override
	public Result<Deadlines> delete(Long id) {
	
		return null;
	}
	
	

}
