package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.Visitor;
import com.healthtraze.etraze.api.masters.service.VisitorService;


@Controller
public class VisitorController implements BaseCrudController<Visitor, String>{
	
	
	private final VisitorService visitorService;
	
	@Autowired
	public VisitorController(VisitorService bankService) {
		this.visitorService = bankService;
	}

	@GetMapping("/visitors")
	@Override
	public List<Visitor> findAll() {
		return visitorService.findAll();
	}

	@GetMapping("/visitor/{id}")
	@Override
	public Visitor findById(@PathVariable String id) {
		return visitorService.findById(id);
	}
	
	
	@GetMapping("/visitorByPhone/{phoneNumber}")
	public Visitor findByPhoneNumber(@PathVariable String phoneNumber) {
		return visitorService.findByPhoneNumber(phoneNumber);
	}
	
	@GetMapping("/all-visitors")
	public List<Visitor> findAllVisitors() {
		return visitorService.findAllVisitors();
	}
	
	@GetMapping("/get-visitors")
	public Result<Object> getVisitors(@RequestParam("search") String search, @RequestParam("page") int page, @RequestParam("month") int month) {
		return visitorService.getVisitors(search, page,month);
	}

	@PostMapping("/visitor")
    @Override
    public Result<Visitor> create(@RequestBody Visitor t) {
        return visitorService.create(t);
    }

    @PutMapping("/visitor")
    @Override
    public Result<Visitor> update(@RequestBody Visitor t) {
        return visitorService.update(t);
    }

	@DeleteMapping("/visitor/{id}")
	@Override
	public Result<Visitor> delete(@PathVariable String id) {
		return visitorService.delete(id);
	}

}
