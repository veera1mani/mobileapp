package com.healthtraze.etraze.api.masters.controller;

import java.util.HashMap;
import java.util.List;

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
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.service.ManufacturerService;

@Controller
public class ManufacturerController implements BaseCrudController<Manufacturer, String> {

	private final ManufacturerRepository manufactureRepository;

	private final ManufacturerService manufacturerService;
	

	public ManufacturerController(ManufacturerRepository manufactureRepository,
			ManufacturerService manufacturerService) {
		this.manufactureRepository = manufactureRepository;
		this.manufacturerService = manufacturerService;
	}

	@GetMapping("/manufacturers")
	@Override
	public List<Manufacturer> findAll() {
		return manufacturerService.findAll();
	}

	@GetMapping(value = "/manufacturerss")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value,
			@RequestParam(required = false) String sortBy, @RequestParam String sortDir) {

		return manufacturerService.findAllManufacture(page, limit, value, sortBy, sortDir);
	}

	@GetMapping("/manufacturer/{id}")
	@Override
	public Manufacturer findById(@PathVariable String id) {
		return manufacturerService.findById(id);
	}

	@PostMapping("/manufacturer")
	@Override
	public Result<Manufacturer> create(@RequestBody Manufacturer t) {
		return manufacturerService.create(t);
	}

	@PutMapping("/manufacturer")
	@Override
	public Result<Manufacturer> update(@RequestBody Manufacturer t) {
		return manufacturerService.update(t);
	}

	@DeleteMapping("/manufacturer/{id}")
	@Override
	public Result<Manufacturer> delete(@PathVariable String id) {
		return manufacturerService.delete(id);
	}
	@PutMapping("/de-activate-manufacturer")
    public Result<Manufacturer> activeAndDeActivateManuFacture(@RequestParam String manufacturerId) {
        return manufacturerService.activeAndDeActivateManuFacture(manufacturerId);
    }


	
	

	@GetMapping(value = "/manufacture-list")
	public List<Manufacturer> findManufactureList(@RequestParam String tenantId) {
		return manufactureRepository.findManufactureByTenant(tenantId);
	}
	
	@GetMapping(value = "/tenant-manufacture-list")
	public List<Manufacturer> findManufactureList() {
		return manufacturerService.findManufactureByTenant();
	}

	@PutMapping("/manu-status")
	public Result<TenantManufacture> updateActions(@RequestBody TenantManufacture id) {
		return manufacturerService.updateActions(id);
	}
	
	
	
}
