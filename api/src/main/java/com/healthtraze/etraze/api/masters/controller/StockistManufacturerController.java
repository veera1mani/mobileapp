package com.healthtraze.etraze.api.masters.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;
import com.healthtraze.etraze.api.masters.service.StockistManufacturerService;

  @RestController
  public class StockistManufacturerController  implements BaseCrudController<StockistManufacture, Long> {
	 
	
	private StockistManufacturerService stockistManufacturerService;
	public StockistManufacturerController(StockistManufacturerService stockistManufacturerService) {
		 this.stockistManufacturerService=stockistManufacturerService;
	}
	@Override
	public List<StockistManufacture> findAll() {
		
		return new ArrayList<>();
	}

	 @Override
	 public StockistManufacture findById(Long id) {
		
				return new StockistManufacture();
	}

	@Override
	public Result<StockistManufacture> create(StockistManufacture t) {
	
		return new Result<>();
	}

	@Override
	public Result<StockistManufacture> update(StockistManufacture t) {
		
		return new Result<>();
	}

	@Override
	@DeleteMapping("/manufacturer-stk/{id}")
	public Result<StockistManufacture> delete(@PathVariable Long id) {
		return stockistManufacturerService.delete(id);
	}
	
	@GetMapping(value = "/stockist-manufacturer-list")
	public List<Manufacturer> getAllManufacturerByStockist() {		
		return stockistManufacturerService.getAllManufacturerByStockist();
	}
	
	@GetMapping(value = "/stockist-manufacturer-list/{id}")
	public List<Manufacturer> getAllManufacturerByStockistId(@PathVariable String id) {		
		return stockistManufacturerService.getAllManufacturerByStockistId(id);
	}
	

}
