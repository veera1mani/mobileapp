package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.ReturnNote;
import com.healthtraze.etraze.api.masters.service.ReturnNoteService;

@Controller
public class ReturnNoteController implements BaseCrudController<ReturnNote,String>{
	
	private final ReturnNoteService returnNoteService;
	
	@Autowired
	public ReturnNoteController(ReturnNoteService returnNoteService) {
		super();
		this.returnNoteService = returnNoteService;
	}

	@Override
	public List<ReturnNote> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnNote findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PostMapping(value = "return-note")
	public Result<ReturnNote> create(ReturnNote t) {
		return returnNoteService.create(t);
	}
	
	@PostMapping(value = "return-notes")
	public Result<ReturnNote> create(@RequestBody List<ReturnNote> t) {
		return returnNoteService.create(t);
	}

	@Override
	@PutMapping(value = "return-note")
	public Result<ReturnNote> update(@RequestBody ReturnNote t) {
		return returnNoteService.update(t);
	}

	@Override
	public Result<ReturnNote> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
