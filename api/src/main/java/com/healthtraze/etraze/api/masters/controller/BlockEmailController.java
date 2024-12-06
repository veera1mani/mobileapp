package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.BlockEmailDTO;
import com.healthtraze.etraze.api.masters.model.BlockEmail;
import com.healthtraze.etraze.api.masters.service.BlockEmailService;
import java.util.Collections;

@RestController
public class BlockEmailController implements BaseCrudController<BlockEmail,String> {
	
	private final BlockEmailService blockEmailService;
	
	
    @Autowired
	public BlockEmailController(BlockEmailService blockEmailService) {
		this.blockEmailService = blockEmailService;
	}

	@Override
	public List<BlockEmail> findAll() {
		return Collections.emptyList();
	}
	
	@GetMapping(value = "/all-block-email")
	public List<BlockEmailDTO> findAllByTenant() {
		return blockEmailService.findAllByTenant();
	}

	@Override
	@GetMapping(value = "block-email/{id}")
	public BlockEmail findById(@PathVariable String id) {
		return blockEmailService.findById(id);
	}

	@Override
	@PostMapping(value = "/block-email")
	public Result<BlockEmail> create( @RequestBody BlockEmail t) {
		return blockEmailService.create(t);
	}

	@Override
	public Result<BlockEmail> update(BlockEmail t) {
		return null;
	}

	@Override
	@DeleteMapping(value = "block-email/{id}")
	public Result<BlockEmail> delete(@PathVariable String id) {
		return blockEmailService.delete(id);
	}

}
