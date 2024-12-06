package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.VisitorHistory;
import com.healthtraze.etraze.api.masters.service.VisitorHistoryService;

@Controller
public class VisitorHistoryController implements BaseCrudController<VisitorHistory, String>  {

private final VisitorHistoryService visitorHistoryService;
	
	@Autowired
	public VisitorHistoryController(VisitorHistoryService visitorHistoryService) {
		this.visitorHistoryService = visitorHistoryService;
	}
	
	@GetMapping(value ="/visitors-history")	
	public Result<Object> findAll(@RequestParam("id") String id ,@RequestParam("month") int mon ,@RequestParam("page") int page ) {		
		return visitorHistoryService.findAll(id,mon,page);
	}

	@Override
	public VisitorHistory findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostMapping(value = "/check-in")
	@Override
	public Result<VisitorHistory> create(VisitorHistory t) {
		return visitorHistoryService.create(t);
	}

	@PutMapping(value = "/check-out")
	@Override
	public Result<VisitorHistory> update(VisitorHistory t) {
		return visitorHistoryService.update(t);
	}

	@Override
	public Result<VisitorHistory> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VisitorHistory> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
