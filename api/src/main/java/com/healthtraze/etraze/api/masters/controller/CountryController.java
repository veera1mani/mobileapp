package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.documents.Country;
import com.healthtraze.etraze.api.masters.service.CountryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController()
@CrossOrigin
@Api(tags = "Country")
public class CountryController implements BaseCrudController<Country, String> {

	private Logger logger = LogManager.getLogger(CountryController.class);
	
	private final CountryService countryService;
	
	@Autowired(required = true)
	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}

	@Override
	@ApiOperation(value = "List all existing countries with the GET method")
	public List<Country> findAll() {
		try {
			return countryService.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@GetMapping(value = "/countries")
	public List<Country> findAllDetails() {
		try {
			return countryService.findAllDetails();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@GetMapping(value = "/country/{id}")
	@Override
	@ApiOperation(value = "Get one existing country with the GET method")
	public Country findById(@PathVariable String id) {
		try {
			return countryService.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/country")
	@Override
    @ApiOperation(value = "Create a new Country with the POST method")
	public Result<Country> create(@RequestBody Country t) {
		try {

			return countryService.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/country")
	@Override
	@ApiOperation(value = "Update an existing Country with the PUT method")
	public Result<Country> update(@RequestBody Country t) {
		try {

			return countryService.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@DeleteMapping(value = "/country/{id}")
	@Override
	@ApiOperation(value = "Delete an existing Country with the DELETE method")
	public Result<Country> delete(@PathVariable String id) {
		Result<Country> result = new Result<>();
		try {
			countryService.delete(id);
			result.setCode(StringIteration.SUCCESS_CODE);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;

	}
	
	@GetMapping(value = "/allCountry")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		
		return countryService.findAllCountry(page,  value,sortBy,sortDir);
	}

	 @PutMapping("/de-activate-country")
	    public Result<Country> activeAndDeActivateCounty(@RequestParam String id) {
	        return countryService.activeAndDeActivateCountry(id);
	    }
	 
	 @PostMapping("/country/fileReader/{sheetIndex}")
		public List<Country> fileReader(@RequestPart(name = "file" , required = true) 
		MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
		 if(file==null || StringUtils.isBlank(file.getOriginalFilename())|| !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".xlsx")) {				return Arrays.asList(new Country());
			}
				return countryService.fileReader(file , sheetIndex);
		}


}
