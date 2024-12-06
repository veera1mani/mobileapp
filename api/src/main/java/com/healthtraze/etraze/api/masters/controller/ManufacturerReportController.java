package com.healthtraze.etraze.api.masters.controller;

import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.ManufacturerReport;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.projections.ManuafctureTenantReport;
import com.healthtraze.etraze.api.masters.projections.ManufactureReport;
import com.healthtraze.etraze.api.masters.service.ManufacturerReportService;

@RestController
public class ManufacturerReportController implements BaseCrudController<ManufacturerReport, String>  {
	
	private final ManufacturerReportService manufacturerReportService;
	
	@Autowired
	public ManufacturerReportController(ManufacturerReportService manufacturerReportService) {
		this.manufacturerReportService = manufacturerReportService;
	}

	@Override
	public List<ManufacturerReport> findAll() {
	
		return null;
	}
	
	@GetMapping("manager-manf-report")
	public List<List<ManufactureReport>>  findAll(@RequestParam String manufacturerId,@RequestParam Integer month) {	
		return manufacturerReportService.findAll(manufacturerId,month);
	}
	
	@GetMapping("manufacture-report")
	public List<List<ManufactureReport>> getAllReportToManufacturer(@RequestParam String tenantId,@RequestParam Integer month ) {	
		return manufacturerReportService.getAllReportToManufacturer(tenantId,month);
	}
	
	@GetMapping("manufacture-tenant-report")
	public List<ManuafctureTenantReport> getAllTenantReport(@RequestParam List<String> tenantId,@RequestParam String code) {	
		return manufacturerReportService.getAllTenantReport(tenantId,code);
	}

	@Override
	public ManufacturerReport findById(String id) {
		
		return null;
	}

	@Override
	public Result<ManufacturerReport> create(ManufacturerReport t) {
	
		return null;
	}

	@Override
	public Result<ManufacturerReport> update(ManufacturerReport t) {
		
		return null;
	}

	@Override
	public Result<ManufacturerReport> delete(String id) {
		
		return null;
	}
	
	@RequestMapping(value = "/upload-manufacturer-report", headers = "content-type=multipart/*", method = RequestMethod.POST)
   	public Result<ManufacturerReport> uploadManufacturerReport(@RequestParam("file") MultipartFile file ,
   			@RequestParam("id") String manfId,
   			@RequestParam("manufacturerId") String manufacturerId,@RequestParam("fileName") String fileName,@RequestParam("month") Integer month) {
       return manufacturerReportService.create(file, manfId,manufacturerId,fileName,month);
	}

	
	@GetMapping(value="listvalue-manufacturer")
	public List<Object[]> listvalueManufacturer() {
		return manufacturerReportService.listvalueManufacturer();
	}
	@GetMapping(value="manufacturer-tenantlist")
	public List<Object[]> tenantByManufacturer() {
		return manufacturerReportService.tenantByManufacturer();
	}
	
	@RequestMapping(value = "/update-manufacturer-report", headers = "content-type=multipart/*", method = RequestMethod.POST)
   	public Result<ManufacturerReport> uploadManufacturerReport(@RequestParam("file") MultipartFile file ,
   			@RequestParam("id") String manfId,@RequestParam("fileName") String fileName,@RequestParam("month") Integer month) {
       return manufacturerReportService.update(file, manfId,fileName,month);
	}

	
	
}
