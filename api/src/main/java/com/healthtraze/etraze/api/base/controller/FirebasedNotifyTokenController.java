package com.healthtraze.etraze.api.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.model.FirebasedNotifyToken;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.FirebasedNotifyTokenService;
import java.util.Collections;

@RestController
public class FirebasedNotifyTokenController implements BaseCrudController<FirebasedNotifyToken, Long> {

	
	private final FirebasedNotifyTokenService firebasedNotifyTokenService;
	
	@Autowired
	public FirebasedNotifyTokenController(FirebasedNotifyTokenService firebasedNotifyTokenService) {
		this.firebasedNotifyTokenService = firebasedNotifyTokenService;
	}

	@Override
	public List<FirebasedNotifyToken> findAll() {

		return Collections.emptyList();
	}

	@Override
	public FirebasedNotifyToken findById(Long id) {
		return null;
	}

	@PostMapping("/notification-token")
	@Override
	public Result<FirebasedNotifyToken> create(@RequestBody FirebasedNotifyToken t) {
		return  firebasedNotifyTokenService.create(t);
	}

	@Override
	public Result<FirebasedNotifyToken> update(FirebasedNotifyToken t) {

		return null;
	}

	@Override
	public Result<FirebasedNotifyToken> delete(Long id) {

		return null;
	}

}
