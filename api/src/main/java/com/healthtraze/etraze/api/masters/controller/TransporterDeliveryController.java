package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.TransporterChartDTO;
import com.healthtraze.etraze.api.masters.model.TransporterDelivery;
import com.healthtraze.etraze.api.masters.service.TransporterDeliveryService;

@Controller
public class TransporterDeliveryController implements BaseCrudController<TransporterDelivery, String>  {

	private final TransporterDeliveryService transporterDeliveryService ; 
	
	@Autowired
	public TransporterDeliveryController(TransporterDeliveryService transporterDeliveryService) {
		super();
		this.transporterDeliveryService = transporterDeliveryService;
	}

	@Override
	public Result<TransporterDelivery> create(TransporterDelivery t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<TransporterDelivery> update(TransporterDelivery t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransporterDelivery> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransporterDelivery findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<TransporterDelivery> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GetMapping("/transporter-expenses")
	public HashMap<String , Object> getChart(@RequestParam("month") String month , @RequestParam("year") String year , @RequestParam("manufacturer") String manfId){
		return transporterDeliveryService.getChart(month,year,manfId);
	}
	
	@PostMapping("/transporter/fileReader/{sheetIndex}")
    public Result<Object> fileReader(@RequestPart(name = "file" , required = true)
    MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
			 if(!file.getOriginalFilename().endsWith(".xlsx")) {
		            return null;
		        }
		            return transporterDeliveryService.fileReader(file , sheetIndex);

    }

}
