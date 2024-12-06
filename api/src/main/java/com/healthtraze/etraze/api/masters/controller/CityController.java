package com.healthtraze.etraze.api.masters.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.service.CityService;

import io.jsonwebtoken.io.IOException;

@RestController()
public class CityController implements BaseCrudController<City, String>{
	
	private Logger logger = LogManager.getLogger(CityController.class);
	
	
	private final CityService cityService;
	
	@Autowired(required = true)
	public CityController(CityService cityService) {
		this.cityService = cityService;
	}

	
	@Override
	public List<City> findAll() {
		try {
			return cityService.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	
	
	@GetMapping(value = "/cities")
	public List<City> findAllDetails() {
		try {
			return cityService.findAllDetails();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	
	@GetMapping(value = "/all-cities")
	public List<City> findAllCities() {
		try {
			return cityService.findAllcities();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	
	
	@GetMapping(value = "/city_list_value")
	public Iterable<Map<String, String>> cityListValue() {
		try {
			return cityService.findAll().stream().map(o -> { HashMap<String, String> map = new HashMap<>(); map.put(Constants.CITYCODE,o.getCityCode()); map.put(Constants.CITYNAME,o.getCityName()); return map;}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/city/{id}")
	@Override
	public City findById(@PathVariable String id) {
		try {
			return cityService.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}
	
	@GetMapping(value = "/city/state/{stateCode}")
	public List<City> findByStateCode(@PathVariable String stateCode) {
		try {
			return cityService.findByStateCode(stateCode);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@PostMapping(value = "/city")
	@Override
	public Result<City> create(@RequestBody City t) {
		
		return cityService.create(t);
	}

	@PutMapping(value = "/city")
	@Override
	public Result<City> update(@RequestBody City t) {
		
		return cityService.update(t);
	}
	@DeleteMapping(value = "/city/{id}")
	@Override
	public Result<City> delete(@PathVariable String id) {
		Result<City> result = null;
		try {
			result = cityService.delete(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return result;

	}
	
	

	
	 @PutMapping("/de-activate-city")
    public Result<City> activeAndDeActivateCity(@RequestParam String id) {
        return cityService.activeAndDeActivateCity(id);
    }

	 @PostMapping("/city/fileReader/{sheetIndex}")
		public List<City> fileReader(@RequestPart(name = "file" , required = true) 
		MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
		 if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".xlsx")) {				return Arrays.asList(new City());
			}
				try {
					return cityService.fileReader(file , sheetIndex);
				} catch (java.io.IOException e) {
					e.printStackTrace();
				}
				return Collections.emptyList();
		}
	
	

	@GetMapping(value = "/allCity")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		
		return cityService.findAllCity(page, value,sortBy,sortDir);
	}

	
}
