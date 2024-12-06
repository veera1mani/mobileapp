package com.healthtraze.etraze.api.masters.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.Claim;
import com.healthtraze.etraze.api.masters.service.ClaimService;


@RestController
public class ClaimController implements BaseCrudController<Claim, String> {
	

	private final ClaimService claimService;

	public ClaimController(ClaimService claimService) {
		this.claimService = claimService;
	}

	@Override
	@GetMapping("/claims")
	public List<Claim> findAll() {
		return claimService.findAll();
	}

	@Override
	@GetMapping( "/claim/{id}")
	public Claim findById(@PathVariable   String id) {
		return claimService.findById(id);
	}

	@Override
	@PostMapping(value ="/claim")
	public Result<Claim> create(@RequestBody   Claim t) {
		return claimService.create(t);	
	}
	

	@Override
	@PutMapping("/claim")
	public Result<Claim> update(@RequestBody  Claim t) {
		return claimService.update(t);
	}

	@Override
	@DeleteMapping("/claim/{id}")
	public Result<Claim> delete(@PathVariable   String id) {
		return claimService.delete(id);
	}

	@GetMapping(value = "/claimss")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		return claimService.findAllClaimsByTenant(page,value,sortBy,sortDir);
	}
	
	@GetMapping("/claims-app")
	public Result<HashMap<String, Object>> findClaims() {
		return claimService.findClaims();
	}
	
	

}