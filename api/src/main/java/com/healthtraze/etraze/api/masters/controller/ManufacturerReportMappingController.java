package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.ManufacturerReportMapping;
import com.healthtraze.etraze.api.masters.service.ManufacturerReportMappingService;

@RestController
public class ManufacturerReportMappingController implements BaseCrudController<ManufacturerReportMapping, String> {

	
	private final ManufacturerReportMappingService manufacturerReportMappingService;
	
	@Autowired
	public ManufacturerReportMappingController(ManufacturerReportMappingService manufacturerReportMappingService) {
		this.manufacturerReportMappingService = manufacturerReportMappingService;
	}
  
	
	@GetMapping("manufacturer-report")
	public List<List<ManufacturerReportMapping>> findAll(@RequestParam String manufacturetId) {
		
		return manufacturerReportMappingService.findAll(manufacturetId);
	}
	
	@Override
	public List<ManufacturerReportMapping> findAll() {
		
		return manufacturerReportMappingService.findAll();
	}

	@Override
	public ManufacturerReportMapping findById(String id) {
		
		return manufacturerReportMappingService.findById(id);
	}

	@Override
	@PostMapping(value="manufacturer-report")
	public Result<ManufacturerReportMapping> create(@RequestBody ManufacturerReportMapping t) {
		
		return manufacturerReportMappingService.create(t);
	}

	@Override
	@PutMapping(value="manufacturer-report")
	public Result<ManufacturerReportMapping> update(@RequestBody ManufacturerReportMapping t) {
	
		return manufacturerReportMappingService.update(t);
	}
	@PutMapping(value="delete-manufacturer-report")
	public Result<ManufacturerReportMapping> delete(@RequestBody ManufacturerReportMapping t) {
	
		return manufacturerReportMappingService.delete(t);
	}

	
	@Override
	public Result<ManufacturerReportMapping> delete(String id) {
		
		return manufacturerReportMappingService.delete(id);
	}
	

}
