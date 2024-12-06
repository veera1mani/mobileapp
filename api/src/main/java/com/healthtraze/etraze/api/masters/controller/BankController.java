package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.Bank;
import com.healthtraze.etraze.api.masters.service.BankService;


@Controller
public class BankController implements BaseCrudController<Bank, String>{
	
	
	private final BankService bankService;
	
	@Autowired
	public BankController(BankService bankService) {
		this.bankService = bankService;
	}

	@GetMapping("/banks")
	@Override
	public List<Bank> findAll() {
		return bankService.findAll();
	}

	@GetMapping("/bank/{id}")
	@Override
	public Bank findById(@PathVariable String id) {
		return bankService.findById(id);
	}

	@PostMapping("/bank")
    @Override
    public Result<Bank> create(@RequestBody Bank t) {
        return bankService.create(t);
    }

    @PutMapping("/bank")
    @Override
    public Result<Bank> update(@RequestBody Bank t) {
        return bankService.update(t);
    }
    
    @PutMapping("/de-activate-bank")
    public Result<Bank> activeAndDeActivateBank(@RequestParam String code) {
        return bankService.activeAndDeActivateBank(code);
    }

	@DeleteMapping("/bank/{id}")
	@Override
	public Result<Bank> delete(@PathVariable String id) {
		return bankService.delete(id);
	}
	
	
	@GetMapping(value = "/allBanks")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		
		return bankService.findAllBank(page,  value,sortBy,sortDir);
	}
	
	@PostMapping("/bank/fileReader/{sheetIndex}")
	public Result<List<Bank>> fileReader(@RequestPart(name = "file" , required = true) 
	MultipartFile file , @PathVariable() String sheetIndex) throws  IOException  {
		if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".xlsx")) {
			return null;
		}
			return bankService.fileReader(file , sheetIndex);
	}


}
