package com.healthtraze.etraze.api.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.MobilePin;
import com.healthtraze.etraze.api.security.service.MobilePinService;
import java.util.Collections;


@RestController
public class MobilePinController implements BaseCrudController<MobilePin, Long> {
	
	private final MobilePinService mobilePinService;
	
	@Autowired
	public MobilePinController(MobilePinService mobilePinService) {
		this.mobilePinService = mobilePinService;		
	}

	@Override
	public List<MobilePin> findAll() {
	
		return Collections.emptyList();
	}

	@Override
	public MobilePin findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GetMapping(value = "/is-pin-available")
	public Result<MobilePin> checkPin() {		
		return mobilePinService.checkPin();
	}

	@PostMapping(value = "/mobile-pin")
	@Override
	public Result<MobilePin> create(@RequestBody MobilePin t) {
		return mobilePinService.create(t);
	}

	@Override
	public Result<MobilePin> update(MobilePin t) {
		
		return null;
	}

	@Override
	public Result<MobilePin> delete(Long id) {
		
		return null;
	}
	

	
}
